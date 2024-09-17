package me.leolin.shortcutbadger.impl

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import me.leolin.shortcutbadger.Badger
import me.leolin.shortcutbadger.ShortcutBadgeException
import me.leolin.shortcutbadger.util.BroadcastHelper.sendDefaultIntentExplicitly

/**
 * @author Leo Lin
 * Deprecated, LG devices will use DefaultBadger
 */
@Deprecated("")
class LGHomeBadger : Badger {

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
            "com.lge.launcher",
            "com.lge.launcher2"
        )
}
