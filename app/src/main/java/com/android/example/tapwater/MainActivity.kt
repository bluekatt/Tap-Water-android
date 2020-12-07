package com.android.example.tapwater

import android.content.SharedPreferences
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.android.example.tapwater.databinding.ActivityMainBinding
import com.android.example.tapwater.ui.settings.SettingsFragmentDirections
import com.android.example.tapwater.ui.settings.SpeedMeasureFragment
import com.android.example.tapwater.ui.settings.SpeedPreference
import com.google.android.material.bottomnavigation.LabelVisibilityMode

class MainActivity : AppCompatActivity(), PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {

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

        setSupportActionBar(binding.toolBar)

        NavigationUI.setupActionBarWithNavController(this, navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.nav_host_fragment)
        return NavigationUI.navigateUp(navController, null)
    }

    override fun onPreferenceStartFragment(caller: PreferenceFragmentCompat, pref: Preference): Boolean {
        // Instantiate the new Fragment
        val navController = findNavController(R.id.nav_host_fragment)
        if(pref.key=="speed") {
            val speedMeasureFragment = SpeedMeasureFragment.newInstance(pref as SpeedPreference)
            speedMeasureFragment.show(supportFragmentManager, speedMeasureFragment.tag)
        } else if(pref.key=="app_info") {
            navController.navigate(SettingsFragmentDirections.actionNavigationSettingsToNavigationAppInfo())
        }
        return true
    }

}