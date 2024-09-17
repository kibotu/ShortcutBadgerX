package me.leolin.shortcutbadger.impl

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.Build
import me.leolin.shortcutbadger.Badger
import me.leolin.shortcutbadger.ShortcutBadgeException
import me.leolin.shortcutbadger.util.BroadcastHelper.sendIntentExplicitly

/**
 * @author leolin
 */
@Deprecated("")
class XiaomiHomeBadger : Badger {

    private var resolveInfo: ResolveInfo? = null

    @Suppress("DEPRECATION")
    @SuppressLint("PrivateApi")
    @Throws(ShortcutBadgeException::class)
    override fun executeBadge(context: Context, componentName: ComponentName, badgeCount: Int) {
        try {
            val miuiNotificationClass = Class.forName("android.app.MiuiNotification")
            val miuiNotification = miuiNotificationClass.newInstance()
            val field = miuiNotification.javaClass.getDeclaredField("messageCount")
            field.isAccessible = true
            try {
                field[miuiNotification] = (if (badgeCount == 0) "" else badgeCount).toString()
            } catch (e: Exception) {
                field[miuiNotification] = badgeCount
            }
        } catch (e: Exception) {
            val localIntent = Intent("android.intent.action.APPLICATION_MESSAGE_UPDATE")
            localIntent.putExtra(
                "android.intent.extra.update_application_component_name",
                componentName.packageName + "/" + componentName.className
            )
            localIntent.putExtra(
                "android.intent.extra.update_application_message_text",
                (if (badgeCount == 0) "" else badgeCount).toString()
            )

            try {
                sendIntentExplicitly(context, localIntent)
            } catch (ignored: ShortcutBadgeException) {
            }
        }
        if (Build.MANUFACTURER.equals("Xiaomi", ignoreCase = true)) {
            tryNewMiuiBadge(context, badgeCount)
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Throws(ShortcutBadgeException::class)
    private fun tryNewMiuiBadge(context: Context, badgeCount: Int) {
        if (resolveInfo == null) {
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)
            resolveInfo =
                context.packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
        }

        if (resolveInfo != null) {
            val mNotificationManager = context
                .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            @Suppress("DEPRECATION") val builder = Notification.Builder(context)
                .setContentTitle("")
                .setContentText("")
                .setSmallIcon(resolveInfo!!.iconResource)
            val notification = builder.build()
            try {
                val field = notification.javaClass.getDeclaredField("extraNotification")
                val extraNotification = field[notification]
                val method = extraNotification.javaClass.getDeclaredMethod(
                    "setMessageCount",
                    Int::class.javaPrimitiveType
                )
                method.invoke(extraNotification, badgeCount)
                mNotificationManager.notify(0, notification)
            } catch (e: Exception) {
                throw ShortcutBadgeException("not able to set badge", e)
            }
        }
    }

    override val supportLaunchers: List<String>
        get() = listOf(
            "com.miui.miuilite",
            "com.miui.home",
            "com.miui.miuihome",
            "com.miui.miuihome2",
            "com.miui.mihome",
            "com.miui.mihome2",
            "com.i.miui.launcher"
        )
}
