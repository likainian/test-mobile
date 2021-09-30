package com.fly.photo

import android.content.Intent
import com.fly.core.base.bindbase.BaseBindActivity
import com.fly.photo.databinding.ActivityPhotoBinding

/**
 * Created by likainian on 2021/9/24
 * Description:
 */

class PhotoActivity :BaseBindActivity<ActivityPhotoBinding>(){
    override fun createBinding(): ActivityPhotoBinding {
        return ActivityPhotoBinding.inflate(layoutInflater)
    }

    override fun initView() {
        supportFragmentManager.beginTransaction().add(R.id.frame,PhotoFrag().apply {
            onConfirm = {
                setResult(0, Intent().apply {
                    putExtra("path",it)
                })
                finish()
            }
        }).commit()
    }
}