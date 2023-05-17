package com.sn.gameelectricity.viewmodel.state

import android.app.Activity
import android.graphics.Color
import android.os.CountDownTimer
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.util.SparseArray
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.LogUtils
import com.google.gson.JsonObject
import com.gyf.immersionbar.ImmersionBar
import com.sn.gameelectricity.R
import com.sn.gameelectricity.app.network.ApiService
import com.sn.gameelectricity.app.network.HttpFactory
import com.sn.gameelectricity.app.util.CacheUtil
import com.sn.gameelectricity.app.util.ConstraintUtil
import com.sn.gameelectricity.app.weight.customview.CountdownProgressBar
import com.sn.gameelectricity.data.model.bean.*
import com.sn.gameelectricity.databinding.FragmentMoneyBinding
import com.sn.gameelectricity.ui.fragment.money.MoneyFragment
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.base.viewmodel.LoadingBean
import me.hgj.jetpackmvvm.base.viewmodel.SingleObserveLiveData
import me.hgj.jetpackmvvm.common.ConfigConstants
import me.hgj.jetpackmvvm.event.UniversalEvent
import me.hgj.jetpackmvvm.ext.convert
import me.hgj.jetpackmvvm.network.NettServerException
import me.hgj.jetpackmvvm.util.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject


/**
 * 赚金币 ViewModel
 */
class MoneyViewModel : BaseViewModel() {

    val gameCurrentGameBuildSceneLive = MutableLiveData<GameBuildSceneBean>()
    val eggFeedQuantityLive = MutableLiveData<GooseEggsFeedBean>()
    val eggFeedQuantityForDisplayLive = MutableLiveData<GooseEggsFeedBean>()
    val todayBuildTaskListLive = MutableLiveData<List<TodayBuildTaskBean>>()
    val eventListLive = SingleObserveLiveData<List<DynamicEventBean>>()
    val friendListLive = SingleObserveLiveData<friendBean>()
    val rankListLive = MutableLiveData<List<LeaderBoardBean>>()
    val myRankLive = MutableLiveData<LeaderBoardBean>()
    val couponRightListLive = SingleObserveLiveData<CouponRightBean>()


    /**
     * 根据状态栏高度设置控件高度
     */
    fun setTopViewHeight(mActivity: Activity, mBinding: FragmentMoneyBinding) {
        var StatusBarHeight = ImmersionBar.getStatusBarHeight(mActivity)
        val constraintUtil = ConstraintUtil(mBinding.clExternal)
        with(constraintUtil.begin()) {
            setMarginTop(
                R.id.cl_user_bg,
                StatusBarHeight + ScreenTools.getInstance().dp2px(mActivity, 25f, true)
            )
            commit()
        }

        val constraintUtil2 = ConstraintUtil(mBinding.clInternal)
        with(constraintUtil2.begin()) {
            setMarginTop(
                R.id.cl_user_bg2,
                StatusBarHeight + ScreenTools.getInstance().dp2px(mActivity, 25f, true)
            )
            commit()
        }
    }

    /**
     * 根据设置控件高度
     */
    fun setTopViewHeight2(constraintLayout: ConstraintLayout, uiId: Int, offSetTop: Int = 0) {
        val constraintUtil = ConstraintUtil(constraintLayout)
        with(constraintUtil.begin()) {
            setMarginTop(
                uiId,
                offSetTop
            )
            commit()
        }
    }

    /**
     * 根据状态栏高度设置控件高度
     */
    fun setTopViewHeight3(
        mActivity: Activity,
        constraintLayout: ConstraintLayout,
        uiId: Int,
        offSetTop: Int = 0
    ) {
        val constraintUtil = ConstraintUtil(constraintLayout)
        var StatusBarHeight = ImmersionBar.getStatusBarHeight(mActivity)
        with(constraintUtil.begin()) {
            setMarginTop(
                uiId,
                offSetTop + StatusBarHeight
            )
            commit()
        }
    }

    /**
     * 游戏建筑
     */
    fun gameCurrentGameBuildScene() {
        HttpFactory.getInstance().APINew(ApiService.BASE_URL).gameCurrentGameBuildScene()
            .convert().funSubscribe(false, {
                if (it is NettServerException) {
                    ToastUtil.showCenter("${it.errorMessage}")
                }
                LogUtils.iTag(ConfigConstants.CONSTANT.TAG_ALL, "${it.message}")
            }, {
                gameCurrentGameBuildSceneLive.postValue(it)
            })
    }

    /**
     * 设置建筑模型
     */
    fun setCurrentGameBuildScene(bean: GameBuildSceneBean, mViewBind: FragmentMoneyBinding) {
        mViewBind.apply {
            ivBg.load(bean.sceneImg, R.drawable.ge_money_bg, R.drawable.ge_money_bg)

            ivStroll.load(
                bean.build_slot_1.buildImg,
                R.drawable.ge_money_stroll,
                R.drawable.ge_money_stroll
            )
            ivMakeups.load(
                bean.build_slot_2.buildImg,
                R.drawable.ge_money_makeups,
                R.drawable.ge_money_makeups
            )
            ivLeisure.load(
                bean.build_slot_3.buildImg,
                R.drawable.ge_money_leisure,
                R.drawable.ge_money_leisure
            )
            ivMotherBaby.load(
                bean.build_slot_4.buildImg,
                R.drawable.ge_money_mother_baby,
                R.drawable.ge_money_mother_baby
            )
            ivDigital.load(
                bean.build_slot_5.buildImg,
                R.drawable.ge_money_digital,
                R.drawable.ge_money_digital
            )
            ivTent.load(
                bean.build_slot_8.buildImg,
                R.drawable.ge_money_tent,
                R.drawable.ge_money_tent
            )
        }
    }


    /**
     * 饲料不足高亮显示
     */
    fun insufficientFeedHighlight(content: String): SpannableString {
        //您可以通过 开启扭蛋、完成任务或 邀请好友 获得饲料
        val msp = SpannableString(content)

        val startIndex = content.indexOf("开启扭蛋、完成任务")
        val endIndex = startIndex + "开启扭蛋、完成任务".length
        val startIndex2 = content.indexOf("邀请好友")
        val endIndex2 = startIndex2 + "邀请好友".length

        val colorSpan = ForegroundColorSpan(Color.parseColor("#EF874E"))
        val colorSpan2 = ForegroundColorSpan(Color.parseColor("#EF874E"))
        msp.setSpan(colorSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        msp.setSpan(colorSpan2, startIndex2, endIndex2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return msp
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
//            LogUtils.iTag(
//                ConfigConstants.CONSTANT.TAG_ALL,
//                "day =-= ${it["day"]}",
//                "hour =-= ${it["hour"]}",
//                "minute =-= ${it["minute"]}",
//                "second =-= ${it["second"]}"
//            )
            onCallBack(it)
        }
    }

    /**
     * 显示倒计时时间
     */
    fun showCountTimeContent(time: Long): String {
        var result = ""
        timePartsCollection(time).apply {
            result = if (time > 60 * 60) {
//                "${this["hour"]}小时${this["minute"]}分${this["second"]}秒"
                "${this["hour"]}小时${this["minute"]}分"
            } else {
                "${this["minute"]}分${this["second"]}秒"
            }
        }
        return result
    }

    /**
     * 显示离线奖励时间
     */
    fun showOfflineBonusTime(time: Long): String {
        var result = ""
        timePartsCollection(time).apply {
            result = if (time > 24 * 60 * 60) {
                "${this["hour"]}小时"
            } else {
                "${this["minute"]}分${this["second"]}秒"
            }
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
     * 倒计时总时间
     */
    fun countDownTotalTime(startTime: Long, endTime: Long): Long {
        var totalTime = 0L
        startCountdownTask(startTime, endTime) {
            val hour = it["hour"]!!.times(60).times(60)
            val minute = it["minute"]!!.times(60)
            val second = it["second"]!!
            totalTime = hour + minute + second
        }
        return totalTime
    }


    /**
     * 开始任务计时
     */
    var countDownMap: SparseArray<CountDownTimer> = SparseArray()

    fun startTimingTask(
        startTime: Long,
        endTime: Long,
        pbCountdown: CountdownProgressBar,
        buildTaskType: MoneyFragment.BuildTaskType,
        onCallBack: () -> Unit
    ) {
        var countDownTimer: CountDownTimer? = null

        val cur_time = System.currentTimeMillis() / 1000L
        startCountdownTask(
            cur_time,
            endTime
        ) {
            val hour = it["hour"]!!.times(60).times(60)
            val minute = it["minute"]!!.times(60)
            val second = it["second"]!!
            val countTime = hour + minute + second
            if (countTime > 0) {
                pbCountdown.apply {
                    visibility = View.VISIBLE
                    init(CountdownProgressBar.PageLocation.MoneyTaskDialog)
                    //总时间
                    val totalTime = countDownTotalTime(startTime, endTime)
                    //已经运行的时间
                    val elapsedTime = totalTime - countTime
                    //设置进度条总值
                    setProgressMax(totalTime.toInt())
                    setProgress(elapsedTime.toInt())
                    countDownTimer = object : CountDownTimer(countTime * 1000L, 1000) {
                        override fun onTick(millisUntilFinished: Long) {
                            val time = millisUntilFinished / 1000L
                            val timeleft = elapsedTime + countTime - time
                            pbCountdown.setProgress(timeleft.toInt())
                            pbCountdown.setTvText(showCountTimeContent(time))
                        }

                        override fun onFinish() {
                            val time = 0L
                            countDownTimer?.cancel()
                            pbCountdown.setProgress(countTime.toInt())
                            pbCountdown.setTvText(showCountTimeContent(time))
                            visibility = View.GONE
                            onCallBack()
                        }
                    }
                    countDownMap.put(buildTaskType.hashCode(), countDownTimer)
                    countDownTimer?.start()
                }
            } else {
                pbCountdown.visibility = View.GONE
                countDownTimer?.cancel()
                countDownTimer = null
            }
        }
    }

    /**
     * 去做任务跳转
     */
    fun doTaskJump(topicId: Int) {
        EventBus.getDefault().post(
            UniversalEvent(
                UniversalEvent.EVENT_JUMP_HOMEFRAGMENT2,
                topicId
            )
        )
    }


    /**
     * 处理我的动态数据
     */
    var transformDynamicData = { dynamicEventBeanList: List<DynamicEventBean> ->
        for (i in dynamicEventBeanList.indices) {
            val currentTime = DateUtil.getDateToString(System.currentTimeMillis(), 4).toInt()
            val createTime = DateUtil.getDateToString(dynamicEventBeanList[i].createTime, 4).toInt()
            val timeDifference = currentTime - createTime

            if (timeDifference < 1) {// 今天
                dynamicEventBeanList[i].timeTimeHead = "今天"
            } else if (timeDifference in 1..1) {//昨天
                dynamicEventBeanList[i].timeTimeHead = "昨天"
            } else {//更多
                dynamicEventBeanList[i].timeTimeHead = "更早"
            }
        }

        for (i in dynamicEventBeanList.indices) {
            if (i == 0) {
                dynamicEventBeanList[i].isshowTimeHead = true
            } else {
                var action2 = dynamicEventBeanList[i - 1].timeTimeHead
                var action3 = dynamicEventBeanList[i].timeTimeHead
                if (action3 != action2) {
                    dynamicEventBeanList[i].isshowTimeHead = true
                }
            }
        }
        dynamicEventBeanList
    }

    /**
     * 排行榜我的排名高亮处理
     */
    fun myRankHighlight(content: String, rank: Int): SpannableString {
        val msp = SpannableString(content)
        val startIndex = content.indexOf("$rank")
        val colorSpan = ForegroundColorSpan(Color.parseColor("#EF874E"))
        msp.setSpan(colorSpan, startIndex, content.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        msp.setSpan(
            AbsoluteSizeSpan(26, true),
            startIndex,
            content.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return msp
    }

    /**
     * 获取离线奖励
     */
    fun getOfflineRewards(onCallBack: (awardCoinNum: Int, awayMiliSeconds: Long) -> Unit) {
        HttpFactory.getInstance().APINew(ApiService.BASE_URL).getOfflineRewards()
            .compose(observableToMain())
            .subscribe({
                if (it.code == 0) {
                    val resultJSON = JSONObject(it.data.toString())
                    val awardCoinNum = resultJSON.optInt("awardCoinNum", 0)
                    val awayMiliSeconds = resultJSON.optLong("awayMiliSeconds", 0)
                    if (awardCoinNum > 0 || awayMiliSeconds > 0) {
                        onCallBack(
                            awardCoinNum,
                            awayMiliSeconds
                        )
                    }
                } else {
                    LogUtils.iTag(ConfigConstants.CONSTANT.TAG_ALL, it.msg)
                }
            }, {
                LogUtils.iTag(ConfigConstants.CONSTANT.TAG_ALL, it.message)
            })
    }

    /**
     * 获取鹅蛋和饲料
     */
    fun eggFeedQuantity() {
        HttpFactory.getInstance().APINew(ApiService.BASE_URL).eggFeedQuantity()
            .compose(observableToMain())
            .convert().funSubscribe(false, {
                if (it is NettServerException) {
                    ToastUtil.showCenter("${it.errorMessage}")
                }
                LogUtils.iTag(ConfigConstants.CONSTANT.TAG_ALL, "${it.message}")
            }, {
                eggFeedQuantityLive.postValue(it)
            })
    }

    /**
     * 获取鹅蛋和饲料
     */
    fun eggFeedQuantity2(onCallBack: (isSuccess: Boolean, gooseEggsFeedBean: GooseEggsFeedBean?, msg: String) -> Unit) {
        HttpFactory.getInstance().APINew(ApiService.BASE_URL).eggFeedQuantity()
            .compose(observableToMain())
            .subscribe({
                if (it.code == 0) {
                    onCallBack(true, it.data, "")
                } else {
                    onCallBack(false, null, it.msg)
                }
            }, {
                onCallBack(false, null, it.message.toString())
            })
    }

    /**
     * 获取鹅蛋和饲料
     */
    fun eggFeedQuantityForDisplay() {
        HttpFactory.getInstance().APINew(ApiService.BASE_URL).eggFeedQuantity()
            .compose(observableToMain())
            .convert().funSubscribe(true, {
                if (it is NettServerException) {
                    ToastUtil.showCenter("${it.errorMessage}")
                }
                LogUtils.iTag(ConfigConstants.CONSTANT.TAG_ALL, "${it.message}")
            }, {
                eggFeedQuantityForDisplayLive.postValue(it)
            })
    }

    /**
     * 投喂饲料
     */
    fun feed(onCallBack: (bean: GooseEggsFeedBean) -> Unit) {
        HttpFactory.getInstance().APINew(ApiService.BASE_URL).feed()
            .compose(observableToMain())
            .convert().funSubscribe(true, {
                if (it is NettServerException) {
                    ToastUtil.showCenter("${it.errorMessage}")
                }
                LogUtils.iTag(ConfigConstants.CONSTANT.TAG_ALL, "${it.message}")
            }, {
                onCallBack(it)
            })
    }

    /**
     * 兑换鹅蛋
     */
    fun eggExchange(onCallBack: (isSuccess: Boolean, msg: String, coinNum: Int, eggNum: Int) -> Unit) {
        HttpFactory.getInstance().APINew(ApiService.BASE_URL).eggExchange()
            .compose(observableToMain())
            .subscribe({
                if (it.code == 0) {
                    val resultJSON = JSONObject(it.data.toString())
                    val coinNum = resultJSON.optInt("coinNum", 0)
                    val eggNum = resultJSON.optInt("eggNum", 0)
                    onCallBack(true, "", coinNum, eggNum)
                } else {
                    onCallBack(false, it.msg, 0, 0)
                }
            }, {
                onCallBack(false, "兑换失败!", 0, 0)
            })
    }

    /**
     * 今日任务
     */
    fun todayBuildTaskList() {
        HttpFactory.getInstance().APINew(ApiService.BASE_URL).todayBuildTaskList()
            .compose(observableToMain())
            .convert().funSubscribe(false, {
                if (it is NettServerException) {
                    ToastUtil.showCenter("${it.errorMessage}")
                }
                LogUtils.iTag(ConfigConstants.CONSTANT.TAG_ALL, "${it.message}")
            }, {
                todayBuildTaskListLive.postValue(it)
            }, LoadingBean(1))
    }

    /**
     * 领取任务
     */
    fun receiveTask(taskId: Int, onCallBack: (isSuccess: Boolean, msg: String) -> Unit) {
        val requestBody = JsonObject().let {
            it.addProperty("taskId", taskId)
            it.addProperty("userId", CacheUtil.getUser()!!.userId)
            it.toString().toRequestBody("application/json".toMediaTypeOrNull())
        }
        HttpFactory.getInstance().APINew(ApiService.BASE_URL).receiveTask(requestBody)
            .compose(observableToMain())
            .subscribe({
                if (it.code == 0) {
                    onCallBack(true, "领取任务成功!")
                } else {
                    onCallBack(false, it.msg)
                }
            }, {
                onCallBack(false, "领取任务失败!")
            })
    }

    /**
     * 任务完成
     */
    fun simpleTaskCompletion(taskId: Int, onCallBack: (isSuccess: Boolean, msg: String) -> Unit) {
        val requestBody = JsonObject().let {
            it.addProperty("taskId", taskId)
            it.addProperty("userId", CacheUtil.getUser()!!.userId)
            it.toString().toRequestBody("application/json".toMediaTypeOrNull())
        }
        HttpFactory.getInstance().APINew(ApiService.BASE_URL).simpleTaskCompletion(requestBody)
            .compose(observableToMain())
            .subscribe({
                if (it.code == 0) {
                    onCallBack(true, "完成任务成功!")
                } else {
                    onCallBack(false, it.msg)
                }
            }, {
                onCallBack(false, "完成任务失败!")
            })
    }

    /**
     * 放弃任务
     */
    fun giveUpGameTask(
        taskId: Int,
        onCallBack: (isSuccess: Boolean, msg: String) -> Unit
    ) {
        val requestBody = JsonObject().let {
            it.addProperty("taskId", taskId)
            it.addProperty("userId", CacheUtil.getUser()!!.userId)
            it.toString().toRequestBody("application/json".toMediaTypeOrNull())
        }
        HttpFactory.getInstance().APINew(ApiService.BASE_URL).giveUpGameTask(requestBody)
            .compose(observableToMain())
            .subscribe({
                if (it.code == 0) {
                    onCallBack(true, "放弃任务成功!")
                } else {
                    onCallBack(false, it.msg)
                }
            }, {
                onCallBack(false, "放弃任务失败!")
            })
    }

    /**
     * 领取奖励
     */
    fun receiveGameTaskRewards(
        taskId: Int,
        userReceiveTaskId: Int,
        onCallBack: (isSuccess: Boolean, msg: String) -> Unit
    ) {
        val requestBody = JsonObject().let {
            it.addProperty("taskId", taskId)
            it.addProperty("userReceiveTaskId", userReceiveTaskId)
            it.addProperty("userId", CacheUtil.getUser()!!.userId)
            it.toString().toRequestBody("application/json".toMediaTypeOrNull())
        }
        HttpFactory.getInstance().APINew(ApiService.BASE_URL).receiveGameTaskRewards(requestBody)
            .compose(observableToMain())
            .subscribe({
                if (it.code == 0) {
                    onCallBack(true, "领取奖励成功!")
                } else {
                    onCallBack(false, it.msg)
                }
            }, {
                onCallBack(false, "领取奖励失败!")
            })
    }

    /**
     * 动态事件列表
     */
    fun eventList(pageNum: Int = 1, pageSize: Int = 10) {
        val userId = CacheUtil.getUser()?.userId
        HttpFactory.getInstance().APINew(ApiService.BASE_URL)
            .eventList(dataNullConvertToInt(userId) as Int, pageNum, pageSize)
            .convert().funSubscribe(true,
                {
                    if (it is NettServerException) {
                        ToastUtil.showCenter("${it.errorMessage}")
                    }
                    LogUtils.iTag(ConfigConstants.CONSTANT.TAG_ALL, "${it.message}")
                }, {
                    eventListLive.postValue(it)
                })
    }

    /**
     * 好友列表
     */
    fun friendList(pageNum: Int = 1, pageSize: Int = 10) {
        val userId = CacheUtil.getUser()?.userId
        HttpFactory.getInstance().APINew(ApiService.BASE_URL)
            .friendList(dataNullConvertToInt(userId) as Int, pageNum, pageSize)
            .convert().funSubscribe(true,
                {
                    if (it is NettServerException) {
                        ToastUtil.showCenter("${it.errorMessage}")
                    }
                    LogUtils.iTag(ConfigConstants.CONSTANT.TAG_ALL, "${it.message}")
                }, {
                    friendListLive.postValue(it)
                })
    }

    /**
     * 领取邀请奖励
     */
    fun receiveInvitationReward(
        invitationId: Int,
        onCallBack: (isSuccess: Boolean, msg: String) -> Unit
    ) {
        val userId = CacheUtil.getUser()?.userId
        val requestBody = JsonObject().let {
            it.addProperty("invitationId", invitationId)
            it.addProperty("userId", CacheUtil.getUser()!!.userId)
            it.toString().toRequestBody("application/json".toMediaTypeOrNull())
        }
        HttpFactory.getInstance().APINew(ApiService.BASE_URL).receiveInvitationReward(requestBody)
            .compose(observableToMain())
            .subscribe({
                if (it.code == 0) {
                    onCallBack(true, "领取奖励成功!")
                } else {
                    onCallBack(false, it.msg)
                }
            }, {
                onCallBack(false, "领取奖励失败!")
            })
    }

    /**
     * 排行榜列表
     */
    fun getRankList(pageNum: Int = 1, pageSize: Int = 10, showLoading: Boolean = false) {
        HttpFactory.getInstance().APINew(ApiService.BASE_URL)
            .getRankList(pageNum, pageSize)
            .convert().funSubscribe(showLoading,
                {
                    if (it is NettServerException) {
                        ToastUtil.showCenter("${it.errorMessage}")
                    }
                    LogUtils.iTag(ConfigConstants.CONSTANT.TAG_ALL, "${it.message}")
                }, {
                    rankListLive.postValue(it)
                })
    }

    /**
     * 获取本人排行
     */
    fun getMyRank() {
        HttpFactory.getInstance().APINew(ApiService.BASE_URL)
            .getMyRank()
            .convert().funSubscribe(false,
                {
                    if (it is NettServerException) {
                        ToastUtil.showCenter("${it.errorMessage}")
                    }
                    LogUtils.iTag(ConfigConstants.CONSTANT.TAG_ALL, "${it.message}")
                }, {
                    myRankLive.postValue(it)
                })
    }

    /**
     * 获取权益中心列表数据
     */
    fun couponRightList(
        pageNum: Int = 1,
        pageSize: Int = 10,
        userId: Long,
        showLoading: Boolean = false
    ) {
        HttpFactory.getInstance().APINew(ApiService.BASE_URL)
            .couponRightList(pageNum, pageSize, userId.toInt())
            .convert().funSubscribe(showLoading,
                {
                    if (it is NettServerException) {
                        ToastUtil.showCenter("${it.errorMessage}")
                    }
                    LogUtils.iTag(ConfigConstants.CONSTANT.TAG_ALL, "${it.message}")
                }, {
                    couponRightListLive.postValue(it)
                })
    }
}