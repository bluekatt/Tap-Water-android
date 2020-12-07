package com.android.example.tapwater

import java.math.RoundingMode
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

fun formatCountable(num: Float): String {
    val nf = NumberFormat.getInstance()
    nf.roundingMode = RoundingMode.CEILING
    nf.minimumFractionDigits = 0
    return nf.format(num)
}