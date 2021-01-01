package com.android.parkjongseok.tapwater.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.parkjongseok.tapwater.database.DayRecordDao
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