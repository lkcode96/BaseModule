package com.lk.baselibrary.utils

import android.graphics.Bitmap
import android.graphics.Matrix
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.journeyapps.barcodescanner.BarcodeEncoder
import java.util.*


object ZxingUtils {
    private val barcodeEncoder by lazy { BarcodeEncoder() }
    fun createQRCode(
        contents: String,
        size: Int = 500,
        whiteBorderScale: Int = 1,
        logo: Bitmap? = null
    ): Bitmap {
        return if (logo == null) {
            barcodeEncoder.encodeBitmap(
                contents, BarcodeFormat.QR_CODE, size, size, hashMapOf(
                    EncodeHintType.MARGIN to whiteBorderScale
                )
            )
        } else {
            createQRCodeWithLogo(contents, size, whiteBorderScale, logo)
        }
    }

    private fun createQRCodeWithLogo(
        contents: String,
        size: Int,
        whiteBorderScale: Int,
        logo: Bitmap
    ): Bitmap {
        val IMAGE_HALFWIDTH = size / 10
        val hints = Hashtable<EncodeHintType, Any>()
        hints[EncodeHintType.CHARACTER_SET] = "utf-8"
        hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.H
        hints[EncodeHintType.MARGIN] = whiteBorderScale
        val bitMatrix = QRCodeWriter().encode(contents, BarcodeFormat.QR_CODE, size, size, hints)
        val width = bitMatrix.width //矩阵高度

        val height = bitMatrix.height //矩阵宽度

        val halfW = width / 2
        val halfH = height / 2
        val m = Matrix()
        val sx: Float = 2.toFloat() * IMAGE_HALFWIDTH / logo.width
        val sy: Float = 2.toFloat() * IMAGE_HALFWIDTH / logo.height
        m.setScale(sx, sy)
        //设置缩放信息
        //将logo图片按martix设置的信息缩放
        val logo = Bitmap.createBitmap(
            logo,
            0,
            0,
            logo.width,
            logo.height,
            m,
            false
        )
        val pixels = IntArray(size * size)
        for (y in 0 until size) {
            for (x in 0 until size) {
                if (x > halfW - IMAGE_HALFWIDTH && x < halfW + IMAGE_HALFWIDTH && y > halfH - IMAGE_HALFWIDTH && y < halfH + IMAGE_HALFWIDTH) {
                    //该位置用于存放图片信息
                    //记录图片每个像素信息
                    pixels[y * width + x] = logo.getPixel(
                        x - halfW
                                + IMAGE_HALFWIDTH, y - halfH + IMAGE_HALFWIDTH
                    )
                } else {
                    if (bitMatrix[x, y]) {
                        pixels[y * size + x] = -0x1000000
                    } else {
                        pixels[y * size + x] = -0x1
                    }
                }
            }
        }
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixels, 0, size, 0, 0, size, size)
        return bitmap
    }
}