package com.fly.video

import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.fly.video.databinding.ItemVideoBinding

/**
 * Created by likainian on 2021/9/24
 * Description:
 */

class VideoAdapter : BaseQuickAdapter<MediaModel, BaseViewHolder>(R.layout.item_video) {

    var onCheckListener:((item:MediaModel)->Unit)? = null

    override fun onItemViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int) {
        DataBindingUtil.bind<ItemVideoBinding>(viewHolder.itemView)
    }

    override fun convert(helper: BaseViewHolder, item: MediaModel) {
        val binding = helper.getBinding<ItemVideoBinding>()
        binding?.let {
            Glide.with(binding.ivImage).load(item.path).into(binding.ivImage)
            binding.tvDuration.text = item.durationV
            binding.root.setOnClickListener {
                onCheckListener?.invoke(item)
            }
        }
    }
}