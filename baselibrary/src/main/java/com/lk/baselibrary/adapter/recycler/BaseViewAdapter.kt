package com.lk.baselibrary.adapter.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.ObservableList
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView


/**
 * 页面描述：
 *
 * Created by ditclear on 2017/10/30.
 */
abstract class BaseViewAdapter<T>(context: Context, private val list: ObservableList<T>) :
    RecyclerView.Adapter<BindingViewHolder<ViewDataBinding>>() {

    protected val mLayoutInflater: LayoutInflater = LayoutInflater.from(context)

    var itemPresenter: ItemClickPresenter<T>? = null

    var itemDecorator: ItemDecorator? = null

    //private var itemAnimator: ItemAnimator? = ScaleInItemAnimator(interpolator = DecelerateInterpolator())
    private var itemAnimator: ItemAnimator? = null

    //AlphaInItemAnimator(interpolator = DecelerateInterpolator())
    var mLastPosition = -1
    private var isFirstOnly = false
    private var showItemAnimator = true

    override fun onBindViewHolder(holder: BindingViewHolder<ViewDataBinding>, position: Int) {
        val item = list[position]
//        holder.binding.setVariable(BR.item, item)
//        holder.binding.setVariable(BR.presenter, itemPresenter)
        holder.binding.executePendingBindings()
        itemDecorator?.decorator(holder, position, getItemViewType(position))

        itemAnimator?.let {
            if (!showItemAnimator) {
                return@let
            }
            val adapterPosition = holder.adapterPosition
            if (!isFirstOnly || adapterPosition > mLastPosition) {
                if (adapterPosition >= mLastPosition) {
                    it.scrollDownAnim(holder.binding.root)
                } else {
                    it.scrollUpAnim(holder.binding.root)
                }
                mLastPosition = adapterPosition
            } else {
                clear(holder.binding.root)
            }
        }
    }

    override fun getItemCount(): Int = list.size

    fun getItem(position: Int): T? = list[position]


    private fun clear(v: View) {
        v.alpha = 1f
        v.scaleY = 1f
        v.scaleX = 1f
        v.translationY = 0f
        v.translationX = 0f
        v.rotation = 0f
        v.rotationX = 0f
        v.rotationY = 0f
        v.pivotX = v.measuredWidth.toFloat() / 2
        v.pivotY = v.measuredHeight.toFloat() / 2
        v.animate().setInterpolator(null).startDelay = 0
    }
}