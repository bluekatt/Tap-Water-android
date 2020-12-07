package com.android.example.tapwater.ui.settings

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.withStyledAttributes
import androidx.preference.DialogPreference
import com.android.example.tapwater.R

class NumberPickerPreference(context: Context, attrs: AttributeSet): DialogPreference(context, attrs) {
    var minValue = 0f
    var maxValue = 0f
    var stepSize = 0f
    var formatStringId = 0
    var countable = false
    var defaultNumber = 0f

    init {
        context.withStyledAttributes(attrs, R.styleable.NumberPickerPreference) {
            stepSize = getFloat(R.styleable.NumberPickerPreference_stepSize, 0f)
            minValue = getFloat(R.styleable.NumberPickerPreference_minValue, 0f)
            maxValue = getFloat(R.styleable.NumberPickerPreference_maxValue, 2f)
            countable = (stepSize>=1)
            formatStringId = getResourceId(R.styleable.NumberPickerPreference_formatString, 0)

            defaultNumber = getFloat(R.styleable.NumberPickerPreference_defaultNumber, 0f)
            doPersistFloat(defaultNumber)
        }
    }

    fun getPersistedFloat() = getPersistedFloat(defaultNumber)

    fun doPersistFloat(value: Float) {
        super.persistFloat(value)
        notifyChanged()
    }

    override fun getSummary(): CharSequence {
        if(formatStringId==0) {
            return (getPersistedFloat()).toString()
        } else if(countable) {
            return context.resources.getQuantityString(
                formatStringId,
                getPersistedFloat().toInt(),
                getPersistedFloat().toInt()
            )
        } else {
            return context.getString(formatStringId, getPersistedFloat())
        }
    }
}