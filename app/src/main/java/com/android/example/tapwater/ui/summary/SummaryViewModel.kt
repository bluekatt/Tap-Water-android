package com.android.example.tapwater.ui.summary

import android.content.Context
import android.os.Build
import androidx.lifecycle.*
import com.android.example.tapwater.R
import com.android.example.tapwater.componentsToDateString
import com.android.example.tapwater.database.DayRecord
import com.android.example.tapwater.database.DayRecordDao
import com.android.example.tapwater.dateStringToComponents
import com.android.example.tapwater.floorDecimal
import com.prolificinteractive.materialcalendarview.CalendarDay
import kotlinx.coroutines.launch
import javax.inject.Inject

class SummaryViewModel @Inject constructor(
    val dayRecordDao: DayRecordDao,
    val context: Context
) : ViewModel() {
    private val dayOfMonth = arrayOf(0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
    val today = CalendarDay.today()

    val records = dayRecordDao.getAllRecords()

    private val _monthTitle = MutableLiveData<String>()
    val monthTitle: LiveData<String>
        get() = _monthTitle

    private val _selectedRecord = MutableLiveData<DayRecord?>()
    val selectedRecord: LiveData<DayRecord?>
        get() = _selectedRecord

    private val _selectedDate = MutableLiveData<String>()
    val selectedDate: LiveData<String>
        get() = _selectedDate

    private val yearFirst = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        context.resources.configuration.locales.get(0).language == "ko"
    } else {
        context.resources.configuration.locale.language == "ko"
    }

    val firstDate = Transformations.map(records) {
        val firstDateString = it.last().date
        val components = dateStringToComponents(firstDateString)
        CalendarDay.from(components[0], components[1], 1)
    }

    val recordExists = Transformations.map(selectedRecord) {
        selectedRecord.value != null && selectedRecord.value!!.drankToday != 0f
    }

    val selectedPercentage = Transformations.map(selectedRecord) {
        val record = selectedRecord.value
        if(record==null) {
            "0%"
        } else {
            "${floorDecimal(record.drankToday / record.dailyGoal * 100f)}%"
        }
    }

    val selectedDrank = Transformations.map(selectedRecord) {
        val record = selectedRecord.value
        if(record==null) {
            ""
        } else {
            context.getString(R.string.selected_drank_format, record.drankToday)
        }
    }

    val selectedGoal = Transformations.map(selectedRecord) {
        val record = selectedRecord.value
        if(record==null) {
            ""
        } else {
            context.getString(R.string.selected_goal_format, record.dailyGoal)
        }
    }

    val lastDate = CalendarDay.from(today.year, today.month, dayOfMonth[today.month] + if(today.month==2 && today.year%4==0) 1 else 0)

    init {
        setMonthTitle(0, today.month, false)
        setSelectedRecord(today)
    }

    fun setMonthTitle(year: Int, month: Int, showYear: Boolean) {
        val monthText = context.resources.getStringArray(R.array.month_label)[month]
        if(showYear) {
            if(yearFirst) {
                _monthTitle.value = context.getString(R.string.year_format, year) + " " + monthText
            } else {
                _monthTitle.value = monthText + " " + context.getString(R.string.year_format, year)
            }
        } else {
            _monthTitle.value = monthText
        }
    }

    fun setSelectedRecord(date: CalendarDay) {
        viewModelScope.launch {
            val dateString = componentsToDateString(date.year, date.month, date.day)
            _selectedRecord.value = dayRecordDao.get(dateString)
        }
        _selectedDate.value = if(yearFirst) {
            context.getString(R.string.selected_date_format, date.year.toString(), date.month.toString(), date.day.toString())
        } else {
            val monthText = context.resources.getStringArray(R.array.month_label_short)[date.month]
            context.getString(R.string.selected_date_format, monthText, date.day.toString(), date.year.toString())
        }
    }
}