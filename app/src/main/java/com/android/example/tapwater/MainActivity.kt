package com.android.example.tapwater

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.util.TimeUtils
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.android.example.tapwater.databinding.ActivityMainBinding
import com.android.example.tapwater.ui.settings.SettingsFragmentDirections
import com.android.example.tapwater.ui.settings.SpeedMeasureFragment
import com.android.example.tapwater.ui.settings.SpeedPreference
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {
    private var notificationFirstCall = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        navView.setupWithNavController(navController)
        navView.itemIconTintList = null
        navView.labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_UNLABELED

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.settings_nav_host_fragment)
        return NavigationUI.navigateUp(navController, null)
    }

    override fun onPreferenceStartFragment(caller: PreferenceFragmentCompat, pref: Preference): Boolean {
        // Instantiate the new Fragment
        val navController = findNavController(R.id.settings_nav_host_fragment)
        if(pref.key=="speed") {
            val speedMeasureFragment = SpeedMeasureFragment.newInstance(pref as SpeedPreference)
            speedMeasureFragment.show(supportFragmentManager, speedMeasureFragment.tag)
        } else if(pref.key=="app_info") {
            navController.navigate(SettingsFragmentDirections.actionNavigationSettingsToNavigationAppInfo())
        }
        return true
    }

    override fun onStop() {
        super.onStop()
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        if(pref.getBoolean("reminder", false)) {
            setNotification(pref.getFloat("reminder_interval", 1f).toLong())
        }
    }

    private fun setNotification(interval: Long) {
        val js = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        val serviceComponent = ComponentName(this, ReminderJobService::class.java)

        js.cancelAll()
        val edit = PreferenceManager.getDefaultSharedPreferences(this).edit()
        edit.putBoolean("reminder_first_call", true)
        edit.apply()

        val jobInfo = JobInfo.Builder(0, serviceComponent)
            .setPeriodic(TimeUnit.HOURS.toMillis(interval))
            .build()

        js.schedule(jobInfo)
    }

}