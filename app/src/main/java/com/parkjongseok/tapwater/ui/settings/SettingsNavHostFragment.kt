package com.parkjongseok.tapwater.ui.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.parkjongseok.tapwater.R

class SettingsNavHostFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings_nav_host, container, false)
    }

    override fun onResume() {
        super.onResume()
        val navController = requireActivity().findNavController(R.id.settings_nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(navController.graph)

        val toolbar: Toolbar = requireView().findViewById(R.id.toolbar)

        navController.addOnDestinationChangedListener { _, dest, arg ->
            when(dest.id) {
                R.id.navigation_open_source_license_detail -> {
                    val args = OpenSourceLicenseDetailFragmentArgs.fromBundle(arg!!)
                    toolbar.title = args.licenseTitle
                }
            }
        }

        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration)
    }
}