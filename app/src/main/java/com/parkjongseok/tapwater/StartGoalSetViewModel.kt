package com.parkjongseok.tapwater

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.preference.PreferenceManager
import javax.inject.Inject

class StartGoalSetViewModel @Inject constructor(
    val context: Context
) : ViewModel() {
    private val _navigateToSpeedMeasure = MutableLiveData<Boolean>()
    val navigateToSpeedMeasure: LiveData<Boolean>
        get() = _navigateToSpeedMeasure

    fun onCompletedClick(value: Float) {
        val pref = PreferenceManager.getDefaultSharedPreferences(context)
        pref.edit().putFloat("daily_goal", value).apply()
        _navigateToSpeedMeasure.value = true
    }
}