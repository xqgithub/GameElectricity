package com.sn.gameelectricity.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.LogUtils
import com.google.gson.JsonObject
import com.sn.gameelectricity.app.network.ApiService
import com.sn.gameelectricity.app.network.HttpFactory
import com.sn.gameelectricity.data.model.bean.CheckinResponse
import com.sn.gameelectricity.data.model.bean.GashaponHomepageResponse
import com.sn.gameelectricity.data.model.bean.VersionResponse
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.util.toJson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class RequestMainViewModel : BaseViewModel() {

    var isNeedCheckinLiveData: MutableLiveData<Boolean> = MutableLiveData()
    var checkinLiveData: MutableLiveData<CheckinResponse> = MutableLiveData()

    var versionLiveData: MutableLiveData<VersionResponse> = MutableLiveData()

    /**
     * 签到
     */
    fun checkin() {
        val jsonBody = JsonObject().apply {
        }

        val requestBody = jsonBody.toString().toRequestBody("application/json".toMediaTypeOrNull())
        HttpFactory.getInstance().APINew(ApiService.BASE_URL)
            .checkin(requestBody)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                LogUtils.e("123", it.toJson())
                if (it.code == 0) {
                    checkinLiveData.value = it.data
                }
            }, {

            })
    }

    /**
     * 是否需要签到
     */
    fun isNeedCheckin() {
        HttpFactory.getInstance().APINew(ApiService.BASE_URL)
            .isNeedCheckin()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                LogUtils.e("123", it.toJson())
                if (it.code == 0) {
                    isNeedCheckinLiveData.value = true
                } else {
                    isNeedCheckinLiveData.value = false
                }
            }, {

            })
    }

    /**
     * 获取最新版本
     */
    fun getVersion(version: String, osType: Int = 1) {
        HttpFactory.getInstance().APINew(ApiService.BASE_URL)
            .checkVersion(version, osType)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                LogUtils.e("123", it.toJson())
                if (it.code == 0) {
                    versionLiveData.value = it.data
                }
            }, {
                LogUtils.e("123", it.toJson())
            })
    }


    /**
     * 扭蛋首页
     */
    fun gashaponHomepage(onSuccess: (GashaponHomepageResponse) -> Unit) {
        HttpFactory.getInstance().APINew(ApiService.BASE_URL)
            .gashaponHomepage()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                LogUtils.e("123", it.toJson())
                if (it.code == 0) {
                    onSuccess(it.data)
                }
            }, {

            })
    }


}