package com.fly.test.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.fly.core.base.bindbase.BaseBindActivity
import com.fly.photo.PhotoActivity
import com.fly.test.databinding.ActivityWaterBinding
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by likainian on 2021/9/27
 * Description:
 */

class WaterActivity : BaseBindActivity<ActivityWaterBinding>() {
    private var bitmap:Bitmap? = null
    override fun createBinding(): ActivityWaterBinding {
        return ActivityWaterBinding.inflate(layoutInflater)
    }

    override fun initView() {
        mViewDataBinding.tvSelect.setOnClickListener {
            startActivityForResult(Intent(this, PhotoActivity::class.java), 0)
        }
        mViewDataBinding.tvRemove.setOnClickListener {
            bitmap?.let {
                mViewDataBinding.ivImage.remove()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val path = data?.getStringExtra("path")
        path?.let {
            Glide.with(mViewDataBinding.ivImage).asBitmap().load(path)
                .addListener(object : RequestListener<Bitmap> {
                    override fun onResourceReady(
                        resource: Bitmap?,
                        model: Any?,
                        target: Target<Bitmap>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        bitmap = resource
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


}