package com.android.parkjongseok.tapwater.ui.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.android.parkjongseok.tapwater.R
import com.android.parkjongseok.tapwater.databinding.FragmentAppInfoBinding
import com.android.parkjongseok.tapwater.getPlayStoreVersion
import kotlinx.coroutines.launch

class AppInfoFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val binding: FragmentAppInfoBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_app_info, container, false)


        lifecycleScope.launch {
            val packageInfo = requireActivity().packageManager.getPackageInfo(requireActivity().packageName, 0)
            val currentVersion = packageInfo.versionName
            val latestVersion = getPlayStoreVersion()

            binding.currentVersion.text = getString(R.string.current_version, currentVersion)
            binding.latestVersion.text = getString(R.string.latest_version, latestVersion)
            if(currentVersion != latestVersion) {
                binding.updateAvailability.text = getString(R.string.update_available)
            } else {
                binding.updateAvailability.text = getString(R.string.up_to_date)
            }
        }

        return binding.root
    }
}