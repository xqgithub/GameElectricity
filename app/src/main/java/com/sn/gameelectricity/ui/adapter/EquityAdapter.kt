package com.sn.gameelectricity.ui.adapter

import android.content.Context
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sn.gameelectricity.R
import com.sn.gameelectricity.data.model.bean.GoodsRightListResponse
import com.sn.gameelectricity.databinding.ItemEquityBinding
import me.hgj.jetpackmvvm.base.adapter.BaseFunBindingRecyclerViewAdapter
import me.hgj.jetpackmvvm.util.PublicPracticalMethodFromKT
import me.hgj.jetpackmvvm.util.ScreenTools
import me.jingbin.library.adapter.BaseByViewHolder

class EquityAdapter(
    val mContext: Context
) : BaseFunBindingRecyclerViewAdapter<GoodsRightListResponse.GoodsVOListDTO, ItemEquityBinding>() {

    override fun bindView(
        holder: BaseByViewHolder<GoodsRightListResponse.GoodsVOListDTO>,
        binding: ItemEquityBinding,
        bean: GoodsRightListResponse.GoodsVOListDTO,
        position: Int
    ) {
        super.bindView(holder, binding, bean, position)
    }

    override fun bindView(
        binding: ItemEquityBinding,
        bean: GoodsRightListResponse.GoodsVOListDTO,
        position: Int
    ) {
        binding.apply {

            PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
                clMain, -1, "",
                ScreenTools.getInstance().dp2px(mContext, 4f, true).toFloat(),
                ScreenTools.getInstance().dp2px(mContext, 4f, true).toFloat(),
                ScreenTools.getInstance().dp2px(mContext, 4f, true).toFloat(),
                ScreenTools.getInstance().dp2px(mContext, 4f, true).toFloat(),
                null, "#FFFFFF"
            )

            Glide.with(mContext)
                .load(bean.goodsIcon as String?)
                .centerCrop()
                .apply(RequestOptions().apply {
                    placeholder(R.drawable.ic_icon_non)
                    error(R.drawable.ic_icon_non)
                })
                .into(sivIcon)

            if (bean.number > 0) {
                tvMark.visibility = View.VISIBLE
                tvMark.text = "x${bean.number}"
            } else {
                tvMark.visibility = View.GONE
            }
        }
    }

}