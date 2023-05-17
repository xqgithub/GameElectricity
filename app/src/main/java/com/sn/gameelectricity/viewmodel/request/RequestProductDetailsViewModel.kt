package com.sn.gameelectricity.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.LogUtils
import com.google.gson.JsonObject
import com.sn.gameelectricity.app.network.ApiService
import com.sn.gameelectricity.app.network.HttpFactory
import com.sn.gameelectricity.app.util.CacheUtil
import com.sn.gameelectricity.data.model.bean.GoodsGetgoodsResponse
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.util.toJson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class RequestProductDetailsViewModel : BaseViewModel() {

    var productGetGoods: MutableLiveData<GoodsGetgoodsResponse> = MutableLiveData()
    var wishLiveData: MutableLiveData<Any> = MutableLiveData()

    /**
     * 商品详情
     */
    fun goodsGetGoods(goodsId: Int) {
        HttpFactory.getInstance().APINew(ApiService.BASE_URL)
            .goodsGetGoods(goodsId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                LogUtils.e("123", it.toJson())
                if (it.code == 0) {
                    productGetGoods.value = it.data
                }
            }, {
                LogUtils.e("123", it.toJson())
            })
    }

    /**
     * 添加心愿购
     */
    fun wish(
        goodsId: Int
    ) {
        val jsonBody = JsonObject().apply {
            addProperty("goodsId", goodsId)
            addProperty("userId", CacheUtil.getUser()?.userId)
        }

        val requestBody = jsonBody.toString().toRequestBody("application/json".toMediaTypeOrNull())
        HttpFactory.getInstance().APINew(ApiService.BASE_URL)
            .wish(requestBody)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                LogUtils.e("789", it.toJson())
                if (it.code == 0) {
                    wishLiveData.value = it.data
                }
            }, {

            })
    }

    /**
     * 添加心愿购
     */
    fun wishForResult(
        goodsId: Int, onResult: () -> Unit
    ) {
        val jsonBody = JsonObject().apply {
            addProperty("goodsId", goodsId)
            addProperty("userId", CacheUtil.getUser()?.userId)
        }

        val requestBody = jsonBody.toString().toRequestBody("application/json".toMediaTypeOrNull())
        HttpFactory.getInstance().APINew(ApiService.BASE_URL)
            .wish(requestBody)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                LogUtils.e("789", it.toJson())
                if (it.code == 0) {
                    onResult()
                }
            }, {

            })
    }


}