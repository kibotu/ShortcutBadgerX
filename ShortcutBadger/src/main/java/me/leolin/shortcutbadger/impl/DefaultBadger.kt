package me.leolin.shortcutbadger.impl

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import me.leolin.shortcutbadger.Badger
import me.leolin.shortcutbadger.ShortcutBadgeException
import me.leolin.shortcutbadger.util.BroadcastHelper.resolveBroadcast
import me.leolin.shortcutbadger.util.BroadcastHelper.sendDefaultIntentExplicitly

/**
 * @author leolin
 */
class DefaultBadger : Badger {

    @Throws(ShortcutBadgeException::class)
    override fun executeBadge(context: Context, componentName: ComponentName, badgeCount: Int) {
        val intent = Intent(IntentConstants.DEFAULT_INTENT_ACTION)
        intent.putExtra("badge_count", badgeCount)
        intent.putExtra("badge_count_package_name", componentName.packageName)
        intent.putExtra("badge_count_class_name", componentName.className)

        sendDefaultIntentExplicitly(context, intent)
    }

    override val supportLaunchers: List<String>
        get() = listOf(
            "fr.neamar.kiss",
            "com.quaap.launchtime",
            "com.quaap.launchtime_official"
        )

    fun isSupported(context: Context): Boolean {
        val intent = Intent(IntentConstants.DEFAULT_INTENT_ACTION)
        return (resolveBroadcast(context, intent).isNotEmpty()
                || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                && resolveBroadcast(
            context,
            Intent(IntentConstants.DEFAULT_OREO_INTENT_ACTION)
        ).isNotEmpty()))
    }
}
