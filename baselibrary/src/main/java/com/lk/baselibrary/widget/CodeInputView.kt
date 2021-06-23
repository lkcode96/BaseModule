package com.lk.baselibrary.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.text.InputFilter
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.lk.baselibrary.R

class CodeInputView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatEditText(context, attrs, defStyleAttr) {
    // <!-- 最大输入长度 -->
    private var mMaxLength = 4

    // <!-- 边框宽度 -->
    private var mBorderWidth = 100

    // <!-- 边框高度 -->
    private var mBorderHeight = 100

    // <!-- 边框间距 -->
    private var mBorderSpacing = 24

    // <!-- 边框背景图 -->
    private var mBorderImage: Drawable? = null

    // 用矩形来保存方框的位置、大小信息
    private var mRect = Rect()

    // 文本颜色
    private var mTextColor = 0

    init {
        mBorderImage = ContextCompat.getDrawable(
            context,
            R.drawable.code_input_view_border_bg
        )

        initAttrs(context, attrs)
        // 设置最大输入长度
        setMaxLength(mMaxLength)
        // 禁止长按
        isLongClickable = false
        // 去掉背景颜色
        setBackgroundColor(Color.TRANSPARENT)
        // 不显示光标
        isCursorVisible = false

    }

    // 设置最大长度
    private fun setMaxLength(maxLength: Int) {
        filters = if (maxLength >= 0) {
            arrayOf(InputFilter.LengthFilter(maxLength))
        } else {
            arrayOf(InputFilter.LengthFilter(0))
        }
    }

    // 初始化属性
    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        if (null != attrs) {
            // AttributeSet 属性值的索引
            val o = context.obtainStyledAttributes(attrs, R.styleable.CodeInputView)
            // <!-- 最大输入长度 -->
            mMaxLength = o.getInteger(R.styleable.CodeInputView_android_maxLength, 4)
            // <!-- 边框宽度 -->
            mBorderWidth = o.getDimension(R.styleable.CodeInputView_borderWidth, 100f).toInt()
            // <!-- 边框高度 -->
            mBorderHeight = o.getDimension(R.styleable.CodeInputView_borderHeight, 100f).toInt()
            // <!-- 边框间距 -->
            mBorderSpacing = o.getDimension(R.styleable.CodeInputView_borderSpacing, 24F).toInt()
            // <!-- 边框背景图 -->
            val drawable = o.getDrawable(R.styleable.CodeInputView_borderImage)
            if (null != drawable) {
                mBorderImage = drawable
            }
            // 回收资源
            o.recycle()
        }
    }

    @Override
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        // 当前输入框的宽高信息
        var width = measuredWidth
        var height = measuredHeight
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        // 判断高度是否小于自定义边框高度
        height = if (height < mBorderHeight) mBorderHeight else height

        // 自定义输入框宽度 = 边框宽度 * 数量 + 边框间距 * (数量 - 1)
        val customWidth = mBorderWidth * mMaxLength
        +mBorderSpacing * (if ((mMaxLength - 1) > 0) (mMaxLength - 1) else 0)

        // 判断宽度是否小于自定义宽度
        width = if (width < customWidth) customWidth else width

        val mWidthMeasureSpec = MeasureSpec.makeMeasureSpec(width, widthMode)
        val mHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height, heightMode)

        // 重新设置测量布局
        setMeasuredDimension(mWidthMeasureSpec, mHeightMeasureSpec)
    }

    @Override
    override fun onDraw(canvas: Canvas) {
        // 获取当前输入文本颜色
        mTextColor = currentTextColor
        // 屏蔽系统文本颜色，直接透明
        setTextColor(Color.TRANSPARENT)
        // 父类绘制
        super.onDraw(canvas)
        // 重新设置文本颜色
        setTextColor(mTextColor)
        // 重绘背景
        drawBorderBackground(canvas)
        // 重绘文本
        drawText(canvas)
    }

    // 绘制背景
    private fun drawBorderBackground(canvas: Canvas) {
        // 下面绘制方框背景颜色
        // 确定反馈位置
        mRect.left = 0
        mRect.top = 0
        mRect.right = mBorderWidth
        mRect.bottom = mBorderHeight
        // 当前画布保存的状态
        val count = canvas.saveCount
        // 保存画布
        canvas.save()
        // 获取当前输入字符串长度
        val length = editableText.length
        for (i in 0 until mMaxLength) {
            // 设置位置
            mBorderImage?.bounds = mRect
            // 设置图像状态
            if (i == length) {
                // 当前输入位高亮的索引
                mBorderImage?.state = intArrayOf(android.R.attr.state_focused)
            } else {
                // 其他输入位置默认
                mBorderImage?.state = intArrayOf(android.R.attr.state_enabled)
            }
            // 画到画布上
            mBorderImage?.draw(canvas)
            // 确定下一个方框的位置
            // X坐标位置
            val dx = mRect.right + mBorderSpacing
            // 保存画布
            canvas.save()
            // [注意细节] 移动画布到下一个位置
            canvas.translate(dx.toFloat(), 0f)
        }
        // [注意细节] 把画布还原到画反馈之前的状态，这样就还原到最初位置了
        canvas.restoreToCount(count)
        // 画布归位
        canvas.translate(0f, 0f)
    }

    // 绘制文本
    private fun drawText(canvas: Canvas) {
        val count = canvas.saveCount
        canvas.translate(0f, 0f)
        val length = editableText.length
        for (i in 0 until length) {
            val text = editableText[i].toString()
            val textPaint = paint
            textPaint.color = mTextColor
            // 获取文本大小
            textPaint.getTextBounds(text, 0, 1, mRect)
            // 计算(x,y) 坐标
            val x = mBorderWidth / 2 + (mBorderWidth + mBorderSpacing) * i - (mRect.centerX())
            val y = canvas.height / 2 + mRect.height() / 2
            canvas.drawText(text, x.toFloat(), y.toFloat(), textPaint)
        }
        canvas.restoreToCount(count)
    }

}