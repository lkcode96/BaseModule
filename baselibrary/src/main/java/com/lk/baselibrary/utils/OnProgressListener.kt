package com.lk.baselibrary.utils

interface OnProgressListener {
    fun onProgress(byteWrite: Long, contentLength: Long)
}