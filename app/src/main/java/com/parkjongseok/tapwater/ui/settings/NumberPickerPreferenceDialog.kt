package com.parkjongseok.tapwater.ui.settings

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.NumberPicker
import androidx.preference.PreferenceDialogFragmentCompat

class NumberPickerPreferenceDialog(): PreferenceDialogFragmentCompat() {
    private lateinit var numberPicker: NumberPicker
    private lateinit var pickerNumbers: Array<String>
    private lateinit var numberPickerPreference: NumberPickerPreference

    override fun onCreateDialogView(context: Context): View {
        numberPicker = NumberPicker(context)
        numberPickerPreference = preference as NumberPickerPreference

        val stepSize = numberPickerPreference.stepSize
        val minValue = numberPickerPreference.minValue / stepSize
        val maxValue = numberPickerPreference.maxValue / stepSize
        val countable = numberPickerPreference.countable
        val formatStringId = numberPickerPreference.formatStringId

        pickerNumbers = Array(((maxValue - minValue+1)/stepSize).toInt()) { i ->
            val value = (i + minValue) * stepSize
            if(formatStringId==0){
                value.toString()
            } else if(countable) {
                context.resources.getQuantityString(formatStringId, value.toInt(), value.toInt())
            } else {
                context.getString(formatStringId, value)
            }
        }

        numberPicker.minValue = minValue.toInt()
        numberPicker.maxValue = maxValue.toInt()
        numberPicker.displayedValues = pickerNumbers
        numberPicker.wrapSelectorWheel = false

        return numberPicker
    }

    override fun onBindDialogView(view: View?) {
        super.onBindDialogView(view)
        val stepSize = numberPickerPreference.stepSize

        numberPicker.value = (numberPickerPreference.getPersistedFloat() / stepSize).toInt()
    }

    override fun onDialogClosed(positiveResult: Boolean) {
        if(positiveResult) {
            val stepSize = numberPickerPreference.stepSize

            numberPicker.clearFocus()
            val newValue = (numberPicker.value) * stepSize
            if(preference.callChangeListener(newValue)) {
                numberPickerPreference.doPersistFloat(newValue)
                preference.summary
            }
        }
    }

    companion object {
        fun newInstance(key: String): NumberPickerPreferenceDialog {
            val fragment = NumberPickerPreferenceDialog()
            val bundle = Bundle(1)
            bundle.putString(ARG_KEY, key)
            fragment.arguments = bundle

            return fragment
        }
    }
}