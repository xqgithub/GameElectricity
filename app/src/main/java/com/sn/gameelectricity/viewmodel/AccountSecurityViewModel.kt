package com.sn.gameelectricity.viewmodel

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.gson.JsonObject
import com.sn.gameelectricity.R
import com.sn.gameelectricity.app.App
import com.sn.gameelectricity.app.network.ApiService
import com.sn.gameelectricity.app.network.HttpFactory
import com.sn.gameelectricity.app.network.cryptography.CryptUtils
import com.sn.gameelectricity.app.util.CacheUtil
import com.sn.gameelectricity.app.util.CommonUtil
import com.sn.gameelectricity.ui.activity.PersonalInfoEditActivity
import com.sn.gameelectricity.ui.enum.ActivityType
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.common.ConfigConstants
import me.hgj.jetpackmvvm.ext.convert
import me.hgj.jetpackmvvm.network.BaseEntity
import me.hgj.jetpackmvvm.util.PublicPracticalMethodFromKT
import me.hgj.jetpackmvvm.util.ToastUtil
import me.hgj.jetpackmvvm.util.delayToDoSometing
import me.hgj.jetpackmvvm.util.observableToMain
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

/**
 * Date:2022/5/13
 * Time:10:34
 * author:dimple
 * 账号安全
 */
class AccountSecurityViewModel : BaseViewModel() {


    /**
     * 设置手机号加密状态
     */
    fun getPhoneEncryption(phone: String): String {
        return phone.replaceRange(3, 7, " **** ")
    }

    fun getPhoneEncryption2(phone: String): String {
        return phone.replaceRange(3, 8, "******")
    }

    /**
     * 设置密码校验密码规则
     */
    fun checkPwd(pwd: String, confirmPwd: String, onCallBack: () -> Unit) {
        var rules_pwd = "^[a-zA-Z0-9]{6,16}+$"
        if (PublicPracticalMethodFromKT.ppmfKT.isBlank(pwd)) {
            ToastUtil.showCenter("密码不能为空！")
            return
        }

//        if (!Pattern.matches(rules_pwd, pwd)) {
//            ToastUtil.showCenter("请输入6-16位英文、数字密码")
//            return
//        }
        if (!CommonUtil.isLetterDigit(pwd)) {
            ToastUtil.showCenter("请输入6-16位英文、数字密码")
            return
        }
        if (pwd != confirmPwd) {
            ToastUtil.showCenter("两次密码输入不一致")
            return
        }
        onCallBack()
    }

    /**
     * 高亮显示
     */
    fun phoneHighlight(content: String): SpannableString {
        val msp = SpannableString(content)
        val startIndex = content.length - 12
        val endIndex = content.length
        val colorSpan = ForegroundColorSpan(Color.parseColor("#EF874E"))
        msp.setSpan(colorSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        return msp
    }


    /**
     * 显示或隐藏密码
     */
    fun visiblePwdAndPwdComfirm(
        tv: View,
        iv: ImageView,
        IsVisible: Boolean = false,
        onCallBack: (IsVisible: Boolean) -> Unit
    ) {
        if (IsVisible) {
            //显示明文
            (tv as TextView).transformationMethod = HideReturnsTransformationMethod.getInstance()
            iv.setImageDrawable(
                ContextCompat.getDrawable(
                    App.instance,
                    R.drawable.ge_login_display
                )
            )
        } else {
            //显示密码
            (tv as TextView).transformationMethod = PasswordTransformationMethod.getInstance()

            iv.setImageDrawable(ContextCompat.getDrawable(App.instance, R.drawable.ge_login_hide))
        }
        onCallBack(!IsVisible)
    }


    /**
     * 发送验证码
     */
    fun userSendSmsCode(
        type: ActivityType,
        phoneNumber: String?,
        onCallBack: (isSuccess: Boolean, msg: String) -> Unit
    ) {
        lateinit var observable: Observable<BaseEntity<Any>>

        val requestBody = JsonObject().let {
            when (type) {
                ActivityType.change_pwd_authentication -> {
                    it.addProperty("smsType", 1)
                }
                ActivityType.change_phone_authentication -> {
                    it.addProperty("smsType", 3)
                }
                ActivityType.destroy_account -> {
                    it.addProperty("smsType", 4)
                }
                ActivityType.change_phone -> {
                    it.addProperty("smsType", 2)
                    it.addProperty("phoneNumber", phoneNumber)
                }
            }
            it.toString().toRequestBody("application/json".toMediaTypeOrNull())
        }


        if (type == ActivityType.change_pwd_authentication ||
            type == ActivityType.change_phone_authentication ||
            type == ActivityType.destroy_account
        ) {
            observable =
                HttpFactory.getInstance().APINew(ApiService.BASE_URL).userSendSmsCode(requestBody)
        } else if (type == ActivityType.change_phone) {
            observable =
                HttpFactory.getInstance().APINew(ApiService.BASE_URL).userSendSmsCode2(requestBody)
        }
        observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.code == 0) {
                    onCallBack(true, "")
                } else {
                    onCallBack(false, it.msg)
                }
            }, {
                onCallBack(false, "验证码发送失败!")
            })
    }


    /**
     * 校验验证码
     */
    fun verifySmsCode(
        smsCode: String,
        mobile: String?,
        type: ActivityType,
        onCallBack: (isSuccess: Boolean, msg: String) -> Unit
    ) {
        val requestBody = JsonObject().let {
            it.addProperty("smsCode", smsCode)
            when (type) {
                ActivityType.change_pwd_authentication -> {
                    it.addProperty("smsType", 1)
                }
                ActivityType.change_phone_authentication -> {
                    it.addProperty("smsType", 3)
                }
                ActivityType.change_phone -> {
                    it.addProperty("smsType", 2)
                    it.addProperty("mobile", mobile)
                }
            }
            it.toString().toRequestBody("application/json".toMediaTypeOrNull())
        }

        HttpFactory.getInstance().APINew(ApiService.BASE_URL).verifySmsCode(requestBody)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.code == 0) {
                    onCallBack(true, "")
                } else {
                    onCallBack(false, it.msg)
                }
            }, {
                onCallBack(false, "验证码校验失败!")
            })
    }

    /**
     * 重设密码
     */
    fun resetPassword(
        smsCode: String,
        pwd: String,
        onCallBack: (isSuccess: Boolean, msg: String) -> Unit
    ) {

        val requestBody = JsonObject().let {
            it.addProperty("smsCode", smsCode)
            it.addProperty("password", CryptUtils.EncryptBySHA256(pwd))
            it.addProperty("mobile", CacheUtil.getUser()?.mobile)
            it.addProperty("smsType", 1)
            it.toString().toRequestBody("application/json".toMediaTypeOrNull())
        }
        HttpFactory.getInstance().APINew(ApiService.BASE_URL).resetPassword(requestBody)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.code == 0) {
                    onCallBack(true, "")
                } else {
                    onCallBack(false, it.msg)
                }
            }, {
                onCallBack(false, "验证码发送失败!")
            })
    }

    /**
     * 修改手机号码
     */
    fun changeMobile(
        smsCode: String,
        mobile: String?,
        onCallBack: (isSuccess: Boolean, msg: String) -> Unit
    ) {
        val requestBody = JsonObject().let {
            it.addProperty("newMobile", mobile)
            it.addProperty("newMobileSmsCode", smsCode)
            it.addProperty("oldMobile", CacheUtil.getUser()?.mobile)
            it.addProperty("oldMobileSmsCode", ConfigConstants.VARIABLE.old_verification_code)
            it.addProperty("smsType", 2)
            it.addProperty("userId", CacheUtil.getUser()?.userId)
            it.toString().toRequestBody("application/json".toMediaTypeOrNull())
        }
        HttpFactory.getInstance().APINew(ApiService.BASE_URL).changeMobile(requestBody)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.code == 0) {
                    onCallBack(true, "")
                } else {
                    onCallBack(false, it.msg)
                }
            }, {
                onCallBack(false, "修改手机号码失败!")
            })
    }

    /**
     * 账号注销
     */
    fun cancellationAccount(
        smsCode: String,
        onCallBack: (isSuccess: Boolean, msg: String) -> Unit
    ) {
        showLoading()
        delayToDoSometing(500) {
            val requestBody = JsonObject().let {
                it.addProperty("smsCode", smsCode)
                it.addProperty("smsType", 4)
                it.addProperty("userId", CacheUtil.getUser()?.userId)
                it.toString().toRequestBody("application/json".toMediaTypeOrNull())
            }
            HttpFactory.getInstance().APINew(ApiService.BASE_URL).cancellationAccount(requestBody)
                .compose(observableToMain())
                .subscribe({
                    dismissLoading()
                    if (it.code == 0) {
                        onCallBack(true, it.msg)
                    } else {
                        onCallBack(false, it.msg)
                    }
                }, {
                    dismissLoading()
                    onCallBack(false, "账号注销失败!")
                })
        }
    }
}