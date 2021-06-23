package com.lk.baselibrary.utils

/**
 * @author LK
 * @date 2020/2/27
 */
class ResponseThrowable(var code: Int, var msg: String) : RuntimeException()