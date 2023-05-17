package com.sn.gameelectricity.ui.activity

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import com.sn.gameelectricity.R
import com.sn.gameelectricity.app.base.BaseActivity1
import com.sn.gameelectricity.databinding.ActivityChangePwdBinding
import com.sn.gameelectricity.viewmodel.AccountSecurityViewModel
import me.hgj.jetpackmvvm.util.PublicPracticalMethodFromKT
import me.hgj.jetpackmvvm.util.ScreenTools
import me.hgj.jetpackmvvm.util.ToastUtil
import singleClick

/**
 * Date:2022/5/16
 * Time:15:10
 * author:dimple
 * 修改密码页面
 */
class ChangePwdActivity : BaseActivity1<AccountSecurityViewModel, ActivityChangePwdBinding>() {

    val phone by lazy {
        intent?.getStringExtra("phone")
    }

    val smsCode by lazy {
        intent?.getStringExtra("smsCode")
    }

    //密码状态是否显示
    private var pwdIsVisible = false

    //确认密码状态是否显示
    private var pwdComfirmIsVisible = false


    override fun initView(savedInstanceState: Bundle?) {

        mViewBind.apply {
            PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle2(
                arrayOf(clPhone, clNewPwd, clNewPwdConfirm),
                -1,
                "",
                ScreenTools.getInstance().dp2px(this@ChangePwdActivity, 27f, true).toFloat(),
                ScreenTools.getInstance().dp2px(this@ChangePwdActivity, 27f, true).toFloat(),
                ScreenTools.getInstance().dp2px(this@ChangePwdActivity, 27f, true).toFloat(),
                ScreenTools.getInstance().dp2px(this@ChangePwdActivity, 27f, true).toFloat(),
                null,
                "#F7F9FE"
            )

            phone?.let {
                tvPhone.text = mViewModel.getPhoneEncryption2(it)
            }

            etPwd.doAfterTextChanged {
                checkNextButton()
            }

            etPwdConfirm.doAfterTextChanged {
                checkNextButton()
            }


            ivBack.singleClick {
                finish()
            }

            ivEye.singleClick {
                mViewModel.visiblePwdAndPwdComfirm(
                    mViewBind.etPwd,
                    mViewBind.ivEye,
                    pwdIsVisible
                ) {
                    pwdIsVisible = it
                }
            }
            ivEye2.singleClick {
                mViewModel.visiblePwdAndPwdComfirm(
                    mViewBind.etPwdConfirm,
                    mViewBind.ivEye2,
                    pwdComfirmIsVisible
                ) {
                    pwdComfirmIsVisible = it
                }
            }

            tvNext.singleClick {
                mViewModel.checkPwd(
                    etPwd.text.toString().trim(),
                    etPwdConfirm.text.toString().trim()
                ) {
                    mViewModel.resetPassword(
                        smsCode!!,
                        etPwd.text.toString().trim()
                    ) { isSuccess, msg ->
                        if (isSuccess) {
                            ToastUtil.showCustomizeToast(
                                me.hgj.jetpackmvvm.R.drawable.ge_toast_success,
                                "修改密码成功", Gravity.CENTER, Toast.LENGTH_LONG
                            )
                            PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                                this@ChangePwdActivity,
                                AccountSecurityActivity::class.java, null, false,
                                -1, -1,
                                Intent.FLAG_ACTIVITY_CLEAR_TOP
                            )

                        }
                    }
                }
            }
        }
        initData()
    }

    private fun initData() {
        checkNextButton()

        mViewModel.visiblePwdAndPwdComfirm(
            mViewBind.etPwd,
            mViewBind.ivEye,
            pwdIsVisible
        ) {
            pwdIsVisible = it
        }

        mViewModel.visiblePwdAndPwdComfirm(
            mViewBind.etPwdConfirm,
            mViewBind.ivEye2,
            pwdComfirmIsVisible
        ) {
            pwdComfirmIsVisible = it
        }

    }

    /**
     * 检查是否可以点击下一步按钮
     */
    private fun checkNextButton() {
        mViewBind.apply {
            var next_bg_color = "#66EF874E"
            val newPwd = etPwd.text.toString().trim()
            val newPwdConfirm = etPwdConfirm.text.toString().trim()

            if (newPwd.length > 5 && newPwdConfirm.length > 5 && newPwd == newPwdConfirm) {
                tvNext.isEnabled = true
                next_bg_color = "#EF874E"
            } else {
                tvNext.isEnabled = false
            }

            PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
                tvNext,
                -1,
                "",
                ScreenTools.getInstance().dp2px(this@ChangePwdActivity, 27f, true)
                    .toFloat(),
                ScreenTools.getInstance().dp2px(this@ChangePwdActivity, 27f, true)
                    .toFloat(),
                ScreenTools.getInstance().dp2px(this@ChangePwdActivity, 27f, true)
                    .toFloat(),
                ScreenTools.getInstance().dp2px(this@ChangePwdActivity, 27f, true)
                    .toFloat(),
                null,
                next_bg_color
            )
        }
    }


}