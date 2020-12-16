package com.android.example.tapwater.ui.summary

import android.content.Context
import androidx.core.content.ContextCompat
import com.android.example.tapwater.R
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

class SelectionDecorator(val context: Context): DayViewDecorator{
    override fun shouldDecorate(day: CalendarDay): Boolean {
        return true
    }

    override fun decorate(view: DayViewFacade) {
        view.setSelectionDrawable(ContextCompat.getDrawable(context, R.drawable.date_selector)!!)
    }
}