package com.sn.gameelectricity.ui.adapter

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sn.gameelectricity.R
import com.sn.gameelectricity.data.model.bean.GetAssResponse
import com.sn.gameelectricity.databinding.ItemCheatingPicsBinding
import me.hgj.jetpackmvvm.base.adapter.BaseFunBindingRecyclerViewAdapter
import me.jingbin.library.adapter.BaseByViewHolder

class CheatingPicsAdapter(
    val mContext: Context
) : BaseFunBindingRecyclerViewAdapter<GetAssResponse, ItemCheatingPicsBinding>() {

    override fun bindView(
        binding: ItemCheatingPicsBinding,
        bean: GetAssResponse,
        position: Int
    ) {
    }

    override fun bindView(
        holder: BaseByViewHolder<GetAssResponse>,
        binding: ItemCheatingPicsBinding,
        bean: GetAssResponse,
        position: Int
    ) {
        binding.apply {

            Glide.with(mContext)
                .load(bean.avatarUrl)
                .centerCrop()
                .apply(RequestOptions().apply {
                    placeholder(R.drawable.ic_share_add)
                    error(R.drawable.ic_share_add)
                })
                .into(sivImg)
//            sivImg.load(bean.avatarUrl)

            tvNickName.text = bean.nickName
        }
    }

}