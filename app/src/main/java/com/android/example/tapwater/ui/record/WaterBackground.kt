package com.android.example.tapwater.ui.record

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.android.example.tapwater.R
import kotlin.math.sin

class WaterBackground(context: Context, attributeSet: AttributeSet): View(context, attributeSet) {
    var percentage = 0F

    private val framesPerSecond = 60
    private var startTime = 0L

    private val path = Path()
    private val paint = Paint().apply {
        color = context.resources.getColor(R.color.primaryTransparentColor, null)
        style = Paint.Style.FILL
    }

    init {
        startTime = System.currentTimeMillis()
        postInvalidate()
    }

    override fun onDraw(canvas: Canvas) {
        val elapsed = (System.currentTimeMillis() - startTime).toFloat()
        drawWater(elapsed)
        canvas.drawPath(path, paint)
        postInvalidateDelayed(1000L/framesPerSecond)
    }

    private fun drawWater(elapsed: Float) {
        path.reset()
        val fillHeight = height * percentage / 100F
        val waveCount = 5
        val waveHeight = if(percentage==0F) 0F else 15F
        path.moveTo(0F, height.toFloat())
        path.lineTo(0F, sin(elapsed*2/1000) + height - fillHeight)
        var prevX = 0F
        var prevY = sin(elapsed*2/1000) + height - fillHeight
        (1..waveCount+1).forEach {
            val x = (it.toFloat() / (waveCount)) * width.toFloat()
            val y = sin(it + elapsed*2/1000) * waveHeight + height - fillHeight
            path.quadTo(prevX, prevY, (x+prevX)/2, (y+prevY)/2)
            prevX = x
            prevY = y
        }
        path.lineTo(width.toFloat(), height.toFloat())
        path.lineTo(0F, height.toFloat())
    }
}