package com.parkjongseok.tapwater

import android.content.res.Resources
import android.os.Build
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.io.IOException
import java.text.NumberFormat

fun floorDecimal(num: Double): String {
    val nf = NumberFormat.getInstance()
    nf.maximumFractionDigits = 3
    nf.minimumFractionDigits = 1
    return nf.format(num)
}

fun floorDecimal(num: Float): String {
    val nf = NumberFormat.getInstance()
    nf.maximumFractionDigits = 3
    nf.minimumFractionDigits = 1
    return nf.format(num)
}

fun dateStringToComponents(date: String): List<Int> {
    val year = date.substring(0, 4).toInt()
    val month = date.substring(4, 6).toInt()
    val day = date.substring(6, 8).toInt()

    return listOf(year, month, day)
}

fun getFormattedTime(timeString: String?, formatType: Int, res: Resources): String {
    val aFirst = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        res.configuration.locales.get(0).language == "ko"
    } else {
        res.configuration.locale.language == "ko"
    }

    if(timeString==null) {
        return ""
    }
    val components = timeString.split(":").map{ it.toInt() }.toMutableList()
    var aMarker = if(aFirst) "오전" else "AM"
    if(components[0]>=12) {
        if(components[0]!=24)
            aMarker = if(aFirst) "오후" else "PM"
        components[0] -= 12
    }
    if(components[0]==0) {
        components[0] = 12
    }
    return if(aFirst) {
        res.getString(formatType, aMarker, components[0])
    } else {
        res.getString(formatType, components[0], aMarker)
    }
}

fun getFormattedDate(dateString: String?, formatType: Int, res: Resources): String {
    val yearFirst = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        res.configuration.locales.get(0).language == "ko"
    } else {
        res.configuration.locale.language == "ko"
    }

    if(dateString==null) {
        return "-"
    }
    val components = dateStringToComponents(dateString).map { it.toString() }
    return if(yearFirst) {
        res.getString(formatType, components[0], components[1], components[2])
    } else {
        val monthText = res.getStringArray(R.array.month_label_short)[components[1].toInt()]
        res.getString(formatType, monthText, components[2], components[0])
    }
}

fun getFormattedMonth(monthString: String?, lineBreak: Boolean, res: Resources): String {
    val yearFirst = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        res.configuration.locales.get(0).language == "ko"
    } else {
        res.configuration.locale.language == "ko"
    }

    if(monthString==null) {
        return "-"
    }
    val year = monthString.substring(0, 4)
    val month = monthString.substring(4, 6).toInt().toString()
    val resourceId = if(lineBreak) R.string.month_format_two_lines else R.string.month_format
    return if (yearFirst) {
        res.getString(resourceId, year, month)
    } else {
        val arrayId = if(lineBreak) R.array.month_label_short else R.array.month_label
        val monthText = res.getStringArray(arrayId)[month.toInt()]
        res.getString(resourceId, monthText, year)
    }
}

fun componentsToDateString(year: Int, month: Int, day: Int): String {
    return String.format("%d%02d%02d", year, month, day)
}
suspend fun getPlayStoreVersion(): String{
    var storeVersion = ""
    withContext(Dispatchers.IO){
        try {
            storeVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=com.parkjongseok.tapwater&hl=en")
                .timeout(30000)
                .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                .referrer("http://www.google.com")
                .get()
                .select("div.hAyfc:nth-child(4) > span:nth-child(2) > div:nth-child(1) > span:nth-child(1)")
                .first()
                .ownText();
        } catch (e: IOException) {
            Log.i("tws", "network error")
        }
    }
    return storeVersion
}

