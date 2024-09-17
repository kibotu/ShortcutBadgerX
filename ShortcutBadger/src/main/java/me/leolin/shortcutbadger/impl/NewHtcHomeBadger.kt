package me.leolin.shortcutbadger.impl

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import me.leolin.shortcutbadger.Badger
import me.leolin.shortcutbadger.ShortcutBadgeException
import me.leolin.shortcutbadger.util.BroadcastHelper.sendIntentExplicitly

/**
 * @author Leo Lin
 */
class NewHtcHomeBadger : Badger {

    @Throws(ShortcutBadgeException::class)
    override fun executeBadge(context: Context, componentName: ComponentName, badgeCount: Int) {
        val intent1 = Intent("com.htc.launcher.action.SET_NOTIFICATION")

        intent1.putExtra("com.htc.launcher.extra.COMPONENT", componentName.flattenToShortString())
        intent1.putExtra("com.htc.launcher.extra.COUNT", badgeCount)

        val intent = Intent("com.htc.launcher.action.UPDATE_SHORTCUT")

        intent.putExtra("packagename", componentName.packageName)
        intent.putExtra("count", badgeCount)

        val intent1Success: Boolean = try {
            sendIntentExplicitly(context, intent1)
            true
        } catch (e: ShortcutBadgeException) {
            false
        }

        val intentSuccess: Boolean = try {
            sendIntentExplicitly(context, intent)
            true
        } catch (e: ShortcutBadgeException) {
            false
        }

        if (!intent1Success && !intentSuccess) {
            throw ShortcutBadgeException("unable to resolve intent: $intent")
        }
    }

    override val supportLaunchers: List<String>
        get() = listOf("com.htc.launcher")
}
