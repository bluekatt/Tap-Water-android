package com.android.example.tapwater.ui.summary

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.lifecycle.*
import com.android.example.tapwater.*
import com.android.example.tapwater.R
import com.android.example.tapwater.database.DayRecord
import com.android.example.tapwater.database.DayRecordDao
import com.prolificinteractive.materialcalendarview.CalendarDay
import kotlinx.coroutines.launch
import javax.inject.Inject

class SummaryViewModel @Inject constructor(
    val dayRecordDao: DayRecordDao,
    val context: Context
) : ViewModel() {
    private val dayOfMonth = arrayOf(0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
    private val today = CalendarDay.today()

    val records = dayRecordDao.getAllRecords()

    private val _monthTitle = MutableLiveData<String>()
    val monthTitle: LiveData<String>
        get() = _monthTitle

    private val _selectedRecord = MutableLiveData<DayRecord?>()
    val selectedRecord: LiveData<DayRecord?>
        get() = _selectedRecord

    private val _selectedDate = MutableLiveData<CalendarDay>()
    val selectedDate: LiveData<CalendarDay>
        get() = _selectedDate

    private val _navigateToMonthSummary = MutableLiveData<Boolean>()
    val navigateToMonthSummary: LiveData<Boolean>
        get() = _navigateToMonthSummary

    private val _monthItemList = MutableLiveData<List<MonthSummaryItem>>()
    val monthItemList: LiveData<List<MonthSummaryItem>>
        get() = _monthItemList

    private val _navigateToDetail = MutableLiveData<String?>()
    val navigateToDetail: LiveData<String?>
        get() = _navigateToDetail

    private val yearFirst = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        context.resources.configuration.locales.get(0).language == "ko"
    } else {
        context.resources.configuration.locale.language == "ko"
    }

    val firstDate = Transformations.map(records) {
        if(it.isEmpty()) {
            CalendarDay.from(today.year, today.month, 1)
        } else {
            val firstDateString = it.last().date
            val components = dateStringToComponents(firstDateString)
            CalendarDay.from(components[0], components[1], 1)
        }
    }

    val recordExists = Transformations.map(selectedRecord) {
        selectedRecord.value != null && selectedRecord.value!!.drankToday != 0f
    }

    val selectedPercentage = Transformations.map(selectedRecord) {
        val record = selectedRecord.value
        if(record==null) {
            "0%"
        } else {
            context.getString(R.string.percentage_format, record.drankToday / record.dailyGoal * 100f)
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

    val selectedDateText = Transformations.map(selectedDate) {
        val date = selectedDate.value
        if(date!=null) {
            if(yearFirst) {
                context.getString(R.string.selected_date_format, date.year.toString(), date.month.toString(), date.day.toString())
            } else {
                val monthText = context.resources.getStringArray(R.array.month_label_short)[date.month]
                context.getString(R.string.selected_date_format, monthText, date.day.toString(), date.year.toString())
            }
        } else {
            ""
        }
    }

    val totalDrankText = Transformations.map(records) { records ->
        var total = 0f
        records.forEach {
            total += it.drankToday
        }
        context.getString(R.string.liter_format, total)
    }

    val achievedDays = Transformations.map(records) { records ->
        var days = 0
        records.forEach {
            if(it.drankToday>=it.dailyGoal) days++
        }
        context.resources.getQuantityString(R.plurals.day_format, days, days)
    }

    val mostDrank = Transformations.map(records) { records ->
        var mostDrank = 0f
        records.forEach { record ->
            if(mostDrank <= record.drankToday) {
                mostDrank = record.drankToday
            }
        }
        context.getString(R.string.liter_format, mostDrank)
    }

    val mostDrankDate = Transformations.map(records) { records ->
        var mostDrank = 0f
        var date = ""
        records.forEach { record ->
            if(mostDrank <= record.drankToday) {
                mostDrank = record.drankToday
                date = record.date
            }
        }
        getFormattedDate(date, R.string.stats_date_format, context.resources)
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
        _selectedDate.value = date
    }

    fun onStatsButtonClicked() {
        _navigateToMonthSummary.value = true
        val itemList: MutableList<MonthSummaryItem> = mutableListOf()
        records.value?.forEach { record ->
            val item = itemList.find { it.month == record.date.substring(0, 6) }
            if(item == null) {
                if(record.drankToday!=0f) itemList.add(MonthSummaryItem(record))
                else itemList.add(MonthSummaryItem(record.date.substring(0, 6)))
            } else {
                if(record.drankToday!=0f) {
                    item.totalGoal += record.dailyGoal
                    item.totalDrank += record.drankToday
                    item.achievedDays += if(record.drankToday >= record.dailyGoal) 1 else 0
                    if(item.mostDrank <= record.drankToday) {
                        item.mostDrankDate = record.date
                        item.mostDrank = record.drankToday
                    }
                }
            }
        }
        itemList.sortBy { it.month }
        val iterator = itemList.listIterator()
        while(iterator.hasNext()) {
            val cur = iterator.next()
            if(!iterator.hasNext()) break
            val next = iterator.next()
            val nextMonth = if(cur.month.toInt()%100 == 12) {
                "${cur.month.toInt()+100-11}"
            } else {
                "${cur.month.toInt()+1}"
            }
            if(nextMonth != next.month){
                iterator.previous()
                iterator.add(MonthSummaryItem(nextMonth))
                iterator.previous()
            }
        }
        _monthItemList.value = itemList
    }

    fun onStatsNavigated() {
        _navigateToMonthSummary.value = false
    }

    fun onDetailButtonClicked() {
        _navigateToDetail.value = selectedRecord.value?.date
    }

    fun onDetailNavigated() {
        _navigateToDetail.value = null
    }
}