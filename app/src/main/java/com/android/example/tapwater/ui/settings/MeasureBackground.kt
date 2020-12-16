package com.android.example.tapwater.ui.settings

import android.content.Context
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.android.example.tapwater.R

class MeasureBackground(context: Context, attributeSet: AttributeSet): View(context, attributeSet) {
    private val paint = Paint().apply {
        color = context.getColor(R.color.primaryColor)
        style = Paint.Style.FILL
        maskFilter = BlurMaskFilter(150f, BlurMaskFilter.Blur.NORMAL)
    }

    private val textPaint = Paint().apply {
        color = Color.WHITE
        textAlign = Paint.Align.CENTER
        textSize = 60f
    }

    private val framesPerSecond = 60
    private var inflating = false
    private var radius = 300f

    override fun onDraw(canvas: Canvas) {
        if(inflating) {
            radius += 1.6f / 100f * 150f
            if(radius >= 300f) {
                inflating = !inflating
            }
        } else {
            radius -= 1.6f / 100f * 150f
            if(radius <= 150f) {
                inflating = !inflating
            }
        }
        canvas.drawCircle((width/2).toFloat(), (height/2).toFloat(), radius, paint)
        canvas.drawText(context.getString(R.string.measure_background_text), (width/2).toFloat(), (height/2).toFloat() + 10f, textPaint)
        postInvalidateDelayed(1000L/framesPerSecond)
    }

}