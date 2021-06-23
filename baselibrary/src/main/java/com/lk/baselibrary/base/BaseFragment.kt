package com.lk.baselibrary.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.kaopiz.kprogresshud.KProgressHUD

abstract class BaseFragment<VB : ViewDataBinding> : Fragment(), Presenter {

    protected open lateinit var mContext: Context
    private var hasLoaded = false

    protected open val hud: KProgressHUD by lazy {
        KProgressHUD.create(mContext)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel("加载中")
            .setCancellable(true)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)
    }

    protected open val mBinding: VB by lazy {
        DataBindingUtil.inflate<VB>(
            LayoutInflater.from(activity),
            getLayoutId(),
            null,
            false
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initStatusBar()
    }

    override fun onResume() {
        super.onResume()
        if (!hasLoaded) {
            loadData()
            hasLoaded = true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       // mBinding.setVariable(BR.presenter, this)
        mBinding.executePendingBindings()
        mBinding.lifecycleOwner = this
        return mBinding.root
    }

    override fun onClick(v: View) {

    }

    open fun initStatusBar() {

    }

    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun init()
    abstract fun loadData()

}