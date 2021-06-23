package com.lk.baselibrary.widget

import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import com.lk.baselibrary.R


class AuthCodeInputView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    // <!-- 默认间距 -->
    var mDefaultSpacing = 16

    // <!-- 默认颜色 -->
    @ColorInt
    var mDefaultColor = Color.BLACK

    // <!-- 默认字体大小 -->
    private var mDefaultTextSize = 36

    // <!-- 当前输入位分割线颜色 -->
    @ColorInt
    var mCurrentSplitLineColor = this.mDefaultColor

    // <!-- 其他输入位分割线颜色 -->
    @ColorInt
    var mOtherSplitLineColor = mDefaultColor

    // <!-- 分割线高度 -->
    private var mSplitLineHeight = 1

    // <!-- 验证码位数 -->
    private var mDigit = 4

    // <!-- 单个验证码宽度 -->
    private var mSingleCaptchaWidth = 100

    // <!-- 验证码当前输入位字体颜色 -->
    @ColorInt
    var mCurrentTextColor = mDefaultColor

    // <!-- 验证码当前输入位字体大小 -->
    private var mCurrentTextSize = mDefaultTextSize

    // <!-- 验证码其他输入位字体颜色 -->
    @ColorInt
    var mOtherTextColor = mDefaultColor

    // <!-- 验证码其它输入位字体大小 -->
    private var mOtherTextSize = mDefaultTextSize

    // 记录当前输入文本
    private var mText = ""

    init {
        orientation = HORIZONTAL
        gravity = Gravity.CENTER
        if (childCount > 0) {
            removeAllViews()
        }
        initAttrs(context, attrs)
        for (index in 0..mDigit) {
            // 实例化 ITEM 组件
            val child = LayoutInflater.from(context)
                .inflate(R.layout.view_auth_code_input_item, this, false)
            val lp = LayoutParams(mSingleCaptchaWidth, ViewGroup.LayoutParams.MATCH_PARENT)
            if (index != 0) {
                lp.leftMargin = mDefaultSpacing
            }
            child.layoutParams = lp
            setViewAttrs(child, null, false)
            // 分割线高度只在初始化时设置一次
            val mSplitV = child.findViewById<View>(R.id.split_v)
            val params = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mSplitLineHeight)
            mSplitV.layoutParams = params
            addView(child)
        }

    }

    private fun setViewAttrs(child: View, text: String?, isSelected: Boolean) {
        val mNumberTv = child.findViewById<TextView>(R.id.number_tv)
        val mSplitV = child.findViewById<View>(R.id.split_v)
        if (isSelected) {
            mNumberTv.setTextColor(mCurrentTextColor)
            mNumberTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, mCurrentTextSize.toFloat())
            mSplitV.setBackgroundColor(mCurrentSplitLineColor)
        } else {
            mNumberTv.setTextColor(mOtherTextColor)
            mNumberTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, mOtherTextSize.toFloat())
            mSplitV.setBackgroundColor(mOtherSplitLineColor)
        }
        mNumberTv.text = text
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        if (null != attrs) {
            // AttributeSet 属性值的索引
            val o = context.obtainStyledAttributes(attrs, R.styleable.AuthCodeInputView)
            // 默认间距
            mDefaultSpacing = o.getDimension(
                R.styleable.AuthCodeInputView_defaultSpacing,
                16f
            ).toInt()
            // 获取默认颜色
            mDefaultColor = o.getColor(R.styleable.AuthCodeInputView_defaultColor, Color.BLACK)
            // 获取默认字体大小
            mDefaultTextSize = o.getDimension(
                R.styleable.AuthCodeInputView_defaultTextSize, 36f
            ).toInt()
            // 输入位分割线颜色
            mCurrentSplitLineColor =
                o.getColor(R.styleable.AuthCodeInputView_currentSplitLineColor, mDefaultColor)
            // 其他输入位分割线颜色
            mOtherSplitLineColor = o.getColor(
                R.styleable.AuthCodeInputView_otherSplitLineColor,
                mDefaultColor
            )
            // 分割线高度
            mSplitLineHeight = o.getDimension(
                R.styleable.AuthCodeInputView_splitLineHeight, 1f
            ).toInt()
            // 验证码位数
            mDigit = o.getInteger(R.styleable.AuthCodeInputView_digit, 4)
            // 单个验证码宽度
            mSingleCaptchaWidth =
                o.getDimension(R.styleable.AuthCodeInputView_singleCaptchaWidth, 100f).toInt()
            // 验证码当前输入位字体颜色
            mCurrentTextColor = o.getColor(
                R.styleable.AuthCodeInputView_currentTextColor,
                mDefaultColor
            );
            // 验证码当前输入位字体大小
            mCurrentTextSize = o.getDimension(
                R.styleable.AuthCodeInputView_currentTextSize, mDefaultTextSize.toFloat()
            ).toInt()
            // 验证码其他输入位字体颜色
            mOtherTextColor = o.getColor(
                R.styleable.AuthCodeInputView_otherTextColor,
                mDefaultColor
            )
            // 验证码其它输入位字体大小
            mOtherTextSize = o.getDimension(
                R.styleable.AuthCodeInputView_otherTextSize,
                mDefaultTextSize.toFloat()
            ).toInt()
            // 回收资源
            o.recycle()
        }
    }

    fun addText(text: String) {
        val tx = if (TextUtils.isEmpty(text)) "" else text
        setText(mText + tx)
    }

    fun delText() {
        val count = if (TextUtils.isEmpty(mText)) 0 else mText.length
        if (count > 0) {
            setText(mText.substring(0, count - 1))
        } else {
            setText("")
        }
    }

    private fun setText(tx: String) {
        val text: String = tx.trim()
        var length = if (TextUtils.isEmpty(text)) 0 else text.length
        if (length > mDigit) {
            this.mText = text.substring(0, mDigit)
            length = mDigit;
        } else {
            this.mText = if (length > 0) text else ""
        }
        for (i in 0..childCount) {
            val child = getChildAt(i)
            when {
                i + 1 < length -> {
                    setViewAttrs(child, text[i].toString(), false)
                }
                i + 1 == length -> {
                    setViewAttrs(child, text[i].toString(), true)
                }
                else -> {
                    setViewAttrs(child, null, false)
                }
            }
        }
    }

    fun getText(): String {
        return mText
    }

}

