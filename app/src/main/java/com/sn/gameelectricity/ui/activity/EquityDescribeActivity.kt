package com.sn.gameelectricity.ui.activity

import android.graphics.Color
import android.os.Bundle
import com.gyf.immersionbar.ImmersionBar
import com.sn.gameelectricity.app.base.BaseActivity1
import com.sn.gameelectricity.databinding.ActivityEquitylDescribeBinding
import com.sn.gameelectricity.viewmodel.state.HomeViewModel
import singleClick


/**
 * 权益说明 Activity
 */
class EquityDescribeActivity : BaseActivity1<HomeViewModel, ActivityEquitylDescribeBinding>() {

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this)
            .transparentStatusBar()
            .statusBarDarkFont(true)
            .navigationBarColorInt(Color.WHITE)
            .navigationBarDarkIcon(true)
            .init()

        mViewBind.apply {

            ivBack.singleClick {
                finish()
            }


        }
    }


}