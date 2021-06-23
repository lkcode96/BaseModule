package com.lk.baselibrary.adapter.decoration

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.view.View

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 * Created by Au61 on 2016/1/15.
 */
class SpacesItemDecoration(
    private val leftRight: Int=0,
    private val topBottom: Int=0.5.toInt(),
    private val mColor: Int = Color.TRANSPARENT
) : RecyclerView.ItemDecoration() {

    private var mEntrust: SpacesItemDecorationEntrust? = null

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (mEntrust == null) {
            mEntrust = getEntrust(parent.layoutManager)
        }
        mEntrust?.onDraw(c, parent, state)
        super.onDraw(c, parent, state)
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if (mEntrust == null) {
            mEntrust = getEntrust(parent.layoutManager)
        }
        mEntrust?.getItemOffsets(outRect, view, parent, state)
    }

    private fun getEntrust(manager: RecyclerView.LayoutManager?): SpacesItemDecorationEntrust {
        //要注意这边的GridLayoutManager是继承LinearLayoutManager，所以要先判断GridLayoutManager
        return when (manager) {
            is GridLayoutManager -> GridEntrust(leftRight, topBottom, mColor)
            is StaggeredGridLayoutManager -> StaggeredGridEntrust(leftRight, topBottom, mColor)
            else -> //其他的都当做Linear来进行计算
                LinearEntrust(leftRight, topBottom, mColor)
        }
    }

}