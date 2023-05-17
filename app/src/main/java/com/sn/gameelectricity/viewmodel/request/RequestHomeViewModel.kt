package com.sn.gameelectricity.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.LogUtils
import com.google.gson.JsonObject
import com.sn.gameelectricity.app.ext.topicListResponse
import com.sn.gameelectricity.app.network.ApiService
import com.sn.gameelectricity.app.network.HttpFactory
import com.sn.gameelectricity.app.util.CacheUtil
import com.sn.gameelectricity.data.model.bean.*
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.util.toJson
import me.hgj.jetpackmvvm.util.ToastUtil
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

/**
 */
class RequestHomeViewModel : BaseViewModel() {

    var homeTopicList: MutableLiveData<MutableList<TopicListResponse>> = MutableLiveData()
    var homeGoodsEveryday: MutableLiveData<MutableList<GoodsEveryResponse>> = MutableLiveData()
    var homeGoodsPage: MutableLiveData<MutableList<GoodsPageResponse>> = MutableLiveData()
    var homeMoreGoodsPage: MutableLiveData<MutableList<GoodsPageResponse>> = MutableLiveData()
    var awardPoolResponseLiveData: MutableLiveData<AwardPoolResponse> = MutableLiveData()

    var homeGoodsAll: MutableLiveData<MutableList<GoodsAllResponse>> = MutableLiveData()
    var homeMoreGoodsAll: MutableLiveData<MutableList<GoodsAllResponse>> = MutableLiveData()
    var equityGoodsPage: MutableLiveData<MutableList<GoodsPageResponse>> = MutableLiveData()


    /**
     * 主题列表
     */
    fun topicList() {
        HttpFactory.getInstance().APINew(ApiService.BASE_URL)
            .topicList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                LogUtils.e(it.toJson())
                if (it.code == 0) {
                    homeTopicList.value = it.data
                    topicListResponse = it.data
                }
            }, {

            })
    }


    /**
     * 每日必抢
     */
    fun goodsEveryday(pageNum: Int, pageSize: Int) {
        HttpFactory.getInstance().APINew(ApiService.BASE_URL)
            .goodsEveryday(pageNum, pageSize)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                LogUtils.e(it.toJson())
                if (it.code == 0) {
                    homeGoodsEveryday.value = it.data
                } else {
                    homeGoodsEveryday.postValue(arrayListOf())
                }
            }, {
                homeGoodsEveryday.postValue(arrayListOf())
            })
    }

    private var pageNo = 1
    private fun isRefreshData() = pageNo == 1

    fun refreshGoodsPage(topicId: Int) {
        pageNo = 1
        goodsPage(pageNo, 10, topicId)
    }

    fun loadMoreGoodsPage(topicId: Int) {
        pageNo++
        goodsPage(pageNo, 10, topicId)
    }

    /**
     * 商品列表
     */
    fun goodsPage(pageNum: Int, pageSize: Int = 10, topicId: Int) {
        HttpFactory.getInstance().APINew(ApiService.BASE_URL)
            .goodsPage(pageNum, pageSize, topicId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                LogUtils.e(it.toJson())
                if (it.code == 0) {
                    if (isRefreshData()) {
                        homeGoodsPage.value = it.data
                    } else {
                        homeMoreGoodsPage.value = it.data
                    }
                } else {
                    homeGoodsPage.postValue(arrayListOf())
                }
            }, {
                homeGoodsPage.postValue(arrayListOf())
                LogUtils.e(it.toJson())
            })
    }

    /**
     * 获取用户奖池
     */
    fun awardPool() {
        HttpFactory.getInstance().APINew(ApiService.BASE_URL)
            .awardPool()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                LogUtils.e(it.toJson())
                if (it.code == 0) {
                    awardPoolResponseLiveData.value = it.data
                }
            }, {
                LogUtils.e(it.toJson())
            })
    }

    /**
     * 刷新用户奖池
     */
    fun putAwardPool(onFail: () -> Unit) {
        HttpFactory.getInstance().APINew(ApiService.BASE_URL)
            .putAwardPool()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                LogUtils.e(it.toJson())
                if (it.code == 0) {
                    awardPoolResponseLiveData.value = it.data
                } else if (it.code == 1879113763) {
                    ToastUtil.showCenter("今日刷新次数已达到上限啦")
                    onFail()
                }
            }, {
                LogUtils.e(it.toJson())
            })
    }


    /**
     * 冲冲冲
     */
    fun everydayActivity(everydayActivityId: Int, onSuccess: () -> Unit, onFail: (String) -> Unit) {
        HttpFactory.getInstance().APINew(ApiService.BASE_URL)
            .everydayActivity(everydayActivityId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                LogUtils.e(it.toJson())
                if (it.code == 0) {
                    onSuccess()
                } else {
                    if (it.code == 1610678280) {
                        onFail("今日已冲过一次，明日再来吧！")
                    } else {
                        onFail("当前太多人在冲，不小心失败了！")
                    }

                }
            }, {
                LogUtils.e(it.toJson())
            })
    }

    /**
     * 摇奖
     */
    fun gashaponPlay(onSuccess: (MutableList<GashaponPlayResponse>) -> Unit) {
        HttpFactory.getInstance().APINew(ApiService.BASE_URL)
            .gashaponPlay()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                LogUtils.e(it.toJson())
                if (it.code == 0) {
                    onSuccess(it.data)
                }
            }, {
                LogUtils.e(it.toJson())
            })
    }

    /**
     * 获取回收内容
     */
    fun gashaponPlay(goodsId: Int, onSuccess: (AwardGetResponse) -> Unit) {
        HttpFactory.getInstance().APINew(ApiService.BASE_URL)
            .awardGet(goodsId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                LogUtils.e(it.toJson())
                if (it.code == 0) {
                    onSuccess(it.data)
                }
            }, {
                LogUtils.e(it.toJson())
            })
    }

    /**
     * 回收奖品
     */
    fun gashaponPlay(goodsId: Int, orderId: Int, recoverType: Int, onSuccess: () -> Unit) {
        CacheUtil.getUser()?.let {
            HttpFactory.getInstance().APINew(ApiService.BASE_URL)
                .awardRecover(goodsId, orderId,recoverType,1, it.userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    LogUtils.e(it.toJson())
                    if (it.code == 0) {
                        onSuccess()
                    }
                }, {
                    LogUtils.e(it.toJson())
                })
        }
    }


    private var goodsAllPageNo = 1
    private fun isGoodsAllRefreshData() = goodsAllPageNo == 1

    fun refreshGoodsAll() {
        goodsAllPageNo = 1
        goodsAll(goodsAllPageNo, 10)
    }

    fun loadMoreGoodsAll() {
        goodsAllPageNo++
        goodsAll(goodsAllPageNo, 10)
    }

    /**
     * 添加心愿购列表
     */
    fun goodsAll(pageNum: Int, pageSize: Int = 10) {
        HttpFactory.getInstance().APINew(ApiService.BASE_URL)
            .goodsAll(pageNum, pageSize)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                LogUtils.e(it.toJson())
                if (it.code == 0) {
                    if (isGoodsAllRefreshData()) {
                        homeGoodsAll.value = it.data
                    } else {
                        homeMoreGoodsAll.value = it.data
                    }
                } else {
                    homeGoodsAll.postValue(arrayListOf())
                }
            }, {
                homeGoodsAll.postValue(arrayListOf())
                LogUtils.e(it.toJson())
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

    /**
     * 权益兑换
     */
    fun equityGoodsPage(pageNum: Int, pageSize: Int, topicId: Int) {
        HttpFactory.getInstance().APINew(ApiService.BASE_URL)
            .goodsPage(pageNum, pageSize, topicId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                LogUtils.e(it.toJson())
                if (it.code == 0) {
                    equityGoodsPage.value = it.data
                } else {
                    equityGoodsPage.postValue(arrayListOf())
                }
            }, {
                equityGoodsPage.postValue(arrayListOf())
            })
    }

}