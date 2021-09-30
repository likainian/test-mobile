package com.fly.test.widget

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.fly.core.util.dp2px
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlin.math.abs

/**
 * Created by likainian on 2021/9/27
 * Description:
 */

class WaterView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0): AppCompatImageView(
    context,
    attrs,
    defStyleAttr
){
    private var bitmap:Bitmap? = null
    private val rectF = RectF()
    private val imageRectF = RectF()
    private val paint = Paint()
    private val paintB = Paint()
    private val rectW = 100f.dp2px().toFloat()
    private val rectH = 100f.dp2px().toFloat()
    private val rectB = 2f.dp2px().toFloat()
    init {
        rectF.set(0f,0f,rectW,rectH)
        paint.color = Color.parseColor("#77ffffff")
        paintB.color = Color.parseColor("#000000")
        paintB.strokeWidth = rectB
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        rectF.set(width/2-rectW/2,height/2-rectH/2,width/2+rectW/2,height/2+rectH/2)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val drawable = drawable
        if (drawable != null) {
            imageRectF.set(0f, 0f, drawable.intrinsicWidth.toFloat(), drawable.intrinsicHeight.toFloat())
            imageMatrix.mapRect(imageRectF)

            check()
            canvas?.drawRect(rectF,paint)
            canvas?.drawLine(rectF.left-rectB/2,rectF.top,rectF.left+rectW/5,rectF.top,paintB)
            canvas?.drawLine(rectF.right-rectW/5,rectF.top,rectF.right+rectB/2,rectF.top,paintB)
            canvas?.drawLine(rectF.left,rectF.bottom,rectF.left+rectW/5,rectF.bottom,paintB)
            canvas?.drawLine(rectF.right-rectW/5,rectF.bottom,rectF.right,rectF.bottom,paintB)
            canvas?.drawLine(rectF.left,rectF.top,rectF.left,rectF.top+rectH/5,paintB)
            canvas?.drawLine(rectF.right,rectF.top,rectF.right,rectF.top+rectH/5,paintB)
            canvas?.drawLine(rectF.left,rectF.bottom-rectH/5,rectF.left,rectF.bottom+rectB/2,paintB)
            canvas?.drawLine(rectF.right,rectF.bottom-rectH/5,rectF.right,rectF.bottom+rectB/2,paintB)
        }
    }

    fun remove(){
        val sid = Observable.create<Bitmap> {
            bitmap?.let {originBitmap->
                val startX = originBitmap.width*(rectF.left-imageRectF.left)/imageRectF.width()
                val endX = originBitmap.width*(rectF.right-imageRectF.left)/imageRectF.width()
                val startY = originBitmap.height*(rectF.top-imageRectF.top)/imageRectF.height()
                val endY = originBitmap.height*(rectF.bottom-imageRectF.top)/imageRectF.height()

                for (x in 0 until originBitmap.width){
                    for (y in 0 until originBitmap.height){
                        if(x>startX&&x<endX&&y>startY&&y<endY){
                            val startColor = originBitmap.getPixel(x, startY.toInt())
                            val endColor = originBitmap.getPixel(x, endY.toInt())
                            val c = createNewColor(startColor,endColor,(y-startY)/(endY-startY))
                            originBitmap.setPixel(x,y, c)
                        }

                    }
                }
                it.onNext(originBitmap)
            }
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe {
                setImageBitmap(it)
            }

    }

    /**
     * 色值上叠加
     **/
    private fun createNewColor(color: Int, overlayColor: Int, alpha: Float): Int {
        val r = Color.red(color)
        val g = Color.green(color)
        val b = Color.blue(color)
        val ovR = Color.red(overlayColor)
        val ovG = Color.green(overlayColor)
        val ovB = Color.blue(overlayColor)
        val newR = createColor(r, ovR, alpha)
        val newG = createColor(g, ovG, alpha)
        val newB = createColor(b, ovB, alpha)
        return Color.rgb(newR, newG, newB)
    }

    private fun createColor(color: Int, overlay: Int, alpha: Float): Int {
        return (color * (1 - alpha) + overlay * alpha).toInt()
    }

    private fun check(){
        if(rectF.left<imageRectF.left){
            rectF.left = imageRectF.left
        }
        if(rectF.right>imageRectF.right){
            rectF.right = imageRectF.right
        }
        if(rectF.top<imageRectF.top){
            rectF.top = imageRectF.top
        }
        if(rectF.bottom>imageRectF.bottom){
            rectF.bottom = imageRectF.bottom
        }
        if(rectF.bottom-rectF.top<rectH/5){
            if(rectF.top+rectH/5>imageRectF.bottom){
                rectF.top = imageRectF.bottom-rectH/5
                rectF.bottom = imageRectF.bottom
            }else{
                rectF.bottom = rectF.top+rectH/5
            }
        }
        if(rectF.right-rectF.left<rectH/5){
            if(rectF.right+rectH/5>imageRectF.right){
                rectF.left = imageRectF.right-rectW/5
                rectF.right = imageRectF.right
            }else{
                rectF.right =rectF.left+ rectH/5
            }
        }
    }

    var startX = 0f
    var startY = 0f
    private var startRectF = RectF(rectF)
    var model = 0//0移动，1234拉伸
    override fun onTouchEvent(event: MotionEvent?): Boolean {

        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = event.x
                startY = event.y
                startRectF = RectF(rectF)
                model = 0
                if(abs(event.x-rectF.left)<20&& abs(event.y-rectF.top)<20){
                    model = 1
                }
                if(abs(event.x-rectF.right)<20&& abs(event.y-rectF.top)<20){
                    model = 2
                }
                if(abs(event.x-rectF.left)<20&& abs(event.y-rectF.bottom)<20){
                    model = 3
                }
                if(abs(event.x-rectF.right)<20&& abs(event.y-rectF.bottom)<20){
                    model = 4
                }
            }
            MotionEvent.ACTION_MOVE -> {
                val mX = event.x-startX
                val mY = event.y-startY
                if(model == 0){
                    rectF.set(startRectF.left+mX,startRectF.top+mY,startRectF.right+mX,startRectF.bottom+mY)
                }
                if(model==1){
                    rectF.set(startRectF.left+mX,startRectF.top+mY,startRectF.right,startRectF.bottom)
                }
                if(model == 2){
                    rectF.set(startRectF.left,startRectF.top+mY,startRectF.right+mX,startRectF.bottom)
                }
                if(model == 3){
                    rectF.set(startRectF.left+mX,startRectF.top,startRectF.right,startRectF.bottom+mY)
                }
                if(model == 4){
                    rectF.set(startRectF.left,startRectF.top,startRectF.right+mX,startRectF.bottom+mY)
                }

                check()
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                performClick()
            }
        }
        return true
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    override fun setImageBitmap(bm: Bitmap?) {
        bitmap = bm
        super.setImageBitmap(bm)
    }
}