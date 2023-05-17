package com.sn.gameelectricity.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.LogUtils
import com.google.gson.JsonObject
import com.sn.gameelectricity.app.network.ApiService
import com.sn.gameelectricity.app.network.HttpFactory
import com.sn.gameelectricity.app.util.CacheUtil
import com.sn.gameelectricity.data.model.bean.CreatAssistResponse
import com.sn.gameelectricity.data.model.bean.GetAssistResponse
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.util.toJson
import me.hgj.jetpackmvvm.util.ToastUtil
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class RequestCheatingViewModel : BaseViewModel() {

    var assistLiveData: MutableLiveData<CreatAssistResponse> = MutableLiveData()
    var getAssistLiveData: MutableLiveData<GetAssistResponse> = MutableLiveData()

    /**
     * 创建助力
     */
    fun assist(goodsId: Int) {
        val jsonBody = JsonObject().apply {
            addProperty("goodsId", goodsId)
            addProperty("userId", CacheUtil.getUser()?.userId)
        }

        val requestBody = jsonBody.toString().toRequestBody("application/json".toMediaTypeOrNull())
        HttpFactory.getInstance().APINew(ApiService.BASE_URL)
            .assist(requestBody)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                LogUtils.e("123", it.toJson())
                if (it.code == 0) {
                    assistLiveData.value = it.data
                }
            }, {

            })
    }

    /**
     * 获取助力
     */
    fun getAssist(groupCode: String, userId: Int) {
        HttpFactory.getInstance().APINew(ApiService.BASE_URL)
            .goodsAssist(groupCode, userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                LogUtils.e("123", it.toJson())
                if (it.code == 0) {
                    getAssistLiveData.value = it.data
                }
//                else if (it.code == 1073807364) {
//                    ToastUtil.showCenter(it.msg)
//                }
            }, {

            })
    }

    /**
     * 加入助力
     */
    fun putAssist(groupCode: String, goodsId: Int, onSuccess: () -> Unit) {
        val jsonBody = JsonObject().apply {
            addProperty("groupCode", groupCode)
            addProperty("goodsId", goodsId)
        }

        val requestBody = jsonBody.toString().toRequestBody("application/json".toMediaTypeOrNull())
        HttpFactory.getInstance().APINew(ApiService.BASE_URL)
            .putAssist(requestBody)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                LogUtils.e("123", it.toJson())
                if (it.code == 0) {
                    onSuccess()
                } else {
                    ToastUtil.showCenter(it.msg)
                }
            }, {

            })
    }


}