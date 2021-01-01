package com.parkjongseok.tapwater.ui.summary

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.parkjongseok.tapwater.R
import com.parkjongseok.tapwater.getFormattedDate
import com.parkjongseok.tapwater.getFormattedMonth

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
    item?.let {
        text = getFormattedDate(item.mostDrankDate, R.string.stats_date_format, context.resources)
    }
}

@BindingAdapter("monthTitle")
fun TextView.setMonthTitle(item: MonthSummaryItem?) {
    item?.let {
        text = getFormattedMonth(item.month, false, context.resources)
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
