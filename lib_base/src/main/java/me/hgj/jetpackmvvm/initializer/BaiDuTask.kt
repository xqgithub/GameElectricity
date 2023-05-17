package me.hgj.jetpackmvvm.initializer

import android.content.Context
import android.os.Looper
import android.os.Message
import android.view.View
import com.baidu.mapapi.SDKInitializer
import me.hgj.jetpackmvvm.util.HandlerUtils
import me.hgj.jetpackmvvm.util.PublicPracticalMethodFromKT

/**
 * Date:2022/5/23
 * Time:10:19
 * author:dimple
 */
class BaiDuTask(val looper: Looper, val context: Context) : FunTask(),
    HandlerUtils.OnReceiveMessageListener {

    private val WHATCODE_INITIALIZER = -1111111

    override fun run() {
        val handler = HandlerUtils.HandlerHolder(looper, context, this)
        PublicPracticalMethodFromKT.ppmfKT.runHandlerFun(handler, WHATCODE_INITIALIZER, 100)
    }

    override fun handlerMessage(msg: Message) {
        when (msg.what) {
            WHATCODE_INITIALIZER -> {
                SDKInitializer.setApiKey("QjtQ7QoSmCrzwID006TAdp9UcTpaibG9")
                SDKInitializer.initialize(context)
            }
        }
    }
}