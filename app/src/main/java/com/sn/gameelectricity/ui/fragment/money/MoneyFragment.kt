package com.sn.gameelectricity.ui.fragment.money

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.*
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import androidx.core.util.forEach
import androidx.core.util.size
import androidx.fragment.app.viewModels
import com.gyf.immersionbar.ImmersionBar
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.enums.PopupAnimation
import com.sn.gameelectricity.R
import com.sn.gameelectricity.app.App
import com.sn.gameelectricity.app.base.BaseFragment1
import com.sn.gameelectricity.app.util.CacheUtil
import com.sn.gameelectricity.app.util.CommonUtil
import com.sn.gameelectricity.app.util.SharedpreferencesUtil
import com.sn.gameelectricity.app.util.load
import com.sn.gameelectricity.app.util.pag.PagAnimationTools
import com.sn.gameelectricity.app.weight.customview.CountdownProgressBar
import com.sn.gameelectricity.app.weight.customview.ImageAndTextUi3
import com.sn.gameelectricity.app.weight.dialog.*
import com.sn.gameelectricity.data.model.bean.GameBuildSceneBean
import com.sn.gameelectricity.data.model.bean.GooseEggsFeedBean
import com.sn.gameelectricity.data.model.bean.NewerGuideBean
import com.sn.gameelectricity.data.model.bean.PayloadGameTaskVO
import com.sn.gameelectricity.databinding.FragmentMoneyBinding
import com.sn.gameelectricity.ui.activity.LeaderboardActivity
import com.sn.gameelectricity.ui.activity.ShowPhoneActivity
import com.sn.gameelectricity.viewmodel.state.MainViewModel
import com.sn.gameelectricity.viewmodel.state.MeViewModel
import com.sn.gameelectricity.viewmodel.state.MoneyViewModel
import me.hgj.jetpackmvvm.common.ConfigConstants
import me.hgj.jetpackmvvm.event.UniversalEvent
import me.hgj.jetpackmvvm.util.*
import me.jessyan.autosize.AutoSizeConfig
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.libpag.PAGView
import singleClick
import kotlin.concurrent.thread


/**
 * 描述　: 赚金币
 */
class MoneyFragment : BaseFragment1<MoneyViewModel, FragmentMoneyBinding>() {

    private val mainViewModel: MainViewModel by viewModels()
    private val meViewModel: MeViewModel by viewModels()

    private lateinit var popGoose: BasePopupView


    enum class BuildTaskType {
        Makeups, Leisure, MotherBaby, Digital
    }

    //美妆任务
    private var buildTask2: List<PayloadGameTaskVO>? = null

    //休闲任务
    private var buildTask3: List<PayloadGameTaskVO>? = null

    //母婴任务
    private var buildTask4: List<PayloadGameTaskVO>? = null

    //数码任务
    private var buildTask5: List<PayloadGameTaskVO>? = null

    val rewardCoinsDialog by lazy {
        RewardCoinsDialog(
            requireContext(),
            ScreenTools.getInstance().dp2px(requireContext(), 260f, true),
            ScreenTools.getInstance().dp2px(requireContext(), 368f, true)
        )
    }

    private var moneyTaskDialog: MoneyTaskDialog? = null

    private var manorDialog: ManorDialog? = null

    //建筑信息
    private var gameBuildSceneBean: GameBuildSceneBean? = null

    //新手引导信息
    private var newerGuideBean: NewerGuideBean? = null

    //鹅蛋和饲料信息
    private var grfBean: GooseEggsFeedBean? = null


//    val collectCoinsDialog by lazy {
//        CollectCoinsDialog(
//            requireContext(),
//            ScreenTools.getInstance().dp2px(requireContext(), 375f, true),
//            (0.85 * AutoSizeConfig.getInstance().screenHeight).toInt()
//        )
//    }

    private var dynamicDialog: DynamicDialog? = null

    private var countDownTimer_MotherBaby: CountDownTimer? = null
    private var countDownTimer_Makeups: CountDownTimer? = null
    private var countDownTimer_Digital: CountDownTimer? = null
    private var countDownTimer_Leisure: CountDownTimer? = null
    private var countDownTimerMap = mutableMapOf<BuildTaskType, CountDownTimer?>()

    private var pagview_finger: PAGView? = null

    private var manorDialogthemeResId: Int = -1


    private var gameFriendListDialog: GameFriendListDialog? = null

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this)
            .statusBarColor(R.color.transparent)
            .navigationBarColorInt(Color.WHITE)
            .navigationBarDarkIcon(true)
            .init()

        //注册EventBus
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }

        countDownTimerMap[BuildTaskType.MotherBaby] = countDownTimer_MotherBaby
        countDownTimerMap[BuildTaskType.Makeups] = countDownTimer_Makeups
        countDownTimerMap[BuildTaskType.Digital] = countDownTimer_Digital
        countDownTimerMap[BuildTaskType.Leisure] = countDownTimer_Leisure

        mViewBind.apply {
            pagview_finger = PagAnimationTools.pagTools.getPagView(
                "hand.pag",
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            mViewBind.rlPagFinger.addView(pagview_finger)

            if (!CacheUtil.getUser()?.boolGuide!!) {
                if (CacheUtil.getUser()?.guideStageId == 2) {
                    manorDialogthemeResId = R.style.TransparentDialog3
                    EventBus.getDefault().post(
                        UniversalEvent(
                            UniversalEvent.EVENT_NEWER_GUIDE_STEP2,
                            null
                        )
                    )
                } else if (CacheUtil.getUser()?.guideStageId == 3) {
                    manorDialogthemeResId = R.style.TransparentDialog3
                    EventBus.getDefault().post(
                        UniversalEvent(
                            UniversalEvent.EVENT_NEWER_GUIDE_STEP3,
                            null
                        )
                    )
                }
            }

            PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle2(
                arrayOf(clUserBg, clUserBg2),
                -1,
                "",
                ScreenTools.getInstance().dp2px(requireContext(), 26f, true).toFloat(),
                ScreenTools.getInstance().dp2px(requireContext(), 26f, true).toFloat(),
                ScreenTools.getInstance().dp2px(requireContext(), 26f, true).toFloat(),
                ScreenTools.getInstance().dp2px(requireContext(), 26f, true).toFloat(),
                null,
                "#E5ECEAFF"
            )

            PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle2(
                arrayOf(tvExchange, tvExchange2),
                -1,
                "",
                ScreenTools.getInstance().dp2px(requireContext(), 26f, true).toFloat(),
                ScreenTools.getInstance().dp2px(requireContext(), 26f, true).toFloat(),
                ScreenTools.getInstance().dp2px(requireContext(), 26f, true).toFloat(),
                ScreenTools.getInstance().dp2px(requireContext(), 26f, true).toFloat(),
                GradientDrawable.Orientation.LEFT_RIGHT,
                "#F19B3F", "#ED7A57"
            )

            ivStroll.singleClick {
                with(SelectionTooltipDialog(requireContext())) {
                    settitle(View.VISIBLE, "敬请期待", "#57493B", 18f)
                    setContent("小主，我们正在努力开发中噢～")
                    setCancel(View.GONE) {}
                    setSure {
                        dismissDialog()
                    }
                    show()
                }
            }

            ivDress.singleClick {
                with(SelectionTooltipDialog(requireContext())) {
                    settitle(View.VISIBLE, "敬请期待", "#57493B", 18f)
                    setContent("小主，我们正在努力开发中噢～")
                    setCancel(View.GONE) {}
                    setSure {
                        dismissDialog()
                    }
                    show()
                }
            }

            ivParadise.singleClick {
                with(SelectionTooltipDialog(requireContext())) {
                    settitle(View.VISIBLE, "敬请期待", "#57493B", 18f)
                    setContent("小主，我们正在努力开发中噢～")
                    setCancel(View.GONE) {}
                    setSure {
                        dismissDialog()
                    }
                    show()
                }
            }

            ivDynamic.singleClick {
                dynamicDialog = DynamicDialog(
                    requireContext(),
                    mViewModel,
                    ScreenTools.getInstance().dp2px(requireContext(), 375f, true),
                    (0.85 * AutoSizeConfig.getInstance().screenHeight).toInt()
                )
                dynamicDialog?.apply {
                    setonClickIvClose {
                        dynamicDialog = null
                    }
                    show()
                }
            }

            ivFriend.singleClick {
                gameFriendListDialog = GameFriendListDialog(
                    requireContext(),
                    mViewModel,
                    WindowManager.LayoutParams.MATCH_PARENT,
                    (0.85 * AutoSizeConfig.getInstance().screenHeight).toInt()
                )
                gameFriendListDialog?.apply {
                    setonClickIvClose {
                        gameFriendListDialog = null
                    }
                    tvFriendOnclick()
                    tvEnemyOnclick()
                    toShare()
                    show()
                }
            }

//            tvExchange.singleClick {
//                EventBus.getDefault().post(
//                    UniversalEvent(
//                        UniversalEvent.EVENT_JUMP_HOMEFRAGMENT2,
//                        null
//                    )
//                )
//            }

            ituiFeeding.singleClick {
                if (CacheUtil.getUser()?.boolGuide!!) {
                    mViewModel.eggFeedQuantity2 { isSuccess, gooseEggsFeedBean, msg ->
                        if (isSuccess) {
                            grfBean = gooseEggsFeedBean
                            manorDialogthemeResId = R.style.TransparentDialog

                            EventBus.getDefault().post(
                                UniversalEvent(
                                    UniversalEvent.EVENT_MAINACTIVITY_STARTTRANSITIONS_ANIMATION,
                                    null
                                )
                            )
                        } else {
                            ToastUtil.showCenter(msg)
                        }
                    }
                } else {
                    mainViewModel.doUserGuide(CacheUtil.getUser()?.guideStageId!!) { isSuccess, bean, msg ->
                        if (isSuccess && !PublicPracticalMethodFromKT.ppmfKT.isBlank(bean)) {
                            CacheUtil.getUser()?.let {
                                it.guideStageId = bean!!.guideStageId
                                CacheUtil.setUser(it)
                            }
                            newerGuideBean = bean
                            EventBus.getDefault().post(
                                UniversalEvent(
                                    UniversalEvent.EVENT_MAINACTIVITY_STARTTRANSITIONS_ANIMATION,
                                    null
                                )
                            )
                        }
                    }
                }
            }

            ivGoose.singleClick {
                if (CacheUtil.getUser()?.boolGuide!!) {
                    mViewModel.eggFeedQuantity2 { isSuccess, gooseEggsFeedBean, msg ->
                        if (isSuccess) {
                            grfBean = gooseEggsFeedBean
                            manorDialogthemeResId = R.style.TransparentDialog

                            EventBus.getDefault().post(
                                UniversalEvent(
                                    UniversalEvent.EVENT_MAINACTIVITY_STARTTRANSITIONS_ANIMATION,
                                    null
                                )
                            )
                        } else {
                            ToastUtil.showCenter(msg)
                        }
                    }
                } else {

                }
            }

            clLeaderboard.singleClick {
//                ToastUtil.showCenter("跳转到排行榜页面")
                PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                    requireActivity(),
                    LeaderboardActivity::class.java, null, false
                )
            }
        }
        mViewModel.setTopViewHeight(requireActivity(), mViewBind)
    }

    override fun initData() {
        super.initData()
//        test()
//        test2()
        dataEcho()
        mViewModel.getOfflineRewards { awardCoinNum, awayMiliSeconds ->
            with(SelectionTooltipDialog(requireContext())) {
                settitle(View.VISIBLE, "离线奖励", "#57493B", 18f)
//                setContent("在您离线期间(${mViewModel.showOfflineBonusTime(awayMiliSeconds / 1000L)})，小鹅获得金币")
                setContent("在您离线期间，小鹅获得金币")
                setCancel(View.GONE) {}
                setRewardedIcon(R.drawable.ge_money_placeholder, true)
                setTvPrizeSnumber("x $awardCoinNum", true)
                setSure {
                    dismissDialog()
                }
                show()
            }
        }
    }

    /**
     * 数据回显
     */
    private fun dataEcho() {
        mViewBind.apply {
            mViewModel.apply {
                gameCurrentGameBuildSceneLive.observe(viewLifecycleOwner) { bean ->
                    gameBuildSceneBean = bean
                    mViewModel.setCurrentGameBuildScene(bean, mViewBind)
                }

                eggFeedQuantityLive.observe(viewLifecycleOwner) { bean ->
                    grfBean = bean
                    manorDialogthemeResId = R.style.TransparentDialog

                    var isituiFeedingshow = false
                    var icon = R.drawable.ge_money_pots
                    var content = ""
                    var iconwidth = 0
                    var iconheight = 0
                    if (dataNullConvertToInt(bean.eggNum) as Int > 0) {
                        isituiFeedingshow = true
                        icon = R.drawable.ge_manor_gooseegg
                        content = "可收取"
                        iconwidth = ScreenTools.getInstance().dp2px(App.instance, 30f, true)
                        iconheight = ScreenTools.getInstance().dp2px(App.instance, 30f, true)
                    } else {
                        if (dataNullConvertToInt(bean.feedPoolCapacityFlag) as Int == 1) {
                            isituiFeedingshow = true
                            icon = R.drawable.ge_money_pots
                            content = "去喂食"
                        }
                        iconwidth = ScreenTools.getInstance().dp2px(App.instance, 36f, true)
                        iconheight = ScreenTools.getInstance().dp2px(App.instance, 16f, true)
                    }

                    ituiFeeding.apply {
                        visibility = if (isituiFeedingshow) View.VISIBLE else View.GONE
                        setTaskStatusFromRes(
                            icon,
                            iconwidth,
                            iconheight
                        )
                        setTextData(content, true)
                        startAnimation()
                    }
                }

                todayBuildTaskListLive.observe(viewLifecycleOwner) { beans ->
                    buildTask2 = null
                    buildTask3 = null
                    buildTask4 = null
                    buildTask5 = null
                    beans.forEach { bean ->
                        when (bean.buildType) {
                            2 -> buildTask2 = bean.payloadGameTaskListVO
                            3 -> buildTask3 = bean.payloadGameTaskListVO
                            4 -> buildTask4 = bean.payloadGameTaskListVO
                            5 -> buildTask5 = bean.payloadGameTaskListVO
                        }
                    }
//                    initTodayTask(buildTask2, buildTask3, buildTask4, buildTask5)
                    //美妆
                    initTodayTask2(
                        buildTask2,
                        mViewBind.ivMakeups,
                        mViewBind.pbMakeups,
                        mViewBind.ituiMakeups,
                        BuildTaskType.Makeups
                    )
                    //休闲
                    initTodayTask2(
                        buildTask3,
                        mViewBind.ivLeisure,
                        mViewBind.pbLeisure,
                        mViewBind.ituiLeisure,
                        BuildTaskType.Leisure
                    )
                    //母婴
                    initTodayTask2(
                        buildTask4,
                        mViewBind.ivMotherBaby,
                        mViewBind.pbMotherBaby,
                        mViewBind.ituiMotherBaby,
                        BuildTaskType.MotherBaby
                    )
                    //数码
                    initTodayTask2(
                        buildTask5,
                        mViewBind.ivDigital,
                        mViewBind.pbDigital,
                        mViewBind.ituiDigital,
                        BuildTaskType.Digital
                    )
                }
            }

            App.appViewModelInstance.userInfoLive.observe(viewLifecycleOwner) { beans ->
                sivAvatar.load(
                    beans.avatarUrl,
                    R.drawable.default_user_avatar,
                    R.drawable.default_user_avatar
                )
                tvUsernickname.text = beans.nickName
                tvMyGoldNum.text =
                    App.appViewModelInstance.goldCoinConversion(beans.goldCoin)
            }
        }
    }


    /**
     * 启动动画
     */
    fun startfingerAnimation() {
        mViewBind.rlPagFinger.visibility = View.VISIBLE
        pagview_finger?.let {
            it.setRepeatCount(0)
            if (!it.isPlaying) {
                it.play()
            }
        }
    }

    fun stopfingerAnimation() {
        mViewBind.rlPagFinger.visibility = View.GONE
        pagview_finger?.let {
            if (it.isPlaying) {
                it.stop()
                it.freeCache()
            }
        }
    }

    fun initTodayTask2(
        buildTask: List<PayloadGameTaskVO>?,
        iv: ImageView,
        pb: CountdownProgressBar,
        itui: ImageAndTextUi3,
        buildTaskType: BuildTaskType
    ) {
        iv.apply {
            var taskNumber = SharedpreferencesUtil.getInt(
                App.instance,
                when (buildTaskType) {
                    BuildTaskType.Makeups -> {
                        ConfigConstants.CONSTANT.MAKEUPS_TASKNUMBER
                    }
                    BuildTaskType.Leisure -> {
                        ConfigConstants.CONSTANT.LEISURE_TASKNUMBER
                    }
                    BuildTaskType.MotherBaby -> {
                        ConfigConstants.CONSTANT.MOTHERBABY_TASKNUMBER
                    }
                    BuildTaskType.Digital -> {
                        ConfigConstants.CONSTANT.DIGITAL_TASKNUMBER
                    }
                },
                0
            )
            buildTask?.let {
                //是否还有剩余任务
                var isRemainingTasks = false
                buildTask.forEach { bean ->
                    if (dataNullConvertToInt(bean.boolReceiveReward) as Int == -99
                        || dataNullConvertToInt(bean.boolReceiveReward) as Int == -1
                    ) {
                        isRemainingTasks = true
                    }
                }
                if (isRemainingTasks) {
                    singleClick {
                        if (PublicPracticalMethodFromKT.ppmfKT.isBlank(buildTask)) {
                            showAllTasksFinish()
                        } else {
                            moneyTaskDialog = MoneyTaskDialog(
                                requireContext(),
                                mViewModel,
                                ScreenTools.getInstance().dp2px(requireContext(), 270f, true),
                                WindowManager.LayoutParams.WRAP_CONTENT,
                            )
                            moneyTaskDialog?.apply {
                                setData(
                                    buildTask!!,
                                    buildTaskType,
                                    taskNumber,
                                    gameBuildSceneBean
                                )
                                onClickIvClose {
                                    moneyTaskDialog = null
                                }
                                show()
                            }
                        }
                    }

                    taskNumber = if (taskNumber > buildTask.size) 0 else taskNumber
                    var bean = it[taskNumber]

                    if (dataNullConvertToInt(bean.taskType) as Int == 3 &&
                        dataNullConvertToInt(bean.completeStatus) as Int == 0
                    ) {//打工类，进行中
                        startTimingTask(
                            dataNullConvertToLong(bean.receiveTime) as Long,
                            dataNullConvertToLong(bean.taskEndTime) as Long,
                            pb,
                            buildTaskType,
                            countDownTimerMap
                        ) {
                            mViewModel.todayBuildTaskList()
                        }

                        pb.singleClick {
                            moneyTaskDialog = MoneyTaskDialog(
                                requireContext(),
                                mViewModel,
                                ScreenTools.getInstance().dp2px(requireContext(), 270f, true),
                                WindowManager.LayoutParams.WRAP_CONTENT,
                            )
                            moneyTaskDialog?.apply {
                                setData(
                                    buildTask!!,
                                    buildTaskType,
                                    taskNumber,
                                    gameBuildSceneBean
                                )
                                onClickIvClose {
                                    moneyTaskDialog = null
                                }
                                show()
                            }
                        }
                        iv.singleClick {
                            EventBus.getDefault().post(
                                UniversalEvent(
                                    UniversalEvent.event_mainactivity_start_cloud_working_animation,
                                    null
                                )
                            )
                        }
                        itui.visibility = View.GONE
                        return
                    } else if (dataNullConvertToInt(bean.boolReceiveReward) as Int == 1) {
                        taskNumber = if (taskNumber + 1 < buildTask.size) taskNumber + 1 else 0
                        bean = it[taskNumber]
                        SharedpreferencesUtil.putInt(
                            App.instance,
                            when (buildTaskType) {
                                BuildTaskType.Makeups -> {
                                    ConfigConstants.CONSTANT.MAKEUPS_TASKNUMBER
                                }
                                BuildTaskType.Leisure -> {
                                    ConfigConstants.CONSTANT.LEISURE_TASKNUMBER
                                }
                                BuildTaskType.MotherBaby -> {
                                    ConfigConstants.CONSTANT.MOTHERBABY_TASKNUMBER
                                }
                                BuildTaskType.Digital -> {
                                    ConfigConstants.CONSTANT.DIGITAL_TASKNUMBER
                                }
                            },
                            if (taskNumber < buildTask.size) taskNumber else 0
                        )
                    } else {

                    }
                    pb.visibility = View.GONE
                    itui.apply {
                        visibility = View.VISIBLE
                        var taskStatusFromRes =
                            if (dataNullConvertToInt(bean.completeStatus) as Int == 3) {
                                if (bean.requestRewardInfoVOList.size > 1) {
                                    R.drawable.ge_money_food_gold
                                } else {
                                    if (bean.requestRewardInfoVOList[0].rewardType == 1) {
                                        R.drawable.ge_me_gold
                                    } else {
                                        R.drawable.ge_money_food
                                    }
                                }
                            } else {
                                R.drawable.ge_money_notebook
                            }

                        setTaskStatusFromRes(
                            taskStatusFromRes,
                            ScreenTools.getInstance().dp2px(App.instance, 34f, true),
                            ScreenTools.getInstance().dp2px(App.instance, 34f, true)
                        )
                        setTextData(
                            "${bean.requestRewardInfoVOList[0].rewardNumber}",
                            !(taskStatusFromRes == R.drawable.ge_money_notebook
                                    || taskStatusFromRes == R.drawable.ge_money_food_gold)
                        )
                        startAnimation()
                        startMoveAnimation(this, -15f)

                        when (buildTaskType) {
                            BuildTaskType.Makeups -> {
                                mViewModel.setTopViewHeight2(
                                    mViewBind.clMakeups,
                                    R.id.itui_makeups,
                                    ScreenTools.getInstance().dp2px(App.instance, 10f, true)
                                )
                            }
                            BuildTaskType.Leisure -> {
                                mViewModel.setTopViewHeight2(
                                    mViewBind.clLeisure,
                                    R.id.itui_leisure,
                                    ScreenTools.getInstance().dp2px(App.instance, 5f, true)
                                )
                            }
                        }

                        singleClick {
                            if (dataNullConvertToInt(bean.completeStatus) as Int == 3) {
                                mViewModel.receiveGameTaskRewards(
                                    dataNullConvertToInt(bean.id) as Int,
                                    dataNullConvertToInt(bean.userReceiveId) as Int
                                ) { isSuccess, msg ->
                                    if (isSuccess) {
                                        if (bean.requestRewardInfoVOList.size == 1 && bean.requestRewardInfoVOList[0].rewardType == 1) {
                                            mViewBind.ituiGoldCoinRecycle.apply {
                                                setTaskStatusFromRes(
                                                    R.drawable.ge_me_gold,
                                                    ScreenTools.getInstance()
                                                        .dp2px(App.instance, 34f, true),
                                                    ScreenTools.getInstance()
                                                        .dp2px(App.instance, 34f, true)
                                                )
                                                setTextData("", false)
                                            }

                                            val location = IntArray(2)
                                            this.getLocationInWindow(location)
                                            val start_x = location[0]
                                            val start_y = location[1]
                                            val location2 = IntArray(2)
                                            getLocationInWindow(location2)
                                            mViewBind.ivMyGold.getLocationInWindow(location2)
                                            var end_x = location2[0] - ScreenTools.getInstance()
                                                .dp2px(App.instance, 20f, true)
                                            var end_y = location2[1] - ScreenTools.getInstance()
                                                .dp2px(App.instance, 20f, true)

                                            App.appViewModelInstance.startTrajectoryAnimation(
                                                mViewBind.ituiGoldCoinRecycle,
                                                start_x.toFloat(),
                                                start_y.toFloat(),
                                                end_x.toFloat(),
                                                end_y.toFloat()
                                            ) { animationState ->
                                                if (animationState == "doOnStart") {
                                                    this.visibility = View.GONE
                                                } else if (animationState == "doOnEnd") {
                                                }
                                            }
                                        } else if (bean.requestRewardInfoVOList.size == 1 && bean.requestRewardInfoVOList[0].rewardType == 3) {
                                            ToastUtil.showReceiveInvitationReward(
                                                R.drawable.ge_money_food,
                                                dataNullConvertToInt(bean.requestRewardInfoVOList[0].rewardNumber) as Int
                                            )
                                        } else {
                                            ToastUtil.showCollectCoinsFeedRewardsToast(
                                                R.drawable.ge_me_gold,
                                                R.drawable.ge_money_food,
                                                dataNullConvertToInt(bean.requestRewardInfoVOList[0].rewardNumber) as Int,
                                                dataNullConvertToInt(bean.requestRewardInfoVOList[1].rewardNumber) as Int,
                                                Gravity.CENTER
                                            )
                                        }
                                        SharedpreferencesUtil.putInt(
                                            App.instance,
                                            when (buildTaskType) {
                                                BuildTaskType.Makeups -> {
                                                    ConfigConstants.CONSTANT.MAKEUPS_TASKNUMBER
                                                }
                                                BuildTaskType.Leisure -> {
                                                    ConfigConstants.CONSTANT.LEISURE_TASKNUMBER
                                                }
                                                BuildTaskType.MotherBaby -> {
                                                    ConfigConstants.CONSTANT.MOTHERBABY_TASKNUMBER
                                                }
                                                BuildTaskType.Digital -> {
                                                    ConfigConstants.CONSTANT.DIGITAL_TASKNUMBER
                                                }
                                            },
                                            if (taskNumber + 1 < buildTask.size) taskNumber + 1 else 0
                                        )

                                        App.appViewModelInstance.getuserInfo()
                                        mViewModel.todayBuildTaskList()
                                    } else {
                                        ToastUtil.showCenter(msg)
                                    }
                                }
                            } else {
                                if (PublicPracticalMethodFromKT.ppmfKT.isBlank(buildTask)) {
                                    showAllTasksFinish()
                                } else {
                                    moneyTaskDialog = MoneyTaskDialog(
                                        requireContext(),
                                        mViewModel,
                                        ScreenTools.getInstance()
                                            .dp2px(requireContext(), 270f, true),
                                        WindowManager.LayoutParams.WRAP_CONTENT,
                                    )
                                    moneyTaskDialog?.apply {
                                        setData(
                                            buildTask!!,
                                            buildTaskType,
                                            taskNumber,
                                            gameBuildSceneBean
                                        )
                                        onClickIvClose {
                                            moneyTaskDialog = null
                                        }
                                        show()
                                    }
                                }
                            }
                        }
                    }

                } else {
                    itui.visibility = View.GONE
                    pb.visibility = View.GONE
                    iv.singleClick {
                        showAllTasksFinish()
                    }
                }
            } ?: let {
                itui.visibility = View.GONE
                pb.visibility = View.GONE
            }
        }
    }

    /**
     * 显示任务全部做完弹框
     */
    fun showAllTasksFinish() {
        with(SelectionTooltipDialog(requireContext())) {
            settitle(View.GONE)
            setContent("今日任务已全部完成，请明日再来，赚取更多金币吧～")
            setCancel(View.GONE) {}
            setSure(View.VISIBLE, "我知道了") {
                dismissDialog()
            }
            show()
        }
    }

    fun startTimingTask(
        startTime: Long,
        endTime: Long,
        pbCountdown: CountdownProgressBar,
        buildTaskType: BuildTaskType,
        _countDownTimerMap: MutableMap<BuildTaskType, CountDownTimer?>,
        onCallBack: () -> Unit
    ) {
        val cur_time = System.currentTimeMillis() / 1000L
        mViewModel.startCountdownTask(
            cur_time,
            endTime
        ) {
            var timerMap = _countDownTimerMap[buildTaskType]
            timerMap?.cancel()
            val hour = it["hour"]!!.times(60).times(60)
            val minute = it["minute"]!!.times(60)
            val second = it["second"]!!
            val countTime = hour + minute + second
            if (countTime > 0) {
                pbCountdown.apply {
                    visibility = View.VISIBLE
                    init(CountdownProgressBar.PageLocation.MoneyTaskDialog)
                    //总时间
                    val totalTime = mViewModel.countDownTotalTime(startTime, endTime)
                    //已经运行的时间
                    val elapsedTime = totalTime - countTime
                    //设置进度条总值
                    setProgressMax(totalTime.toInt())
                    setProgress(elapsedTime.toInt())
                    timerMap = object : CountDownTimer(countTime * 1000L, 1000) {
                        override fun onTick(millisUntilFinished: Long) {
                            val time = millisUntilFinished / 1000L
                            val timeleft = elapsedTime + countTime - time
                            pbCountdown.setProgress(timeleft.toInt())
                            pbCountdown.setTvText(mViewModel.showCountTimeContent(time))
                        }

                        override fun onFinish() {
                            val time = 0L
                            timerMap?.cancel()
                            pbCountdown.setProgress(countTime.toInt())
                            pbCountdown.setTvText(mViewModel.showCountTimeContent(time))
                            visibility = View.GONE
                            onCallBack()
                        }
                    }
                    timerMap?.start()
                    countDownTimerMap[buildTaskType] = timerMap!!
                }
            } else {
                pbCountdown.visibility = View.GONE
                timerMap?.cancel()
                timerMap = null
            }
        }
    }


    /**
     * 任务测试1
     */
    private fun test() {
        mViewBind.apply {
            ituiMotherBaby.apply {
                visibility = View.VISIBLE
                setTaskStatusFromRes(
                    R.drawable.ge_money_food,
                    ScreenTools.getInstance().dp2px(App.instance, 34f, true),
                    ScreenTools.getInstance().dp2px(App.instance, 34f, true)

                )
                setTextData("777g")
                startAnimation()
            }
            ituiMakeups.apply {
                visibility = View.VISIBLE
                setTaskStatusFromRes(
                    R.drawable.ge_money_notebook,
                    ScreenTools.getInstance().dp2px(App.instance, 34f, true),
                    ScreenTools.getInstance().dp2px(App.instance, 34f, true)
                )
                setTextData("", false)
                startAnimation()
                mViewModel.setTopViewHeight2(
                    clMakeups,
                    R.id.itui_makeups,
                    ScreenTools.getInstance().dp2px(App.instance, 10f, true)
                )
            }
            ituiDigital.apply {
                visibility = View.VISIBLE
                setTaskStatusFromRes(
                    R.drawable.ge_me_gold,
                    ScreenTools.getInstance().dp2px(App.instance, 34f, true),
                    ScreenTools.getInstance().dp2px(App.instance, 34f, true)
                )
                setTextData("7777", true)
                startAnimation()
//                mViewModel.setTopViewHeight2(
//                    clDigital,
//                    R.id.itui_digital,
//                    ScreenTools.getInstance().dp2px(App.instance, 5f, true)
//                )
            }
            ituiLeisure.apply {
                visibility = View.GONE
                setTaskStatusFromRes(
                    R.drawable.ge_money_notebook,
                    ScreenTools.getInstance().dp2px(App.instance, 34f, true),
                    ScreenTools.getInstance().dp2px(App.instance, 34f, true)
                )
                setTextData("8888", false)
                mViewModel.setTopViewHeight2(
                    clLeisure,
                    R.id.itui_leisure,
                    ScreenTools.getInstance().dp2px(App.instance, 5f, true)
                )
            }
        }
    }

    /**
     * 任务测试2
     */
    private fun test2() {
        mViewBind.apply {
            mViewModel.startCountdownTask(
                ConfigConstants.VARIABLE.cur_time,
                ConfigConstants.VARIABLE.end_time
            ) {
                val hour = it["hour"]!!.times(60).times(60)
                val minute = it["minute"]!!.times(60)
                val second = it["second"]!!
                val countTime = hour + minute + second
                pbMotherBaby.apply {
                    visibility = View.GONE
                    init(CountdownProgressBar.PageLocation.MoneyFragment)
                    setProgressMax(countTime.toInt())
                    countDownTimer_MotherBaby =
                        object : CountDownTimer(countTime * 1000L, 1000) {
                            override fun onTick(millisUntilFinished: Long) {
                                val time = millisUntilFinished / 1000L
                                val timeleft = countTime - time
                                setProgress(timeleft.toInt())
                                setTvText(mViewModel.showCountTimeContent(time))
                            }

                            override fun onFinish() {
                                val time = 0L
                                setProgress(countTime.toInt())
                                setTvText(mViewModel.showCountTimeContent(time))
                                countDownTimer_MotherBaby?.cancel()
                            }
                        }
                    countDownTimer_MotherBaby?.start()
                }
            }

            mViewModel.startCountdownTask(
                ConfigConstants.VARIABLE.cur_time,
                ConfigConstants.VARIABLE.end_time
            ) {
                val hour = it["hour"]!!.times(60).times(60)
                val minute = it["minute"]!!.times(60)
                val second = it["second"]!!
                val countTime = hour + minute + second
                pbMakeups.apply {
                    visibility = View.GONE
                    init(CountdownProgressBar.PageLocation.MoneyFragment)
                    setProgressMax(countTime.toInt())
                    countDownTimer_Makeups = object : CountDownTimer(countTime * 1000L, 1000) {
                        override fun onTick(millisUntilFinished: Long) {
                            val time = millisUntilFinished / 1000L
                            val timeleft = countTime - time
                            setProgress(timeleft.toInt())
                            setTvText(mViewModel.showCountTimeContent(time))
                        }

                        override fun onFinish() {
                            val time = 0L
                            setProgress(countTime.toInt())
                            setTvText(mViewModel.showCountTimeContent(time))
                            countDownTimer_Makeups?.cancel()
                        }
                    }
                    countDownTimer_Makeups?.start()
                }
            }

            mViewModel.startCountdownTask(
                ConfigConstants.VARIABLE.cur_time,
                ConfigConstants.VARIABLE.end_time
            ) {
                val hour = it["hour"]!!.times(60).times(60)
                val minute = it["minute"]!!.times(60)
                val second = it["second"]!!
                val countTime = hour + minute + second
                pbDigital.apply {
                    visibility = View.GONE
                    init(CountdownProgressBar.PageLocation.MoneyFragment)
                    setProgressMax(countTime.toInt())
                    countDownTimer_Digital = object : CountDownTimer(countTime * 1000L, 1000) {
                        override fun onTick(millisUntilFinished: Long) {
                            val time = millisUntilFinished / 1000L
                            val timeleft = countTime - time
                            setProgress(timeleft.toInt())
                            setTvText(mViewModel.showCountTimeContent(time))
                        }

                        override fun onFinish() {
                            val time = 0L
                            setProgress(countTime.toInt())
                            setTvText(mViewModel.showCountTimeContent(time))
                            countDownTimer_Digital?.cancel()
                        }
                    }
                    countDownTimer_Digital?.start()
                }
            }

            mViewModel.startCountdownTask(
                ConfigConstants.VARIABLE.cur_time,
                ConfigConstants.VARIABLE.end_time
            ) {
                val hour = it["hour"]!!.times(60).times(60)
                val minute = it["minute"]!!.times(60)
                val second = it["second"]!!
                val countTime = hour + minute + second
                pbLeisure.apply {
                    visibility = View.VISIBLE
                    init(CountdownProgressBar.PageLocation.MoneyFragment)
                    setProgressMax(countTime.toInt())
                    countDownTimer_Leisure = object : CountDownTimer(countTime * 1000L, 1000) {
                        override fun onTick(millisUntilFinished: Long) {
                            val time = millisUntilFinished / 1000L
                            val timeleft = countTime - time
                            setProgress(timeleft.toInt())
                            setTvText(mViewModel.showCountTimeContent(time))
                        }

                        override fun onFinish() {
                            val time = 0L
                            setProgress(countTime.toInt())
                            setTvText(mViewModel.showCountTimeContent(time))
                            countDownTimer_Leisure?.cancel()
                        }
                    }
                    countDownTimer_Leisure?.start()
                }
            }
        }
    }


    /**
     * 初始化弹框
     */
    private fun initPopGoose() {
        val location = IntArray(2)
        mViewBind.ivGoose.getLocationOnScreen(location)
        val x = location[0]
        val y = location[1]
//        LogUtils.iTag(
//            ConfigConstants.CONSTANT.TAG_ALL,
//            " x =-= $x",
//            " y =-= $y"
//        )

        with(PopGooseFuntion(requireContext())) {
            popGoose = XPopup.Builder(requireContext())
                .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                .offsetY(y - ScreenTools.getInstance().dp2px(requireActivity(), 64f, true))
                .offsetX(x + ScreenTools.getInstance().dp2px(requireActivity(), 30f, true))
                .popupAnimation(PopupAnimation.ScrollAlphaFromLeftBottom)
                .asCustom(this).show()

            feedOnClick {
                //收货金币弹框
//                rewardCoinsDialog.apply {
//                    setCoinsContent("金币 7777")
//                    setContent("可以去兑换为你精心准备的超值礼物啦！")
//                    setSure {
//                        dismissDialog()
//                    }
//                    show()
//                }
            }

            eggCollectionOnclick {
            }
        }
    }


    override fun lazyLoadData() {

    }

    //Event通知
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun handleEvent(event: UniversalEvent) {
        if (event.actionType == UniversalEvent.EVENT_MAINACTIVITY_ENDTRANSITIONS_ANIMATION
        ) {
            manorDialog = ManorDialog(
                requireActivity(),
                mViewModel,
                ScreenTools.getInstance().dp2px(requireContext(), 375f, true),
                WindowManager.LayoutParams.MATCH_PARENT,
                manorDialogthemeResId
            )
            manorDialog?.apply {
                initGooseAnimation(grfBean)
                setGooseEggMaxNum(
                    if (PublicPracticalMethodFromKT.ppmfKT.isBlank(grfBean?.maxEggNum)) 0 else grfBean?.maxEggNum!!
                )
                setGooseNestAndFeedTrough(gameBuildSceneBean, grfBean)

                if (!CacheUtil.getUser()?.boolGuide!! && CacheUtil.getUser()?.guideStageId == 3) {
                    initFingerAnimation("hand.pag")
                    startFingerAnimation()
                    setMaskLayerWhetherDisplay(true)
                    newerGuideBean?.let {
                        setTvNumberAwardsData(it.eggNum)
                    }
                    onClickivGooseNest {
                        mainViewModel.doUserGuide(CacheUtil.getUser()?.guideStageId!!) { isSuccess, bean, msg ->
                            if (isSuccess && !PublicPracticalMethodFromKT.ppmfKT.isBlank(bean)) {

                                CacheUtil.getUser()?.let {
                                    it.guideStageId = bean!!.guideStageId
                                    CacheUtil.setUser(it)
                                }


                                mViewBind.clExternal.visibility = View.VISIBLE
                                mViewBind.clInternal.visibility = View.GONE

                                newerGuideBean = bean
                                ToastUtil.showCollectGooseEggRewardsToast(
                                    R.drawable.ge_manor_gooseegg,
                                    R.drawable.ge_me_gold,
                                    bean!!.eggNum,
                                    bean!!.goldCoinNum,
                                    Gravity.CENTER
                                )
                                CommonUtil.setWindowStatusBarColor(
                                    requireActivity(),
                                    R.color.transparent
                                )
                                setMaskLayerWhetherDisplay(false)
                                stopfingerAnimation()
                                dismissDialog()

                                with(SelectionTooltipDialog(requireContext())) {
                                    settitle(View.VISIBLE, "恭喜获得", "#57493B", 18f)
                                    setContent("快去兑换为你精心准备的超值好物吧～")
                                    setCancel(View.GONE) {}
                                    setRewardedIcon(R.drawable.ge_money_placeholder, true)
                                    newerGuideBean?.let {
                                        setTvPrizeSnumber("x ${it.goldCoinNum.toString()}", true)
                                    }
                                    setSure {
                                        mainViewModel.doUserGuide(4) { isSuccess, bean, msg ->
                                            if (isSuccess) {
                                                CacheUtil.getUser()?.let {
                                                    it.boolGuide = true
                                                    CacheUtil.setUser(it)
                                                }
                                                dismissDialog()
                                                EventBus.getDefault().post(
                                                    UniversalEvent(
                                                        UniversalEvent.EVENT_JUMP_HOMEFRAGMENT,
                                                        null
                                                    )
                                                )
                                            }
                                        }
                                    }
                                    show()
                                }

                                EventBus.getDefault().post(
                                    UniversalEvent(
                                        UniversalEvent.EVENT_MAINACTIVITY_REFRESH_CLMASKLAYER,
                                        null
                                    )
                                )

                            }
                        }
                    }
                    CommonUtil.setWindowStatusBarColor(requireActivity(), R.color.transparent2)
                    mViewBind.clMaskLayer.visibility = View.GONE
                } else {
                    onclickItuiFeed {
                        mViewModel.feed() {
                            changeGooseAnimation(it)
//                            setGooseNestAndFeedTrough(gameBuildSceneBean, it)
                            EventBus.getDefault().post(
                                UniversalEvent(
                                    UniversalEvent.event_feed_refresh,
                                    it
                                )
                            )
                        }
                    }
                    onClickivGooseNest {

                    }
                }
                closeDialog {
                    manorDialog = null
                    if (CacheUtil.getUser()?.guideStageId == 3) {
                        mViewBind.clMaskLayer.visibility = View.VISIBLE
                        CommonUtil.setWindowStatusBarColor(
                            requireActivity(),
                            R.color.transparent
                        )
                    }
                }
                show()
            }
        } else if (event.actionType == UniversalEvent.EVENT_NEWER_GUIDE_STEP2) {
            mViewBind.apply {
                startfingerAnimation()
                clExternal.visibility = View.INVISIBLE
                clInternal.visibility = View.VISIBLE
                clMaskLayer.apply {
                    visibility = View.VISIBLE
                    singleClick {

                    }
                }
                ituiFeeding.apply {
                    visibility = View.VISIBLE

                    setTaskStatusFromRes(
                        R.drawable.ge_manor_gooseegg,
                        ScreenTools.getInstance().dp2px(App.instance, 30f, true),
                        ScreenTools.getInstance().dp2px(App.instance, 30f, true)
                    )
                    setTextData("去领取", true)
                    startAnimation()
                }
            }
        } else if (event.actionType == UniversalEvent.EVENT_NEWER_GUIDE_STEP3) {
            mViewBind.apply {
                startfingerAnimation()
                clExternal.visibility = View.INVISIBLE
                clInternal.visibility = View.VISIBLE
                clMaskLayer.apply {
                    visibility = View.VISIBLE
                    singleClick {

                    }
                }

                ituiFeeding.apply {
                    visibility = View.VISIBLE

                    setTaskStatusFromRes(
                        R.drawable.ge_manor_gooseegg,
                        ScreenTools.getInstance().dp2px(App.instance, 30f, true),
                        ScreenTools.getInstance().dp2px(App.instance, 30f, true)
                    )
                    setTextData("去领取", true)
                    startAnimation()
                }
                mainViewModel.userGuideDetails(CacheUtil.getUser()?.guideStageId!!) { bean ->
                    newerGuideBean = bean
                    EventBus.getDefault().post(
                        UniversalEvent(
                            UniversalEvent.EVENT_MAINACTIVITY_STARTTRANSITIONS_ANIMATION,
                            null
                        )
                    )
                }

            }
        } else if (event.actionType == UniversalEvent.EVENT_MONEYTASKDIALOG_DISSMISS) {
            val bean = event.message as MoneyTaskDialog
            bean?.dismissDialog()
            bean == null
        } else if (event.actionType == UniversalEvent.EVENT_OPEN_DYNAMICDIALOG) {
            gameFriendListDialog = GameFriendListDialog(
                requireContext(),
                mViewModel,
                WindowManager.LayoutParams.MATCH_PARENT,
                (0.85 * AutoSizeConfig.getInstance().screenHeight).toInt()
            )
            gameFriendListDialog?.apply {
                setonClickIvClose {
                    gameFriendListDialog = null
                }
                toShare()
                show()
            }
        } else if (event.actionType == UniversalEvent.event_feed_refresh) {
            event.message.isNotNull({
                val bean = it as GooseEggsFeedBean
                manorDialog?.setGooseNestAndFeedTrough(gameBuildSceneBean, bean)
            })
        }
    }

    override fun onResume() {
        super.onResume()
        App.appViewModelInstance.getuserInfo()
        mViewModel.gameCurrentGameBuildScene()

        if (CacheUtil.getUser()?.boolGuide!!) {
            mViewModel.eggFeedQuantity()
            mViewModel.todayBuildTaskList()
        }
        mViewBind.sivBellwether.load(
            SharedpreferencesUtil.getString(
                requireContext(),
                ConfigConstants.VARIABLE.leaderboard_first_avatar,
                ""
            ),
            R.drawable.ge_leaderboard_default_avatar, R.drawable.ge_leaderboard_default_avatar
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }
}