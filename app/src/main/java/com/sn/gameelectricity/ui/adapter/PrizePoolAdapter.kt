package com.sn.gameelectricity.ui.adapter

import android.content.Context
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sn.gameelectricity.R
import com.sn.gameelectricity.data.model.bean.AwardPoolListResponse
import com.sn.gameelectricity.databinding.ItemPrizePoolBinding
import me.hgj.jetpackmvvm.base.adapter.BaseFunBindingRecyclerViewAdapter
import me.hgj.jetpackmvvm.util.PublicPracticalMethodFromKT
import me.hgj.jetpackmvvm.util.ScreenTools
import me.jingbin.library.adapter.BaseByViewHolder
import java.text.DecimalFormat

class PrizePoolAdapter(
    val mContext: Context
) : BaseFunBindingRecyclerViewAdapter<AwardPoolListResponse, ItemPrizePoolBinding>() {

    override fun bindView(
        holder: BaseByViewHolder<AwardPoolListResponse>,
        binding: ItemPrizePoolBinding,
        bean: AwardPoolListResponse,
        position: Int
    ) {
        super.bindView(holder, binding, bean, position)
    }

    override fun bindView(
        binding: ItemPrizePoolBinding,
        bean: AwardPoolListResponse,
        position: Int
    ) {
        binding.apply {
            PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
                clMain, -1, "",
                ScreenTools.getInstance().dp2px(mContext, 17f, true).toFloat(),
                ScreenTools.getInstance().dp2px(mContext, 17f, true).toFloat(),
                ScreenTools.getInstance().dp2px(mContext, 17f, true).toFloat(),
                ScreenTools.getInstance().dp2px(mContext, 17f, true).toFloat(),
                null, "#FFFFFF"
            )

            Glide.with(mContext)
                .load(bean.icon as String?)
                .centerCrop()
                .apply(RequestOptions().apply {
                    placeholder(R.drawable.img_loading)
                    error(R.drawable.iv_pool_empty)
                })
                .into(sivIcon)

            tvGoodsName.text = bean.goodsName

            when (bean.awardType) {
                1 -> {
                    bean.discountPrice?.let {
                        tvDiscountPrice.text =
                            "¥" + DecimalFormat("#.##").format(bean.discountPrice)
                    } ?: let {
                        tvDiscountPrice.text = ""
                    }

                    bean.defaultPrice?.let {
                        tvDefaultPrice.text =
                            "原价 ¥" + DecimalFormat("#.##").format(bean.defaultPrice)
                    } ?: let {
                        tvDefaultPrice.text = ""
                    }

                    tvDefaultPrice.visibility = View.VISIBLE
                }
                2 -> {
                    tvDefaultPrice.visibility = View.GONE
                    if (bean.awardId >= 200000) {
                        bean.discountPrice?.let {
                            tvDiscountPrice.text =
                                DecimalFormat("#.##").format(bean.discountPrice) + "g"
                        } ?: let {
                            tvDiscountPrice.text = "0g"
                        }
                    } else {
                        bean.discountPrice?.let {
                            tvDiscountPrice.text =
                                DecimalFormat("#.##").format(bean.discountPrice) + "金币"
                        } ?: let {
                            tvDiscountPrice.text = "0金币"
                        }
                    }

                }
            }


            when (bean.poolType) {
                1 -> sivImg01.setBackgroundResource(R.drawable.ic_home_prize_level_all)
                2 -> sivImg01.setBackgroundResource(R.drawable.ic_home_prize_level_cs)
                3 -> sivImg01.setBackgroundResource(R.drawable.ic_home_prize_level_ss)
                4 -> sivImg01.setBackgroundResource(R.drawable.ic_home_prize_level_xy)
                5 -> sivImg01.setBackgroundResource(R.drawable.ic_home_prize_level_zg)
            }

        }
    }

}