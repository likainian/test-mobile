package com.fly.video

import android.provider.MediaStore
import androidx.lifecycle.MutableLiveData
import com.fly.core.base.BaseViewModel
import com.fly.core.base.appContext
import java.io.File

/**
 * Created by likainian on 2021/9/24
 * Description:
 */

class VideoViewModel: BaseViewModel() {
    val mediaFolderData = MutableLiveData<ArrayList<MediaFolder>>()
    fun getMediaPhotosFolder() {
        Thread{
            val mediaFolders = ArrayList<MediaFolder>()
            val map = HashMap<String,MediaFolder>()
            map["all"] = MediaFolder("all")
            val projection = arrayOf(
                MediaStore.Images.ImageColumns._ID,
                MediaStore.Images.ImageColumns.DATA,
                MediaStore.Video.Media.DURATION
            )
            val where =
                (MediaStore.Images.Media.MIME_TYPE + "=? or "
                        + MediaStore.Video.Media.MIME_TYPE + "=? or "
                        + MediaStore.Video.Media.MIME_TYPE + "=? or "
                        + MediaStore.Video.Media.MIME_TYPE + "=? or "
                        + MediaStore.Video.Media.MIME_TYPE + "=? or "
                        + MediaStore.Video.Media.MIME_TYPE + "=? or "
                        + MediaStore.Video.Media.MIME_TYPE + "=? or "
                        + MediaStore.Video.Media.MIME_TYPE + "=? or "
                        + MediaStore.Video.Media.MIME_TYPE + "=?")

            val whereArgs = arrayOf(
                "video/mp4", "video/3gp", "video/avi",
                "video/rmvb", "video/vob", "video/flv",
                "video/mkv", "video/mov", "video/mpg"
            )
            val cursor = appContext.contentResolver
                .query(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    projection,
                    where,
                    whereArgs,
                    MediaStore.Images.Media.DATE_MODIFIED + " desc"//日期
                )
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    cursor.let {
                        val id = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media._ID))
                        val path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
                        val duration = it.getInt(it.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION))
                        val imageFile = File(path)
                        if (!imageFile.exists()) return@let

                        val folderFile = imageFile.parentFile
                        folderFile?.name?.let {name->
                            if (!map.contains(name)) {
                                map[name] = MediaFolder(name)
                            }
                            map[name]?.mediaList?.add(MediaModel(path,duration))
                            map["all"]?.mediaList?.add(MediaModel(path,duration))
                        }

                    }
                }
                cursor.close()
            }

            map["Screenshots"]?.let {
                mediaFolders.add(0,it)
                map.remove("Screenshots")
            }
            map["Camera"]?.let {
                mediaFolders.add(0,it)
                map.remove("Camera")
            }
            map["all"]?.let {
                mediaFolders.add(0,it)
                map.remove("all")
            }

            map.forEach {
                mediaFolders.add(it.value)
            }
            mediaFolderData.postValue(mediaFolders)
        }.start()

    }
}