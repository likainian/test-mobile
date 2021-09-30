package com.fly.video

import android.content.Intent
import com.fly.core.base.bindbase.BaseBindActivity
import com.fly.video.databinding.ActivityVideoBinding

class VideoActivity: BaseBindActivity<ActivityVideoBinding>(){

    override fun createBinding(): ActivityVideoBinding {
        return ActivityVideoBinding.inflate(layoutInflater)
    }

    override fun initView() {
        supportFragmentManager.beginTransaction().add(R.id.frame,VideoFrag().apply {
            onConfirm = {
                setResult(0, Intent().apply {
                    putExtra("path",it)
                })
                finish()
            }
        }).commit()
    }
}