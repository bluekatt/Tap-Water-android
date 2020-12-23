package com.android.example.tapwater.ui.summary

import android.os.Build
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.android.example.tapwater.R
import com.android.example.tapwater.dateStringToComponents

@BindingAdapter("totalGoal")
fun TextView.setTotalGoal(item: MonthSummaryItem?) {
    item?.let {
        text = context.getString(R.string.liter_format, item.totalGoal)
    }
}

@BindingAdapter("totalDrank")
fun TextView.setTotalDrank(item: MonthSummaryItem?) {
    item?.let {
        text = context.getString(R.string.liter_format, item.totalDrank)
    }
}

@BindingAdapter("achievedDays")
fun TextView.setAchievedDays(item: MonthSummaryItem?) {
    item?.let {
        text = context.resources.getQuantityString(R.plurals.day_format, item.achievedDays, item.achievedDays)
    }
}

@BindingAdapter("mostDrank")
fun TextView.setMostDrank(item: MonthSummaryItem?) {
    item?.let {
        text = context.getString(R.string.liter_format, item.mostDrank)
    }
}

@BindingAdapter("mostDrankDate")
fun TextView.setMostDrankDate(item: MonthSummaryItem?) {
    val yearFirst = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        context.resources.configuration.locales.get(0).language == "ko"
    } else {
        context.resources.configuration.locale.language == "ko"
    }

    item?.let {
        val components = dateStringToComponents(item.mostDrankDate).map { it.toString() }
        text = if(yearFirst) {
            context.getString(R.string.stats_date_format, components[0], components[1], components[2])
        } else {
            val monthText = context.resources.getStringArray(R.array.month_label_short)[components[1].toInt()]
            context.getString(R.string.stats_date_format, monthText, components[2], components[0])
        }
    }
}

@BindingAdapter("monthTitle")
fun TextView.setMonthTitle(item: MonthSummaryItem?) {
    val yearFirst = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        context.resources.configuration.locales.get(0).language == "ko"
    } else {
        context.resources.configuration.locale.language == "ko"
    }

    item?.let {
        val year = item.month.substring(0, 4)
        val month = item.month.substring(4, 6)
        text = if (yearFirst) {
            context.getString(R.string.stats_month_format, year, month)
        } else {
            val monthText =
                context.resources.getStringArray(R.array.month_label)[month.toInt()]
            context.getString(R.string.stats_month_format, monthText, year)
        }
    }
}

@BindingAdapter("percentage")
fun CirclePercentageView.setPercentage(item: MonthSummaryItem) {
    percentage = if(item.totalGoal!=0f) {
        360f * item.totalDrank / item.totalGoal
    } else {
        0f
    }
}

@BindingAdapter("percentage")
fun TextView.setPercentage(item: MonthSummaryItem?) {
    item?.let {
        text = if(item.totalGoal!=0f) {
            context.getString(R.string.percentage_format, item.totalDrank / item.totalGoal * 100f)
        } else {
            "0%"
        }
    }
}
