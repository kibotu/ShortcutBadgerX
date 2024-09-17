package me.leolin.shortcutbadger.impl

import android.content.ComponentName
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import me.leolin.shortcutbadger.Badger
import me.leolin.shortcutbadger.ShortcutBadgeException

/**
 * @author Radko Roman
 * @since  13.04.17.
 */
class EverythingMeHomeBadger : Badger {

    @Throws(ShortcutBadgeException::class)
    override fun executeBadge(context: Context, componentName: ComponentName, badgeCount: Int) {
        val contentValues = ContentValues()
        contentValues.put("package_name", componentName.packageName)
        contentValues.put("activity_name", componentName.className)
        contentValues.put("count", badgeCount)
        context.contentResolver.insert(
            Uri.parse("content://me.everything.badger/apps"),
            contentValues
        )
    }

    override val supportLaunchers: List<String>
        get() = listOf("me.everything.launcher")
}
