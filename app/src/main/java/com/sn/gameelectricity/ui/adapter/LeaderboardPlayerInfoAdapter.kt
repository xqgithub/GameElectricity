package com.sn.gameelectricity.ui.adapter

import android.content.Context
import android.graphics.drawable.GradientDrawable
import com.sn.gameelectricity.R
import com.sn.gameelectricity.app.App
import com.sn.gameelectricity.app.util.load
import com.sn.gameelectricity.data.model.bean.GoodsVO
import com.sn.gameelectricity.databinding.AdapterLeaderboardPlayerInfoBinding
import me.hgj.jetpackmvvm.base.adapter.BaseFunBindingRecyclerViewAdapter
import me.hgj.jetpackmvvm.util.PublicPracticalMethodFromKT
import me.hgj.jetpackmvvm.util.ScreenTools

/**
 * Date:2022/7/8
 * Time:16:11
 * author:dimple
 * 玩家排行榜信息
 */
class LeaderboardPlayerInfoAdapter(val mContext: Context) :
    BaseFunBindingRecyclerViewAdapter<GoodsVO, AdapterLeaderboardPlayerInfoBinding>() {
    override fun bindView(
        binding: AdapterLeaderboardPlayerInfoBinding,
        bean: GoodsVO,
        position: Int
    ) {
        binding.apply {
            ivCommodityIcon.load(bean.goodsIcon, R.drawable.img_empty, R.drawable.img_empty)
            tvNums.apply {
                text = "x${bean.number}"
                PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
                    this,
                    -1,
                    "",
                    ScreenTools.getInstance().dp2px(App.instance, 3f, true).toFloat(),
                    ScreenTools.getInstance().dp2px(App.instance, 3f, true).toFloat(),
                    ScreenTools.getInstance().dp2px(App.instance, 3f, true).toFloat(),
                    ScreenTools.getInstance().dp2px(App.instance, 3f, true).toFloat(),
                    GradientDrawable.Orientation.LEFT_RIGHT,
                    "#6C72FD", "#C973FE"
                )
            }
        }
    }
}