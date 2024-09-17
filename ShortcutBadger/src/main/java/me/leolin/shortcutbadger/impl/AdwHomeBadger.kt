package me.leolin.shortcutbadger.impl

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import me.leolin.shortcutbadger.Badger
import me.leolin.shortcutbadger.ShortcutBadgeException
import me.leolin.shortcutbadger.util.BroadcastHelper.sendIntentExplicitly

/**
 * @author Gernot Pansy
 */
class AdwHomeBadger : Badger {

    @Throws(ShortcutBadgeException::class)
    override fun executeBadge(context: Context, componentName: ComponentName, badgeCount: Int) {
        val intent = Intent("org.adw.launcher.counter.SEND")
        intent.putExtra("PNAME", componentName.packageName)
        intent.putExtra("CNAME", componentName.className)
        intent.putExtra("COUNT", badgeCount)

        sendIntentExplicitly(context, intent)
    }

    override val supportLaunchers: List<String>
        get() = listOf(
            "org.adw.launcher",
            "org.adwfreak.launcher"
        )
}
