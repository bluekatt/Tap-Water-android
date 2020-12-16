package com.android.example.tapwater.ui.summary

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.style.LineBackgroundSpan

class RecordSpan(percentage: Float, val selected: Boolean = false): LineBackgroundSpan {
    val reversePer = 1f - percentage
    private val color = Color.argb(255, (125 + 130 * reversePer).toInt(), (175 + 80 * reversePer).toInt(), (235 + 20 * reversePer).toInt())

    override fun drawBackground(
        canvas: Canvas, paint: Paint,
        left: Int, right: Int, top: Int, baseline: Int, bottom: Int,
        text: CharSequence, start: Int, end: Int, lineNumber: Int,
    ) {
        val oldColor = paint.color
        paint.color = color
        canvas.drawCircle((left+right)/2f, (top+bottom)/2f, if(selected) 40f*1.6f else 40f, paint)
        paint.color = oldColor
    }

}
