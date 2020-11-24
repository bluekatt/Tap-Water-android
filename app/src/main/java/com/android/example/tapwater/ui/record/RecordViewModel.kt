package com.android.example.tapwater.ui.record

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.android.example.tapwater.R
import com.android.example.tapwater.database.DayRecord
import com.android.example.tapwater.database.DayRecordDao
import com.android.example.tapwater.floorDouble
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

const val SPEED  = 92.1

class RecordViewModel @Inject constructor(
    val dayRecordDao: DayRecordDao,
    val context: Context
): ViewModel() {
    private lateinit var todayRecord: DayRecord

    val drankPercentage = MediatorLiveData<Double>()

    private val _drankToday = MutableLiveData<Double>()
    val drankToday: LiveData<Double>
        get() = _drankToday

    private val _dailyGoal = MutableLiveData<Double>()
    val dailyGoal: LiveData<Double>
        get() = _dailyGoal

    private var timer = Timer()

    private val _isDrinking = MutableLiveData<Boolean>()
    val isDrinking: LiveData<Boolean>
        get() = _isDrinking

    val drankPercentageString = Transformations.map(drankPercentage) {
        context.resources.getString(R.string.percentage, floorDouble(it))
    }

    val drankTodayString = Transformations.map(drankToday) {
        context.resources.getString(R.string.drank_today, floorDouble(it))
    }

    val dailyGoalString = Transformations.map(dailyGoal) {
        context.resources.getString(R.string.daily_goal, floorDouble(it))
    }

    init {
        initializeDayRecord()
        _isDrinking.value = false
        timer.scheduleAtFixedRate(object: TimerTask() {
            override fun run() {
                if(isDrinking.value==true) {
                    _drankToday.postValue(_drankToday.value?.plus(0.1 * SPEED / 1000))
                }
            }
        }, 0L, 100L)

        drankPercentage.addSource(drankToday, Observer {
            if(dailyGoal.value==null) {
                drankPercentage.value = 0.0
            } else {
                drankPercentage.value = it / dailyGoal.value!! * 100
            }
        })
        drankPercentage.addSource(dailyGoal, Observer {
            if(drankToday.value==null) {
                drankPercentage.value = 0.0
            } else {
                drankPercentage.value = drankToday.value!! / it * 100
            }
        })
    }

    private fun initializeDayRecord() {
        viewModelScope.launch {
            todayRecord = getTodayRecordFromDB()
            _drankToday.value = todayRecord.drankToday
            _dailyGoal.value = todayRecord.dailyGoal
        }
    }

    private suspend fun getTodayRecordFromDB(): DayRecord {
        val df = SimpleDateFormat("yyyyMMdd", Locale.KOREA)

        var record = dayRecordDao.getTodayRecord()
        while(record==null || record.date!=df.format(Date())) {
            record = DayRecord(dailyGoal = 2.0, date = df.format(Date()))
            dayRecordDao.insert(record)
            record = dayRecordDao.getTodayRecord()
        }
        return record
    }

    fun onDrinkClicked() {
        _isDrinking.value = true
    }

    fun onStopClicked() {
        _isDrinking.value = false
        todayRecord.drankToday = drankToday.value!!
        viewModelScope.launch {
            updateRecordToDB(todayRecord)
        }
    }

    private suspend fun updateRecordToDB(record: DayRecord) {
        dayRecordDao.update(record)
    }
}