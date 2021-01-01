package com.parkjongseok.tapwater.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.parkjongseok.tapwater.database.DayRecordDao
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val dayRecordDao: DayRecordDao
): ViewModel(){
    fun removeData() {
        viewModelScope.launch {
            dayRecordDao.deleteAll()
        }
    }
}