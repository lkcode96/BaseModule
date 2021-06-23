package com.lk.baselibrary.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import com.lk.baselibrary.R
import com.lk.baselibrary.base.Presenter
import com.lk.baselibrary.databinding.CustomTitleBarLayoutBinding


class TitleBarLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    var centerText: String?
    var rightText = ObservableField<String>()
    var rightImg: Drawable? = null
    var themeMode: Int = 0
    var isShowLiftImg: Boolean = true
    var mBinding: CustomTitleBarLayoutBinding
    private val inflater by lazy { context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater }
    private val typeArray by lazy {
        context.obtainStyledAttributes(
            attrs,
            R.styleable.TitleBarLayout
        )
    }

    init {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.custom_title_bar_layout, this, true)
        centerText = typeArray.getString(R.styleable.TitleBarLayout_title_text)
        rightText.set(typeArray.getString(R.styleable.TitleBarLayout_right_text))
        rightImg = typeArray.getDrawable(R.styleable.TitleBarLayout_right_img)
        themeMode = typeArray.getInteger(R.styleable.TitleBarLayout_theme_mode, 0)
        isShowLiftImg = typeArray.getBoolean(R.styleable.TitleBarLayout_is_show_left_img, true)
        typeArray.recycle()
        //mBinding.setVariable(BR.item, this)
        mBinding.executePendingBindings()
    }

    fun setPresenter(presenter: Presenter) {
        // mBinding.setVariable(BR.presenter, presenter)
        mBinding.executePendingBindings()
    }

    fun setTitle_text(str: String?) {
        //mBinding.textView170.text = str
    }
}