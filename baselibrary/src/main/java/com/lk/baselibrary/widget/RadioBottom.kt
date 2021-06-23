package com.lk.baselibrary.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.CountDownTimer
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import com.lk.baselibrary.R

class RadioBottom @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    var text = resources.getString(R.string.app_name)

    var textColor = Color.GRAY

    private var mTextSize =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14.0f, resources.displayMetrics)
    var mBackGround = Color.GRAY

    var mRadioSize = 0f

    var isStroke = false

    var isBold = false

    private var rect: Rect = Rect()
    private var paint: Paint = Paint()
    private var framePaint = Paint(Paint.FILTER_BITMAP_FLAG)
    private var rec: RectF? = null
    private var count: Int = 59
    lateinit var timer: CountDownTimer
    private var lineWidth =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1f, resources.displayMetrics)
    private val typeArray by lazy { context.obtainStyledAttributes(attrs, R.styleable.RadioBottom) }

    init {
        typeArray.let {
            text = it.getString(R.styleable.RadioBottom_bottom_text).toString()
            textColor = it.getColor(R.styleable.RadioBottom_text_color, Color.BLACK)
            mTextSize = it.getDimension(
                R.styleable.RadioBottom_text_size,
                TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    14.0f,
                    resources.displayMetrics
                )
            )
            mBackGround = it.getColor(R.styleable.RadioBottom_back_ground, Color.GRAY)
            mRadioSize = it.getDimension(R.styleable.RadioBottom_radio_size, 0f)
            isStroke = it.getBoolean(R.styleable.RadioBottom_is_stroke, false)
            isBold = it.getBoolean(R.styleable.RadioBottom_is_bold, false)
            it.recycle()
        }
        paint.isAntiAlias = true
        paint.textSize = mTextSize
        paint.getTextBounds(text, 0, text.length, rect)
        paint.color = textColor
        //设置字体是否为粗体
        paint.isFakeBoldText = isBold
        framePaint.isAntiAlias = true
        framePaint.color = mBackGround
        if (isStroke) {
            framePaint.style = Paint.Style.STROKE
            framePaint.strokeWidth = lineWidth
            framePaint.strokeJoin = Paint.Join.ROUND
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val width: Int
        val height: Int
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize
        } else {
            paint.textSize = mTextSize
            paint.getTextBounds(text, 0, text.length, rect)
            val desired = paddingLeft + rect.width() + paddingRight
            width = if (desired <= widthSize) desired else widthSize
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize
        } else {
            paint.textSize = mTextSize
            paint.getTextBounds(text, 0, text.length, rect)
            val desired = paddingTop + rect.height() + paddingBottom
            height = if (desired <= heightSize) desired else heightSize
        }
        setMeasuredDimension(width, height)

    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        canvas?.apply {
            rec = if (isStroke) {
                RectF(
                    lineWidth / 2,
                    lineWidth / 2,
                    measuredWidth - lineWidth / 2,
                    measuredHeight - lineWidth / 2
                )
            } else {
                RectF(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat())
            }
            drawRoundRect(rec!!, mRadioSize, mRadioSize, framePaint)
            val fontMetrics = paint.fontMetricsInt
            val baseline =
                (measuredHeight - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top
            val startX = (width / 2 - paint.measureText(text) / 2).toInt()
            drawText(text, startX.toFloat(), baseline.toFloat(), paint)
        }

    }


    fun start() {
        isClickable = false//禁用点击,防止重复操作
        text = "${count + 1}s"
        mBackGround = Color.GRAY
        timer = object : CountDownTimer((count * 1000).toLong(), 1000) {
            override fun onFinish() {
                stop()
            }

            override fun onTick(p0: Long) {
                text = "" + (p0 / 1000) + " s"
                postInvalidate()
            }
        }.start()

    }

    fun setBottomText(text: String?) {
        if (text == null) return
        this.text = text
    }

    fun setBottom_text(text: String?) {
        if (text == null) return
        this.text = text
    }

    fun setBackGround(color: Int) {
        mBackGround = color
    }

    fun setBack_ground(color: Int?) {
        if (color == null) return
        this.mBackGround = color
        framePaint.color = color
        postInvalidate()
    }

    fun setIs_stroke(b: Boolean?) {
        if (b == null) return
        this.isStroke = b
        framePaint.style = Paint.Style.STROKE
        framePaint.strokeWidth = lineWidth
        framePaint.strokeJoin = Paint.Join.ROUND
        postInvalidate()
    }

    fun setText_color(t: Int) {
        this.textColor = t
        paint.color = t
        postInvalidate()
    }

    /**
     * 结束计时,重新开始
     */
    private fun stop() {
        text = "重新获取"
        isClickable = true//重新开启点击事件
        invalidate()
    }


}