package com.sn.gameelectricity.ui.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import com.sn.gameelectricity.R
import com.sn.gameelectricity.app.util.load
import com.sn.gameelectricity.data.model.bean.GoodsPageResponse
import com.sn.gameelectricity.databinding.ItemHomeEquityBinding
import com.sn.gameelectricity.viewmodel.state.HomeViewModel
import me.hgj.jetpackmvvm.base.adapter.BaseFunBindingRecyclerViewAdapter
import me.hgj.jetpackmvvm.util.PublicPracticalMethodFromKT
import me.hgj.jetpackmvvm.util.ScreenTools
import me.jingbin.library.adapter.BaseByViewHolder
import java.text.DecimalFormat
import java.text.MessageFormat

class HomeEquityAdapter(
    val mContext: Context, val mViewModel: HomeViewModel
) : BaseFunBindingRecyclerViewAdapter<GoodsPageResponse, ItemHomeEquityBinding>() {

    override fun bindView(
        binding: ItemHomeEquityBinding,
        bean: GoodsPageResponse,
        position: Int
    ) {

    }

    override fun bindView(
        holder: BaseByViewHolder<GoodsPageResponse>,
        binding: ItemHomeEquityBinding,
        bean: GoodsPageResponse,
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
            sivImg.load(bean.icon)
            tvGoodsName.text = bean.goodsName
            tvDefaultPrice.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
            tvDiscountPrice.text =
                "¥" + DecimalFormat("#.##").format(bean.discountPrice)
            tvDefaultPrice.text = "原价 ¥" + DecimalFormat("#.##").format(bean.defaultPrice)


//            tvNum.text = MessageFormat.format(
//                "{0}/{1}",
//                bean.usedNum,
//                bean.totalNum
//            )
            tvNum.text = "${bean.usedNum}/${bean.totalNum}"
            progressBar.progress = ((bean.usedNum * 100 / bean.totalNum).toInt())

            when (bean.rightStatus) {
                0 -> {//是否已兑换 0-否 1-是
                    if (bean.usedNum == bean.totalNum) {
                        tvOperate.isEnabled = false
                        tvOperate.text = "已抢完"
                        tvOperate.setTextColor(Color.parseColor("#FFFFFF"))
                        tvOperate.setBackgroundResource(R.drawable.shape_radius_ced3de)
                    } else {
                        tvOperate.isEnabled = true
                        tvOperate.text = "立即兑换"
                        tvOperate.setTextColor(Color.parseColor("#FFFFFF"))
                        tvOperate.setBackgroundResource(R.drawable.shape_radius_f19b3f)
                    }
                }
                1 -> {
//                    if (bean.usedNum == bean.totalNum) {
                    tvOperate.isEnabled = false
                    tvOperate.text = "已抢完"
                    tvOperate.setTextColor(Color.parseColor("#FFFFFF"))
                    tvOperate.setBackgroundResource(R.drawable.shape_radius_ced3de)
//                    } else {
//                        tvOperate.isEnabled = true
//                        tvOperate.text = "去抽奖 >"
//                        tvOperate.setTextColor(Color.parseColor("#FFFFFF"))
//                        tvOperate.setBackgroundResource(R.drawable.shape_radius_f19b3f)
//                    }
                }
            }
            when (bean.couponType) {//权益兑换类型 1-金券 2-银券 3-青铜券
                1 -> {
                    tvCouponNum.text = "黄金奖券 x${bean.couponNum}"
                    sivImgCouponImg.setImageResource(R.drawable.ic_equity_level_golden)
                }
                2 -> {
                    tvCouponNum.text = "白银奖券 x${bean.couponNum}"
                    sivImgCouponImg.setImageResource(R.drawable.ic_equity_level_golden2)
                }
                3 -> {
                    tvCouponNum.text = "青铜奖券 x${bean.couponNum}"
                    sivImgCouponImg.setImageResource(R.drawable.ic_equity_level_golden3)
                }
            }
            holder.addOnClickListener(R.id.tvOperate)

        }
    }

}