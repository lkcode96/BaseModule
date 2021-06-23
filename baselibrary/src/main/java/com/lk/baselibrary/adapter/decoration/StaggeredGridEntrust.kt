package com.lk.baselibrary.adapter.decoration

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 * 作者：请叫我百米冲刺 on 2016/12/21 下午12:45
 * 邮箱：mail@hezhilin.cc
 */

class StaggeredGridEntrust(leftRight: Int, topBottom: Int, mColor: Int) :
		SpacesItemDecorationEntrust(leftRight, topBottom, mColor) {
	private var left = 0f
	private var right = 0f
	private var top = 0f
	private var bottom = 0f
	override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
		//TODO 因为排列的不确定性，暂时没找到比较好的处理方式
		val layoutManager = parent.layoutManager as StaggeredGridLayoutManager
		if (layoutManager.childCount == 0) {
			return
		}
		
		
		val spanCount = layoutManager.spanCount
		val childCount = parent.childCount
		
		//获取最后显示的几项
		val intros = IntArray(spanCount)
		layoutManager.findLastVisibleItemPositions(intros)
		
		//换个思路，每个item都画边和右边，当然第一排的不需要上边，最右边的不需要右边
		if (layoutManager.orientation == StaggeredGridLayoutManager.VERTICAL) {
			for (i in 0 until childCount - 1) {
				val child = parent.getChildAt(i)
				val params = child.layoutParams as StaggeredGridLayoutManager.LayoutParams
				//得到它在总数里面的位置
				val position = parent.getChildAdapterPosition(child)
				//它在每列的位置
				val spanPosition = params.spanIndex
				//将带有颜色的分割线处于中间位置
				val centerLeft: Float = ((layoutManager.getLeftDecorationWidth(child) + 1 - leftRight) / 2).toFloat()
				val centerTop: Float = ((layoutManager.getBottomDecorationHeight(child) + 1 - topBottom) / 2).toFloat()
				//画上边的
				if (position > spanCount - 1) {
					left = (child.left + params.leftMargin).toFloat()
					if (spanPosition > 0) {
						left -= centerLeft
					}
					right = (child.right + params.rightMargin).toFloat()
					if (spanPosition + 1 != spanCount) {//最右边的不需要那一丢丢
						right += centerLeft
					}
					top = (child.top + params.topMargin - centerTop - topBottom)
					bottom = top + topBottom
					mDivider.setBounds(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())
					mDivider.draw(c)
				}
				//画右边的
				if ((spanPosition + 1) % spanCount != 0) {
					left = (child.right + params.rightMargin + centerLeft)
					right = left + leftRight;
					top = (child.top + params.topMargin).toFloat()
					//第一排的不需要上面那一丢丢
					if (position > spanCount - 1) {//换个思路，都给top了
						top -= centerTop + centerTop + topBottom
					}
					bottom = (child.bottom + params.bottomMargin).toFloat()
					mDivider.setBounds(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())
					mDivider.draw(c)
				}
			}
			
		} else {
			for (i in 0 until childCount - 1) {
				val child = parent.getChildAt(i)
				val params = child.layoutParams as StaggeredGridLayoutManager.LayoutParams
				//得到它在总数里面的位置
				val position = parent.getChildAdapterPosition(child)
				//它在每列的位置
				val spanPosition = params.spanIndex
				//将带有颜色的分割线处于中间位置
				val centerLeft = (layoutManager.getRightDecorationWidth(child) + 1 - leftRight) / 2
				val centerTop = (layoutManager.getTopDecorationHeight(child) + 1 - topBottom) / 2
				//画左边
				if (position > spanCount - 1) {
					left = (child.left + params.leftMargin - centerLeft - leftRight).toFloat()
					right = left + leftRight
					top = (child.top + params.topMargin - centerTop).toFloat()
					if (spanPosition == 0) {
						top += centerTop
					}
					bottom = child.bottom + params.bottomMargin.toFloat()
					if (spanPosition + 1 != spanCount) {
						bottom += centerTop
					}
					mDivider.setBounds(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())
					mDivider.draw(c)
				}
				//画上面的
				if (spanPosition > 0) {
					left = child.left + params.leftMargin.toFloat()
					if (position > spanCount - 1) {//换个思路，都给left了
						left -= centerLeft + centerLeft + leftRight
					}
					right = child.right + params.rightMargin.toFloat()
					top = (child.top + params.bottomMargin - centerTop - topBottom).toFloat()
					bottom = top + topBottom;
					mDivider.setBounds(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())
					mDivider.draw(c)
				}
			}
		}
	}
	
	override fun getItemOffsets(
			outRect: Rect,
			view: View,
			parent: RecyclerView,
			state: RecyclerView.State
	) {
		val layoutManager = parent.layoutManager as StaggeredGridLayoutManager?
		val lp = view.layoutParams as StaggeredGridLayoutManager.LayoutParams
		val childPosition = parent.getChildAdapterPosition(view)
		val spanCount = layoutManager!!.spanCount
		val spanSize = if (lp.isFullSpan) layoutManager.spanCount else 1
		
		if (layoutManager.orientation == GridLayoutManager.VERTICAL) {
			if (getSpanGroupIndex(childPosition, spanCount, spanSize) == 0) {//第一排的需要上面
				outRect.top = topBottom
			}
			outRect.bottom = topBottom
			//这里忽略和合并项的问题，只考虑占满和单一的问题
			if (lp.isFullSpan) {//占满
				outRect.left = leftRight
				outRect.right = leftRight
			} else {
				outRect.left =
						((spanCount - lp.spanIndex).toFloat() / spanCount * leftRight).toInt()
				outRect.right =
						(leftRight.toFloat() * (spanCount + 1) / spanCount - outRect.left).toInt()
			}
		} else {
			if (getSpanGroupIndex(childPosition, spanCount, spanSize) == 0) {//第一排的需要left
				outRect.left = leftRight
			}
			outRect.right = leftRight
			//这里忽略和合并项的问题，只考虑占满和单一的问题
			if (lp.isFullSpan) {//占满
				outRect.top = topBottom
				outRect.bottom = topBottom
			} else {
				outRect.top = ((spanCount - lp.spanIndex).toFloat() / spanCount * topBottom).toInt()
				outRect.bottom =
						(topBottom.toFloat() * (spanCount + 1) / spanCount - outRect.top).toInt()
			}
		}
	}
	
	private fun getSpanGroupIndex(adapterPosition: Int, spanCount: Int, spanSize: Int): Int {
		var span = 0
		var group = 0
		for (i in 0 until adapterPosition) {
			span += spanSize
			if (span == spanCount) {
				span = 0
				group++
			} else if (span > spanCount) {
				// did not fit, moving to next row / column
				span = spanSize
				group++
			}
		}
		if (span + spanSize > spanCount) {
			group++
		}
		return group
	}
}
