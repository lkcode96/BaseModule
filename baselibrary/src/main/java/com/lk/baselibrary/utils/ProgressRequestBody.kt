package com.lk.baselibrary.utils

import okhttp3.MediaType
import okhttp3.RequestBody
import okio.*

class ProgressRequestBody(private val body: RequestBody, private var listener: OnProgressListener) :
    RequestBody() {
    var mIsSecond = false
    override fun contentLength(): Long {
        return body.contentLength()
    }

    override fun contentType(): MediaType? {
        return body.contentType()
    }

    override fun writeTo(sink: BufferedSink) {
        val s = CountingSink(sink, contentLength(), listener)
        val bufferedSink = s.buffer()
        body.writeTo(bufferedSink)
        bufferedSink.flush()
        mIsSecond = true
    }

    inner class CountingSink(
        delegate: Sink,
        private val length: Long,
        private val listener: OnProgressListener
    ) :
        ForwardingSink(delegate) {
        var byteWrite: Long = 0
        override fun write(source: Buffer, byteCount: Long) {
            super.write(source, byteCount)
            byteWrite += byteCount
            if (mIsSecond) {
                listener.onProgress(byteWrite, length)
            }
        }
    }

    override fun isOneShot(): Boolean {
        return true
    }
}