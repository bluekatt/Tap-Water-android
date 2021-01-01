package com.android.parkjongseok.tapwater.ui.settings

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.android.parkjongseok.tapwater.MyApplication
import com.android.parkjongseok.tapwater.R
import javax.inject.Inject

class SettingsFragment : PreferenceFragmentCompat() {
    private val DIALOG_FRAGMENT_TAG = "NumberPickerDialog"

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    val viewModel: SettingsViewModel by viewModels { viewModelFactory }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        (requireActivity().application as MyApplication).appComponent.inject(this)
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        findPreference<Preference>("remove_record")?.setOnPreferenceClickListener {
            val builder = AlertDialog.Builder(requireActivity())
            builder.setTitle(getString(R.string.dialog_remove_record_title))
            builder.setMessage(getString(R.string.dialog_remove_record_content))

            builder.setNegativeButton(getString(R.string.dialog_cancel_button)) { dialog, _ ->
                dialog.cancel()
            }

            builder.setPositiveButton(getString(R.string.dialog_remove_button)) { _, _ ->
                viewModel.removeData()
                Toast.makeText(requireContext(), getString(R.string.clear_toast), Toast.LENGTH_LONG).show()
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