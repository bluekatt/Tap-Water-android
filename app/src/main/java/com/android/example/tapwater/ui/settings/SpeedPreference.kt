package com.android.example.tapwater.ui.settings

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.withStyledAttributes
import androidx.preference.Preference
import com.android.example.tapwater.R

class SpeedPreference(context: Context, attrs: AttributeSet): Preference(context, attrs) {
    var defaultSpeed = 0f
    init {
        context.withStyledAttributes(attrs, R.styleable.SpeedPreference) {
            defaultSpeed = getFloat(R.styleable.SpeedPreference_defaultSpeed, 0f)
            doPersistFloat(defaultSpeed)
        }
    }

    fun getPersistedFloat() = getPersistedFloat(defaultSpeed)

    fun doPersistFloat(value: Float) {
        super.persistFloat(value)
        notifyChanged()
    }

    override fun getSummary(): CharSequence {
        return context.getString(R.string.speed_format, getPersistedFloat())
    }
}