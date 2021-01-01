package com.android.parkjongseok.tapwater.ui.summary

import android.graphics.Typeface
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

class SelectedDayDecorator(var date: CalendarDay?, var percentage: Float): DayViewDecorator {

    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return date == day
    }

    override fun decorate(view: DayViewFacade) {
        view.addSpan(RelativeSizeSpan(1.2f))
        view.addSpan(StyleSpan(Typeface.BOLD))
        view.addSpan(RecordSpan(percentage, true))
    }
}