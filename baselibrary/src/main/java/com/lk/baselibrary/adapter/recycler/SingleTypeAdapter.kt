package com.lk.baselibrary.adapter.recycler

import android.content.Context
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableList
import androidx.databinding.ViewDataBinding

class SingleTypeAdapter<T>(context: Context, private val layoutRes: Int, list: ObservableList<T>) :
    BaseViewAdapter<T>(context, list) {
    init {
        list.addOnListChangedCallback(object :
            ObservableList.OnListChangedCallback<ObservableList<T>>() {
            override fun onChanged(contributorViewModels: ObservableList<T>) {
                notifyDataSetChanged()
            }

            override fun onItemRangeChanged(
                contributorViewModels: ObservableList<T>,
                i: Int,
                i1: Int
            ) {
                notifyItemRangeChanged(i, i1)
            }

            override fun onItemRangeInserted(
                contributorViewModels: ObservableList<T>,
                i: Int,
                i1: Int
            ) {
                notifyItemRangeInserted(i, i1)
            }

            override fun onItemRangeMoved(
                contributorViewModels: ObservableList<T>,
                i: Int,
                i1: Int,
                i2: Int
            ) {
                notifyItemMoved(i, i1)
            }

            override fun onItemRangeRemoved(
                contributorViewModels: ObservableList<T>,
                i: Int,
                i1: Int
            ) {

                if (contributorViewModels.isEmpty()) {
                    mLastPosition = -1
                    notifyDataSetChanged()
                } else {
                    notifyItemRangeRemoved(i, i1)
                }
            }

        })
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BindingViewHolder<ViewDataBinding> =
        BindingViewHolder(
            DataBindingUtil.inflate<ViewDataBinding>(
                mLayoutInflater,
                layoutRes,
                parent,
                false
            )
        )

}