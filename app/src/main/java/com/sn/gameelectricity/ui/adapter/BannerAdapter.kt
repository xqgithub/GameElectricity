package com.sn.gameelectricity.ui.adapter

import cc.shinichi.library.ImagePreview
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sn.gameelectricity.R
import com.sn.gameelectricity.databinding.ItemEvaluateBannerImageBinding
import com.zhpan.bannerview.BaseBannerAdapter
import com.zhpan.bannerview.BaseViewHolder
import singleClick

class BannerAdapter : BaseBannerAdapter<String>() {
    override fun bindData(
        holder: BaseViewHolder<String>,
        data: String,
        position: Int,
        pageSize: Int
    ) {
        val binding = ItemEvaluateBannerImageBinding.bind(holder.itemView)
        Glide.with(binding.imageView.context)
            .load(data)
            .apply(RequestOptions().apply {
                placeholder(R.drawable.img_loading)
                error(R.drawable.img_empty)
            })
            .into(binding.imageView)
        binding.imageView.singleClick {
            ImagePreview.getInstance()
                .setContext(binding.imageView.context)
                .setIndex(position)
                .setImage(data)
                .setImageList(mList)
                .setShowCloseButton(true)
                .setCloseIconResId(R.drawable.ic_icon_floate_close)
                .setEnableDragClose(true)
                .setEnableUpDragClose(true)
                .setShowDownButton(false)
                .setLoadStrategy(ImagePreview.LoadStrategy.Default)
                .start()
        }
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_evaluate_banner_image
    }

}