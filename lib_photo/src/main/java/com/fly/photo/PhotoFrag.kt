package com.fly.photo

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.fly.core.base.bindbase.BaseBindFragment
import com.fly.core.util.dp2px
import com.fly.core.widget.GridItemDecoration
import com.fly.photo.databinding.FragPhotoBinding

/**
 * Created by likainian on 2021/9/24
 * Description:
 */

class PhotoFrag : BaseBindFragment<FragPhotoBinding>() {
    override fun createBinding(): FragPhotoBinding {
        return FragPhotoBinding.inflate(layoutInflater)
    }

    var onConfirm:((path:String?)->Unit)? = null

    private val adapterPhoto by lazy {
        PhotoAdapter()
            .apply {
                onCheckListener = {
                    onConfirm?.invoke(it.path)
                }
            }
    }
    private val adapterFolder by lazy {
        PhotoFolderAdapter().apply {
            setOnItemClickListener { adapter, view, position ->
                vm.mediaFolderData.value?.let {
                    mViewDataBinding.tvTitle.text = it[position].folderName
                    adapterPhoto.setNewData(it[position].mediaList)
                    mViewDataBinding.recyclerFolder.visibility = View.GONE
                }
            }
        }
    }

    private val vm by lazy {
        ViewModelProvider(this).get(PhotoViewModel::class.java)
    }

    override fun initView(view: View) {
        mViewDataBinding.recyclerImage.layoutManager = GridLayoutManager(activity,3)
        mViewDataBinding.recyclerImage.addItemDecoration(GridItemDecoration(2.dp2px()))
        mViewDataBinding.recyclerImage.adapter = adapterPhoto

        mViewDataBinding.recyclerFolder.layoutManager = LinearLayoutManager(activity)

        mViewDataBinding.recyclerFolder.adapter = adapterFolder

        mViewDataBinding.tvTitle.setOnClickListener {
            if(mViewDataBinding.recyclerFolder.isShown){
                mViewDataBinding.recyclerFolder.visibility = View.GONE
                mViewDataBinding.flBack.visibility = View.VISIBLE
                mViewDataBinding.tvTitle.isSelected = false
            }else{
                mViewDataBinding.recyclerFolder.visibility = View.VISIBLE
                mViewDataBinding.flBack.visibility = View.GONE
                mViewDataBinding.tvTitle.isSelected = true
            }
        }
        mViewDataBinding.flBack.setOnClickListener {
            activity?.finish()
        }
    }

    override fun initData() {
        if(activity?.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            vm.getMediaPhotosFolder()
        }else{
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE),0)
        }

        vm.mediaFolderData.observe(this, Observer {
            mViewDataBinding.tvTitle.text = it[1].folderName
            adapterPhoto.setNewData(it[1].mediaList)
            adapterFolder.setNewData(it)
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        var isOK = true
        for (value in grantResults) {
            if (value != PackageManager.PERMISSION_GRANTED) {
                isOK = false
                break
            }
        }
        if(isOK){
            vm.getMediaPhotosFolder()
        }
    }
}