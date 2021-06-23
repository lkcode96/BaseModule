package com.lk.baselibrary.base

import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author LK
 * @date 2020-01-12
 */
object ImageBindingAdapter {
    @BindingAdapter(value = ["date", "format"], requireAll = false)
    @JvmStatic
    fun bindFormat(view: TextView, date: String?, format: String?) {
        if (date == null) {
            return
        }
        val formatter: SimpleDateFormat = if (format != null) {
            SimpleDateFormat(format, Locale.getDefault())
        } else {
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        }
        val timeStamp = date.toLong()
        view.text = formatter.format(Date(timeStamp))
    }

    @BindingAdapter("time")
    @JvmStatic
    fun bindDataSplit(view: TextView, date: String) {
        val list = date.split(" ")
        view.text = list[list.size - 1]
    }

    @BindingAdapter("imageUrl")
    @JvmStatic
    fun bindImgUrl(view: ImageView, url: String?) {

    }

    @BindingAdapter("imageUrl")
    @JvmStatic
    fun bindImgUrl(view: ImageView, drawableId: Int?) {

    }

    @BindingAdapter("imageUrl")
    @JvmStatic
    fun bindImgUrl(view: ImageView, url: Uri?) {

    }

    @BindingAdapter("imageUrl", "isVideo")
    @JvmStatic
    fun bindImgUrl(view: ImageView, url: File, isVideo: Boolean) {

    }

    @BindingAdapter("imageUrl")
    @JvmStatic
    fun bindImgUrl(view: ImageView, url: File?) {
    }

    @BindingAdapter("imageUrl", "isVideo")
    @JvmStatic
    fun bindImgUrlVideo(view: ImageView, url: String?, isVideo: Boolean) {

    }

    @BindingAdapter("imageUrl")
    @JvmStatic
    fun bindImgUrl(view: ImageView, drawable: Drawable?) {

    }

    @BindingAdapter("imageUrl", "error")
    @JvmStatic
    fun bindImgUrl(view: ImageView, url: String, error: Drawable) {

    }

    @BindingAdapter(value = ["imageUrl", "isCircle"])
    @JvmStatic
    fun bindImgUrl(view: ImageView, url: String?, isCircle: Boolean) {

    }

    @BindingAdapter(value = ["imageUrl", "isRound", "roundSize"], requireAll = false)
    @JvmStatic
    fun bindImgUrl(view: ImageView, url: String?, isRound: Int, roundSize: Int = 5) {

    }


    @BindingAdapter(value = ["visible"])
    @JvmStatic
    fun bindVisibility(v: View, visible: Boolean) {
        v.visibility = if (visible) View.VISIBLE else View.GONE
    }

    @BindingAdapter(value = ["isMoreData"])
    @JvmStatic
    fun finishLoad(v: SmartRefreshLayout, isMoreData: Boolean) {
//        v.finishLoadMore(200)
//        v.finishRefresh(200)
//        v.setNoMoreData(isMoreData)
    }

}