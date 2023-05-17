package com.sn.gameelectricity.ui.adapter

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target.SIZE_ORIGINAL
import com.sn.gameelectricity.R
import com.sn.gameelectricity.databinding.ItemProductPicBinding
import me.hgj.jetpackmvvm.base.adapter.BaseFunBindingRecyclerViewAdapter

class ProductPicAdapter(
    val mContext: Context
) : BaseFunBindingRecyclerViewAdapter<String, ItemProductPicBinding>() {

    override fun bindView(binding: ItemProductPicBinding, bean: String, position: Int) {
        binding.apply {
            val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(SIZE_ORIGINAL, SIZE_ORIGINAL)
                .format(DecodeFormat.PREFER_RGB_565)
                .placeholder(R.drawable.img_loading)
                .error(R.drawable.img_empty)

            Glide.with(mContext)
                .setDefaultRequestOptions(requestOptions)
                .load(bean)
                .into(ivGoodsPic)
        }
    }

}