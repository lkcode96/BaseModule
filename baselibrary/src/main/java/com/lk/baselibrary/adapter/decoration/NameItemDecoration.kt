package com.lk.baselibrary.adapter.decoration

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.lk.baselibrary.utils.dp2px
import com.lk.baselibrary.utils.sp2px
import com.lk.baselibrary.R

/**
 * @author: Administrator
 * @date: 2021/5/27
 */
class NameItemDecoration(val context: Context, private val listener: OnItemTitleListener) :
    RecyclerView.ItemDecoration() {
    private val mPaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = context.resources.getColor(R.color.all_div_bg, null)
        }
    }
    private val mItemPaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = context.resources.getColor(R.color.all_div_bg, null)
        }
    }
    private val mTextPaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.BLACK
            textSize = context.sp2px(15)
        }
    }
    private val mTextPaddingLeft = context.dp2px(16)
    private val mTextRect = Rect()
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        val count = parent.childCount
        for (i in 0 until count) {
            val view = parent.getChildAt(i)
            val psi = parent.getChildLayoutPosition(view)
            val left = parent.paddingLeft
            val right = parent.width - parent.paddingRight
            if (isItemHeader(psi)) {
                val mItemHeaderHeight = view.context.dp2px(40)
                c.drawRect(
                    left.toFloat(), (view.top - mItemHeaderHeight),
                    right.toFloat(), view.top.toFloat(), mItemPaint
                )
                val text = listener.getGroupName(psi)
                mTextPaint.getTextBounds(text, 0, text.length, mTextRect)
                c.drawText(
                    text,
                    (left + mTextPaddingLeft),
                    (view.top - mItemHeaderHeight) + mItemHeaderHeight / 2 + mTextRect.height() / 2,
                    mTextPaint
                )
            } else {
                c.drawRect(
                    0f,
                    (view.top - 1).toFloat(),
                    parent.width.toFloat(),
                    view.top.toFloat(),
                    mPaint
                )
            }
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val psi = parent.getChildLayoutPosition(view)
        if (isItemHeader(psi)) {
            outRect.top = view.context.dp2px(40).toInt()
        } else {
            outRect.top = 1
        }
    }

    /**
     * 判断position对应的Item是否是组的第一项
     *
     * @param position
     * @return
     */
    fun isItemHeader(position: Int): Boolean {
        return if (position == 0) {
            true
        } else {
            val lastGroupName: String = listener.getGroupName(position - 1)
            val currentGroupName: String = listener.getGroupName(position)
            //判断上一个数据的组别和下一个数据的组别是否一致，如果不一致则是不同组，也就是为第一项（头部）
            lastGroupName != currentGroupName
        }
    }

    interface OnItemTitleListener {
        fun getGroupName(psi: Int): String
    }
}