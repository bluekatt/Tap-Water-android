package com.android.example.tapwater.ui.record

import android.content.Context
import androidx.lifecycle.*
import com.android.example.tapwater.R
import com.android.example.tapwater.database.DayRecord
import com.android.example.tapwater.database.DayRecordDao
import com.android.example.tapwater.getFormattedDate
import com.android.example.tapwater.getFormattedTime
import kotlinx.coroutines.launch
import javax.inject.Inject

class RecordDetailViewModel @Inject constructor(
    private val dayRecordDao: DayRecordDao,
    private val context: Context
): ViewModel() {
    var lastTime = 0

    private var detailDate = ""
    private val _showChart = MutableLiveData<Boolean>()
    val showChart: LiveData<Boolean>
        get() = _showChart

    private val _record = MutableLiveData<DayRecord>()
    val record: LiveData<DayRecord>
        get() = _record

    private val _selectedTime = MutableLiveData<Float>()
    val selectedTime: LiveData<Float>
        get() = _selectedTime

    private val _selectedDrank = MutableLiveData<String>()
    val selectedDrank: LiveData<String>
        get() = _selectedDrank

    val recordDate = Transformations.map(record) {
        getFormattedDate(it.date, R.string.stats_date_format, context.resources)
    }

    val drank = Transformations.map(record) {
        context.getString(R.string.liter_format, it.drankToday)
    }

    val goal = Transformations.map(record) {
        context.getString(R.string.liter_format, it.dailyGoal)
    }

    val average = Transformations.map(record) {
        context.getString(R.string.liter_format, it.drankToday / 24f)
    }

    val selectedTimeFormatted = Transformations.map(selectedTime) {
        if(it.toInt()!=lastTime+1)
            getFormattedTime("${it.toInt()}:00", R.string.time_format, context.resources)
        else
            context.getString(R.string.record_detail_now)
    }

    val mostDrankTime = Transformations.map(record) {
        var mostDrank = 0f
        var time = "-"
        var drankInTime = 0f
        var lastHour = 0
        it.drinkLog.forEach { item ->
            val hour = item.time.substring(0,2).toInt()
            if(hour != lastHour) {
                if(drankInTime>mostDrank) {
                    time = getFormattedTime("${lastHour}:00", R.string.time_format, context.resources) +
                            " - " +
                            getFormattedTime("${lastHour+1}:00", R.string.time_format, context.resources)
                    mostDrank = drankInTime
                    drankInTime = 0f
                }
            }
            drankInTime += item.volume
            lastHour = hour
        }
        if(drankInTime>mostDrank) {
            time = getFormattedTime("${lastHour}:00", R.string.time_format, context.resources) +
                    " - " +
                    getFormattedTime("${lastHour+1}:00", R.string.time_format, context.resources)
            mostDrank = drankInTime
        }
        time
    }

    var firstShow = true

    init {
        _showChart.value = true
    }

    fun onChartButtonClicked() {
        _showChart.value = true
    }

    fun onListButtonClicked() {
        _showChart.value = false
    }

    fun setRecord(date: String?) {
        if(date==null) {
            return
        }
        viewModelScope.launch {
            getRecordFromDB(date)
        }
    }

    fun setSelectedTimeAndVolume(time: Float, volume: Float) {
        _selectedTime.value = time
        _selectedDrank.value = context.getString(R.string.liter_format, volume)
    }

    fun removeRecord() {
        val todayRecord = record.value
        if(todayRecord!=null) {
            viewModelScope.launch {
                val recordToRemove = todayRecord.drinkLog.last()
                val newLog = todayRecord.drinkLog.toMutableList()
                newLog.removeLast()
                todayRecord.drankToday -= recordToRemove.volume
                todayRecord.drinkLog = newLog
                updateRecordToDB(todayRecord)
                getRecordFromDB(detailDate)
            }
        }
    }

    private suspend fun getRecordFromDB(date: String) {
        detailDate = date
        _record.value = dayRecordDao.get(date)
    }

    private suspend fun updateRecordToDB(record: DayRecord) {
        dayRecordDao.update(record)
    }
}