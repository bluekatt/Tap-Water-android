package com.parkjongseok.tapwater.ui.settings

import android.content.Context
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.parkjongseok.tapwater.R
import javax.inject.Inject

class SpeedMeasureViewModel @Inject constructor(
    val context: Context
) : ViewModel() {
    val volume = ObservableField<String>()

    private val _speed = MutableLiveData<Float>()
    val speed: LiveData<Float>
        get() = _speed

    private val _measuredCups = MutableLiveData<Int>()
    val measuredCups: LiveData<Int>
        get() = _measuredCups

    private val _measureCompleted = MutableLiveData<Boolean>()
    val measureComplete: LiveData<Boolean>
        get() = _measureCompleted

    private val _measuring = MutableLiveData<Boolean>()
    val measuring: LiveData<Boolean>
        get() = _measuring

    private val _manualInput = MutableLiveData<Boolean>()
    val manualInput: LiveData<Boolean>
        get() = _manualInput

    private val _navigateToHelpInfo = MutableLiveData<Boolean>()
    val navigateToHelpInfo: LiveData<Boolean>
        get() = _navigateToHelpInfo

    private val _navigateToVolume = MutableLiveData<Boolean>()
    val navigateToVolume: LiveData<Boolean>
        get() = _navigateToVolume

    private val _navigateToCups = MutableLiveData<Boolean>()
    val navigateToCups: LiveData<Boolean>
        get() = _navigateToCups

    private val _navigateToGuide = MutableLiveData<Boolean>()
    val navigateToGuide: LiveData<Boolean>
        get() = _navigateToGuide

    val buttonEnabled = Transformations.map(measuredCups) {
        List(3) { index ->
            index == it
        }
    }

    val speedText = Transformations.map(speed) {
        if(speed.value != null && speed.value != 0f) {
            context.getString(R.string.speed_format, speed.value)
        } else {
            ""
        }
    }

    private var startTime = 0L

    fun initializeData() {
        volume.set("")
        _speed.value = 0f
        _measuredCups.value = 0
        _measuring.value = false
        _manualInput.value = false
    }

    init {
        initializeData()
    }

    fun onManualInputClicked() {
        _manualInput.value = true
    }

    fun onManualInputEnabled() {
        _manualInput.value = false
    }

    fun onCupClicked() {
        startTime = System.currentTimeMillis()
        _measuring.value = true
    }

    fun onDrinkCompleteClicked() {
        val duration = (System.currentTimeMillis() - startTime)
        if(speed.value != null && measuredCups.value != null) {
            _speed.value = (_speed.value!! * _measuredCups.value!! + volume.get()!!.toFloat() / duration * 1000) / (_measuredCups.value!! + 1)
            _measuredCups.value = _measuredCups.value!! + 1
        }
        _measuring.value = false
    }

    fun willBeDismissed() {
        _measureCompleted.value = false
    }

    fun onCupTypeClicked(vol: String) {
        volume.set(vol)
    }

    fun onResetClicked() {
        initializeData()
        _navigateToGuide.value = true
    }

    fun onResetNavigated() {
        _navigateToGuide.value = false
    }

    fun onGuideNextButtonClicked() {
        _navigateToVolume.value = true
    }

    fun onGuideNextNavigated() {
        _navigateToVolume.value = false
    }

    fun onSetVolumeClicked() {
        _navigateToCups.value = true
    }

    fun onSetVolumeNavigated() {
        _navigateToCups.value = false
    }

    fun onHelpInfoButtonClicked() {
        _navigateToHelpInfo.value = true
    }

    fun onHelpInfoNavigated() {
        _navigateToHelpInfo.value = false
    }

    fun onMeasureCompleteClicked() {
        _measureCompleted.value = true
    }
}