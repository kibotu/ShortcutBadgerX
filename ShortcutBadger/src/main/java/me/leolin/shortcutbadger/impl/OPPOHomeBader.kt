package me.leolin.shortcutbadger.impl

import android.content.ComponentName
import android.content.Context
import android.net.Uri
import android.os.Bundle
import me.leolin.shortcutbadger.Badger
import me.leolin.shortcutbadger.ShortcutBadgeException

/**
 * Created by NingSo on 2016/10/14.上午10:09
 *
 * @author: NingSo
 * Email: ningso.ping@gmail.com
 */
class OPPOHomeBader : Badger {

    private var currentTotalCount = -1

    @Throws(ShortcutBadgeException::class)
    override fun executeBadge(context: Context, componentName: ComponentName, badgeCount: Int) {
        if (currentTotalCount == badgeCount) {
            return
        }
        currentTotalCount = badgeCount
        executeBadgeByContentProvider(context, badgeCount)
    }

    override val supportLaunchers: List<String>
        get() = listOf("com.oppo.launcher")

    /**
     * Send request to OPPO badge content provider to set badge in OPPO home launcher.
     *
     * @param context       the context to use
     * @param badgeCount    the badge count
     */
    @Throws(ShortcutBadgeException::class)
    private fun executeBadgeByContentProvider(context: Context, badgeCount: Int) {
        try {
            val extras = Bundle()
            extras.putInt("app_badge_count", badgeCount)
            context.contentResolver.call(
                Uri.parse("content://com.android.badge/badge"),
                "setAppBadgeCount",
                null,
                extras
            )
        } catch (ignored: Throwable) {
            throw ShortcutBadgeException("Unable to execute Badge By Content Provider")
        }
    }
}