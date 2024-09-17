package me.leolin.shortcutbadger.impl

import android.content.ComponentName
import android.content.Context
import android.net.Uri
import android.os.Bundle
import me.leolin.shortcutbadger.Badger
import me.leolin.shortcutbadger.ShortcutBadgeException

class ZTEHomeBadger : Badger {

    @Throws(ShortcutBadgeException::class)
    override fun executeBadge(context: Context, componentName: ComponentName, badgeCount: Int) {
        val extra = Bundle()
        extra.putInt("app_badge_count", badgeCount)
        extra.putString("app_badge_component_name", componentName.flattenToString())

        var stockSuccess = false
        try {
            context.contentResolver.call(
                Uri.parse("content://com.android.launcher3.cornermark.unreadbadge"),
                "setAppUnreadCount", null, extra
            )
            stockSuccess = true
        } catch (ignored: Exception) {
        }

        var mifavorSuccess = false
        try {
            context.contentResolver.call(
                Uri.parse("content://com.zte.mifavor.launcher.unreadbadge"),
                "setAppUnreadCount", null, extra
            )
            mifavorSuccess = true
        } catch (ignored: Exception) {
        }

        if (!stockSuccess && !mifavorSuccess) throw ShortcutBadgeException("Both stock and mifavor content resolver have failed")
    }

    override val supportLaunchers: List<String>
        get() = listOf("com.zte.mifavor.launcher")
}

