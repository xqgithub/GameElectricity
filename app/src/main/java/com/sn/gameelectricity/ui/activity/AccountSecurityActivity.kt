package com.sn.gameelectricity.ui.activity

import android.os.Bundle
import com.sn.gameelectricity.app.base.BaseActivity1
import com.sn.gameelectricity.app.util.CacheUtil
import com.sn.gameelectricity.databinding.ActivityAccountSecurityBinding
import com.sn.gameelectricity.ui.enum.ActivityType
import com.sn.gameelectricity.viewmodel.AccountSecurityViewModel
import me.hgj.jetpackmvvm.util.PublicPracticalMethodFromKT
import me.hgj.jetpackmvvm.util.ToastUtil
import singleClick

/**
 * Date:2022/5/13
 * Time:10:27
 * author:dimple
 * 账号安全页面
 */
class AccountSecurityActivity :
    BaseActivity1<AccountSecurityViewModel, ActivityAccountSecurityBinding>() {

    private var phone = "18588537243"

    override fun initView(savedInstanceState: Bundle?) {
        mViewBind.apply {

            ivBack.singleClick {
                finish()
            }

            clPhone.singleClick {
                with(Bundle()) {
                    putString("phone", phone)
                    PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                        this@AccountSecurityActivity,
                        ShowPhoneActivity::class.java, this, false
                    )
                }
            }
            clChangePwd.singleClick {
                with(Bundle()) {
                    putString("phone", phone)
                    putSerializable("activitytype", ActivityType.change_pwd_authentication)
                    PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                        this@AccountSecurityActivity,
                        AuthenticationActivity::class.java, this, false
                    )
                }
            }

            clDestroyAccount.singleClick {
                PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                    this@AccountSecurityActivity,
                    DestroyAccountActivity::class.java, null, false
                )
            }
        }
        initData()
    }

    /**
     * 初始化数据
     */
    private fun initData() {
        val user = CacheUtil.getUser()!!
        phone = user.mobile
        mViewBind.tvPhone.text = mViewModel.getPhoneEncryption(phone)
    }
}