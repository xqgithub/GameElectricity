package com.sn.gameelectricity.ui.adapter

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.View
import com.blankj.utilcode.util.LogUtils
import com.sn.gameelectricity.R
import com.sn.gameelectricity.app.util.load
import com.sn.gameelectricity.data.model.bean.PayloadUserFriendsVO
import com.sn.gameelectricity.data.model.bean.friendBean
import com.sn.gameelectricity.databinding.AdapterGameFriendBinding
import com.sn.gameelectricity.ui.activity.PersonalInfoEditActivity
import com.sn.gameelectricity.viewmodel.state.MoneyViewModel
import me.hgj.jetpackmvvm.base.adapter.BaseFunBindingRecyclerViewAdapter
import me.hgj.jetpackmvvm.common.ConfigConstants
import me.hgj.jetpackmvvm.util.PublicPracticalMethodFromKT
import me.hgj.jetpackmvvm.util.ScreenTools
import me.hgj.jetpackmvvm.util.dataNullConvertToInt
import me.jingbin.library.adapter.BaseByViewHolder

/**
 * Date:2022/5/19
 * Time:15:55
 * author:dimple
 * 游戏好友和仇人列表 适配器
 */
class GameFriendAdapter(
    val mContext: Context,
    val moneyViewModel: MoneyViewModel
) : BaseFunBindingRecyclerViewAdapter<PayloadUserFriendsVO, AdapterGameFriendBinding>() {

    override fun bindView(
        holder: BaseByViewHolder<PayloadUserFriendsVO>,
        binding: AdapterGameFriendBinding,
        bean: PayloadUserFriendsVO,
        position: Int
    ) {
//        super.bindView(holder, binding, bean, position)

        binding.apply {
            PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
                clMain, -1, "",
                ScreenTools.getInstance().dp2px(mContext, 8f, true).toFloat(),
                ScreenTools.getInstance().dp2px(mContext, 8f, true).toFloat(),
                ScreenTools.getInstance().dp2px(mContext, 8f, true).toFloat(),
                ScreenTools.getInstance().dp2px(mContext, 8f, true).toFloat(),
                null, "#FFFFFF"
            )
            sivAvatar.load(
                bean.avatarUrl,
                R.drawable.default_user_avatar,
                R.drawable.default_user_avatar
            )
            tvName.text = bean.nickName
            tvContent.text = "累计产生贡献值${bean.contribute}"

            if (!PublicPracticalMethodFromKT.ppmfKT.isBlank(bean.boolReceiveReward) &&
                dataNullConvertToInt(bean.boolReceiveReward) as Int == -1
            ) {
                ivReceive.visibility = View.VISIBLE
                holder.addOnClickListener(R.id.iv_receive)
            } else {
                ivReceive.visibility = View.GONE
            }
        }
    }


    override fun bindView(
        binding: AdapterGameFriendBinding,
        bean: PayloadUserFriendsVO,
        position: Int
    ) {
    }


}