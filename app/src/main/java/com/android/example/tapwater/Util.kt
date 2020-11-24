package com.android.example.tapwater

import java.text.NumberFormat

fun floorDouble(num: Double): String {
    val nf = NumberFormat.getInstance()
    nf.maximumFractionDigits = 3
    nf.minimumFractionDigits = 1
    return nf.format(num)
}