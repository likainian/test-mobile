package com.fly.video

data class MediaFolder(
    var folderName:String,
    var mediaList:ArrayList<MediaModel> = ArrayList()
)

data class MediaModel(
    var path:String?,
    var duration:Int = 0
){
    val durationV:String
    get() {
        return (duration/1000).toString()+"s"
    }
}
