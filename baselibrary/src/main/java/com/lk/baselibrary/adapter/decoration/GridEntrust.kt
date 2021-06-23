package com.lk.baselibrary.adapter.decoration

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * 作者：请叫我百米冲刺 on 2016/12/6 上午11:39
 * 邮箱：mail@hezhilin.cc
 */

class GridEntrust(leftRight: Int, topBottom: Int, mColor: Int) :
    SpacesItemDecorationEntrust(leftRight, topBottom, mColor) {

    public override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val layoutManager = parent.layoutManager as GridLayoutManager?
        val lookup = layoutManager!!.spanSizeLookup

        if (layoutManager.childCount == 0) {
            return
        }
        //判断总的数量是否可以整除
        val spanCount = layoutManager.spanCount
        var left: Int
        var right: Int
        var top: Int
        var bottom: Int
        val childCount = parent.childCount
        if (layoutManager.orientation == GridLayoutManager.VERTICAL) {
            for (i in 0 until childCount) {
                val child = parent.getChildAt(i)
                //将带有颜色的分割线处于中间位置
                val centerLeft =
                    ((layoutManager.getLeftDecorationWidth(child) + layoutManager.getRightDecorationWidth(
                        child
                    )).toFloat() * spanCount / (spanCount + 1) + 1 - leftRight) / 2
                val centerTop =
                    ((layoutManager.getBottomDecorationHeight(child) + 1 - topBottom) / 2).toFloat()
                //得到它在总数里面的位置
                val position = parent.getChildAdapterPosition(child)
                //获取它所占有的比重
                val spanSize = lookup.getSpanSize(position)
                //获取每排的位置
                val spanIndex = lookup.getSpanIndex(position, layoutManager.spanCount)
                //判断是否为第一排
                val isFirst =
                    layoutManager.spanSizeLookup.getSpanGroupIndex(position, spanCount) == 0
                //画上边的，第一排不需要上边的,只需要在最左边的那项的时候画一次就好
                if (!isFirst && spanIndex == 0) {
                    left = layoutManager.getLeftDecorationWidth(child)
                    right = parent.width - layoutManager.getLeftDecorationWidth(child)
                    top = (child.top - centerTop).toInt() - topBottom
                    bottom = top + topBottom
                    mDivider.setBounds(left, top, right, bottom)
                    mDivider.draw(c)
                }
                //最右边的一排不需要右边的
                val isRight = spanIndex + spanSize == spanCount
                if (!isRight) {
                    //计算右边的
                    left = (child.right + centerLeft).toInt()
                    right = left + leftRight
                    top = child.top
                    if (!isFirst) {
                        top -= centerTop.toInt()
                    }
                    bottom = (child.bottom + centerTop).toInt()
                    mDivider.setBounds(left, top, right, bottom)
                    mDivider.draw(c)
                }
            }
        } else {
            for (i in 0 until childCount) {
                val child = parent.getChildAt(i)
                //将带有颜色的分割线处于中间位置
                val centerLeft =
                    ((layoutManager.getRightDecorationWidth(child) + 1 - leftRight) / 2).toFloat()
                val centerTop =
                    ((layoutManager.getTopDecorationHeight(child) + layoutManager.getBottomDecorationHeight(
                        child
                    )).toFloat() * spanCount / (spanCount + 1) - topBottom) / 2
                //得到它在总数里面的位置
                val position = parent.getChildAdapterPosition(child)
                //获取它所占有的比重
                val spanSize = lookup.getSpanSize(position)
                //获取每排的位置
                val spanIndex = lookup.getSpanIndex(position, layoutManager.spanCount)
                //判断是否为第一列
                val isFirst =
                    layoutManager.spanSizeLookup.getSpanGroupIndex(position, spanCount) == 0
                //画左边的，第一排不需要左边的,只需要在最上边的那项的时候画一次就好
                if (!isFirst && spanIndex == 0) {
                    left = (child.left - centerLeft).toInt() - leftRight
                    right = left + leftRight
                    top = layoutManager.getRightDecorationWidth(child)
                    bottom = parent.height - layoutManager.getTopDecorationHeight(child)
                    mDivider.setBounds(left, top, right, bottom)
                    mDivider.draw(c)
                }
                //最下的一排不需要下边的
                val isRight = spanIndex + spanSize == spanCount
                if (!isRight) {
                    //计算右边的
                    left = child.left
                    if (!isFirst) {
                        left -= centerLeft.toInt()
                    }
                    right = (child.right + centerTop).toInt()
                    top = (child.bottom + centerLeft).toInt()
                    bottom = top + leftRight
                    mDivider.setBounds(left, top, right, bottom)
                    mDivider.draw(c)
                }
            }
        }
    }

    public override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val layoutManager = parent.layoutManager as GridLayoutManager?
        val lp = view.layoutParams as GridLayoutManager.LayoutParams
        val childPosition = parent.getChildAdapterPosition(view)
        val spanCount = layoutManager!!.spanCount

        if (layoutManager.orientation == GridLayoutManager.VERTICAL) {
            //判断是否在第一排
            if (layoutManager.spanSizeLookup.getSpanGroupIndex(
                    childPosition,
                    spanCount
                ) == 0
            ) {//第一排的需要上面
                outRect.top = topBottom
            }
            outRect.bottom = topBottom
            //这里忽略和合并项的问题，只考虑占满和单一的问题
            if (lp.spanSize == spanCount) {//占满
                outRect.left = leftRight
                outRect.right = leftRight
            } else {
                outRect.left =
                    ((spanCount - lp.spanIndex).toFloat() / spanCount * leftRight).toInt()
                outRect.right =
                    (leftRight.toFloat() * (spanCount + 1) / spanCount - outRect.left).toInt()
            }
        } else {
            if (layoutManager.spanSizeLookup.getSpanGroupIndex(
                    childPosition,
                    spanCount
                ) == 0
            ) {//第一排的需要left
                outRect.left = leftRight
            }
            outRect.right = leftRight
            //这里忽略和合并项的问题，只考虑占满和单一的问题
            if (lp.spanSize == spanCount) {//占满
                outRect.top = topBottom
                outRect.bottom = topBottom
            } else {
                outRect.top = ((spanCount - lp.spanIndex).toFloat() / spanCount * topBottom).toInt()
                outRect.bottom =
                    (topBottom.toFloat() * (spanCount + 1) / spanCount - outRect.top).toInt()
            }
        }
    }


}
