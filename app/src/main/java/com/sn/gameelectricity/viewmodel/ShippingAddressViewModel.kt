package com.sn.gameelectricity.viewmodel

import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.LogUtils
import com.google.gson.JsonObject
import com.sn.gameelectricity.app.network.ApiService
import com.sn.gameelectricity.app.network.HttpFactory
import com.sn.gameelectricity.app.util.CacheUtil
import com.sn.gameelectricity.app.weight.dialog.AreaDialog
import com.sn.gameelectricity.data.model.bean.DeliveryAddressDetails
import com.sn.gameelectricity.data.model.bean.AddressSaveResponse
import com.sn.gameelectricity.data.model.bean.AreaBean
import com.sn.gameelectricity.data.model.bean.DeliveryAddressBean
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.base.viewmodel.SingleObserveLiveData
import me.hgj.jetpackmvvm.common.ConfigConstants
import me.hgj.jetpackmvvm.ext.convert
import me.hgj.jetpackmvvm.ext.util.toJson
import me.hgj.jetpackmvvm.network.NettServerException
import me.hgj.jetpackmvvm.util.ToastUtil
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

/**
 * Date:2022/5/9
 * Time:17:43
 * author:dimple
 * 收货地址 ViewModel
 */
class ShippingAddressViewModel : BaseViewModel() {


    val deliveryAddressLive = MutableLiveData<List<DeliveryAddressBean>>()
    val deliveryAddressAreaListLive = SingleObserveLiveData<List<AreaBean>>()
    val deliveryAddressDetailsLive = MutableLiveData<DeliveryAddressDetails>()
    val deliveryAdLive = MutableLiveData<List<DeliveryAddressBean>>()


    /**
     * 收货地址列表
     */
    fun deliveryAddressList(pageNum: Int = 1, pageSize: Int = 10) {
        HttpFactory.getInstance().APINew(ApiService.BASE_URL)
            .deliveryAddressList(pageNum, pageSize)
            .convert().funSubscribe(true, {
                if (it is NettServerException) {
                    ToastUtil.showCenter("${it.errorMessage}")
                }
                LogUtils.iTag(ConfigConstants.CONSTANT.TAG_ALL, "${it.message}")
            }, {
                deliveryAddressLive.postValue(it)
            })
    }

    /**
     * 地区列表
     */
    fun deliveryAddressAreaList(
        areaCode: Int,
        areaType: AreaDialog.AreaType,
        pageNum: Int = 1,
        pageSize: Int = 200
    ) {
        var areaLevel = when (areaType) {
            AreaDialog.AreaType.PROVINCE -> 1
            AreaDialog.AreaType.CITY -> 2
            AreaDialog.AreaType.AREA -> 3
            AreaDialog.AreaType.STREET -> 4
        }

        HttpFactory.getInstance().APINew(ApiService.BASE_URL)
            .deliveryAddressAreaList(areaCode, areaLevel, pageNum, pageSize)
            .convert().funSubscribe(true,
                {
                    if (it is NettServerException) {
                        ToastUtil.showCenter("${it.errorMessage}")
                    }
                    LogUtils.iTag(ConfigConstants.CONSTANT.TAG_ALL, "${it.message}")
                }, {
                    deliveryAddressAreaListLive.postValue(it)
                })
    }

    /**
     * 保存地址
     */
    fun deliveryAddressSave(
        address: String,
        addresseeName: String,
        phoneNumber: String,
        streetCode: Int,
        type: Int,
        onCallBack: (isSuccess: Boolean, msg: String, addressSaveResponse: AddressSaveResponse) -> Unit
    ) {
        val requestBody = JsonObject().let {
            it.addProperty("address", address)
            it.addProperty("addresseeName", addresseeName)
            it.addProperty("phoneNumber", phoneNumber)
            it.addProperty("status", 0)
            it.addProperty("streetCode", streetCode)
            it.addProperty("type", type)
            it.addProperty("userId", CacheUtil.getUser()!!.userId)
            it.toString().toRequestBody("application/json".toMediaTypeOrNull())
        }

        HttpFactory.getInstance().APINew(ApiService.BASE_URL).deliveryAddressSave(requestBody)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                LogUtils.e(it.toJson())
                if (it.code == 0) {
                    onCallBack(true, "保存地址成功", it.data)
                } else {
                    onCallBack(false, it.msg, it.data)
                }
            }, {
                onCallBack(false, "保存地址失败!", AddressSaveResponse())
            })
    }

    /**
     * 更新收货地址
     */
    fun deliveryAddressupdateById(
        address: String,
        addresseeName: String,
        phoneNumber: String,
        streetCode: Int,
        type: Int,
        id: Int,
        onCallBack: (isSuccess: Boolean, msg: String) -> Unit
    ) {
        val requestBody = JsonObject().let {
            it.addProperty("address", address)
            it.addProperty("addresseeName", addresseeName)
            it.addProperty("id", id)
            it.addProperty("phoneNumber", phoneNumber)
            it.addProperty("status", 0)
            it.addProperty("streetCode", streetCode)
            it.addProperty("type", type)
            it.addProperty("userId", CacheUtil.getUser()!!.userId)
            it.toString().toRequestBody("application/json".toMediaTypeOrNull())
        }

        HttpFactory.getInstance().APINew(ApiService.BASE_URL)
            .deliveryAddressupdateById(requestBody)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.code == 0) {
                    onCallBack(true, "更新地址成功")
                } else {
                    onCallBack(false, it.msg)
                }
            }, {
                onCallBack(false, "更新地址失败!")
            })
    }


    /**
     * 删除收货地址
     */
    fun deliveryAddressDelete(id: Int, onCallBack: (isSuccess: Boolean, msg: String) -> Unit) {
        HttpFactory.getInstance().APINew(ApiService.BASE_URL).deliveryAddressDelete(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.code == 0) {
                    onCallBack(true, "删除成功")
                } else {
                    onCallBack(false, it.msg)
                }
            }, {
                onCallBack(false, "删除地址失败!")
            })
    }

    /**
     *收货地址详情
     */
    fun deliveryAddressDetails(id: Int) {
        HttpFactory.getInstance().APINew(ApiService.BASE_URL).deliveryAddressDetails(id)
            .convert().funSubscribe(true, {
                if (it is NettServerException) {
                    ToastUtil.showCenter("${it.errorMessage}")
                }
                LogUtils.iTag(ConfigConstants.CONSTANT.TAG_ALL, "${it.message}")
            }, {
                deliveryAddressDetailsLive.postValue(it)
            })
    }


    /**
     * 收货地址列表
     */
    fun deliveryAdList(pageNum: Int = 1, pageSize: Int = 10) {
        HttpFactory.getInstance().APINew(ApiService.BASE_URL)
            .deliveryAddressList(pageNum, pageSize)
            .convert().funSubscribe(true, {
                if (it is NettServerException) {
                    ToastUtil.showCenter("${it.errorMessage}")
                }
                LogUtils.iTag(ConfigConstants.CONSTANT.TAG_ALL, "${it.message}")
            }, {
                deliveryAdLive.postValue(it)
            })
    }
}