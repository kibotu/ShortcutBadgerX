package me.leolin.shortcutbadger.impl

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import me.leolin.shortcutbadger.Badger
import me.leolin.shortcutbadger.ShortcutBadgeException

/**
 * @author leolin
 * see also https://dev.vivo.com.cn/documentCenter/doc/459.
 */
class VivoHomeBadger : Badger {

    @Throws(ShortcutBadgeException::class)
    override fun executeBadge(context: Context, componentName: ComponentName, badgeCount: Int) {
        val intent = Intent("launcher.action.CHANGE_APPLICATION_NOTIFICATION_NUM")
        intent.putExtra("packageName", context.packageName)
        intent.putExtra("className", componentName.className)
        intent.putExtra("notificationNum", badgeCount)
        intent.addFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP) // Intent.FLAG_RECEIVER_INCLUDE_BACKGROUND for Android 8.0
        context.sendBroadcast(intent)
    }

    override val supportLaunchers: List<String>
        get() = listOf(
            "com.vivo.launcher",
            "com.bbk.launcher2"
        )
}
