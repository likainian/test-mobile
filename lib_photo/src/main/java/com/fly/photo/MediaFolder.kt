package com.fly.photo

data class MediaFolder(
    var folderName:String,
    var mediaList:ArrayList<MediaModel> = ArrayList()
)

data class MediaModel(
    var path:String?
)
