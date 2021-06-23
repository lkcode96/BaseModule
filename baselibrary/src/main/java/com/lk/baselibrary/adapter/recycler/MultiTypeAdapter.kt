package com.lk.baselibrary.adapter.recycler

import android.content.Context
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.databinding.ViewDataBinding
import androidx.annotation.LayoutRes
import androidx.collection.ArrayMap
import android.view.ViewGroup

/**
 * 页面描述：MultiTypeAdapter
 *
 * Created by ditclear on 2017/10/30.
 */
open class MultiTypeAdapter(
    context: Context,
    list: ObservableArrayList<Any>,
    val multiViewType: MultiViewType
) : BaseViewAdapter<Any>(context, list) {

    protected var mCollectionViewType: MutableList<Int> = mutableListOf()

    private val mItemTypeToLayoutMap = ArrayMap<Int, Int>()

    init {
        list.addOnListChangedCallback(object :
            ObservableList.OnListChangedCallback<ObservableList<Any>>() {
            override fun onItemRangeMoved(
                sender: ObservableList<Any>?,
                fromPosition: Int,
                toPosition: Int,
                itemCount: Int
            ) {
                notifyItemMoved(fromPosition, toPosition)
            }

            override fun onItemRangeRemoved(
                sender: ObservableList<Any>?,
                positionStart: Int,
                itemCount: Int
            ) {
                for (i in positionStart + itemCount - 1 downTo positionStart) mCollectionViewType.removeAt(
                    i
                )
                if (sender?.isNotEmpty() == true) {
                    notifyItemRangeRemoved(positionStart, itemCount)
                } else {
                    mLastPosition = -1
                    notifyDataSetChanged()
                }
            }

            override fun onItemRangeChanged(
                sender: ObservableList<Any>?,
                positionStart: Int,
                itemCount: Int
            ) {
                notifyItemRangeChanged(positionStart, itemCount)
            }

            override fun onItemRangeInserted(
                sender: ObservableList<Any>?,
                positionStart: Int,
                itemCount: Int
            ) {
                sender?.run {
                    (positionStart until positionStart + itemCount).forEach {
                        mCollectionViewType.add(it, multiViewType.getViewType(this[it]))
                    }
                    notifyItemRangeInserted(positionStart, itemCount)
                }
            }

            override fun onChanged(sender: ObservableList<Any>?) {
                notifyDataSetChanged()
            }

        })
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BindingViewHolder<ViewDataBinding> {
        return BindingViewHolder(
            DataBindingUtil.inflate<ViewDataBinding>(
                mLayoutInflater,
                getLayoutRes(viewType),
                parent,
                false
            )
        )
    }

    fun addViewTypeToLayoutMap(viewType: Int?, layoutRes: Int?) {
        mItemTypeToLayoutMap[viewType] = layoutRes
    }

    override fun getItemViewType(position: Int): Int =
        if (mCollectionViewType.size == 0) 0 else mCollectionViewType[position]

    interface MultiViewType {
        fun getViewType(item: Any): Int
    }

    @LayoutRes
    protected fun getLayoutRes(viewType: Int): Int = mItemTypeToLayoutMap[viewType]!!
}