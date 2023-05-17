package com.sn.gameelectricity.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.core.widget.doAfterTextChanged
import com.sn.gameelectricity.app.base.BaseActivity1
import com.sn.gameelectricity.databinding.ActivityChangePhoneBinding
import com.sn.gameelectricity.ui.enum.ActivityType
import com.sn.gameelectricity.viewmodel.AccountSecurityViewModel
import me.hgj.jetpackmvvm.util.PublicPracticalMethodFromKT
import me.hgj.jetpackmvvm.util.ScreenTools
import singleClick

/**
 * Date:2022/5/16
 * Time:10:35
 * author:dimple
 */
class ChangePhoneActivity : BaseActivity1<AccountSecurityViewModel, ActivityChangePhoneBinding>() {

    override fun initView(savedInstanceState: Bundle?) {

        mViewBind.apply {

            PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
                clPhone,
                -1,
                "",
                ScreenTools.getInstance().dp2px(this@ChangePhoneActivity, 27f, true).toFloat(),
                ScreenTools.getInstance().dp2px(this@ChangePhoneActivity, 27f, true).toFloat(),
                ScreenTools.getInstance().dp2px(this@ChangePhoneActivity, 27f, true).toFloat(),
                ScreenTools.getInstance().dp2px(this@ChangePhoneActivity, 27f, true).toFloat(),
                null,
                "#F7F9FE"
            )


            ivBack.singleClick {
                finish()
            }

            etPhone.doAfterTextChanged {
                checkNextButton()
            }

            tvNext.singleClick {
                with(Bundle()) {
                    putString("phone", etPhone.text.toString().trim())
                    putSerializable("activitytype", ActivityType.change_phone)
                    PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                        this@ChangePhoneActivity,
                        AuthenticationActivity::class.java, this, false,
                        -1,
                        -1,
                        Intent.FLAG_ACTIVITY_CLEAR_TOP
                    )
                }
            }
        }

        initData()
    }

    private fun initData() {
        checkNextButton()
    }


    /**
     * 检查是否可以点击下一步按钮
     */
    private fun checkNextButton() {
        mViewBind.apply {
            val phone = etPhone.text.toString().trim()
            var next_bg_color = "#66EF874E"
            if (!PublicPracticalMethodFromKT.ppmfKT.isBlank(phone)) {
                if (phone.length == 11) {
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
                ScreenTools.getInstance().dp2px(this@ChangePhoneActivity, 27f, true)
                    .toFloat(),
                ScreenTools.getInstance().dp2px(this@ChangePhoneActivity, 27f, true)
                    .toFloat(),
                ScreenTools.getInstance().dp2px(this@ChangePhoneActivity, 27f, true)
                    .toFloat(),
                ScreenTools.getInstance().dp2px(this@ChangePhoneActivity, 27f, true)
                    .toFloat(),
                null,
                next_bg_color
            )
        }
    }
}