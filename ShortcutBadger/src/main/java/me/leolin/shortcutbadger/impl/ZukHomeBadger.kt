package me.leolin.shortcutbadger.impl

import android.content.ComponentName
import android.content.Context
import android.net.Uri
import android.os.Bundle
import me.leolin.shortcutbadger.Badger
import me.leolin.shortcutbadger.ShortcutBadgeException

/**
 * Created by wuxuejian on 2016/10/9.
 * 需在设置 -- 通知和状态栏 -- 应用角标管理 中开启应用
 */
class ZukHomeBadger : Badger {

    @Throws(ShortcutBadgeException::class)
    override fun executeBadge(context: Context, componentName: ComponentName, badgeCount: Int) {
        val extra = Bundle()
        extra.putInt("app_badge_count", badgeCount)
        context.contentResolver.call(
            Uri.parse("content://com.android.badge/badge"),
            "setAppBadgeCount",
            null,
            extra
        )
    }

    override val supportLaunchers: List<String>
        get() = listOf("com.zui.launcher")
}
