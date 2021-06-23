package com.lk.baselibrary.base

/**
 * @author LK
 * @date 2020-01-05
 */
data class ResponseData<out T>(
    val code: Int,
    val message: String,
    val success: Boolean,
    val data: T,
    val time: String
)