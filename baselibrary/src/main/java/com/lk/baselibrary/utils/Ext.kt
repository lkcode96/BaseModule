package com.lk.baselibrary.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Point
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Parcelable
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatEditText
import androidx.databinding.ObservableArrayList
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import java.io.Serializable
import java.util.regex.Pattern
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext


fun Context.toast(msg: String, duration: Int = Toast.LENGTH_SHORT) =
    Toast.makeText(this, msg, duration).show()

fun Activity.toast(msg: String, duration: Int = Toast.LENGTH_SHORT) =
    Toast.makeText(this, msg, duration).show()

fun Fragment.toast(msg: String, duration: Int = Toast.LENGTH_SHORT) =
    Toast.makeText(this.context, msg, duration).show()

fun <T> MutableLiveData<T>.set(t: T?) = this.postValue(t)
fun <T> MutableLiveData<T>.get() = this.value

fun <T> MutableLiveData<T>.get(t: T): T = get() ?: t

fun <T> MutableLiveData<T>.init(t: T) = MutableLiveData<T>().apply {
    postValue(t)
}

fun MutableLiveData<Int>.plus() = MutableLiveData<Int>().apply {
    postValue(this.value!!.plus(1))
}


fun <T> MutableLiveData<List<T>>.toObservableArrayList(): ObservableArrayList<T> {
    return if (value == null) {
        ObservableArrayList()
    } else {
        ObservableArrayList<T>().apply { addAll(value!!) }
    }
}

fun Activity.setBackgroundAlpha(bgAlpha: Float) {
    val lp = this.window.attributes
    lp.alpha = bgAlpha
    this.window.attributes = lp
}

fun CoroutineScope.delayLaunch(
    timeMills: Long,
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend () -> Unit
): Job = launch(context, start) {
    check(timeMills >= 0) { "timeMills must be positive" }
    delay(timeMills)
    block()
}

fun CoroutineScope.repeatLaunch(
    interval: Long, count: Int = Int.MAX_VALUE,
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend (Int) -> Unit
): Job = launch(context, start) {
    check(interval >= 0) { "interval time must be positive" }
    check(count > 0) { "repeat count must larger than 0" }

    repeat(count) { index ->
        block(index)
        delay(interval)
    }
}

fun Context.callPhone(phone: String) {
    startActivity(Intent(Intent.ACTION_DIAL).apply {
        data = Uri.parse("tel:${phone}")
    })
}

inline fun <reified T : Activity> Context.launchActivity(
    vararg params: Pair<String, Any>
) {
    startActivity(
        Intent(
            this,
            T::class.java
        ).putExtras(*params)
    )
}

inline fun <reified T : Activity> Context.appExit(
    vararg params: Pair<String, Any>
) {
    startActivity(
        Intent(
            this,
            T::class.java
        ).putExtras(*params).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    )
}

inline fun <reified T : Activity> Fragment.launchActivity(
    vararg params: Pair<String, Any>
) {
    startActivity(
        Intent(
            activity,
            T::class.java
        ).putExtras(*params)
    )
}

inline fun <reified T : Activity> Fragment.launchActivityForResult(
    requestCode: Int,
    vararg params: Pair<String, Any>
) {
    startActivityForResult(
        Intent(
            activity,
            T::class.java
        ).putExtras(*params), requestCode
    )
}

inline fun <reified T : Activity> Activity.launchActivityForResult(
    requestCode: Int,
    vararg params: Pair<String, Any>

) {
    this.startActivityForResult(
        Intent(
            this,
            T::class.java
        ).putExtras(*params), requestCode
    )
}

fun Activity.forResult(
    resultCode: Int,
    vararg params: Pair<String, Any>

) {
    this.setResult(
        resultCode, Intent().putExtras(*params)
    )
    finish()
}

fun Intent.putExtras(vararg params: Pair<String, Any>): Intent {
    if (params.isEmpty()) return this
    params.forEach { (key, value) ->
        when (value) {
            is Int -> putExtra(key, value)
            is Byte -> putExtra(key, value)
            is Char -> putExtra(key, value)
            is Long -> putExtra(key, value)
            is Float -> putExtra(key, value)
            is Short -> putExtra(key, value)
            is Double -> putExtra(key, value)
            is Boolean -> putExtra(key, value)
            is Bundle -> putExtra(key, value)
            is String -> putExtra(key, value)
            is IntArray -> putExtra(key, value)
            is ByteArray -> putExtra(key, value)
            is CharArray -> putExtra(key, value)
            is LongArray -> putExtra(key, value)
            is FloatArray -> putExtra(key, value)
            is Parcelable -> putExtra(key, value)
            is ShortArray -> putExtra(key, value)
            is DoubleArray -> putExtra(key, value)
            is BooleanArray -> putExtra(key, value)
            is CharSequence -> putExtra(key, value)
            is Serializable -> putExtra(key, value)
            is Array<*> -> {
                when {
                    value.isArrayOf<String>() ->
                        putExtra(key, value as Array<String?>)
                    value.isArrayOf<Parcelable>() ->
                        putExtra(key, value as Array<Parcelable?>)
                    value.isArrayOf<CharSequence>() ->
                        putExtra(key, value as Array<CharSequence?>)
                    else -> putExtra(key, value)
                }
            }
        }
    }
    return this
}

fun Activity.alert(
    message: String,
    listener: (dialog: DialogInterface) -> Unit = {},
    negativeListener: () -> Unit = {},
    positiveTx: String = "确定",
    negativeTx: String = "取消",
    title: String = "提示",
) {
    AlertDialog.Builder(this).setTitle(title).setMessage(message).setPositiveButton(
        positiveTx
    ) { dialog, _ ->
        listener(dialog)
        dialog.dismiss()
    }
        .setNegativeButton(negativeTx) { dialog, _ ->
            negativeListener()
            dialog.dismiss()
        }.create().show()
}

fun Fragment.alert(
    message: String,
    listener: (dialog: DialogInterface) -> Unit,
    negativeListener: () -> Unit = {},
    positiveTx: String = "确定",
    negativeTx: String = "取消",
    title: String = "提示"
) {
    AlertDialog.Builder(context!!).setTitle(title).setMessage(message).setPositiveButton(
        positiveTx
    ) { dialog, _ -> listener(dialog) }
        .setNegativeButton(negativeTx) { dialog, _ ->
            negativeListener()
            dialog.dismiss()
        }.create().show()
}

fun String.isPassword(): Boolean {
    //6-16位 不能全是数字，字母
    val regex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,16}$"
    val p = Pattern.compile(regex)
    val m = p.matcher(this)
    return m.matches()
}

fun String.isPhone(): Boolean {
    val p =
        Pattern.compile("^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$")
    return p.matcher(this).matches()
}

//textView倒计时
fun TextView.countDown() {
    val countDownTimer = object : CountDownTimer(60000, 1000) {
        @SuppressLint("SetTextI18n")
        override fun onTick(millisUntilFinished: Long) {
            isClickable = false
            text = "${millisUntilFinished / 1000}秒后重新获取"
        }

        override fun onFinish() {
            isClickable = true
            text = "获取验证码"
        }
    }
    countDownTimer.start()
}

fun AppCompatEditText.isShowPwd(flag: Boolean) {
    if (flag) {
        //设置EditText的密码为可见的
        this.transformationMethod = HideReturnsTransformationMethod.getInstance()
    } else {
        //设置密码为隐藏的
        this.transformationMethod = PasswordTransformationMethod.getInstance()
    }
}

//隐藏键盘
fun Activity.hideBoard() {
    (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
}

fun Context.screenWidth(): Int {
    val vm = getSystemService(WINDOW_SERVICE) as WindowManager
    val point = Point()
    vm.defaultDisplay.getRealSize(point)
    return point.x
}

fun Context.screenHeight(): Int {
    val vm = getSystemService(WINDOW_SERVICE) as WindowManager
    val point = Point()
    vm.defaultDisplay.getRealSize(point)
    return point.y
}

fun Context.dp2px(dp: Int): Float {
    val density = resources.displayMetrics.density
    return (dp * density + 0.5f * if (dp >= 0) 1 else -1)
}

fun Context.sp2px(spValue: Int): Float {
    val fontScale = resources.displayMetrics.scaledDensity
    return (spValue * fontScale + 0.5f)
}

fun String.hideId(): String {
    val stringBuilder = StringBuilder(this)
    stringBuilder.replace(4, 10, "******")
    return stringBuilder.toString()
}

fun Context.getVersion(): String {
    return packageManager.getPackageInfo(packageName, 0).versionName
}






