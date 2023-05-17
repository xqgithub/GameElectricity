package com.sn.gameelectricity.viewmodel.state

import android.app.Activity
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.LogUtils
import com.sn.gameelectricity.app.network.ApiService
import com.sn.gameelectricity.app.network.HttpFactory
import com.sn.gameelectricity.app.network.RetrofitServiceManager
import com.sn.gameelectricity.app.util.CacheUtil
import com.sn.gameelectricity.data.model.bean.FocusListBean
import com.sn.gameelectricity.data.model.bean.PersonalInfo
import com.sn.gameelectricity.data.model.bean.StatisticsOrderBean
import com.sn.gameelectricity.ui.activity.OrderListActivity
import com.sn.gameelectricity.ui.fragment.OrderListFragment
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.common.ConfigConstants
import me.hgj.jetpackmvvm.ext.convert
import me.hgj.jetpackmvvm.network.NettServerException
import me.hgj.jetpackmvvm.util.PublicPracticalMethodFromKT
import me.hgj.jetpackmvvm.util.ToastUtil
import me.hgj.jetpackmvvm.util.dataNullConvert
import me.hgj.jetpackmvvm.util.dataNullConvertToString

/**
 * 描述　: 专门存放 MeFragment 界面数据的ViewModel
 */
class MeViewModel : BaseViewModel() {

    val universalListLive = MutableLiveData<List<FocusListBean>>()
    val userInfoLive = MutableLiveData<PersonalInfo>()
    val countUserOrderLive = MutableLiveData<StatisticsOrderBean>()

    fun focusList(keyword: String, pageNum: Int, pageSize: Int) {
        RetrofitServiceManager.getInstance().getApiService(ApiService.BASE_TEST_YAPI)
            .myFocusList("", 0, 10)
            .convert().funSubscribe(true, {
                if (it is NettServerException) {
                    ToastUtil.showCenter("${it.errorMessage}")
                }
                LogUtils.iTag(ConfigConstants.CONSTANT.TAG_ALL, "${it.message}")
            }, {
                universalListLive.postValue(it)
            })
    }

    /**
     * 跳转到订单列表页面
     */
    fun intentToJumpOrderListActivity(mActivity: Activity, type: OrderListFragment.OrderType) {
        with(Bundle()) {
            putSerializable("type", type)
            PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                mActivity,
                OrderListActivity::class.java, this, false
            )
        }
    }


    /**
     * 设置手机号加密状态
     */
    fun getPhoneEncryption(phone: String): String {
        return phone.replaceRange(3, 7, " **** ")
    }

    /**
     * 获取用户信息
     */
    fun getuserInfo() {
        HttpFactory.getInstance().APINew(ApiService.BASE_URL).getuserInfo()
            .convert().funSubscribe(false, {
                if (it is NettServerException) {
                    ToastUtil.showCenter("${it.errorMessage}")
                }
                LogUtils.iTag(ConfigConstants.CONSTANT.TAG_ALL, "${it.message}")
            }, { bean ->
                CacheUtil.getUser()?.let {
                    it.avatarUrl = dataNullConvertToString(bean.avatarUrl) as String
                    it.email = dataNullConvertToString(bean.email) as String
                    it.gender = dataNullConvertToString(bean.gender) as Int
                    it.guideStageId = bean.guideStageId
                    it.goldCoin = bean.goldCoin
                    it.score = dataNullConvertToString(bean.score) as Int
                    it.idCard = dataNullConvertToString(bean.idCard) as String
                    it.mobile = bean.mobile
                    it.nickName = bean.nickName
                    it.realName = dataNullConvertToString(bean.realName) as String
                    it.userId = bean.userId
                    it.birthday = dataNullConvertToString(bean.birthday) as String
                    it.boolGuide = bean.boolGuide
                    it.boolNewUserActivityExchange = bean.boolNewUserActivityExchange
                    it.iv = bean.iv
                    it.key = bean.key
                    it.userCode = dataNullConvert(bean.userCode)
                    it.loadingUrl = dataNullConvert(bean.loadingUrl)
                    it.userRightsCouponId = dataNullConvertToString(bean.userRightsCouponId) as Int
                    CacheUtil.setUser(it)
                }
                userInfoLive.postValue(bean)
            })
    }

    /**
     * 统计订单
     */
    fun countUserOrder() {
        HttpFactory.getInstance().APINew(ApiService.BASE_URL).countUserOrder()
            .convert().funSubscribe(false, {
                if (it is NettServerException) {
                    ToastUtil.showCenter("${it.errorMessage}")
                }
                LogUtils.iTag(ConfigConstants.CONSTANT.TAG_ALL, "${it.message}")
            }, {
                countUserOrderLive.postValue(it)
            })
    }
}