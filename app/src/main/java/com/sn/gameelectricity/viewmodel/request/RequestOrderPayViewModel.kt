package com.sn.gameelectricity.viewmodel.request

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.JsonObject
import com.sn.gameelectricity.app.network.ApiService
import com.sn.gameelectricity.app.network.HttpFactory
import com.sn.gameelectricity.app.util.Base64
import com.sn.gameelectricity.app.util.CacheUtil
import com.sn.gameelectricity.app.util.IsInstallApp
import com.sn.gameelectricity.data.model.bean.CreatOrderResponse
import com.sn.gameelectricity.data.model.bean.GoodsGetDiscountResponse
import com.sn.gameelectricity.data.model.bean.PayStateResponse
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.common.ConfigConstants
import me.hgj.jetpackmvvm.ext.convert
import me.hgj.jetpackmvvm.ext.util.toJson
import me.hgj.jetpackmvvm.network.NettServerException
import me.hgj.jetpackmvvm.util.ToastUtil
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.UnsupportedEncodingException
import java.net.URLEncoder

class RequestOrderPayViewModel : BaseViewModel() {

    var orderGetDiscount: MutableLiveData<GoodsGetDiscountResponse> = MutableLiveData()

    var orderCreatOrder: MutableLiveData<CreatOrderResponse> = MutableLiveData()

    var createTransactionOrderLive: MutableLiveData<CreatOrderResponse> = MutableLiveData()

    /**
     * 金币抵扣
     */
    fun goodsGetDiscount(goodsId: Int) {
        HttpFactory.getInstance().APINew(ApiService.BASE_URL)
            .goodsGetDiscount(goodsId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                LogUtils.e("123", it.toJson())
                if (it.code == 0) {
                    orderGetDiscount.value = it.data
                }
            }, {

            })
    }

    /**
     * 创建订单
     */
    fun createOrder(
        addressId: Int,
        contactPhone: String,
        discountAmt: Float,
        discountConsume: Int,
        discountType: Int,
        distributionType: Int,
        factAmt: Float,
        goodsId: Int,
        groupAssistId: Int,
        id: Int,
        orderAmt: Float,
        orderNumber: Int,
        orderType: Int,
        payStatus: Int,
        payType: String,
        postageAmount: Float,
        status: Int,
        supplierAddress: String,
        remark: String
    ) {
        val jsonBody = JsonObject().apply {
            addProperty("addressId", addressId)
            addProperty("contactPhone", contactPhone)
            addProperty("discountAmt", discountAmt)
            addProperty("discountConsume", discountConsume)
            addProperty("discountType", discountType)
            addProperty("distributionType", distributionType)
            addProperty("factAmt", factAmt)
            addProperty("goodsId", goodsId)
            addProperty("groupAssistId", groupAssistId)
            addProperty("id", id)
            addProperty("mobileOS", "android")
            addProperty("orderAmt", orderAmt)
            addProperty("orderNumber", orderNumber)
            addProperty("orderType", orderType)
            addProperty("payIp", "")
            addProperty("payStatus", payStatus)
            addProperty("payTime", "")
            addProperty("payType", payType)
            addProperty("postageAmount", postageAmount)
            addProperty("remark", remark)
            addProperty("status", status)
            addProperty("supplierAddress", supplierAddress)
            addProperty("userId", CacheUtil.getUser()?.userId)
        }

        val requestBody = jsonBody.toString().toRequestBody("application/json".toMediaTypeOrNull())
        HttpFactory.getInstance().APINew(ApiService.BASE_URL)
            .createOrder(requestBody)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                LogUtils.e("123", it.toJson())
                if (it.code == 0) {
                    orderCreatOrder.value = it.data
                }
            }, {

            })
    }

    /**
     * 创建支付订单
     */
    fun createTransactionOrder(
        addressId: Int,
        distributionType: Int,
        factAmt: Double,
        orderAmt: Double,
        orderNo: String,
        payIp: String,
        payType: String,
        postageAmount: Double,
        remark: String,
        mobileOS: String = "android"
    ) {
        val requestBody = JsonObject().let {
            it.addProperty("addressId", addressId)
            it.addProperty("distributionType", distributionType)
            it.addProperty("factAmt", factAmt)
            it.addProperty("mobileOS", mobileOS)
            it.addProperty("orderAmt", orderAmt)
            it.addProperty("orderNo", orderNo)
            it.addProperty("payIp", payIp)
            it.addProperty("payType", payType)
            it.addProperty("postageAmount", postageAmount)
            it.addProperty("userId", CacheUtil.getUser()?.userId)
            it.addProperty("remark", remark)
            it.toString().toRequestBody("application/json".toMediaTypeOrNull())
        }

        HttpFactory.getInstance().APINew(ApiService.BASE_URL)
            .createTransactionOrder(requestBody)
            .convert().funSubscribe(true,
                {
                    if (it is NettServerException) {
                        ToastUtil.showCenter("${it.errorMessage}")
                    }
                    LogUtils.iTag(ConfigConstants.CONSTANT.TAG_ALL, "${it.message}")
                }, {
                    createTransactionOrderLive.postValue(it)
                })
    }

    /**
     * 特殊订单完成
     */
    fun specialOrderCompletion(
        addressId: Int,
        distributionType: Int,
        factAmt: Double,
        orderAmt: Double,
        orderNo: String,
        payIp: String,
        payType: String,
        postageAmount: Double,
        remark: String,
        mobileOS: String = "android"
    ) {
        val requestBody = JsonObject().let {
            it.addProperty("addressId", addressId)
            it.addProperty("distributionType", distributionType)
            it.addProperty("factAmt", factAmt)
            it.addProperty("mobileOS", mobileOS)
            it.addProperty("orderAmt", orderAmt)
            it.addProperty("orderNo", orderNo)
            it.addProperty("payIp", payIp)
            it.addProperty("payType", payType)
            it.addProperty("postageAmount", postageAmount)
            it.addProperty("remark", remark)
            it.addProperty("userId", CacheUtil.getUser()?.userId)
            it.toString().toRequestBody("application/json".toMediaTypeOrNull())
        }

        HttpFactory.getInstance().APINew(ApiService.BASE_URL)
            .specialOrderCompletion(requestBody)
            .convert().funSubscribe(true,
                {
                    if (it is NettServerException) {
                        ToastUtil.showCenter("${it.errorMessage}")
                    }
                    LogUtils.iTag(ConfigConstants.CONSTANT.TAG_ALL, "${it.message}")
                }, {
//                    createTransactionOrderLive.postValue(it)
                })
    }

    /**
     * 支付宝支付
     *
     *  @param qrCodeUrl 交易url
     */
    fun aLiPay(context: Context, qrCodeUrl: String) {
        if (IsInstallApp.checkAliPayInstalled(context)) {
            val intent = Intent(Intent.ACTION_VIEW)
            var uri: Uri? = null
            try {
                val total =
                    "alipays://platformapi/startapp?saId=10000007&qrcode=" + URLEncoder.encode(
                        qrCodeUrl,
                        "UTF-8"
                    )
                Log.e("total===", "" + total)
                uri = Uri.parse(total)
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.data = uri
            context.startActivity(intent)
        } else {
            ToastUtils.showShort("请安装支付宝APP！")
        }
    }

    fun wxPay(context: Context, qrCodeUrl: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        var uri: Uri? = null
        try {
            val base64Url = String(Base64.getEncoder().encode(qrCodeUrl.toByteArray()))
            val payString: String = "https://gmpaytest.aifun.com/?wxPayUrl=" + base64Url
            LogUtils.e("payString==", "" + payString)
            uri = Uri.parse(payString)
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.data = uri
        context.startActivity(intent)
    }

    var countDownTimer: CountDownTimer? = null

    fun getCountDownTime(time: Long, orderNo: String, onSuccess: () -> Unit) {
        if (countDownTimer == null) {
            countDownTimer = object : CountDownTimer(time, 3000) {
                override fun onTick(l: Long) {
                    queryOrder(orderNo) {
                        countDownTimer?.cancel()
                        countDownTimer = null
                        onSuccess()
                        LogUtils.e("789")
                        ToastUtils.showShort("支付成功")
                    }
                }

                override fun onFinish() {
                    countDownTimer?.cancel()
                    countDownTimer = null
                    ToastUtils.showShort("支付失败")
                }
            }
            (countDownTimer as CountDownTimer).start()
        }
    }

    /**
     * 支付结果
     */
    fun queryOrder(orderNo: String, onSuccess: (PayStateResponse) -> Unit) {
        HttpFactory.getInstance().APINew(ApiService.BASE_URL)
            .queryOrder(orderNo)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                LogUtils.e("123", it.toJson())
                if (it.code == 0) {
                    when (it.data.respCode) {
                        "0000" -> onSuccess(it.data)
                    }
                }
            }, {

            })
    }


}