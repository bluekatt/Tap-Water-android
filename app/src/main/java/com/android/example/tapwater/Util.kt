package com.android.example.tapwater

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

fun componentsToDateString(year: Int, month: Int, day: Int): String {
    return "${year}${month}${day}"
}
suspend fun getPlayStoreVersion(): String{
    var storeVersion = ""
    withContext(Dispatchers.IO){
        try {
            storeVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=com.android.example.tapwater&hl=en")
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

