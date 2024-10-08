package me.leolin.shortcutbadger.impl

import android.annotation.SuppressLint
import android.content.AsyncQueryHandler
import android.content.ComponentName
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Looper
import me.leolin.shortcutbadger.Badger
import me.leolin.shortcutbadger.ShortcutBadgeException

/**
 * @author Leo Lin
 */
class SonyHomeBadger : Badger {

    private val BADGE_CONTENT_URI: Uri by lazy { Uri.parse("content://com.sonymobile.home.resourceprovider/badge") }

    private var queryHandler: AsyncQueryHandler? = null

    @Throws(ShortcutBadgeException::class)
    override fun executeBadge(
        context: Context, componentName: ComponentName,
        badgeCount: Int
    ) {
        if (sonyBadgeContentProviderExists(context)) {
            executeBadgeByContentProvider(context, componentName, badgeCount)
        } else {
            executeBadgeByBroadcast(context, componentName, badgeCount)
        }
    }

    override val supportLaunchers: List<String>
        get() = listOf("com.sonyericsson.home", "com.sonymobile.home")

    /**
     * Send request to Sony badge content provider to set badge in Sony home launcher.
     *
     * @param context       the context to use
     * @param componentName the componentName to use
     * @param badgeCount    the badge count
     */
    private fun executeBadgeByContentProvider(
        context: Context, componentName: ComponentName,
        badgeCount: Int
    ) {
        if (badgeCount < 0) {
            return
        }

        val contentValues = createContentValues(badgeCount, componentName)
        if (Looper.myLooper() == Looper.getMainLooper()) {
            // We're in the main thread. Let's ensure the badge update happens in a background
            // thread by using an AsyncQueryHandler and an async update.
            if (queryHandler == null) {
                queryHandler = @SuppressLint("HandlerLeak")
                object : AsyncQueryHandler(
                    context.applicationContext.contentResolver
                ) {
                }
            }
            insertBadgeAsync(contentValues)
        } else {
            // Already in a background thread. Let's update the badge synchronously. Otherwise,
            // if we use the AsyncQueryHandler, this thread may already be dead by the time the
            // async execution finishes, which will lead to an IllegalStateException.
            insertBadgeSync(context, contentValues)
        }
    }

    /**
     * Asynchronously inserts the badge counter.
     *
     * @param contentValues Content values containing the badge count, package and activity names
     */
    private fun insertBadgeAsync(contentValues: ContentValues) {
        queryHandler?.startInsert(0, null, BADGE_CONTENT_URI, contentValues)
    }

    /**
     * Synchronously inserts the badge counter.
     *
     * @param context       Caller context
     * @param contentValues Content values containing the badge count, package and activity names
     */
    private fun insertBadgeSync(context: Context, contentValues: ContentValues) {
        context.applicationContext.contentResolver.insert(BADGE_CONTENT_URI, contentValues)
    }

    /**
     * Creates a ContentValues object to be used in the badge counter update. The package and
     * activity names must correspond to an activity that holds an intent filter with action
     * "android.intent.action.MAIN" and category android.intent.category.LAUNCHER" in the manifest.
     * Also, it is not allowed to publish badges on behalf of another client, so the package and
     * activity names must belong to the process from which the insert is made.
     * To be able to insert badges, the app must have the PROVIDER_INSERT_BADGE
     * permission in the manifest file. In case these conditions are not
     * fulfilled, or any content values are missing, there will be an unhandled
     * exception on the background thread.
     *
     * @param badgeCount    the badge count
     * @param componentName the component name from which package and class name will be extracted
     */
    private fun createContentValues(
        badgeCount: Int,
        componentName: ComponentName
    ): ContentValues {
        val contentValues = ContentValues()
        contentValues.put("badge_count", badgeCount)
        contentValues.put("package_name", componentName.packageName)
        contentValues.put("activity_name", componentName.className)
        return contentValues
    }

    companion object {

        private fun executeBadgeByBroadcast(
            context: Context, componentName: ComponentName,
            badgeCount: Int
        ) {
            val intent = Intent("com.sonyericsson.home.action.UPDATE_BADGE")
            intent.putExtra(
                "com.sonyericsson.home.intent.extra.badge.PACKAGE_NAME",
                componentName.packageName
            )
            intent.putExtra(
                "com.sonyericsson.home.intent.extra.badge.ACTIVITY_NAME",
                componentName.className
            )
            intent.putExtra(
                "com.sonyericsson.home.intent.extra.badge.MESSAGE",
                badgeCount.toString()
            )
            intent.putExtra("com.sonyericsson.home.intent.extra.badge.SHOW_MESSAGE", badgeCount > 0)
            context.sendBroadcast(intent)
        }

        /**
         * Check if the latest Sony badge content provider exists .
         *
         * @param context the context to use
         * @return true if Sony badge content provider exists, otherwise false.
         */
        private fun sonyBadgeContentProviderExists(context: Context): Boolean {
            var exists = false
            val info = context.packageManager.resolveContentProvider(
                "com.sonymobile.home.resourceprovider",
                0
            )
            if (info != null) {
                exists = true
            }
            return exists
        }
    }
}
