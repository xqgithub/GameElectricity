package com.sn.gameelectricity.ui.activity

import android.content.Intent
import android.os.Bundle
import com.sn.gameelectricity.app.base.BaseActivity1
import com.sn.gameelectricity.databinding.ActivityShowPhoneBinding
import com.sn.gameelectricity.ui.enum.ActivityType
import com.sn.gameelectricity.viewmodel.AccountSecurityViewModel
import me.hgj.jetpackmvvm.util.PublicPracticalMethodFromKT
import me.hgj.jetpackmvvm.util.ScreenTools
import singleClick

/**
 * Date:2022/5/13
 * Time:11:07
 * author:dimple
 * 用户手机号展示 页面
 */
class ShowPhoneActivity : BaseActivity1<AccountSecurityViewModel, ActivityShowPhoneBinding>() {

    private val phone by lazy {
        intent?.getStringExtra("phone")
    }

    override fun initView(savedInstanceState: Bundle?) {
        mViewBind.apply {

            PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
                tvChangePhone,
                ScreenTools.getInstance().dp2px(this@ShowPhoneActivity, 1f, true),
                "#EF874E",
                ScreenTools.getInstance().dp2px(this@ShowPhoneActivity, 22f, true).toFloat(),
                ScreenTools.getInstance().dp2px(this@ShowPhoneActivity, 22f, true).toFloat(),
                ScreenTools.getInstance().dp2px(this@ShowPhoneActivity, 22f, true).toFloat(),
                ScreenTools.getInstance().dp2px(this@ShowPhoneActivity, 22f, true).toFloat(),
                null,
                "#00000000"
            )


            phone?.let {
                tvPhone.text = mViewModel.getPhoneEncryption2(it)
            }

            ivBack.singleClick {
                finish()
            }

            tvChangePhone.singleClick {
                with(Bundle()) {
                    putString("phone", phone)
                    putSerializable("activitytype", ActivityType.change_phone_authentication)
                    PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                        this@ShowPhoneActivity,
                        AuthenticationActivity::class.java,
                        this,
                        false,
                        -1,
                        -1,
                        Intent.FLAG_ACTIVITY_CLEAR_TOP
                    )
                }
            }
        }
    }
}