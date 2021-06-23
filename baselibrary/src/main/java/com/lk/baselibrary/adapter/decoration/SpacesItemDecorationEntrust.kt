package com.lk.baselibrary.adapter.decoration

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View

import androidx.recyclerview.widget.RecyclerView

/**
 * 作者：请叫我百米冲刺 on 2016/12/6 上午11:37
 * 邮箱：mail@hezhilin.cc
 */

abstract class SpacesItemDecorationEntrust(
    protected var leftRight: Int,
    protected var topBottom: Int,
    mColor: Int
) {

    //color的传入方式是resouce.getcolor
    protected var mDivider: Drawable = ColorDrawable()

    init {
        if (mColor != 0) {
            mDivider = ColorDrawable(mColor)
        }
    }

    internal abstract fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State)

    internal abstract fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    )

}
