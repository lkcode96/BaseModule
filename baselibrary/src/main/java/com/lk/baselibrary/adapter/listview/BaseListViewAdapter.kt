package com.lk.baselibrary.adapter.listview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableArrayList
import androidx.databinding.ViewDataBinding

import com.lk.baselibrary.adapter.recycler.ItemClickPresenter

class BaseListViewAdapter<T>(
    private val layoutId: Int,
    private val mContext: Context,
    private var list: ObservableArrayList<T>
) : BaseAdapter() {
    var itemPresenter: ItemClickPresenter<T>? = null
    override fun getView(p0: Int, p1: View?, p2: ViewGroup): View {
        val item = list[p0]
        val dataBinding: ViewDataBinding = if (p1 == null) {
            DataBindingUtil.inflate(LayoutInflater.from(mContext), layoutId, p2, false)
        } else {
            DataBindingUtil.getBinding(p1)!!
        }
//        dataBinding.setVariable(BR.item, item)
//        dataBinding.setVariable(BR.presenter, itemPresenter)
        dataBinding.executePendingBindings()
        return dataBinding.root
    }

    override fun getItem(p0: Int): T = list[p0]

    override fun getItemId(p0: Int): Long = p0.toLong()

    override fun getCount(): Int = list.size

}