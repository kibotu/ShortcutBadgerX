package me.leolin.shortcutbadger.impl

import android.content.ComponentName
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import me.leolin.shortcutbadger.Badger
import me.leolin.shortcutbadger.ShortcutBadgeException
import me.leolin.shortcutbadger.util.CloseHelper.close

/**
 * @author Leo Lin
 */
class SamsungHomeBadger : Badger {

    private val defaultBadger: DefaultBadger by lazy { DefaultBadger() }

    @Throws(ShortcutBadgeException::class)
    override fun executeBadge(context: Context, componentName: ComponentName, badgeCount: Int) {
        if (defaultBadger.isSupported(context)) {
            defaultBadger.executeBadge(context, componentName, badgeCount)
        } else {
            val uri = Uri.parse("content://com.sec.badge/apps?notify=true")
            val contentResolver = context.contentResolver
            var cursor: Cursor? = null
            try {
                cursor = contentResolver.query(
                    uri,
                    arrayOf("_id", "class"),
                    "package=?",
                    arrayOf(componentName.packageName),
                    null
                )
                if (cursor != null) {
                    val entryActivityName = componentName.className
                    var entryActivityExist = false
                    while (cursor.moveToNext()) {
                        val id = cursor.getInt(0)
                        val contentValues = getContentValues(componentName, badgeCount, false)
                        contentResolver.update(uri, contentValues, "_id=?", arrayOf(id.toString()))
                        if (entryActivityName == cursor.getString(cursor.getColumnIndex("class"))) {
                            entryActivityExist = true
                        }
                    }

                    if (!entryActivityExist) {
                        val contentValues = getContentValues(componentName, badgeCount, true)
                        contentResolver.insert(uri, contentValues)
                    }
                }
            } finally {
                close(cursor)
            }
        }
    }

    private fun getContentValues(
        componentName: ComponentName,
        badgeCount: Int,
        isInsert: Boolean
    ): ContentValues {
        val contentValues = ContentValues()
        if (isInsert) {
            contentValues.put("package", componentName.packageName)
            contentValues.put("class", componentName.className)
        }

        contentValues.put("badgecount", badgeCount)

        return contentValues
    }

    override val supportLaunchers: List<String>
        get() = listOf(
            "com.sec.android.app.launcher",
            "com.sec.android.app.twlauncher"
        )
}
