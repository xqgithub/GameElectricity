package com.sn.gameelectricity.viewmodel

import android.content.Context
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.LogUtils
import com.sn.gameelectricity.R
import com.sn.gameelectricity.app.App
import com.sn.gameelectricity.app.network.ApiService
import com.sn.gameelectricity.app.network.HttpFactory
import com.sn.gameelectricity.app.weight.customview.AvatarsView
import com.sn.gameelectricity.data.model.bean.OrderListbean
import com.sn.gameelectricity.ui.fragment.OrderListFragment
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.common.ConfigConstants
import me.hgj.jetpackmvvm.ext.convert
import me.hgj.jetpackmvvm.network.NettServerException
import me.hgj.jetpackmvvm.util.ScreenTools
import me.hgj.jetpackmvvm.util.ToastUtil
import java.util.HashMap

/**
 * Date:2022/5/9
 * Time:17:43
 * author:dimple
 * 订单列表 ViewModel
 */
class OrderListViewModel : BaseViewModel() {

    val orderListLive = MutableLiveData<List<OrderListbean>>()
    val searchUserOrderListLive = MutableLiveData<List<OrderListbean>>()

    /**
     * 订单列表
     */
    fun orderList(orderType: OrderListFragment.OrderType, pageNum: Int = 1, pageSize: Int = 10) {
        val mMap: MutableMap<String, Int> = HashMap()
        when (orderType) {
            OrderListFragment.OrderType.PENDING_PAYMENT -> mMap["status"] = 0
            OrderListFragment.OrderType.PENDING_DELIVERED -> mMap["status"] = 1
            OrderListFragment.OrderType.PENDING_DELIVERY -> mMap["status"] = 2
            OrderListFragment.OrderType.PENDING_RECEIPT -> mMap["status"] = 3
            OrderListFragment.OrderType.COMPLETED -> mMap["status"] = 4
        }
        mMap["pageNum"] = pageNum
        mMap["pageSize"] = pageSize

        HttpFactory.getInstance().APINew(ApiService.BASE_URL).orderList(mMap)
            .convert().funSubscribe(false, {
                if (it is NettServerException) {
                    ToastUtil.showCenter("${it.errorMessage}")
                }
                LogUtils.iTag(ConfigConstants.CONSTANT.TAG_ALL, "${it.message}")
            }, {
                orderListLive.postValue(it)
            })
    }

    /**
     * 订单搜索
     */
    fun searchUserOrder(searchKeywords: String = "", pageNum: Int = 1, pageSize: Int = 10) {
        HttpFactory.getInstance().APINew(ApiService.BASE_URL)
            .searchUserOrder(pageNum, pageSize, searchKeywords)
            .convert().funSubscribe(true, {
                if (it is NettServerException) {
                    ToastUtil.showCenter("${it.errorMessage}")
                }
                LogUtils.iTag(ConfigConstants.CONSTANT.TAG_ALL, "${it.message}")
            }, {
                searchUserOrderListLive.postValue(it)
            })
    }


    /**
     * 添加助力人员头像
     */
    fun addHelpAvatars(
        context: Context,
        contentLayout: ConstraintLayout,
        avatars: MutableList<String>
    ) {
        val avatarsViewIDs = mutableListOf<Int>()
        for (i in avatars.indices) {
            val avatarsView = AvatarsView(context)
            avatarsView.setAvatarWHData(
                ScreenTools.getInstance().dp2px(App.instance, 34f, true),
                ScreenTools.getInstance().dp2px(App.instance, 34f, true)
            )
            avatarsView.setAvatarData(avatars[i]) {

            }
            //自定义LayoutParams 实现AvatarsView的位置等基本信息
            val cl_layoutParams: ConstraintLayout.LayoutParams = ConstraintLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )
            avatarsView.id = 10000 + i
            when (i) {
                0 -> {
                    cl_layoutParams.leftToLeft = R.id.cl_main
                }
                else -> {
                    cl_layoutParams.leftToLeft = avatarsViewIDs[i - 1]
                    cl_layoutParams.leftMargin =
                        ScreenTools.getInstance()
                            .dp2px(App.instance, 34f, true) - ScreenTools.getInstance()
                            .dp2px(App.instance, 10f, true)
                }
            }
            avatarsViewIDs.add(avatarsView.id)
            avatarsView.layoutParams = cl_layoutParams
            contentLayout.addView(avatarsView)
        }
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


}