package me.leolin.shortcutbadger.impl

import android.content.ComponentName
import android.content.Context
import android.net.Uri
import android.os.Bundle
import me.leolin.shortcutbadger.Badger
import me.leolin.shortcutbadger.ShortcutBadgeException

/**
 * @author Nikolay Pakhomov
 * created 16/04/2018
 */
class YandexLauncherBadger : Badger {

    @Throws(ShortcutBadgeException::class)
    override fun executeBadge(context: Context, componentName: ComponentName, badgeCount: Int) {
        val extras = Bundle()
        extras.putString("class", componentName.className)
        extras.putString("package", componentName.packageName)
        extras.putString("badges_count", badgeCount.toString())
        context.contentResolver.call(CONTENT_URI, "setBadgeNumber", null, extras)
    }

    override val supportLaunchers: List<String>
        get() = listOf(PACKAGE_NAME)

    companion object {
        const val PACKAGE_NAME: String = "com.yandex.launcher"
        private val CONTENT_URI: Uri by lazy { Uri.parse("content://com.yandex.launcher.badges_external") }

        fun isVersionSupported(context: Context): Boolean = try {
            context.contentResolver.call(CONTENT_URI, "", null, null)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }
}
