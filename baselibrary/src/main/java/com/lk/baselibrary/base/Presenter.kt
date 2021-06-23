package com.lk.baselibrary.base

import android.view.View

interface Presenter :View.OnClickListener{
    override fun onClick(v:View)
}