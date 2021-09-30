package com.fly.test.activity

import android.R.attr.*
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.fly.core.base.bindbase.BaseBindActivity
import com.fly.photo.PhotoActivity
import com.fly.test.databinding.ActivityCbgBinding
import com.huawei.hms.mlsdk.MLAnalyzerFactory
import com.huawei.hms.mlsdk.common.MLFrame
import com.huawei.hms.mlsdk.imgseg.MLImageSegmentationSetting
import java.util.*


/**
 * Created by likainian on 2021/9/17
 * Description:
 */

class CBgActivity : BaseBindActivity<ActivityCbgBinding>() {

    override fun createBinding(): ActivityCbgBinding {
        return ActivityCbgBinding.inflate(layoutInflater)
    }

    override fun initView() {
        mViewDataBinding.tvSelect.setOnClickListener {
            startActivityForResult(Intent(this, PhotoActivity::class.java),0)
//            EasyPhotos.createAlbum(this, true, GlideEngine.getInstance())//参数说明：上下文，是否显示相机按钮，是否使用宽高数据（false时宽高数据为0，扫描速度更快），[配置Glide为图片加载引擎](https://github.com/HuanTanSheng/EasyPhotos/wiki/12-%E9%85%8D%E7%BD%AEImageEngine%EF%BC%8C%E6%94%AF%E6%8C%81%E6%89%80%E6%9C%89%E5%9B%BE%E7%89%87%E5%8A%A0%E8%BD%BD%E5%BA%93)
//                .setFileProviderAuthority("com.fly.test.provider")//参数说明：见下方`FileProvider的配置
//                .start(object : SelectCallback() {
//                    override fun onResult(
//                        photos: ArrayList<Photo>?,
//                        paths: ArrayList<String>?,
//                        isOriginal: Boolean
//                    ) {
//                        paths?.firstOrNull {
//                            Glide.with(mViewDataBinding.ivImage).asBitmap().load(it)
//                                .addListener(object :RequestListener<Bitmap>{
//                                    override fun onResourceReady(
//                                        resource: Bitmap?,
//                                        model: Any?,
//                                        target: Target<Bitmap>?,
//                                        dataSource: DataSource?,
//                                        isFirstResource: Boolean
//                                    ): Boolean {
//                                        mlBg(resource)
//                                        return false
//                                    }
//
//                                    override fun onLoadFailed(
//                                        e: GlideException?,
//                                        model: Any?,
//                                        target: Target<Bitmap>?,
//                                        isFirstResource: Boolean
//                                    ): Boolean {
//                                        return false
//                                    }
//
//                                }).into(mViewDataBinding.ivImage)
//                          return@firstOrNull  false
//                        }
//                    }
//                })
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val path = data?.getStringExtra("path")
        path?.let {
            Glide.with(mViewDataBinding.ivImage).asBitmap().load(path)
                .addListener(object :RequestListener<Bitmap>{
                    override fun onResourceReady(
                        resource: Bitmap?,
                        model: Any?,
                        target: Target<Bitmap>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        resource?.let {
                            mlBg(resource)
                        }
                        return false
                    }

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Bitmap>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                }).into(mViewDataBinding.ivImage)
        }

    }

    private fun mlBg(originBitmap: Bitmap?){
        Thread {
            val setting = MLImageSegmentationSetting.Factory()
                .setAnalyzerType(MLImageSegmentationSetting.BODY_SEG)
                .setExact(true)
                .create()
            val analyzer = MLAnalyzerFactory.getInstance().getImageSegmentationAnalyzer(setting)
            val mlFrame = MLFrame.Creator().setBitmap(originBitmap).create()
            // 创建一个task，处理图像分割检测器返回的结果。
            analyzer?.asyncAnalyseFrame(mlFrame)?.addOnSuccessListener {
                if (it != null) {
                    val foreground = it.getForeground()
                    mViewDataBinding.ivImage.setBackgroundColor(Color.RED)
                    Glide.with(mViewDataBinding.ivImage).load(foreground).into(mViewDataBinding.ivImage)
                }
            }
        }.start()
    }


}