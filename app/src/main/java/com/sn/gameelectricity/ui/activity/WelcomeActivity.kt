package com.sn.gameelectricity.ui.activity

import android.Manifest
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.sn.gameelectricity.R
import com.sn.gameelectricity.app.base.BaseActivity1
import com.sn.gameelectricity.app.util.CacheUtil
import com.sn.gameelectricity.app.util.permission.PermissionRequestManager
import com.sn.gameelectricity.app.weight.dialog.PrivacyPolicyDialog
import com.sn.gameelectricity.databinding.ActivityWelcomeBinding
import com.sn.gameelectricity.ui.activity.login.LoginActivity
import com.sn.gameelectricity.ui.activity.web.WebActivity
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

@Suppress("DEPRECATED_IDENTITY_EQUALS")
class WelcomeActivity : BaseActivity1<BaseViewModel, ActivityWelcomeBinding>() {

    override fun initView(savedInstanceState: Bundle?) {

        //防止出现按Home键回到桌面时，再次点击重新进入该界面bug
        if (intent.flags and Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT !== 0) {
            finish()
            return
        }
        if (CacheUtil.isPrivacy()) {
            initData()
        } else {
            showPrivacyDialog()
        }

    }

    private fun initData() {
        PermissionRequestManager.request(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
            .request({ allGranted, grantedList, deniedList ->
                setup()
            }, true)
    }

    private fun setup() {
        if (CacheUtil.isLogin()) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }

    private fun showPrivacyDialog() {
        var message: String = resources?.getString(R.string.PrivacyDialogMessage).toString()
        val spannableString = SpannableString(message)
        val serviceClickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                if (widget is TextView) {
                    widget.highlightColor = Color.TRANSPARENT
                }
                jumpToWeb(
                    "https://itest.aifun.com/#/Useragreement",
                    "用户协议",
                    4
                )
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.isUnderlineText = false
                ds.bgColor = Color.TRANSPARENT
                ds.color = ContextCompat.getColor(this@WelcomeActivity, R.color.tag_orange)
            }
        }
        val privacyClickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                if (widget is TextView) {
                    widget.highlightColor = Color.TRANSPARENT
                }
                jumpToWeb(
                    "https://itest.aifun.com/#/Privacy", "隐私政策", 2
                )
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.isUnderlineText = false
                ds.bgColor = Color.TRANSPARENT
                ds.color = ContextCompat.getColor(this@WelcomeActivity, R.color.tag_orange)
            }
        }
        spannableString.setSpan(
            serviceClickableSpan,
            message.indexOf("《"),
            message.indexOf("》") + 1,
            Spanned.SPAN_INCLUSIVE_EXCLUSIVE
        )
        spannableString.setSpan(
            privacyClickableSpan,
            message.lastIndexOf("《"),
            message.lastIndexOf("》") + 1,
            Spanned.SPAN_INCLUSIVE_EXCLUSIVE
        )
        val dialog = PrivacyPolicyDialog(
            this,
            spannableString,
            {
                CacheUtil.setIsPrivacy(true)
                initData()
            }
        ) { onBackPressed() }
        dialog.show()
    }

    private fun jumpToWeb(url: String, title: String, keyPage: Int) {
        val intent = Intent(this, WebActivity::class.java)
        intent.putExtra("url", url)
        intent.putExtra("title", title)
        startActivity(intent)
    }

}