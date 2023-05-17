package com.sn.gameelectricity.viewmodel.state

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.LogUtils
import com.google.gson.JsonObject
import com.sn.gameelectricity.app.network.ApiService
import com.sn.gameelectricity.app.network.HttpFactory
import com.sn.gameelectricity.app.util.AESCrypt
import com.sn.gameelectricity.app.util.CacheUtil
import com.sn.gameelectricity.data.model.bean.NewbiePoliteBean
import com.sn.gameelectricity.data.model.bean.NewbieRedeemGiftsBean
import com.sn.gameelectricity.data.model.bean.NewerGuideBean
import io.reactivex.rxjava3.core.Observable
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.common.ConfigConstants
import me.hgj.jetpackmvvm.ext.convert
import me.hgj.jetpackmvvm.network.NettServerException
import me.hgj.jetpackmvvm.util.ToastUtil
import me.hgj.jetpackmvvm.util.dataNullConvert
import me.hgj.jetpackmvvm.util.isNotNull
import me.hgj.jetpackmvvm.util.observableToMain
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit

class MainViewModel : BaseViewModel() {

    val newcomerCeremonyLive = MutableLiveData<NewbiePoliteBean>()

    /**
     * 获取启用的新人有礼活动
     */
    fun newcomerCeremony() {
        HttpFactory.getInstance().APINew(ApiService.BASE_URL).newcomerCeremony()
            .convert().funSubscribe(false, {
                if (it is NettServerException) {
                    ToastUtil.showCenter("${it.errorMessage}")
                }
                LogUtils.iTag(ConfigConstants.CONSTANT.TAG_ALL, "${it.message}")
            }, {
                newcomerCeremonyLive.postValue(it)
            })
    }

    /**
     * 做新手引导
     */
    fun doUserGuide(
        guideStageId: Int,
        onCallBack: (isSuccess: Boolean, newerGuideBean: NewerGuideBean?, msg: String) -> Unit
    ) {
//        HttpFactory.getInstance().APINew(ApiService.BASE_TEST).doUserGuide(guideStageId)
//            .convert().funSubscribe(false, {
//                if (it is NettServerException) {
//                    ToastUtil.showCenter("${it.errorMessage}")
//                }
//                LogUtils.iTag(ConfigConstants.CONSTANT.TAG_ALL, "${it.message}")
//            }, {
//                onCallBack(it)
//            })

        HttpFactory.getInstance().APINew(ApiService.BASE_URL)
            .doUserGuide(guideStageId)
            .compose(observableToMain())
            .subscribe({
                if (it.code == 0) {
                    onCallBack(true, it.data, it.msg)
                } else {
                    onCallBack(false, null, it.msg)
                }
            }, {
                onCallBack(false, null, it.message.toString())
            })
    }

    /**
     * 通过新手引导步骤编号获取明细
     */
    fun userGuideDetails(guideStageId: Int, onCallBack: (newerGuideBean: NewerGuideBean) -> Unit) {
        HttpFactory.getInstance().APINew(ApiService.BASE_URL).userGuideDetails(guideStageId)
            .convert().funSubscribe(false, {
                if (it is NettServerException) {
                    ToastUtil.showCenter("${it.errorMessage}")
                }
                LogUtils.iTag(ConfigConstants.CONSTANT.TAG_ALL, "${it.message}")
            }, {
                onCallBack(it)
            })
    }

    /**
     * 新手引导兑换
     */
    fun exchange(onCallBack: (bean: NewbieRedeemGiftsBean) -> Unit) {
        HttpFactory.getInstance().APINew(ApiService.BASE_URL).exchange()
            .convert().funSubscribe(false, {
                if (it is NettServerException) {
                    ToastUtil.showCenter("${it.errorMessage}")
                }
                LogUtils.iTag(ConfigConstants.CONSTANT.TAG_ALL, "${it.message}")
            }, {
                onCallBack(it)
            })
    }


    /**
     * 用户心跳请求
     */
    fun heart() {
        CacheUtil.getUser().isNotNull({
            val current_time = System.currentTimeMillis()
            val requestBody = JsonObject().let {
                it.addProperty("heartTime", current_time)
                it.addProperty(
                    "sign", signFromAESMode(
                        "${CacheUtil.getUser()?.userId}:${CacheUtil.getUser()?.token}:${current_time}",
                        dataNullConvert(CacheUtil.getUser()?.key),
                        dataNullConvert(CacheUtil.getUser()?.iv)
                    )
                )
                it.addProperty("userId", CacheUtil.getUser()!!.userId)
                it.toString().toRequestBody("application/json".toMediaTypeOrNull())
            }

            val disposable = Observable.interval(0L, 10 * 60, TimeUnit.SECONDS)
                .doOnDispose {
//                ToastUtil.showCenter("心跳已经停止")
                    LogUtils.iTag(ConfigConstants.CONSTANT.TAG_ALL, "心跳已经停止")
                }
                .compose(observableToMain())
                .subscribe {
                    LogUtils.iTag(ConfigConstants.CONSTANT.TAG_ALL, "心跳 =-= $it")
                    HttpFactory.getInstance().APINew(ApiService.BASE_URL).heart(requestBody)
                        .compose(observableToMain())
                        .subscribe({
                            if (it.code == 0) {
                            } else {
                                LogUtils.iTag(ConfigConstants.CONSTANT.TAG_ALL, it.msg)
                            }
                        }, {
                            LogUtils.iTag(ConfigConstants.CONSTANT.TAG_ALL, it.message)
                        })
                }
            saveDisposable(disposable)
        }, {

        })
    }

    var signFromAESMode = { params: String, key: String, iv: String ->
        val encrypt_result =
            AESCrypt.encrypt(CacheUtil.getUser()?.key, CacheUtil.getUser()?.iv, params)
        val decrypt_result =
            AESCrypt.decrypt(CacheUtil.getUser()?.key, CacheUtil.getUser()?.iv, encrypt_result)
        encrypt_result
    }


    /**
     * 高亮显示
     */
    fun countDownHighlight(content: String, num: String): SpannableString {
        val msp = SpannableString(content)
        val startIndex = content.indexOf("s") - num.length
        val endIndex = content.indexOf("s") + 1
        val colorSpan = ForegroundColorSpan(Color.parseColor("#EF874E"))
        msp.setSpan(colorSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        return msp
    }


    override fun onCleared() {
        super.onCleared()
    }


}