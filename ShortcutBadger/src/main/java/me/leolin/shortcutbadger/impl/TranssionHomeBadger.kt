package me.leolin.shortcutbadger.impl

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import me.leolin.shortcutbadger.Badger
import me.leolin.shortcutbadger.ShortcutBadgeException

/**
 * @author leolin
 */
class TranssionHomeBadger : Badger {

    @Throws(ShortcutBadgeException::class)
    override fun executeBadge(context: Context, componentName: ComponentName, badgeCount: Int) {
        val intent = Intent("com.mediatek.action.UNREAD_CHANGED")
        intent.putExtra("com.mediatek.intent.extra.UNREAD_COMPONENT", componentName)
        intent.putExtra("com.mediatek.intent.extra.UNREAD_NUMBER", badgeCount)
        context.sendBroadcast(intent)
    }

    override val supportLaunchers: List<String>
        get() = listOf(
            "com.transsion.XOSLauncher",
            "com.transsion.hilauncher",
            "com.transsion.itel.launcher"
        )
}

