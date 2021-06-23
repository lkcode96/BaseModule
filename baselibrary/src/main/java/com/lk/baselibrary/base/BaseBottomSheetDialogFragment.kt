package com.lk.baselibrary.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheetDialogFragment<VB : ViewDataBinding> : BottomSheetDialogFragment(),
    Presenter {
    lateinit var mContext: Context
    protected open val mBinding: VB by lazy {
        DataBindingUtil.inflate<VB>(layoutInflater, getLayoutId(), null, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        //mBinding.setVariable(BR.presenter, this)
        mBinding.executePendingBindings()
        mBinding.lifecycleOwner = this
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun init()
}