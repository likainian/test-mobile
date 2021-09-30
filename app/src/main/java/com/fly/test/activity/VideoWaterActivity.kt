package com.fly.test.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.RectF
import android.media.MediaMetadataRetriever
import android.os.Bundle
import com.fly.core.base.bindbase.BaseBindActivity
import com.fly.photo.PhotoActivity
import com.fly.test.databinding.ActivityWaterBinding
import com.fly.test.databinding.ActivityWaterVideoBinding
import com.fly.video.VideoActivity
import com.hw.videoprocessor.VideoProcessor

/**
 * Created by likainian on 2021/9/30
 * Description:
 */

class VideoWaterActivity : BaseBindActivity<ActivityWaterVideoBinding>() {
    override fun createBinding(): ActivityWaterVideoBinding {
        return ActivityWaterVideoBinding.inflate(layoutInflater)
    }

    override fun initView() {
        mViewDataBinding.tvSelect.setOnClickListener {
            startActivityForResult(Intent(this, VideoActivity::class.java), 0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val path = data?.getStringExtra("path")
        mViewDataBinding.videoView.setVideoPath(path)
        mViewDataBinding.videoView.start()
        val mmr = MediaMetadataRetriever()
        mmr.setDataSource(path)
        val height = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)?.toInt() ?: 0
        val width = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)?.toInt() ?: 0
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        bitmap.eraseColor(Color.TRANSPARENT) //填充颜色
        mViewDataBinding.ivImage.setImageBitmap(bitmap)
    }

    fun water(path:String,rectF: RectF){
        VideoProcessor.processor(this)
            .input(path)
            .output("")
            .process()
    }
}