package com.android.example.tapwater.ui.record

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import androidx.preference.PreferenceManager
import com.android.example.tapwater.R
import com.android.example.tapwater.database.DayRecord
import com.android.example.tapwater.database.DayRecordDao
import com.android.example.tapwater.floorDecimal
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

const val SPEED  = 92.1
private const val DEFAULT_GOAL = 2f

class RecordViewModel @Inject constructor(
    val dayRecordDao: DayRecordDao,
    val context: Context
): ViewModel() {
    private lateinit var todayRecord: DayRecord

    val drankPercentage = MediatorLiveData<Float>()

    private val _drankToday = MutableLiveData<Float>()
    val drankToday: LiveData<Float>
        get() = _drankToday

    private val _dailyGoal = MutableLiveData<Float>()
    val dailyGoal: LiveData<Float>
        get() = _dailyGoal

    private var timer = Timer()

    private val _isDrinking = MutableLiveData<Boolean>()
    val isDrinking: LiveData<Boolean>
        get() = _isDrinking

    val drankPercentageString = Transformations.map(drankPercentage) {
        context.resources.getString(R.string.percentage, floorDecimal(it))
    }

    val drankTodayString = Transformations.map(drankToday) {
        context.resources.getString(R.string.drank_today, floorDecimal(it))
    }

    val dailyGoalString = Transformations.map(dailyGoal) {
        context.resources.getString(R.string.daily_goal, floorDecimal(it))
    }

    init {
        _isDrinking.value = false
        _dailyGoal.value = PreferenceManager.getDefaultSharedPreferences(context).getFloat("daily_goal", DEFAULT_GOAL)

        initializeDayRecord()

        timer.scheduleAtFixedRate(object: TimerTask() {
            override fun run() {
                if(isDrinking.value==true) {
                    _drankToday.postValue(_drankToday.value?.plus((0.1 * SPEED / 1000).toFloat()))
                }
            }
        }, 0L, 100L)

        drankPercentage.addSource(drankToday, Observer {
            if(dailyGoal.value==null) {
                drankPercentage.value = 0f
            } else {
                drankPercentage.value = it / dailyGoal.value!! * 100
            }
        })
        drankPercentage.addSource(dailyGoal, Observer {
            if(drankToday.value==null) {
                drankPercentage.value = 0f
            } else {
                drankPercentage.value = drankToday.value!! / it * 100
            }
        })
    }

    fun initializeDayRecord() {
        viewModelScope.launch {
            todayRecord = getTodayRecordFromDB()
            _drankToday.value = todayRecord.drankToday
            if(todayRecord.dailyGoal!=dailyGoal.value && dailyGoal.value!=null) {
                todayRecord.dailyGoal = dailyGoal.value!!
                updateRecordToDB(todayRecord)
            }
        }
    }

    private suspend fun getTodayRecordFromDB(): DayRecord {
        val df = SimpleDateFormat("yyyyMMdd", Locale.getDefault())

        val today = df.format(Date())

        var record = dayRecordDao.get(today)

        while(record==null) {
            record = DayRecord(dailyGoal = dailyGoal.value ?: DEFAULT_GOAL, date = df.format(Date()))
            dayRecordDao.insert(record)
            record = dayRecordDao.get(today)
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