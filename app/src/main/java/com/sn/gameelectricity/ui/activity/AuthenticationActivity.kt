package com.sn.gameelectricity.ui.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Gravity
import android.view.View
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import com.sn.gameelectricity.R
import com.sn.gameelectricity.app.base.BaseActivity1
import com.sn.gameelectricity.app.network.TokenInterceptorHelper
import com.sn.gameelectricity.app.util.CacheUtil
import com.sn.gameelectricity.app.weight.dialog.SelectionTooltipDialog
import com.sn.gameelectricity.databinding.ActivityAuthenticationBinding
import com.sn.gameelectricity.ui.activity.login.LoginActivity
import com.sn.gameelectricity.ui.enum.ActivityType
import com.sn.gameelectricity.viewmodel.AccountSecurityViewModel
import com.sn.gameelectricity.viewmodel.PersonalSettingViewModel
import com.sn.gameelectricity.viewmodel.request.RequestMainViewModel
import me.hgj.jetpackmvvm.common.ConfigConstants
import me.hgj.jetpackmvvm.event.UniversalEvent
import me.hgj.jetpackmvvm.ext.lifecycle.KtxActivityManger
import me.hgj.jetpackmvvm.util.CodeCountdown
import me.hgj.jetpackmvvm.util.PublicPracticalMethodFromKT
import me.hgj.jetpackmvvm.util.ScreenTools
import me.hgj.jetpackmvvm.util.ToastUtil
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import singleClick

/**
 * Date:2022/5/13
 * Time:13:52
 * author:dimple
 * 身份验证/更换手机号 页面
 */
class AuthenticationActivity :
    BaseActivity1<AccountSecurityViewModel, ActivityAuthenticationBinding>() {

    val phone by lazy {
        intent?.getStringExtra("phone")
    }

    val activityType by lazy {
        intent?.getSerializableExtra("activitytype") as ActivityType
    }

    private val personalSettingViewModel: PersonalSettingViewModel by viewModels()

    private var codeCountDownTimer: CountDownTimer? = null


    override fun initView(savedInstanceState: Bundle?) {

        mViewBind.apply {

            if (activityType == ActivityType.change_phone_authentication ||
                activityType == ActivityType.change_pwd_authentication
            ) {
                tvTitle.text = "身份验证"

                phone?.let {
                    tvContent.text =
                        mViewModel.phoneHighlight("已绑定手机号${mViewModel.getPhoneEncryption2(it)}")
//                    mViewModel.phoneHighlight("已向+86 ${mViewModel.getPhoneEncryption2(it)}发送验证码")
                }
            } else if (activityType == ActivityType.destroy_account) {
                tvTitle.text = "注销账号"
                tvContentTitle.text = "您正在注销账号"
                tvNext.text = "确认注销账号"
                phone?.let {
                    tvContent.text =
                        mViewModel.phoneHighlight("${mViewModel.getPhoneEncryption2(it)}")
                }
            } else {
                tvTitle.text = "更换手机号"

                phone?.let {
                    tvContent.text =
                        mViewModel.phoneHighlight("新手机号${mViewModel.getPhoneEncryption2(it)}")
//                    mViewModel.phoneHighlight("已向+86 ${mViewModel.getPhoneEncryption2(it)}发送验证码")
                }
            }

            PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
                clVerificationCode,
                -1,
                "",
                ScreenTools.getInstance().dp2px(this@AuthenticationActivity, 27f, true).toFloat(),
                ScreenTools.getInstance().dp2px(this@AuthenticationActivity, 27f, true).toFloat(),
                ScreenTools.getInstance().dp2px(this@AuthenticationActivity, 27f, true).toFloat(),
                ScreenTools.getInstance().dp2px(this@AuthenticationActivity, 27f, true).toFloat(),
                null,
                "#F7F9FE"
            )

            ivBack.singleClick {
                finish()
            }

            tvNext.singleClick {
                if (activityType == ActivityType.destroy_account) {
                    mViewModel.cancellationAccount(
                        etVerificationCode.text.toString().trim()
                    ) { isSuccess, msg ->
                        if (isSuccess) {
                            with(SelectionTooltipDialog(this@AuthenticationActivity)) {
                                settitle(View.VISIBLE, "注销成功", "#57493B", 18f)
                                setContent("账号注销成功")
                                setCancel(View.GONE) {}
                                setRewardedIcon(R.drawable.ge_money_placeholder, false)
                                setSureBg(24f, null, "#FDEDE4")
                                setSure(View.VISIBLE, "我知道了", "#EF874E") {
                                    CacheUtil.setIsLogin(false)
                                    TokenInterceptorHelper.getInstance()
                                        .saveAppToken(context, "")
                                    KtxActivityManger.finishAllActivity()
                                    PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                                        this@AuthenticationActivity,
                                        LoginActivity::class.java, null, true
                                    )
                                }
                                show()
                            }
                        } else {
                            ToastUtil.showCenter(msg)
                        }
                    }
                } else {
                    mViewModel.verifySmsCode(
                        etVerificationCode.text.toString().trim(),
                        phone,
                        activityType
                    ) { isSuccess, msg ->
                        if (isSuccess) {
                            when (activityType) {
                                ActivityType.change_phone_authentication -> {
                                    ConfigConstants.VARIABLE.old_verification_code =
                                        etVerificationCode.text.toString().trim()
                                    PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                                        this@AuthenticationActivity,
                                        ChangePhoneActivity::class.java, null, false
                                    )
                                }
                                ActivityType.change_pwd_authentication -> {
                                    with(Bundle()) {
                                        putString(
                                            "smsCode",
                                            etVerificationCode.text.toString().trim()
                                        )
                                        putString("phone", phone)
                                        PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                                            this@AuthenticationActivity,
                                            ChangePwdActivity::class.java, this, false
                                        )
                                    }
                                }
                                else -> {
                                    //请求接口，实现更换手机号
                                    //跳转到账户安全页面
                                    mViewModel.changeMobile(
                                        etVerificationCode.text.toString().trim(),
                                        phone
                                    ) { isSuccess, msg ->
                                        if (isSuccess) {
                                            ToastUtil.showCustomizeToast(
                                                me.hgj.jetpackmvvm.R.drawable.ge_toast_success,
                                                "手机号更换成功", Gravity.CENTER
                                            )
                                            CacheUtil.setIsLogin(false)
                                            TokenInterceptorHelper.getInstance()
                                                .saveAppToken(this@AuthenticationActivity, "")
                                            KtxActivityManger.finishAllActivity()
                                            PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                                                this@AuthenticationActivity,
                                                LoginActivity::class.java, null, true
                                            )
//                                        PublicPracticalMethodFromKT.ppmfKT.intentToJump(
//                                            this@AuthenticationActivity,
//                                            AccountSecurityActivity::class.java, null, false,
//                                            -1, -1,
//                                            Intent.FLAG_ACTIVITY_CLEAR_TOP
//                                        )
                                        } else {
                                            ToastUtil.showCenter(msg)
                                        }
                                    }
                                }
                            }
                        } else {
                            ToastUtil.showCenter(msg)
                        }
                    }
                }
            }

            tvResend.singleClick {
                mViewModel.userSendSmsCode(activityType, phone) { isSuccess, msg ->
                    if (isSuccess) {
                        if (activityType != ActivityType.destroy_account) {
                            tvContent.text =
                                mViewModel.phoneHighlight(
                                    "验证码已发送至${
                                        mViewModel.getPhoneEncryption2(
                                            phone!!
                                        )
                                    }"
                                )
                        }
                        tvContent.visibility = View.VISIBLE
                        if (codeCountDownTimer == null) {
                            codeCountDownTimer = CodeCountdown(60000, 1000)
                        }
                        codeCountDownTimer?.start()
                    } else {
                        ToastUtil.showCenter(msg)
                    }
                }
            }

            etVerificationCode.doAfterTextChanged {
                checkNextButton()
            }
        }
        initData()
    }

    private fun initData() {
        //注册EventBus
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }

//        mViewModel.userSendSmsCode(activityType, phone) { isSuccess, msg ->
//            if (isSuccess) {
//                if (codeCountDownTimer == null) {
//                    codeCountDownTimer = CodeCountdown(60000, 1000)
//                }
//                codeCountDownTimer?.start()
//            } else {
//                ToastUtil.showCenter(msg)
//            }
//        }
        checkNextButton()
    }


    /**
     * 检查是否可以点击下一步按钮
     */
    private fun checkNextButton() {
        mViewBind.apply {
            val code = etVerificationCode.text.toString().trim()
            var next_bg_color = "#66EF874E"
            if (!PublicPracticalMethodFromKT.ppmfKT.isBlank(code)) {
                if (code.length == 6) {
                    tvNext.isEnabled = true
                    next_bg_color = "#EF874E"
                }
            } else {
                tvNext.isEnabled = false
            }

            PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
                tvNext,
                -1,
                "",
                ScreenTools.getInstance().dp2px(this@AuthenticationActivity, 27f, true)
                    .toFloat(),
                ScreenTools.getInstance().dp2px(this@AuthenticationActivity, 27f, true)
                    .toFloat(),
                ScreenTools.getInstance().dp2px(this@AuthenticationActivity, 27f, true)
                    .toFloat(),
                ScreenTools.getInstance().dp2px(this@AuthenticationActivity, 27f, true)
                    .toFloat(),
                null,
                next_bg_color
            )
        }
    }

    //Event通知
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun handleEvent(event: UniversalEvent) {
        if (event.actionType == UniversalEvent.EVENT_VERIFICATION_CODE
        ) {
            if ((event.message as Long) > 0) {
                mViewBind.tvResend.apply {
                    isEnabled = false
                    text = "${event.message}s"
                    setTextColor(Color.parseColor("#CED3DE"))
                }
            } else {
                mViewBind.tvResend.apply {
                    isEnabled = true
                    text = "获取验证码"
                    setTextColor(Color.parseColor("#EF874E"))
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        codeCountDownTimer?.let {
            it.cancel()
        }
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }

}