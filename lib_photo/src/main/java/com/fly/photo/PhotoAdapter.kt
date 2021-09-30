package com.fly.photo

import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.fly.photo.databinding.ItemPhotoBinding

/**
 * Created by likainian on 2021/9/24
 * Description:
 */

class PhotoAdapter : BaseQuickAdapter<MediaModel, BaseViewHolder>(R.layout.item_photo) {

    var onCheckListener:((item:MediaModel)->Unit)? = null

    override fun onItemViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int) {
        DataBindingUtil.bind<ItemPhotoBinding>(viewHolder.itemView)
    }

    override fun convert(helper: BaseViewHolder, item: MediaModel) {
        val binding = helper.getBinding<ItemPhotoBinding>()
        binding?.let {
            Glide.with(binding.ivImage).load(item.path).into(binding.ivImage)
            binding.root.setOnClickListener {
                onCheckListener?.invoke(item)
            }
        }
    }
}