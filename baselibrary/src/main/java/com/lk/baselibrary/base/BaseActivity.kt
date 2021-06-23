package com.lk.baselibrary.base

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.gyf.immersionbar.ImmersionBar
import com.lk.baselibrary.common.AppManager
import com.kaopiz.kprogresshud.KProgressHUD
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope


abstract class BaseActivity<VB : ViewDataBinding> : AppCompatActivity(), Presenter,
    CoroutineScope by MainScope() {
    protected open val mBinding: VB by lazy {
        DataBindingUtil.setContentView<VB>(
            this,
            getLayoutId()
        )
    }
    protected open val hud: KProgressHUD by lazy {
        KProgressHUD.create(this)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel("加载中")
            .setCancellable(true)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)
    }

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppManager.addActivity(this)
        //mBinding.setVariable(BR.presenter, this)
        mBinding.executePendingBindings()
        mBinding.lifecycleOwner = this
        initStatusBar()
        initData()
    }

    override fun onClick(v: View) {
//        if (v.id == R.id.title_back) {
//            finish()
//        }
    }

    override fun onResume() {
        if (requestedOrientation != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        super.onResume()
    }

    override fun onDestroy() {
        AppManager.finishActivity(this)
        super.onDestroy()
    }

    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun initData()

    open fun initStatusBar() {
        ImmersionBar.with(this).statusBarColor(R.color.white).navigationBarColor(R.color.white).statusBarDarkFont(true).init()
    }

}