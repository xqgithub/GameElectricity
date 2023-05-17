package com.sn.gameelectricity.ui.adapter

import android.content.Context
import com.sn.gameelectricity.R
import com.sn.gameelectricity.app.App
import com.sn.gameelectricity.data.model.bean.PayloadGroupAssistMemberVO
import com.sn.gameelectricity.databinding.AdapterOrderDetailsHelperBinding
import com.sn.gameelectricity.databinding.AdapterOrderListBinding
import me.hgj.jetpackmvvm.base.adapter.BaseFunBindingRecyclerViewAdapter
import me.hgj.jetpackmvvm.util.ScreenTools

/**
 * Date:2022/6/21
 * Time:17:30
 * author:dimple
 * 订单列表详情 助力好友 头像适配器
 */
class OrderDetailsHelperAdapter(val mContext: Context) :
    BaseFunBindingRecyclerViewAdapter<PayloadGroupAssistMemberVO, AdapterOrderDetailsHelperBinding>() {

    override fun bindView(
        binding: AdapterOrderDetailsHelperBinding,
        bean: PayloadGroupAssistMemberVO,
        position: Int
    ) {
        binding.apply {
            ituiHelpFriend.apply {
                setAvatarDataFromUrl2(
                    bean.avatarUrl,
                    R.drawable.ge_help_friend_add,
                    ScreenTools.getInstance().dp2px(App.instance, 44f, true),
                    ScreenTools.getInstance().dp2px(App.instance, 44f, true)
                )
                setTextData2(if (bean.nickName == "") "邀请好友" else bean.nickName, 12f, "#061925")
            }
        }
    }
}