package me.leolin.shortcutbadger.impl

import android.content.ComponentName
import android.content.Context
import android.net.Uri
import android.os.Bundle
import me.leolin.shortcutbadger.Badger
import me.leolin.shortcutbadger.ShortcutBadgeException

/**
 * @author Jason Ling
 */
class HuaweiHomeBadger : Badger {

    @Throws(ShortcutBadgeException::class)
    override fun executeBadge(context: Context, componentName: ComponentName, badgeCount: Int) {
        val localBundle = Bundle()
        localBundle.putString("package", context.packageName)
        localBundle.putString("class", componentName.className)
        localBundle.putInt("badgenumber", badgeCount)

        var huaweiSuccess = false
        try {
            context.contentResolver.call(
                Uri.parse("content://com.huawei.android.launcher.settings/badge/"),
                "change_badge",
                null,
                localBundle
            )
            huaweiSuccess = true
        } catch (ignored: Exception) {
        }

        var honorSuccess = false
        try {
            context.contentResolver.call(
                Uri.parse("content://com.hihonor.android.launcher.settings/badge/"),
                "change_badge",
                null,
                localBundle
            )
            honorSuccess = true
        } catch (ignored: Exception) {
        }

        if (!huaweiSuccess && !honorSuccess) throw ShortcutBadgeException("Both huawei and honor content resolver have failed")
    }

    override val supportLaunchers: List<String>
        get() = listOf(
            "com.huawei.android.launcher",
            "com.hihonor.android.launcher"
        )
}
