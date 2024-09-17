package me.leolin.shortcutbadger.example

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import me.leolin.shortcutbadger.ShortcutBadger.applyCount
import me.leolin.shortcutbadger.ShortcutBadger.removeCount
import me.leolin.shortcutbadger.example.BadgeIntentService

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val numInput = findViewById<EditText>(R.id.numInput)

        val button = findViewById<Button>(R.id.btnSetBadge)
        button.setOnClickListener {
            var badgeCount = 0
            try {
                badgeCount = numInput.text.toString().toInt()
            } catch (e: NumberFormatException) {
                Toast.makeText(applicationContext, "Error input", Toast.LENGTH_SHORT).show()
            }

            val success = applyCount(this@MainActivity, badgeCount)
            Toast.makeText(
                applicationContext,
                "Set count=$badgeCount, success=$success",
                Toast.LENGTH_SHORT
            ).show()
        }

        val launchNotification = findViewById<Button>(R.id.btnSetBadgeByNotification)
        launchNotification.setOnClickListener {
            var badgeCount = 0
            try {
                badgeCount = numInput.text.toString().toInt()
            } catch (e: NumberFormatException) {
                Toast.makeText(applicationContext, "Error input", Toast.LENGTH_SHORT).show()
            }

            finish()
            startService(
                Intent(this@MainActivity, BadgeIntentService::class.java).putExtra(
                    "badgeCount",
                    badgeCount
                )
            )
        }

        val removeBadgeBtn = findViewById<Button>(R.id.btnRemoveBadge)
        removeBadgeBtn.setOnClickListener {
            val success = removeCount(this@MainActivity)
            Toast.makeText(applicationContext, "success=$success", Toast.LENGTH_SHORT).show()
        }


        //find the home launcher Package
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        var currentHomePackage = "none"
        val resolveInfo = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)

        // in case of duplicate apps (Xiaomi), calling resolveActivity from one will return null
        if (resolveInfo != null) {
            currentHomePackage = resolveInfo.activityInfo.packageName
        }

        val textViewHomePackage = findViewById<TextView>(R.id.textViewHomePackage)
        textViewHomePackage.text = "launcher:$currentHomePackage"
    }
}
