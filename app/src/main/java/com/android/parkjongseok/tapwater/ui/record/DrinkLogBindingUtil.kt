package com.android.parkjongseok.tapwater.ui.record

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.android.parkjongseok.tapwater.R
import com.android.parkjongseok.tapwater.database.DrinkLogItem

@BindingAdapter("drank")
fun TextView.setDrank(item: DrinkLogItem?) {
    item?.let {
        text = context.getString(R.string.liter_format, item.volume)
    }
}

@BindingAdapter("logTime")
fun TextView.setLogTime(item: DrinkLogItem?) {
    item?.let {
        text = item.time
    }
}

