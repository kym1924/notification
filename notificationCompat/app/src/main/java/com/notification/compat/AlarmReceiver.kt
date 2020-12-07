package com.notification.compat

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        createNotificationChannel(context)

        // Create an explicit intent for an Activity in your app
        val alarmIntent = Intent(context, IntentActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val alarmPendingIntent : PendingIntent = PendingIntent.getActivity(context, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        // ConstraintLayout can't be used for RemoteViews
        val convertView = RemoteViews(context.packageName, R.layout.layout_notification)
        convertView.setImageViewResource(R.id.img_custom_notification, R.mipmap.ic_launcher_round)
        convertView.setTextViewText(R.id.tv_title, "Custom notification")
        convertView.setTextViewText(R.id.tv_content, "Custom notification should be LinearLayout")

        val builder = NotificationCompat.Builder(context, context.getString(R.string.app_name))
            .setSmallIcon(R.drawable.ic_alarm)
            .setCustomContentView(convertView)
            .setContentIntent(alarmPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            // automatically removes the notification when the user taps it.
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify(0, builder.build())
        }
    }

    private fun createNotificationChannel(context : Context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.notification)
            val descriptionText = context.getString(R.string.notification)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(context.getString(R.string.app_name), name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}