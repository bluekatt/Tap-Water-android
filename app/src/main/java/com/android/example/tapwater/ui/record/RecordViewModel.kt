package com.android.example.tapwater.ui.record

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import androidx.preference.PreferenceManager
import com.android.example.tapwater.R
import com.android.example.tapwater.database.DayRecord
import com.android.example.tapwater.database.DayRecordDao
import com.android.example.tapwater.database.DrinkLogItem
import com.android.example.tapwater.floorDecimal
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class RecordViewModel @Inject constructor(
    val dayRecordDao: DayRecordDao,
    val context: Context
): ViewModel() {
    private val defaultSpeed  = 92f
    private val defaultGoal = 2f
    private val dateDF = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
    private val timeDF = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

    lateinit var todayRecord: DayRecord

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

    private val _navigateToDetail = MutableLiveData<Boolean>()
    val navigateToDetail: LiveData<Boolean>
        get() = _navigateToDetail

    val drankPercentageString = Transformations.map(drankPercentage) {
        context.getString(R.string.percentage_format, it)
    }

    val drankTodayString = Transformations.map(drankToday) {
        context.resources.getString(R.string.drank_today, floorDecimal(it))
    }

    val dailyGoalString = Transformations.map(dailyGoal) {
        context.resources.getString(R.string.daily_goal, floorDecimal(it))
    }

    val showCompleted = Transformations.map(isDrinking) {
        if(!it) {
            val alreadyCompleted = PreferenceManager.getDefaultSharedPreferences(context).getBoolean("already_completed", false)
            if(drankPercentage.value!!>=100f && !alreadyCompleted) {
                PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean("already_completed", true).apply()
                true
            } else {
                false
            }
        } else {
            false
        }
    }

    init {
        _isDrinking.value = false
        _dailyGoal.value = PreferenceManager.getDefaultSharedPreferences(context).getFloat("daily_goal", defaultGoal)

        initializeDayRecord()

        timer.scheduleAtFixedRate(object: TimerTask() {
            override fun run() {
                val speed = PreferenceManager.getDefaultSharedPreferences(context).getFloat("speed", defaultSpeed)
                if(isDrinking.value==true) {
                    _drankToday.postValue(_drankToday.value?.plus((0.1 * speed / 1000).toFloat()))
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
        val today = dateDF.format(Date())

        var record = dayRecordDao.get(today)

        while(record==null) {
            record = DayRecord(dailyGoal = dailyGoal.value ?: defaultGoal, date = dateDF.format(Date()))
            dayRecordDao.insert(record)
            record = dayRecordDao.get(today)
            PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean("already_completed", false).apply()
        }
        return record
    }

    fun onDrinkClicked() {
        _isDrinking.value = true
    }

    fun onStopClicked() {
        _isDrinking.value = false

        val currentTime = timeDF.format(Date())

        val drinkLog = todayRecord.drinkLog.toMutableList()
        drinkLog.add(DrinkLogItem(currentTime, drankToday.value!! - todayRecord.drankToday))
        todayRecord.drinkLog = drinkLog
        todayRecord.drankToday = drankToday.value!!
        viewModelScope.launch {
            updateRecordToDB(todayRecord)
        }
    }

    fun onDetailClicked() {
        _navigateToDetail.value = true
    }

    fun onDetailNavigated() {
        _navigateToDetail.value = false
    }

    private suspend fun updateRecordToDB(record: DayRecord) {
        dayRecordDao.update(record)
    }
}