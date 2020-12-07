package com.android.example.tapwater.ui.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.android.example.tapwater.R

class SettingsFragment : PreferenceFragmentCompat() {
    private val DIALOG_FRAGMENT_TAG = "NumberPickerDialog"

    override fun onResume() {
        super.onResume()
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    override fun onDisplayPreferenceDialog(preference: Preference?) {
        if (parentFragmentManager.findFragmentByTag(DIALOG_FRAGMENT_TAG) != null) {
            return
        }
        if (preference is NumberPickerPreference) {
            val dialog = NumberPickerPreferenceDialog.newInstance(preference.key)
            dialog.setTargetFragment(this, 0)
            dialog.show(parentFragmentManager, DIALOG_FRAGMENT_TAG)
        } else {
            super.onDisplayPreferenceDialog(preference)
        }
    }
}