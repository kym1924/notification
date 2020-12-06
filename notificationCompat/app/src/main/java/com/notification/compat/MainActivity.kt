package com.notification.compat

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.RemoteViews
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val notificationId = 0

        createNotificationChannel()

        btn_notification.setOnClickListener{
            // Create an explicit intent for an Activity in your app
            val intent = Intent(this, IntentActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }

            val pendingIntent : PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

            // ConstraintLayout can't be used for RemoteViews
            val convertView = RemoteViews(packageName, R.layout.layout_notification)
            convertView.setImageViewResource(R.id.img_custom_notification, R.mipmap.ic_launcher_round)
            convertView.setTextViewText(R.id.tv_title, "Custom notification")
            convertView.setTextViewText(R.id.tv_content, "Custom notification should be LinearLayout")

            val builder = NotificationCompat.Builder(this, getString(R.string.app_name))
                .setSmallIcon(R.drawable.ic_alarm)
                .setCustomContentView(convertView)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // automatically removes the notification when the user taps it.
                .setAutoCancel(true)

            with(NotificationManagerCompat.from(this)) {
                // notificationId is a unique int for each notification that you must define
                notify(notificationId, builder.build())
            }
        }
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.notification)
            val descriptionText = getString(R.string.notification)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(getString(R.string.app_name), name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}