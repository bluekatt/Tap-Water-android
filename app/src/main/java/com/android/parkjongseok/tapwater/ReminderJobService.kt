package com.android.parkjongseok.tapwater

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.preference.PreferenceManager
import java.util.*

class ReminderJobService: JobService() {
    override fun onStartJob(params: JobParameters): Boolean {
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        if(pref.getBoolean("reminder_first_call", true)) {
            val edit = pref.edit()
            edit.putBoolean("reminder_first_call", false)
            edit.apply()

            return false
        }
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        val builder = NotificationCompat.Builder(this, "TAP_WATER")
            .setSmallIcon(R.drawable.ic_stat_name)
            .setContentTitle(getString(R.string.notification_title_text))
            .setContentText(getString(R.string.notification_content_text))
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.app_name)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("TAP_WATER", name, importance)
            channel.apply {
                description = getString(R.string.notification_channel_description)
            }
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(UUID.randomUUID().variant(), builder.build())

        return false
    }

    override fun onStopJob(params: JobParameters): Boolean {
        return false
    }
}