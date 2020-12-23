package com.android.example.tapwater.ui.summary

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import com.android.example.tapwater.R

class CirclePercentageView(context: Context, attrs: AttributeSet): View(context, attrs) {
    private val bluePaint = Paint().apply {
        color = context.getColor(R.color.primaryColor)
        style = Paint.Style.STROKE
        strokeWidth = 20f
    }

    private val grayPaint = Paint().apply {
        color = context.getColor(R.color.lightGray)
        style = Paint.Style.STROKE
        strokeWidth = 20f
    }

    var percentage = 0f
    private var drew = 0f
    private val framesPerSecond = 60

    override fun onDraw(canvas: Canvas) {
        canvas.drawCircle(measuredWidth/2f, measuredHeight/2f, (measuredWidth-40)/2f, grayPaint)
        canvas.drawArc(20f, 20f, measuredWidth.toFloat()-20f, measuredHeight.toFloat()-20f, 270f, drew, false, bluePaint)

        if(drew<percentage) {
            drew += percentage / 20f
            postInvalidateDelayed(1000L/framesPerSecond)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val width = when (widthMode) {
            MeasureSpec.EXACTLY -> widthSize
            MeasureSpec.AT_MOST -> (paddingLeft + paddingRight + suggestedMinimumWidth)
                .coerceAtMost(widthSize)
            else -> widthMeasureSpec
        }

        val height = when (heightMode) {
            MeasureSpec.EXACTLY -> heightSize
            MeasureSpec.AT_MOST -> (paddingTop + paddingBottom + suggestedMinimumHeight)
                .coerceAtMost(heightSize)
            else -> heightMeasureSpec
        }

        setMeasuredDimension(width, height)
    }
}