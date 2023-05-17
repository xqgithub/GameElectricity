package com.sn.gameelectricity.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.LogUtils
import com.google.gson.JsonObject
import com.sn.gameelectricity.app.network.ApiService
import com.sn.gameelectricity.app.network.HttpFactory
import com.sn.gameelectricity.app.util.CacheUtil
import com.sn.gameelectricity.data.model.bean.CreatRightOrderResponse
import com.sn.gameelectricity.data.model.bean.GoodsRightDetailResponse
import com.sn.gameelectricity.data.model.bean.GoodsRightListResponse
import com.sn.gameelectricity.data.model.bean.UserCouponResponse
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

/**
 */
class RequestEquityViewModel : BaseViewModel() {

    var goodsRightList: MutableLiveData<GoodsRightListResponse> = MutableLiveData()
    var userCouponLiveData: MutableLiveData<UserCouponResponse> = MutableLiveData()

    var creatRightOrderResponseLive: MutableLiveData<CreatRightOrderResponse> = MutableLiveData()

    /**
     * 获取权益中心列表
     */
    fun goodsRightList(pageNum: Int, pageSize: Int) {
        CacheUtil.getUser()?.let {
            HttpFactory.getInstance().APINew(ApiService.BASE_URL)
                .goodsRightList(pageNum, pageSize, it.userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    LogUtils.e(it.toJson())
                    if (it.code == 0) {
                        goodsRightList.value = it.data
                    }
                }, {
                })
        }
    }


    /**
     * 获取用户权益券
     */
    fun userCoupon() {
        HttpFactory.getInstance().APINew(ApiService.BASE_URL)
            .userCoupon()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                LogUtils.e(it.toJson())
                if (it.code == 0) {
                    userCouponLiveData.value = it.data
                }
            }, {

            })
    }


    /**
     * 获取权益商品详情
     */
    fun goodsRightDetail(goodsId: Int, userId: Int, onSuccess: (GoodsRightDetailResponse) -> Unit) {
        HttpFactory.getInstance().APINew(ApiService.BASE_URL)
            .goodsRightDetail(goodsId, userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                LogUtils.e(it.toJson())
                if (it.code == 0) {
                    onSuccess(it.data)
                }
            }, {
            })
    }

    /**
     * 创建权益订单
     */
    fun createRightsOrder(
        addressId: Int,
        distributionType: Int,
        factAmt: Double,
        orderAmt: Double,
        payIp: String,
        payType: String,
        postageAmount: Double,
        remark: String,
        goodsId: Int,
        userRightsGoodsId: Int,
        mobileOS: String = "android"
    ) {
        val requestBody = JsonObject().let {
            it.addProperty("addressId", addressId)
            it.addProperty("contactPhone", "")
            it.addProperty("discountAmt", 0)
            it.addProperty("discountConsume", 0)
            it.addProperty("discountType", 0)
            it.addProperty("distributionType", distributionType)
            it.addProperty("factAmt", factAmt)
            it.addProperty("goodsId", goodsId)
            it.addProperty("groupAssistId", 0)
            it.addProperty("id", 0)
            it.addProperty("mobileOS", mobileOS)
            it.addProperty("orderAmt", orderAmt)
            it.addProperty("orderNumber", 1)
            it.addProperty("orderType", 7)
            it.addProperty("payIp", payIp)
            it.addProperty("payStatus", 1)
            it.addProperty("payTime", "")
            it.addProperty("payType", payType)
            it.addProperty("postageAmount", postageAmount)
            it.addProperty("remark", remark)
            it.addProperty("status", 0)
            it.addProperty("supplierAddress", "")
            it.addProperty("userId", CacheUtil.getUser()?.userId)
            it.addProperty("userRightsGoodsId", userRightsGoodsId)
            it.toString().toRequestBody("application/json".toMediaTypeOrNull())
        }

        HttpFactory.getInstance().APINew(ApiService.BASE_URL)
            .createRightsOrder(requestBody)
            .convert().funSubscribe(true,
                {
                    if (it is NettServerException) {
                        ToastUtil.showCenter("${it.errorMessage}")
                    }
                    LogUtils.iTag(ConfigConstants.CONSTANT.TAG_ALL, "${it.message}")
                }, {
                    creatRightOrderResponseLive.postValue(it)
                })
    }


    /**
     * 权益兑换
     */
    fun rightsActivity(
        rightsActivityId: Int,
        onSuccess: () -> Unit,
        onFail: (Int) -> Unit
    ) {
        HttpFactory.getInstance().APINew(ApiService.BASE_URL)
            .rightsActivity(rightsActivityId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                LogUtils.e(it.toJson())
                if (it.code == 0) {
                    onSuccess()
                } else {
//                    if (it.code == 1879113784) {
//                        onFail("这个商品太火被换光啦！")
//                    } else if (it.code == 1879113782) {
//                        onFail("你的奖券数量不足")
//                    } else {
//                        onFail("活动君走神了！")
//                    }
                    onFail(it.code)
                }
            }, {
                LogUtils.e(it.toJson())
            })
    }


}