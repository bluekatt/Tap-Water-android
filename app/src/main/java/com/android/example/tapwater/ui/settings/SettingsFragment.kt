package com.android.example.tapwater.ui.settings

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.android.example.tapwater.MyApplication
import com.android.example.tapwater.R
import javax.inject.Inject

class SettingsFragment : PreferenceFragmentCompat() {
    private val DIALOG_FRAGMENT_TAG = "NumberPickerDialog"

    @Inject lateinit var viewModel: SettingsViewModel

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        (requireActivity().application as MyApplication).appComponent.inject(this)
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        findPreference<Preference>("remove_record")?.setOnPreferenceClickListener {
            val builder = AlertDialog.Builder(requireActivity())
            builder.setTitle(getString(R.string.dialog_remove_record_title))
            builder.setMessage(getString(R.string.dialog_remove_record_content))

            builder.setNegativeButton(getString(R.string.dialog_cancel_button)) { dialog, id ->
                dialog.cancel()
            }

            builder.setPositiveButton(getString(R.string.dialog_remove_button)) { _, _ ->
                viewModel.removeData()
                Toast.makeText(requireContext(), getString(R.string.remove_toast), Toast.LENGTH_LONG).show()
            }

            builder.show()
            true
        }
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