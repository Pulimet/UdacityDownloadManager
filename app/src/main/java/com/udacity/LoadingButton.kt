package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.core.content.ContextCompat
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private const val RECT_PADDING = 30f
    }

    private var isAnimated = false
    private var bgColor = Color.YELLOW
    private var textColor = Color.BLACK
    private var textSizeFloat = 70f
    private var btnCurrentText = "Nothing"
    private var btnLoadingText = "We are loading"
    private var btnRegularText = ""
    private var widthSize = 0
    private var heightSize = 0
    private var hCenter = 0
    private var textX = 0f
    private var textY = 0f
    private var animInitialValue = 0

    // Rect
    private var bgRect = Rect(0, 0, 100, 100)
    private var bgRectAnim = Rect(0, 0, 100, 100)
    private val arcRect: RectF = RectF(0f, 0f, 0f, 0f)
    private val textBound = Rect()

    // Paints
    private val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bgAnimPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.bg_anim)
    }
    private val arcPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.arc)
    }
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
    }

    private var buttonState: ButtonState by Delegates.observable(ButtonState.Completed) { p, old, new ->
        when (buttonState) {
            ButtonState.Clicked -> {
                isAnimated = true
                startLoadingAnimation()
            }
            ButtonState.Loading -> {}
            ButtonState.Completed -> {
                isAnimated = false
            }
        }
    }

    init {
        isClickable = true
        getDataFromAttributes(context, attrs)
        setPaintsValues()
    }

    override fun performClick(): Boolean {
        if (!isAnimated) buttonState = ButtonState.Clicked
        return super.performClick()
    }

    private fun startLoadingAnimation() {
        ValueAnimator.ofInt(0, 101).apply {
            duration = 4000
            interpolator = DecelerateInterpolator()
            addUpdateListener { valueAnimator ->
                animInitialValue = valueAnimator.animatedValue as Int
                invalidate()
                if (animatedValue == 101) buttonState = ButtonState.Completed
            }
        }.start()
        buttonState = ButtonState.Loading
    }

    private fun getDataFromAttributes(context: Context, attrs: AttributeSet?) {
        context.theme.obtainStyledAttributes(attrs, R.styleable.LoadingButton, 0, 0).apply {
            try {
                bgColor = getColor(R.styleable.LoadingButton_bgColor, Color.YELLOW)
                textColor = getColor(R.styleable.LoadingButton_textColor, Color.BLACK)
                btnCurrentText = getString(R.styleable.LoadingButton_text) ?: "Nothing"
                btnRegularText = btnCurrentText
            } finally {
                recycle()
            }
        }
    }

    private fun setPaintsValues() {
        bgPaint.color = bgColor
        textPaint.color = textColor
        textPaint.textSize = textSizeFloat
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minWidth: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        widthSize = resolveSizeAndState(minWidth, widthMeasureSpec, 1)
        heightSize = resolveSizeAndState(MeasureSpec.getSize(widthSize), heightMeasureSpec, 0)
        hCenter = widthSize / 2

        // Background
        bgRect.right = widthSize
        bgRect.bottom = heightSize
        bgRectAnim.bottom = heightSize
        // Text
        textX = bgRect.centerX().toFloat()
        textY = (bgRect.centerY().toFloat() - ((textPaint.descent() + textPaint.ascent()) / 2))
        // Arc
        arcRect.set(
            RECT_PADDING,
            RECT_PADDING,
            heightSize.toFloat(),
            heightSize.toFloat() - RECT_PADDING
        )

        setMeasuredDimension(widthSize, heightSize)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.apply {
            drawInitialBg()
            drawInitialBgAnimation()
            drawInitialText()
        }
    }

    private fun Canvas.drawInitialBg() {
        drawRect(bgRect, bgPaint)
    }

    private fun Canvas.drawInitialBgAnimation() {
        when (animInitialValue) {
            1 -> btnCurrentText = btnLoadingText
            101 -> btnCurrentText = btnRegularText
        }
        if (animInitialValue in 1..100) {
            drawProgressRect()
            drawProgressArc()
        }
    }

    private fun Canvas.drawProgressRect() {
        bgRectAnim.right = widthSize * animInitialValue / 100
        drawRect(bgRectAnim, bgAnimPaint)
    }

    private fun Canvas.drawProgressArc() {
        val sweepAngle = 360f * animInitialValue / 100
        arcRect.left = hCenter + textBound.width() / 2 + RECT_PADDING * 2
        arcRect.right = arcRect.left + heightSize.toFloat() - RECT_PADDING * 2
        drawArc(arcRect, 0f, sweepAngle, true, arcPaint)
    }

    private fun Canvas.drawInitialText() {
        drawText(btnCurrentText, textX, textY, textPaint)
        textPaint.getTextBounds(btnCurrentText, 0, btnCurrentText.length - 1, textBound)
    }

}