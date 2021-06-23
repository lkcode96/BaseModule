package com.lk.baselibrary.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment


abstract class BaseDialogFragment<VB : ViewDataBinding> : DialogFragment(), Presenter {
    protected open lateinit var mContext: Context
    protected open val mBinding: VB by lazy {
        DataBindingUtil.inflate<VB>(layoutInflater, getLayoutId(), null, true)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        dialog?.setCanceledOnTouchOutside(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val lp = dialog!!.window!!.attributes
       // lp.windowAnimations = R.style.bottomSheet_animation
        dialog!!.window!!.attributes = lp
       // mBinding.setVariable(BR.presenter, this)
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