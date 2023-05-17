package com.sn.gameelectricity.ui.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.view.View
import com.sn.gameelectricity.R
import com.sn.gameelectricity.app.util.load
import com.sn.gameelectricity.data.model.bean.GoodsEveryResponse
import com.sn.gameelectricity.databinding.ItemHomeDailyrobBinding
import com.sn.gameelectricity.viewmodel.state.HomeViewModel
import me.hgj.jetpackmvvm.base.adapter.BaseFunBindingRecyclerViewAdapter
import me.hgj.jetpackmvvm.util.PublicPracticalMethodFromKT
import me.hgj.jetpackmvvm.util.ScreenTools
import me.jingbin.library.adapter.BaseByViewHolder
import java.text.DecimalFormat
import java.text.MessageFormat

class HomeDailyRobAdapter(
    val mContext: Context, val mViewModel: HomeViewModel
) : BaseFunBindingRecyclerViewAdapter<GoodsEveryResponse, ItemHomeDailyrobBinding>() {

    override fun bindView(
        binding: ItemHomeDailyrobBinding,
        bean: GoodsEveryResponse,
        position: Int
    ) {

    }

    override fun bindView(
        holder: BaseByViewHolder<GoodsEveryResponse>,
        binding: ItemHomeDailyrobBinding,
        bean: GoodsEveryResponse,
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

            tvGoodsName.text = bean.goodsName
            tvDefaultPrice.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
            tvDiscountPrice.text =
                "¥" + DecimalFormat("#.##").format(bean.discountPrice)
            tvDefaultPrice.text = "原价 ¥" + DecimalFormat("#.##").format(bean.defaultPrice)
            tvNum.text = MessageFormat.format(
                "{0}/{1}",
                bean.usedNum,
                bean.totalNum
            )

            progressBar.progress = (bean.usedNum * 100 / bean.totalNum)

            when (bean.rewardType) {
                1 -> {//活动状态: 1-未参与 2-未获得 3-已获得
                    if (bean.type == 2) {//类型: 1-今日必抢 2-明日预告
                        tvType.visibility = View.VISIBLE
                        tvOperate.isEnabled = false
                        tvOperate.text = "冲冲冲！"
                        tvOperate.setTextColor(Color.parseColor("#FFFFFF"))
                        tvOperate.setBackgroundResource(R.drawable.shape_radius_f9d5b4)
                    } else {
                        sivImg.load(bean.icon)
                        if (bean.usedNum == bean.totalNum) {
                            tvOperate.isEnabled = false
                            tvOperate.text = "已抢完"
                            tvOperate.setTextColor(Color.parseColor("#FFFFFF"))
                            tvOperate.setBackgroundResource(R.drawable.shape_radius_ced3de)
                        } else {
                            tvOperate.isEnabled = true
                            tvOperate.text = "冲冲冲！"
                            tvOperate.setTextColor(Color.parseColor("#FFFFFF"))
                            tvOperate.setBackgroundResource(R.drawable.shape_radius_f19b3f)
                        }
                        tvType.visibility = View.GONE
                    }
                }
                2 -> {//活动状态: 1-未参与 2-未获得 3-已获得
                    if (bean.type == 2) {//类型: 1-今日必抢 2-明日预告
                        tvType.visibility = View.VISIBLE
                        tvOperate.isEnabled = false
                        tvOperate.text = "去抽奖 >"
                        tvOperate.setTextColor(Color.parseColor("#FFFFFF"))
                        tvOperate.setBackgroundResource(R.drawable.shape_radius_f9d5b4)
                    } else {
                        sivImg.load(bean.icon)
                        if (bean.usedNum == bean.totalNum) {
                            tvOperate.isEnabled = false
                            tvOperate.text = "已抢完"
                            tvOperate.setTextColor(Color.parseColor("#FFFFFF"))
                            tvOperate.setBackgroundResource(R.drawable.shape_radius_ced3de)
                        } else {
                            tvOperate.isEnabled = true
                            tvOperate.text = "去抽奖 >"
                            tvOperate.setTextColor(Color.parseColor("#FFFFFF"))
                            tvOperate.setBackgroundResource(R.drawable.shape_radius_f19b3f)
                        }
                        tvType.visibility = View.GONE
                    }
                }
                3 -> {//1-未参与 2-未获得 3-已获得
                    sivImg.load(bean.icon)
                    tvOperate.text = "已获得"
                    tvOperate.isEnabled = false
                    tvOperate.setTextColor(Color.parseColor("#EF874E"))
                    tvOperate.setBackgroundResource(R.drawable.shape_radius_f9d5b4)
                }
            }
            holder.addOnClickListener(R.id.tvOperate)

        }
    }

}