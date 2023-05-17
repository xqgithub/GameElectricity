package com.sn.gameelectricity.ui.adapter

import android.content.Context
import android.graphics.Paint
import android.view.View
import com.sn.gameelectricity.R
import com.sn.gameelectricity.app.util.load
import com.sn.gameelectricity.data.model.bean.GoodsPageResponse
import com.sn.gameelectricity.databinding.ItemHomeHotBinding
import com.sn.gameelectricity.viewmodel.state.HomeViewModel
import me.hgj.jetpackmvvm.base.adapter.BaseFunBindingRecyclerViewAdapter
import me.hgj.jetpackmvvm.util.PublicPracticalMethodFromKT
import me.hgj.jetpackmvvm.util.ScreenTools
import me.jingbin.library.adapter.BaseByViewHolder
import java.text.DecimalFormat

class HomeHotAdapter(
    val mContext: Context, val mViewModel: HomeViewModel
) : BaseFunBindingRecyclerViewAdapter<GoodsPageResponse, ItemHomeHotBinding>() {

    override fun bindView(
        holder: BaseByViewHolder<GoodsPageResponse>,
        binding: ItemHomeHotBinding,
        bean: GoodsPageResponse,
        position: Int
    ) {
        super.bindView(holder, binding, bean, position)
        holder.addOnClickListener(R.id.tvWish)
    }

    override fun bindView(binding: ItemHomeHotBinding, bean: GoodsPageResponse, position: Int) {
        binding.apply {
            PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
                clMain, -1, "",
                ScreenTools.getInstance().dp2px(mContext, 17f, true).toFloat(),
                ScreenTools.getInstance().dp2px(mContext, 17f, true).toFloat(),
                ScreenTools.getInstance().dp2px(mContext, 17f, true).toFloat(),
                ScreenTools.getInstance().dp2px(mContext, 17f, true).toFloat(),
                null, "#FFFFFF"
            )
            tvDefaultPrice.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG

            sivIcon.load(bean.icon)
            if (bean.mark.isNullOrEmpty()) {
                tvMark.visibility = View.GONE
            } else {
                tvMark.text = bean.mark
                tvMark.visibility = View.VISIBLE
            }

            tvGoodsName.text = bean.goodsName
            tvDiscountPrice.text = "¥" + DecimalFormat("#.##").format(bean.discountPrice)
            tvDefaultPrice.text = "原价 ¥" + DecimalFormat("#.##").format(bean.defaultPrice)
            tvGoldCoinPrice.text = "金币抵扣" + DecimalFormat("#.##").format(bean.goldCoinPrice) + "元"
            tvGroupSuccessPrice.text =
                "¥" + DecimalFormat("#.##").format(bean.groupSuccessPrice) + " 半价购"

            if (bean.wishShoppingUserNum > 0) {
                clWishShoppingUserNum.visibility = View.VISIBLE
                tvWishShoppingUserNum.text = "${bean.wishShoppingUserNum}+"
            } else {
                clWishShoppingUserNum.visibility = View.GONE
            }

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