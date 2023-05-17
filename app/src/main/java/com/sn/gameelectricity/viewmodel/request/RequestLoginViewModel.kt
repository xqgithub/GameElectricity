package com.sn.gameelectricity.viewmodel.request

import com.blankj.utilcode.util.LogUtils
import com.google.gson.JsonObject
import com.sn.gameelectricity.app.App
import com.sn.gameelectricity.app.network.ApiService
import com.sn.gameelectricity.app.network.HttpFactory
import com.sn.gameelectricity.app.network.cryptography.CryptUtils
import com.sn.gameelectricity.app.util.CommonUtil
import com.sn.gameelectricity.data.model.bean.UserInfo
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.util.toJson
import me.hgj.jetpackmvvm.util.ToastUtil
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class RequestLoginViewModel : BaseViewModel() {


    /**
     * 短信发送
     */
    fun sendSmsCode(phoneNumber: String, smsType: Int, onSuccess: () -> Unit) {
        val jsonBody = JsonObject().apply {
            addProperty("internationalCallingCode", 86)
            addProperty("phoneNumber", phoneNumber)
            addProperty("smsType", smsType)
        }

        val requestBody = jsonBody.toString().toRequestBody("application/json".toMediaTypeOrNull())
        HttpFactory.getInstance().APINew(ApiService.BASE_URL)
            .sendSmsCode(requestBody)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                LogUtils.e("123", it.toJson())
                if (it.code == 0) {
                    onSuccess()
                }
            }, {

            })
    }


    /**
     * 密码登陆注册
     */
    fun userRegisterOrLoginByPassword(
        mobile: String,
        password: String,
        onSuccess: (UserInfo) -> Unit
    ) {
        val jsonBody = JsonObject().apply {
            addProperty("deviceBrand", CommonUtil.getMobileBrand())
            addProperty("deviceNo", CommonUtil.getDeviceId(App.instance.applicationContext))
            addProperty("ip", "")
            addProperty("mobile", mobile)
            addProperty("os", "android")
            addProperty("password", CryptUtils.EncryptBySHA256(password))
            addProperty("region", "")
            addProperty("smsCode", "")
            addProperty("userCode", "")
        }

        val requestBody = jsonBody.toString().toRequestBody("application/json".toMediaTypeOrNull())
        HttpFactory.getInstance().APINew(ApiService.BASE_URL)
            .userRegisterOrLoginByPassword(requestBody)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                LogUtils.e("123", it.toJson())
                if (it.code == 0) {
                    onSuccess(it.data)
                } else {
                    ToastUtil.showCenter("帐号或密码不正确")
                }
            }, {

            })
    }

    /**
     * 短信登陆注册
     */
    fun userRegisterOrLoginBySmsCode(
        mobile: String,
        smsCode: String,
        userCode: String,
        onSuccess: (UserInfo) -> Unit
    ) {
        val jsonBody = JsonObject().apply {
            addProperty("deviceBrand", CommonUtil.getMobileBrand())
            addProperty("deviceNo", CommonUtil.getDeviceId(App.instance.applicationContext))
            addProperty("ip", "")
            addProperty("mobile", mobile)
            addProperty("os", "Android")
            addProperty("password", "")
            addProperty("region", "")
            addProperty("smsCode", smsCode)
            addProperty("userCode", userCode)
        }

        val requestBody = jsonBody.toString().toRequestBody("application/json".toMediaTypeOrNull())
        HttpFactory.getInstance().APINew(ApiService.BASE_URL)
            .userRegisterOrLoginBySmsCode(requestBody)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                LogUtils.e("123", it.toJson())
                if (it.code == 0) {
                    onSuccess(it.data)
                } else if (it.code == 805371961) {
                    ToastUtil.showCenter("该手机号注销冷却中，请稍后再试！")
                } else {
                    ToastUtil.showCenter(it.msg)
                }
            }, {

            })
    }


    /**
     * 重设密码
     */
    fun userRresetPassword(
        mobile: String,
        password: String,
        smsCode: String,
        onSuccess: (String) -> Unit
    ) {
        val jsonBody = JsonObject().apply {
            addProperty("mobile", mobile)
            addProperty("smsCode", smsCode)
            addProperty("smsType", 1)
            addProperty("password", CryptUtils.EncryptBySHA256(password))
        }

        val requestBody = jsonBody.toString().toRequestBody("application/json".toMediaTypeOrNull())
        HttpFactory.getInstance().APINew(ApiService.BASE_URL)
            .userRresetPassword(requestBody)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                LogUtils.e("123", it.toJson())
                if (it.code == 0) {
                    onSuccess(it.msg)
                } else {
                    ToastUtil.showCenter(it.msg)
                }
            }, {

            })
    }

    /**
     * 该手机是否注册
     */
    fun boolUserRegister(mobile: String, onSuccess: (Int) -> Unit) {
        HttpFactory.getInstance().APINew(ApiService.BASE_URL)
            .boolUserRegister(mobile)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                LogUtils.e("123", it.toJson())
                if (it.code == 0) {
                    onSuccess(it.data.boolUserRegister)
                }
            }, {

            })
    }

}