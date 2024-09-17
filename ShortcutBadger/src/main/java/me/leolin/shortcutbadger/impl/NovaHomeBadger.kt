package me.leolin.shortcutbadger.impl

import android.content.ComponentName
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import me.leolin.shortcutbadger.Badger
import me.leolin.shortcutbadger.ShortcutBadgeException

/**
 * Shortcut Badger support for Nova Launcher.
 * TeslaUnread must be installed.
 * User: Gernot Pansy
 * Date: 2014/11/03
 * Time: 7:15
 */
class NovaHomeBadger : Badger {

    @Throws(ShortcutBadgeException::class)
    override fun executeBadge(context: Context, componentName: ComponentName, badgeCount: Int) {
        val contentValues = ContentValues()
        contentValues.put("tag", componentName.packageName + "/" + componentName.className)
        contentValues.put("count", badgeCount)
        context.contentResolver.insert(
            Uri.parse("content://com.teslacoilsw.notifier/unread_count"),
            contentValues
        )
    }

    override val supportLaunchers: List<String>
        get() = listOf("com.teslacoilsw.launcher")
}
