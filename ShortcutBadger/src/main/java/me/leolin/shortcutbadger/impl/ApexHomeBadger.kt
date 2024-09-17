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
class ApexHomeBadger : Badger {

    @Throws(ShortcutBadgeException::class)
    override fun executeBadge(context: Context, componentName: ComponentName, badgeCount: Int) {
        val intent = Intent("com.anddoes.launcher.COUNTER_CHANGED")
        intent.putExtra("package", componentName.packageName)
        intent.putExtra("count", badgeCount)
        intent.putExtra("class", componentName.className)

        sendIntentExplicitly(context, intent)
    }

    override val supportLaunchers: List<String>
        get() = listOf("com.anddoes.launcher")
}
