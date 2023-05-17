package com.sn.gameelectricity.ui.adapter

import android.content.Context
import android.view.View
import com.blankj.utilcode.util.LogUtils
import com.sn.gameelectricity.R
import com.sn.gameelectricity.app.util.load
import com.sn.gameelectricity.data.model.bean.GoodsAllResponse
import com.sn.gameelectricity.databinding.ItemAddWishBinding
import me.hgj.jetpackmvvm.base.adapter.BaseFunBindingRecyclerViewAdapter
import me.jingbin.library.adapter.BaseByViewHolder
import java.text.DecimalFormat

class AddWishAdapter(
    val mContext: Context
) : BaseFunBindingRecyclerViewAdapter<GoodsAllResponse, ItemAddWishBinding>() {

    override fun bindView(
        holder: BaseByViewHolder<GoodsAllResponse>,
        binding: ItemAddWishBinding,
        bean: GoodsAllResponse,
        position: Int
    ) {
        super.bindView(holder, binding, bean, position)
        holder.addOnClickListener(R.id.tvWish)
    }

    override fun bindView(binding: ItemAddWishBinding, bean: GoodsAllResponse, position: Int) {
        binding.apply {

            sivIcon.load(bean.icon)
            tvGoodsName.text = bean.goodsName
            tvDefaultPrice.text = "¥" + DecimalFormat("#.##").format(bean.discountPrice)

            when (bean.desireType) {
                1 -> {//是否已加入心愿购 1-未加入 2-已加入
                    tvWish.visibility = View.VISIBLE
                    tvWished.visibility = View.GONE
                }
                2 -> {
                    tvWish.visibility = View.GONE
                    tvWished.visibility = View.VISIBLE
                }
            }
        }
    }

}