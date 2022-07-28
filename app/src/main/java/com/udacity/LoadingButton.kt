package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var bgColor = Color.YELLOW
    private var textColor = Color.BLACK
    private var textSizeFloat = 70f
    private var text = "Nothing"
    private var widthSize = 0
    private var heightSize = 0
    private var textX = 0f
    private var textY = 0f

    private var bgRect = Rect(0, 0, 100, 100)

    private val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
    }

    private val valueAnimator = ValueAnimator()

    private var buttonState: ButtonState by Delegates.observable(ButtonState.Completed) { p, old, new ->

    }

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.LoadingButton, 0, 0).apply {
            try {
                bgColor = getColor(R.styleable.LoadingButton_bgColor, Color.YELLOW)
                textColor = getColor(R.styleable.LoadingButton_textColor, Color.BLACK)
                text = getString(R.styleable.LoadingButton_text) ?: "Nothing"
            } finally {
                recycle()
            }
        }
        bgPaint.color = bgColor
        textPaint.color = textColor
        textPaint.textSize = textSizeFloat
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minWidth: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        widthSize = resolveSizeAndState(minWidth, widthMeasureSpec, 1)
        heightSize = resolveSizeAndState(MeasureSpec.getSize(widthSize), heightMeasureSpec, 0)
        bgRect.right = widthSize
        bgRect.bottom = heightSize
        textX = bgRect.centerX().toFloat()
        textY = (bgRect.centerY().toFloat() - ((textPaint.descent() + textPaint.ascent()) / 2))
        setMeasuredDimension(widthSize, heightSize)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.apply {
            drawRect(bgRect, bgPaint)
            drawText(text, textX, textY, textPaint)
        }
    }

}