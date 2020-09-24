package com.wizzpass.hilt.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream

/**
 * Created by novuyo on 24,September,2020
 */

public fun convertBase64ToBitmap(b64: String): Bitmap? {
    val imageAsBytes =
        Base64.decode(b64.toByteArray(), Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.size)
}

@Throws(Exception::class)
fun getStringImage(bmp: Bitmap?): String {
    if (bmp == null) throw Exception("No image found")
    val baos = ByteArrayOutputStream()
    bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos)
    val imageBytes = baos.toByteArray()
    return Base64.encodeToString(imageBytes, Base64.DEFAULT)
}
