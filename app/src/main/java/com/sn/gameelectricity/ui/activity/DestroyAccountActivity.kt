package com.sn.gameelectricity.ui.activity

import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import com.sn.gameelectricity.R
import com.sn.gameelectricity.app.base.BaseActivity1
import com.sn.gameelectricity.app.util.CacheUtil
import com.sn.gameelectricity.databinding.ActivityDestroyaccountBinding
import com.sn.gameelectricity.ui.enum.ActivityType
import com.sn.gameelectricity.viewmodel.AccountSecurityViewModel
import me.hgj.jetpackmvvm.util.PublicPracticalMethodFromKT
import me.hgj.jetpackmvvm.util.ScreenTools
import singleClick

/**
 * Date:2022/7/5
 * Time:16:54
 * author:dimple
 * 注销账号
 */
class DestroyAccountActivity :
    BaseActivity1<AccountSecurityViewModel, ActivityDestroyaccountBinding>() {

    override fun initView(savedInstanceState: Bundle?) {

        mViewBind.apply {
            PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
                tvAgree,
                -1,
                "",
                0f,
                0f,
                0f,
                0f,
                GradientDrawable.Orientation.LEFT_RIGHT,
                "#F19B3F", "#ED7A57"
            )
        }
        initData()
        initListener()
    }

    private fun initData() {
        mViewBind.atvAgreement.text =
            this@DestroyAccountActivity.getString(R.string.destroyaccount_protocol)
    }

    private fun initListener() {
        mViewBind.apply {
            ivBack.singleClick {
                finish()
            }
            tvDisagree.singleClick {
                finish()
            }
            tvAgree.singleClick {
                with(Bundle()) {
                    putString("phone", CacheUtil.getUser()?.mobile)
                    putSerializable("activitytype", ActivityType.destroy_account)
                    PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                        this@DestroyAccountActivity,
                        AuthenticationActivity::class.java, this, false,
                        -1,
                        -1,
                        Intent.FLAG_ACTIVITY_CLEAR_TOP
                    )
                }
            }
        }
    }


}