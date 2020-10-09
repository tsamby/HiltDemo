package com.wizzpass.hilt.util

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.Base64
import androidx.constraintlayout.widget.ConstraintLayout
import com.wizzpass.hilt.R
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

@SuppressLint("ResourceAsColor")
fun setBorder(constraintLayout : ConstraintLayout) {

    var border = GradientDrawable()
    border.setColor(-0x1) //white background

    border.setStroke(4, R.color.colorAccent)
    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
        constraintLayout.setBackgroundDrawable(border)
    } else {
        constraintLayout.setBackground(border)
    }
}
@SuppressLint("ResourceAsColor")
fun setBorderRed(constraintLayout : ConstraintLayout) {

    var border1 = GradientDrawable()
    border1.setColor(-0x1) //white background
    border1.cornerRadius = 18f

    border1.setStroke(4, Color.RED)
    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
        constraintLayout.setBackgroundDrawable(border1)
    } else {
        constraintLayout.setBackground(border1)
    }
}


