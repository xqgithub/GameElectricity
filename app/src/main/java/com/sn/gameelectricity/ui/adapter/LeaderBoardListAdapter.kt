package com.sn.gameelectricity.ui.adapter

import android.content.Context
import android.graphics.drawable.GradientDrawable
import com.sn.gameelectricity.R
import com.sn.gameelectricity.app.App
import com.sn.gameelectricity.data.model.bean.LeaderBoardBean
import com.sn.gameelectricity.databinding.AdapterLeaderboardListBinding
import me.hgj.jetpackmvvm.base.adapter.BaseFunBindingRecyclerViewAdapter
import me.hgj.jetpackmvvm.util.PublicPracticalMethodFromKT
import me.hgj.jetpackmvvm.util.ScreenTools
import me.hgj.jetpackmvvm.util.load
import me.jingbin.library.adapter.BaseByViewHolder

/**
 * Date:2022/7/7
 * Time:14:42
 * author:dimple
 * 排行榜列表数据
 */
class LeaderBoardListAdapter(val mContext: Context) :
    BaseFunBindingRecyclerViewAdapter<LeaderBoardBean, AdapterLeaderboardListBinding>() {


    override fun bindView(
        holder: BaseByViewHolder<LeaderBoardBean>,
        binding: AdapterLeaderboardListBinding,
        bean: LeaderBoardBean,
        position: Int
    ) {
        binding.apply {
            var rankNumber = position + 4

            var clItemBgColor = if (rankNumber % 2 == 0) {//偶数
                "#FFFFFF"
            } else {//奇数
                "#FBF6FF"
            }
            PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
                clMain,
                -1,
                "",
                0f,
                0f,
                0f,
                0f,
                null,
                clItemBgColor
            )
            tvRankNumber.text = "${rankNumber}"
            sivAvatar.load(
                bean.avatar,
                R.drawable.default_user_avatar,
                R.drawable.default_user_avatar
            )
            tvName.text = "${bean.nickName}"
            tvInviteesNum.text = "${bean.num}"
        }
    }

    override fun bindView(
        binding: AdapterLeaderboardListBinding,
        bean: LeaderBoardBean,
        position: Int
    ) {
    }
}