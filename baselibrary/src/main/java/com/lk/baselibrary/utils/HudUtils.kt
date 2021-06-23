package com.lk.baselibrary.utils

import android.content.Context
import com.kaopiz.kprogresshud.KProgressHUD

/**
 * @author: Administrator
 * @date: 2021/6/17
 */
object HudUtils {
    fun initHud(context: Context): KProgressHUD {
        return KProgressHUD.create(context)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel("加载中")
            .setDetailsLabel("Downloading data")
            .setCancellable(true)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)
    }
}