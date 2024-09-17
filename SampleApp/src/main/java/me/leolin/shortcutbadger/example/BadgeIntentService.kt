package me.leolin.shortcutbadger.example

import android.annotation.TargetApi
import android.app.IntentService
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import me.leolin.shortcutbadger.ShortcutBadger.applyNotification

class BadgeIntentService : IntentService("BadgeIntentService") {
    private var notificationId = 0

    private var notificationManager: NotificationManager? = null

    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun onStart(intent: Intent?, startId: Int) {
        super.onStart(intent, startId)
    }

    override fun onHandleIntent(intent: Intent?) {
        if (intent != null) {
            val badgeCount = intent.getIntExtra("badgeCount", 0)

            notificationManager!!.cancel(notificationId)
            notificationId++

            val builder = Notification.Builder(applicationContext)
                .setContentTitle("ShortcutBadgerX")
                .setContentText("testing")
                .setNumber(badgeCount)
                .setSmallIcon(R.drawable.ic_launcher)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                setupNotificationChannel()

                builder.setChannelId(NOTIFICATION_CHANNEL)
            }

            val notification = builder.build()
            applyNotification(applicationContext, notification, badgeCount)
            notificationManager!!.notify(notificationId, notification)
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun setupNotificationChannel() {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL, "ShortcutBadger Sample",
            NotificationManager.IMPORTANCE_DEFAULT
        )

        notificationManager!!.createNotificationChannel(channel)
    }

    companion object {
        private const val NOTIFICATION_CHANNEL = "me.leolin.shortcutbadger.example"
    }
}
