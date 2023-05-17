package com.sn.gameelectricity.app.network

import android.content.Intent
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.Gson
import com.sn.gameelectricity.ui.activity.login.LoginActivity
import me.hgj.jetpackmvvm.base.appContext
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import java.io.IOException
import java.lang.Exception

/**
 * 描述　: token过期拦截器
 */
class TokenOutInterceptor : Interceptor {

    val gson: Gson by lazy { Gson() }

    @kotlin.jvm.Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var response = chain.proceed(chain.request())
        try {
            if (response.body != null && response.body!!.contentType() != null) {
                val mediaType = response.body!!.contentType()
                val string = response.body!!.string()
                val responseBody = ResponseBody.create(mediaType, string)
                //判断逻辑 模拟一下
                val apiResponse = gson.fromJson(string, BaseEntity::class.java)
                if (apiResponse.code == 9 || apiResponse.code == 8) {
                    LogUtils.e("123")
                    ToastUtils.showShort(apiResponse.msg)
                    appContext.startActivity(Intent(appContext, LoginActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    })
                }
                response = response.newBuilder().body(responseBody).build()
            } else {
                response
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }
}