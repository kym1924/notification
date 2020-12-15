

# notification

<br>

## 1. createNotificationChannel

```kotlin
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
```

<br>

<br>

## 2. BasicNotification

​    <img src="https://user-images.githubusercontent.com/63637706/102226691-07636f00-3f2c-11eb-91de-154ad39686c4.gif" width="350" height="600">

```kotlin
val builder = NotificationCompat.Builder(this, getString(R.string.app_name))
              .setSmallIcon(R.drawable.ic_alarm)
              .setContentTitle("kym1924")
              .setContentText("Notification")
              .setPriority(NotificationCompat.PRIORITY_DEFAULT)

with(NotificationManagerCompat.from(this)) {
    // notificationId is a unique int for each notification that you must define
    notify(notificationId, builder.build())
}
```

<br><br>

## 3. BigTextNotification

​    <img src="https://user-images.githubusercontent.com/63637706/102226700-092d3280-3f2c-11eb-90bd-2e814a0f6389.gif" width="350" height="600">

```kotlin
val builder = NotificationCompat.Builder(this, getString(R.string.app_name))
              .setSmallIcon(R.drawable.ic_alarm)
              .setContentTitle("kym1924")
              .setContentText("Notification")
              // setting BigTextStyle
              .setStyle(NotificationCompat.BigTextStyle()
             	.bigText("Much longer text that cannot fit one line you can use .setStyle"))
              .setPriority(NotificationCompat.PRIORITY_DEFAULT)

with(NotificationManagerCompat.from(this)) {
    notify(notificationId, builder.build())
}
```

<br>

<br>

## 4. PendingIntent

​    <img src="https://user-images.githubusercontent.com/63637706/102226703-0a5e5f80-3f2c-11eb-8166-46259a238556.gif" width="350" height="600">

```kotlin
// Create an explicit intent for an Activity in your app
val intent = Intent(this, IntentActivity::class.java).apply {
             flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }

val pendingIntent : PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
val builder = NotificationCompat.Builder(this, getString(R.string.app_name))
              .setSmallIcon(R.drawable.ic_alarm)
              .setContentTitle("kym1924")
              .setContentText("notification")
              .setContentIntent(pendingIntent)
              .setPriority(NotificationCompat.PRIORITY_DEFAULT)
              // automatically removes the notification when the user taps it
              .setAutoCancel(true)
                                         
with(NotificationManagerCompat.from(this)) {
    notify(notificationId, builder.build())
}
```

<br>

<br>

## 5. setCustomContentView

<img src="https://user-images.githubusercontent.com/63637706/102227629-231b4500-3f2d-11eb-8f6d-c69ff3bf5db6.gif" width="350" height="600">

```kotlin
// ConstraintLayout can't be used for RemoteViews
val convertView = RemoteViews(packageName, R.layout.layout_notification)
convertView.setImageViewResource(R.id.img_custom_notification, R.mipmap.ic_launcher_round)
convertView.setTextViewText(R.id.tv_title, "kym1924")
convertView.setTextViewText(R.id.tv_content, "Custom notification")

val builder = NotificationCompat.Builder(this, getString(R.string.app_name))
              .setSmallIcon(R.drawable.ic_alarm)
              .setContentTitle("kym1924")
              .setContentText("notification")
              .setCustomContentView(convertView) // setting custom layout
              .setPriority(NotificationCompat.PRIORITY_DEFAULT)
              .setAutoCancel(true)
                                         
with(NotificationManagerCompat.from(this)) {
    notify(notificationId, builder.build())
}
```

<br>

<br>

## 6. setCustomBigContentView

<img src="https://user-images.githubusercontent.com/63637706/102232909-04b84800-3f33-11eb-8f42-4f468ab65c57.gif" width="350" height="600">

```kotlin
// ConstraintLayout can't be used for RemoteViews
val convertView = RemoteViews(packageName, R.layout.layout_notification)
convertView.setImageViewResource(R.id.img_custom_notification, R.mipmap.ic_launcher_round)
convertView.setTextViewText(R.id.tv_title, "kym1924")
convertView.setTextViewText(R.id.tv_content, "Custom notification")

val convertHeadUpView = RemoteViews(packageName, R.layout.layout_notification_head_up)
convertHeadUpView.setImageViewResource(R.id.img_head_logo, R.drawable.ic_alarm)
convertHeadUpView.setTextViewText(R.id.tv_head_title, "kym")

val builder = NotificationCompat.Builder(this, getString(R.string.app_name))
              .setSmallIcon(R.drawable.ic_alarm)
              .setContentTitle("kym1924")
              .setContentText("notification")
              .setCustomContentView(convertHeadUpView) // setting custom layout for HeadUpView
			  .setCustomBigContentView(convertView) // setting custom layout for Notification
              .setPriority(NotificationCompat.PRIORITY_DEFAULT)
              .setAutoCancel(true)

with(NotificationManagerCompat.from(this)) {
    notify(notificationId, builder.build())
}
```

<br>

<br>

## 7. Using Handler for delaying 10 seconds

<img src="https://user-images.githubusercontent.com/63637706/102235322-9032d880-3f35-11eb-98e5-d8afdfd42bac.gif" width="350" height="600">

```kotlin
with(NotificationManagerCompat.from(this)) {
    Handler(Looper.getMainLooper()).postDelayed ({
        notify(notificationId, builder.build())
    }, 10000)
}
```

<br>

<br>

## 8. AlarmReceiver

```kotlin
class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        createNotificationChannel(context)

        val alarmIntent = Intent(context, IntentActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val alarmPendingIntent : PendingIntent = PendingIntent.getActivity
        	(context, requestCode, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val convertView = RemoteViews(context.packageName, R.layout.layout_notification)
        convertView.setImageViewResource(R.id.img_custom_notification, R.mipmap.ic_launcher_round)
        convertView.setTextViewText(R.id.tv_title, "kym1924")
        convertView.setTextViewText(R.id.tv_content, "Custom notification")

        val builder = NotificationCompat.Builder(context, context.getString(R.string.app_name))
            .setSmallIcon(R.drawable.ic_alarm)
            .setCustomContentView(convertView)
            .setContentIntent(alarmPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            notify(requestCode, builder.build())
        }
    }
    
    private fun createNotificationChannel(context : Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.notification)
            val descriptionText = context.getString(R.string.notification)
            val importance = NotificationManager.IMPORTANCE_MAX
            val channel = NotificationChannel(context.getString(R.string.app_name), name, importance)
            .apply {
                    description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
```

<br>

<br>

## 9. Using BroadcastReceiver()

<img src="https://user-images.githubusercontent.com/63637706/102240353-1aca0680-3f3b-11eb-9ebc-74b964defbbb.gif" width="350" height="600">

<br>

## 9-1. after 1 minute Notification

```kotlin
val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager

...

val time = (SystemClock.elapsedRealtime()+60*1000) // after 1 minute
val intent = Intent(this, AlarmReceiver::class.java)
val pendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT)        		
alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent)

...
```

<br>

## 9-2. Repeating Notification

```kotlin
val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager

...

val calendar = Calendar.getInstance()
    calendar.set(Calendar.HOUR_OF_DAY, hour)
    calendar.set(Calendar.MINUTE, minute)
val intent = Intent(this, AlarmReceiver::class.java)
val pendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT)

alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
                                 AlarmManager.INTERVAL_DAY, pendingIntent)

...
```