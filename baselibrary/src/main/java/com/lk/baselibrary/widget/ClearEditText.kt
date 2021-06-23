package com.lk.baselibrary.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.res.ResourcesCompat
import com.lk.baselibrary.R

class ClearEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatEditText(context, attrs, defStyleAttr), TextWatcher, View.OnFocusChangeListener {
    var mClearDrawable: Drawable? = null
    var hasFocus = false
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        if (hasFocus) {
            setClearIconVisible(s.isNotEmpty())
        }

    }

    override fun afterTextChanged(s: Editable?) {

    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        this.hasFocus = hasFocus
        if (hasFocus) {
            setClearIconVisible(text!!.isNotEmpty())
        } else {
            setClearIconVisible(false)
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            if (compoundDrawables[2] != null) {
                val x = event.x.toInt()
                val y = event.y.toInt()
                val rect = compoundDrawables[2].bounds
                val height = rect.height()
                val distance = (getHeight() - height) / 2
                val isInnerWidth = x > width - totalPaddingRight && x < width - paddingRight
                val isInnerHeight = y > distance && y < distance + height
                if (isInnerHeight && isInnerWidth) {
                    this.setText("")
                }
            }
        }
        return super.onTouchEvent(event)
    }

    init {
        mClearDrawable = compoundDrawables[2]
        if (mClearDrawable == null) {
            mClearDrawable = ResourcesCompat.getDrawable(resources, R.mipmap.ic_clear, null)
        }

        mClearDrawable!!.setBounds(
            0,
            0,
            mClearDrawable!!.intrinsicWidth,
            mClearDrawable!!.intrinsicHeight
        );
        //默认设置隐藏图标
        setClearIconVisible(false)
        //设置焦点改变的监听
        onFocusChangeListener = this
        //设置输入框里面内容发生改变的监听
        addTextChangedListener(this)
        isFocusableInTouchMode=true
    }

    /**
     * 设置清除图标的显示与隐藏，调用setCompoundDrawables为EditText绘制上去
     * @param visible
     */
    private fun setClearIconVisible(visible: Boolean) {
        val right = if (visible) mClearDrawable else null
        setCompoundDrawables(
            compoundDrawables[0],
            compoundDrawables[1], right, compoundDrawables[3]
        )
    }

}