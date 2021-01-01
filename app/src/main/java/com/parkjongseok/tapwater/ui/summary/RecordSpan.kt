package com.parkjongseok.tapwater.ui.summary

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.style.LineBackgroundSpan

class RecordSpan(percentage: Float, val selected: Boolean = false): LineBackgroundSpan {
    private val reversePer = if(1f - percentage>0) 1f-percentage else 0f
    private val color = Color.argb(255, (125 + 130 * reversePer).toInt(), (175 + 80 * reversePer).toInt(), (235 + 20 * reversePer).toInt())
    private val cornerColor = Color.argb(255, 125, 175, 235)

    override fun drawBackground(
        canvas: Canvas, paint: Paint,
        left: Int, right: Int, top: Int, baseline: Int, bottom: Int,
        text: CharSequence, start: Int, end: Int, lineNumber: Int,
    ) {
        val oldColor = paint.color
        val oldStyle = paint.style
        val oldStrokeWidth = paint.strokeWidth

        paint.color = color
        canvas.drawCircle((left+right)/2f, (top+bottom)/2f, if(selected) 40f*1.5f else 40f, paint)

        paint.color = cornerColor
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 4f
        if(selected) {
            canvas.drawCircle((left+right)/2f, (top+bottom)/2f, 40f*1.5f, paint)
        }

        paint.color = oldColor
        paint.style = oldStyle
        paint.strokeWidth = oldStrokeWidth
    }

}
