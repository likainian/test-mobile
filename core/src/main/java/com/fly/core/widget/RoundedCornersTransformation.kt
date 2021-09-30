package com.fly.core.widget

import android.graphics.*
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import java.security.MessageDigest

/**
 * Created by likainian on 2021/9/26
 * Description:
 */

class RoundedCornersTransformation(val radius:Int) : BitmapTransformation() {
    private val ID = "com.fly.core.widget.RoundedCornersTransformation"
    private val ID_BYTES = ID.toByteArray(CHARSET)
    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(ID_BYTES)
    }

    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
        val width = toTransform.width
        val height = toTransform.height

        val bitmap1 = pool[width, height, Bitmap.Config.ARGB_8888]
        val bitmap = TransformationUtils.centerCrop(pool, bitmap1, outWidth, outHeight)
        bitmap.setHasAlpha(true)

        val canvas = Canvas(bitmap)
        val paint = Paint()
        paint.isAntiAlias = true
        paint.shader = BitmapShader(toTransform, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        val rectF = RectF(0f, 0f, outWidth.toFloat(), outHeight.toFloat())
        canvas.drawRoundRect(rectF, radius.toFloat(), radius.toFloat(), paint)
        return bitmap
    }
}