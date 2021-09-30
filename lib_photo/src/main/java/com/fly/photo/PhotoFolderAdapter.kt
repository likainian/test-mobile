package com.fly.photo

import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.fly.core.util.dp2px
import com.fly.core.widget.RoundedCornersTransformation
import com.fly.photo.databinding.ItemPhotoBinding
import com.fly.photo.databinding.ItemPhotoFolderBinding

/**
 * Created by likainian on 2021/9/24
 * Description:
 */

class PhotoFolderAdapter : BaseQuickAdapter<MediaFolder, BaseViewHolder>(R.layout.item_photo_folder) {

    override fun onItemViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int) {
        DataBindingUtil.bind<ItemPhotoFolderBinding>(viewHolder.itemView)
    }

    override fun convert(helper: BaseViewHolder, item: MediaFolder) {
        val binding = helper.getBinding<ItemPhotoFolderBinding>()
        binding?.let {
            binding.tvName.text = item.folderName
            binding.tvNum.text = item.mediaList.size.toString()
            Glide.with(binding.ivImage).load(item.mediaList[0].path)
                .apply(RequestOptions().transform(RoundedCornersTransformation(10.dp2px())))
                .into(binding.ivImage)
        }
    }
}