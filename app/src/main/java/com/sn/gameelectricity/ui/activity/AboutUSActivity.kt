package com.sn.gameelectricity.ui.activity

import android.app.Activity
import android.os.Bundle
import com.blankj.utilcode.util.AppUtils
import com.sn.gameelectricity.app.base.BaseActivity1
import com.sn.gameelectricity.databinding.ActivityAboutUsBinding
import com.sn.gameelectricity.viewmodel.PersonalSettingViewModel
import singleClick

/**
 * Date:2022/5/16
 * Time:19:13
 * author:dimple
 * 关于我们 页面
 */
class AboutUSActivity : BaseActivity1<PersonalSettingViewModel, ActivityAboutUsBinding>() {

    override fun initView(savedInstanceState: Bundle?) {
        mViewBind.apply {

            atvIntroduce.text =
                "基于大数据挖掘及算法推荐技术的个性化游戏资讯发现引擎，我们以\"个性化推荐技术+游戏兴趣社交\"的模式切入，为每位玩家推荐有价值的、个性化的信息，提供连接人与信息的新型服务，进而打造基于兴趣推荐的个性化游戏资讯分享社交平台。"

            tvVersionName.text = "V${AppUtils.getAppVersionName()}"

            ivBack.singleClick {
                finish()
            }
        }

    }
}