package com.sn.gameelectricity.viewmodel

import android.app.Activity
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.LogUtils
import com.google.gson.JsonObject
import com.gyf.immersionbar.ImmersionBar
import com.sn.gameelectricity.R
import com.sn.gameelectricity.app.appViewModel
import com.sn.gameelectricity.app.network.ApiService
import com.sn.gameelectricity.app.network.HttpFactory
import com.sn.gameelectricity.app.util.CacheUtil
import com.sn.gameelectricity.app.util.ConstraintUtil
import com.sn.gameelectricity.data.model.bean.LogisticsInformationBean
import com.sn.gameelectricity.data.model.bean.OrderDetailsBean
import com.sn.gameelectricity.data.model.bean.TraceVO
import com.sn.gameelectricity.databinding.ActivityOrderDetailsBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.common.ConfigConstants
import me.hgj.jetpackmvvm.ext.convert
import me.hgj.jetpackmvvm.network.NettServerException
import me.hgj.jetpackmvvm.util.PublicPracticalMethodFromKT
import me.hgj.jetpackmvvm.util.ScreenTools
import me.hgj.jetpackmvvm.util.ToastUtil
import me.hgj.jetpackmvvm.util.observableToMain
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

/**
 * Date:2022/5/10
 * Time:19:52
 * author:dimple
 * 订单详情 ViewModel
 */
class OrderDetailsViewModel : BaseViewModel() {

    val orderByIdLive = MutableLiveData<OrderDetailsBean>()
    val deliveryByDeliveryNoLive = MutableLiveData<LogisticsInformationBean>()

    /**
     * 根据状态栏高度设置控件高度
     */
    fun setTopViewHeight(mActivity: Activity, mBinding: ActivityOrderDetailsBinding) {
        var StatusBarHeight = ImmersionBar.getStatusBarHeight(mActivity)
        val constraintUtil = ConstraintUtil(mBinding.clMain)
        with(constraintUtil.begin()) {
            setMarginTop(
                R.id.iv_back,
                StatusBarHeight + ScreenTools.getInstance().dp2px(mActivity, 25f, true)
            )
            commit()
        }
    }

    /**
     * 高亮显示
     */
    fun orderSerialHighlight(Highlightcontent: String, content: String): SpannableString {
        val msp = SpannableString(content)
        val startIndex = content.indexOf(Highlightcontent)
        val colorSpan = ForegroundColorSpan(Color.parseColor("#061925"))
        msp.setSpan(colorSpan, startIndex, content.length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        return msp
    }

    /**
     * 通过id获取订单
     */
    fun orderById(orderId: Int) {
        HttpFactory.getInstance().APINew(ApiService.BASE_URL).orderById(orderId)
            .convert().funSubscribe(true, {
                if (it is NettServerException) {
                    ToastUtil.showCenter("${it.errorMessage}")
                }
                LogUtils.iTag(ConfigConstants.CONSTANT.TAG_ALL, "${it.message}")
            }, {
                orderByIdLive.postValue(it)
            })
    }

    /**
     * 删除订单
     */
    fun deleteOrderById(orderId: Int, onCallBack: (isSuccess: Boolean, msg: String) -> Unit) {
        val requestBody = JsonObject().let {
            it.addProperty("orderId", orderId)
            it.toString().toRequestBody("application/json".toMediaTypeOrNull())
        }
        HttpFactory.getInstance().APINew(ApiService.BASE_URL).deleteOrderById(requestBody)
            .compose(observableToMain())
            .subscribe({
                if (it.code == 0) {
                    onCallBack(true, "删除订单成功!")
                } else {
                    onCallBack(false, it.msg)
                }
            }, {
                onCallBack(false, "删除订单失败!")
            })
    }

    /**
     * 取消订单
     */
    fun cancelOrderById(orderId: Int, onCallBack: (isSuccess: Boolean, msg: String) -> Unit) {
        val requestBody = JsonObject().let {
            it.addProperty("orderId", orderId)
            it.addProperty("userId", CacheUtil.getUser()?.userId)
            it.toString().toRequestBody("application/json".toMediaTypeOrNull())
        }
        HttpFactory.getInstance().APINew(ApiService.BASE_URL).cancelOrderById(requestBody)
            .compose(observableToMain())
            .subscribe({
                if (it.code == 0) {
                    onCallBack(true, "取消订单成功!")
                } else {
                    onCallBack(false, it.msg)
                }
            }, {
                onCallBack(false, "取消订单失败!")
            })
    }

    /**
     * 物流信息
     */
    fun deliveryByDeliveryNo(deliveryNo: String, orderNo: String) {
        HttpFactory.getInstance().APINew(ApiService.BASE_URL)
            .deliveryByDeliveryNo(deliveryNo, orderNo)
            .convert().funSubscribe(false, {
                if (it is NettServerException) {
                    ToastUtil.showCenter("${it.errorMessage}")
                }
                LogUtils.iTag(ConfigConstants.CONSTANT.TAG_ALL, "${it.message}")
            }, {
                deliveryByDeliveryNoLive.postValue(it)
            })
    }

    /**
     * 更改收货地址
     */
    fun updateOrderAddressId(
        addressId: Int,
        orderId: Int,
        onCallBack: (isSuccess: Boolean, msg: String) -> Unit
    ) {
        HttpFactory.getInstance().APINew(ApiService.BASE_URL)
            .updateOrderAddressId(addressId, orderId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.code == 0) {
                    onCallBack(true, "更改收货地址成功")
                } else {
                    onCallBack(false, it.msg)
                }
            }, {
                onCallBack(false, "更改收货地址失败!")
            })
    }


    /**
     * 开始计时任务
     */
    fun startCountdownTask(
        cur_time: Long,
        end_time: Long,
        onCallBack: (params: MutableMap<String, Long>) -> Unit
    ) {
        timePartsCollection(end_time - cur_time).let {
            LogUtils.iTag(
                ConfigConstants.CONSTANT.TAG_ALL,
                "day =-= ${it["day"]}",
                "hour =-= ${it["hour"]}",
                "minute =-= ${it["minute"]}",
                "second =-= ${it["second"]}"
            )
            onCallBack(it)
        }
    }

    /**
     * 显示倒计时时间
     */
    fun showCountTimeContent(time: Long): String {
        var result = ""
        timePartsCollection(time).apply {
            val minute = if (this["minute"]!! > 9L) "${this["minute"]}" else "0${this["minute"]}"
            val second = if (this["second"]!! > 9L) "${this["second"]}" else "0${this["second"]}"
            result = "$minute:$second"
        }
        return result
    }

    /**
     * 时间差的各个部件（天，小时，分钟，秒）
     */
    private var timePartsCollection = { createtime: Long ->
//        var timeDifference = System.currentTimeMillis() - createtime * 1000L
        var timeDifference = createtime * 1000L
        val yushu_day: Long = timeDifference % (1000 * 60 * 60 * 24)
        val yushu_hour: Long = (timeDifference % (1000 * 60 * 60 * 24)
                % (1000 * 60 * 60))
        val yushu_minute: Long = (timeDifference % (1000 * 60 * 60 * 24)
                % (1000 * 60 * 60) % (1000 * 60))
        val yushu_second: Long = (timeDifference % (1000 * 60 * 60 * 24)
                % (1000 * 60 * 60) % (1000 * 60) % 1000)

        val day: Long = timeDifference / (1000 * 60 * 60 * 24)
        val hour = yushu_day / (1000 * 60 * 60)
        val minute = yushu_hour / (1000 * 60)
        val second = yushu_minute / 1000

        val mutableMap = mutableMapOf<String, Long>()
        mutableMap["day"] = day
        mutableMap["hour"] = hour
        mutableMap["minute"] = minute
        mutableMap["second"] = second
        mutableMap
    }

    /**
     * 处理物流信息数据
     */
    var transformLogisticsInformationData = { traceVOList: List<TraceVO> ->
        for (i in traceVOList.indices) {
            var action = '0'
            var shippingStatus = ""
            if (PublicPracticalMethodFromKT.ppmfKT.isBlank(traceVOList[i].action)) {
                traceVOList[i].ishowaction = false
                traceVOList[i].actionname = shippingStatus
            } else {
                action = traceVOList[i].action.toString().first()
                when {
                    action == '0' -> {
                        shippingStatus = "暂无轨迹信息"
                    }
                    action == '1' -> {
                        shippingStatus = "已揽收"
                    }
                    action == '2' -> {
                        shippingStatus = "运输中"
                    }
                    action == '3' -> {
                        shippingStatus = "已经签收"
                    }
                    action == '4' -> {
                        shippingStatus = "问题件"
                    }
                }
                traceVOList[i].actionname = shippingStatus
                if (i == 0) {
                    traceVOList[i].ishowaction = true
                } else {
                    var action2 = traceVOList[i - 1].action.toString().first()
                    var action3 = traceVOList[i].action.toString().first()
                    if (action3 != action2) {
                        traceVOList[i].ishowaction = true
                    }
                }
            }
        }
        traceVOList
    }
}