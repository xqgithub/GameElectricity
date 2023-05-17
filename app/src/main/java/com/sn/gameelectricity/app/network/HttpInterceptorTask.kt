package com.sn.gameelectricity.app.network

import android.content.Context
import com.azhon.appupdate.util.LogUtil
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.LogUtils
import me.hgj.jetpackmvvm.common.ConfigConstants
import me.hgj.jetpackmvvm.initializer.FunTask
import me.hgj.jetpackmvvm.util.JsonUtil
import me.hgj.jetpackmvvm.util.MLogUtils
import okhttp3.logging.HttpLoggingInterceptor

/**
 * Date:2022/5/26
 * Time:14:11
 * author:dimple
 * 接口请求拦截器
 */
class HttpInterceptorTask(val context: Context) : FunTask() {

    override fun run() {
        if (AppUtils.isAppDebug()) {
            var mMessage = StringBuilder()

            //添加Log信息拦截器
            val loggingInterceptor = HttpLoggingInterceptor {
                var message = it
                // 请求或者响应开始
                if (message.startsWith("--> POST")
                    || message.startsWith("--> GET")
                    || message.startsWith("--> PUT")
                ) {
                    mMessage.setLength(0)
                }
                // 以{}或者[]形式的说明是响应结果的json数据，需要进行格式化
                if ((message.startsWith("{") && message.endsWith("}"))
                    || (message.startsWith("[") && message.endsWith("]"))
                ) {
                    message = JsonUtil.formatJson(JsonUtil.decodeUnicode(message))
                }
                mMessage.append(message.plus("\n"))

                // 响应结束，打印整条日志
                if (message.startsWith("<-- END")) {
                    MLogUtils.debugLongInfo(
                        ConfigConstants.CONSTANT.interface_log,
                        mMessage.toString()
                    )
                }
            }
//            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            HttpFactory.addOkhttpInterceptor(loggingInterceptor)
        }
    }
}