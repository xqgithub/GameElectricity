package com.sn.gameelectricity.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import com.azhon.appupdate.manager.DownloadManager
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.sn.gameelectricity.R
import com.sn.gameelectricity.app.base.BaseActivity1
import com.sn.gameelectricity.app.eventViewModel
import com.sn.gameelectricity.app.ext.topicListResponse
import com.sn.gameelectricity.app.util.CacheUtil
import com.sn.gameelectricity.app.util.ClipboardUtil
import com.sn.gameelectricity.app.util.pag.PagAnimationTools
import com.sn.gameelectricity.app.weight.customview.MainNavigationLayout
import com.sn.gameelectricity.app.weight.dialog.*
import com.sn.gameelectricity.app.weight.prioritywindow.WindowTaskManager
import com.sn.gameelectricity.app.weight.prioritywindow.WindowType
import com.sn.gameelectricity.app.weight.prioritywindow.WindowWrapper
import com.sn.gameelectricity.data.model.bean.NewbiePoliteBean
import com.sn.gameelectricity.databinding.ActivityMainBinding
import com.sn.gameelectricity.ui.activity.login.LoginActivity
import com.sn.gameelectricity.ui.fragment.home.HomeFragment
import com.sn.gameelectricity.ui.fragment.me.MeFragment
import com.sn.gameelectricity.ui.fragment.money.MoneyFragment
import com.sn.gameelectricity.viewmodel.request.RequestCheatingViewModel
import com.sn.gameelectricity.viewmodel.request.RequestMainViewModel
import com.sn.gameelectricity.viewmodel.state.MainViewModel
import com.sn.gameelectricity.viewmodel.state.MeViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_cheating_other.*
import me.hgj.jetpackmvvm.common.ConfigConstants
import me.hgj.jetpackmvvm.event.UniversalEvent
import me.hgj.jetpackmvvm.ext.util.toJson
import me.hgj.jetpackmvvm.network.manager.NetState
import me.hgj.jetpackmvvm.util.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.json.JSONObject
import org.libpag.PAGScaleMode
import org.libpag.PAGView
import singleClick
import java.util.concurrent.TimeUnit


/**
 * 项目主页Activity
 */
class MainActivity : BaseActivity1<MainViewModel, ActivityMainBinding>(),
    MainNavigationLayout.OnNavigationItemSelectedListener {

    private var appUpdateDialogBuild: WindowWrapper? = null
    private var loginRewardDialogBuild: WindowWrapper? = null
    private var cheatingOtherDialogBuild: WindowWrapper? = null
    private var newbiePoliteDialogBuild: WindowWrapper? = null

    private var groupCode: String = ""
    private var userCode: String = ""

    /**
     * 登录奖励弹窗的优先级
     */
    val LOGINREWARD_PRIORITY = 2

    /**
     * 更新弹窗的优先级
     */
    val UPDATE_PRIORITY = 4

    /**
     * 新人有礼弹窗优先级
     */
    val NEWBIEPOLITE_PRIORITY = 3


    private val fragments = mutableListOf<Fragment>()
    private var mCurrentItem = 0

    private val requestMainViewModel: RequestMainViewModel by viewModels()
    private val requestCheatingViewModel: RequestCheatingViewModel by viewModels()
    private val meViewModel: MeViewModel by viewModels()

    private var manager: DownloadManager? = null

    val loginRewardDialog by lazy {
        LoginRewardDialog(this)
    }

    val appUpdateDialog by lazy {
        AppUpdateDialog(this)
    }

    val cheatingOtherDialog by lazy {
        CheatingOtherDialog(this)
    }

    private val newbiePoliteDialog by lazy {
        NewbiePoliteDialog(
            this,
            ScreenTools.getInstance().dp2px(this, 240f, true),
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    private var pagview_cloud: PAGView? = null
    private var pagview_working: PAGView? = null


    override fun initView(savedInstanceState: Bundle?) {
        //注册EventBus
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }

        if (!CacheUtil.isLogin()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        navigationLayout.setOnNavigationItemSelectedListener(this)
        fragments.add(HomeFragment())
        fragments.add(MoneyFragment())
        fragments.add(MeFragment())
        setCurrentItem(mCurrentItem)

        initDialog()
        initCloudAnimation()
        initWorkingAnimation()

        mViewBind.clPag.singleClick {
            stopWorkingAnimation()
            stopCloudAnimation()
            mViewBind.clPag.visibility = View.GONE
        }
    }


    private fun setCurrentItem(position: Int) {
        val transaction = supportFragmentManager.beginTransaction()
        val currentFragment = fragments[mCurrentItem]
        val targetFragment = fragments[position]
        if (currentFragment != targetFragment) {
            if (!targetFragment.isAdded) {
                transaction.hide(currentFragment)
                    .add(R.id.fragmentContainer, targetFragment).show(targetFragment)
            } else {
                transaction.hide(currentFragment)
                    .show(targetFragment)
            }
            transaction.setMaxLifecycle(currentFragment, Lifecycle.State.STARTED)
            transaction.setMaxLifecycle(targetFragment, Lifecycle.State.RESUMED)
        } else {
            if (!targetFragment.isAdded) {
                transaction.add(R.id.fragmentContainer, targetFragment).show(targetFragment)
                transaction.setMaxLifecycle(targetFragment, Lifecycle.State.RESUMED)
            }
        }
        if (mCurrentItem != position || position == 0)
            transaction.commit()
        mCurrentItem = position
    }

    override fun onNavigationItemSelected(position: Int) {
        if (position == 0) {
            setCurrentItem(0)
        } else if (position == 1) {
            setCurrentItem(1)
        } else if (position == 2) {
            setCurrentItem(2)
        }
    }

    override fun createObserver() {
        mViewModel.heart()

//        requestMainViewModel.getVersion(AppUtils.getAppVersionName())
        requestMainViewModel.versionLiveData.observe(this, {
            LogUtils.e("321", it.toJson())
            if (it.isNeedUpdate) {
                WindowTaskManager.getInstance()
                    .showWindow(this@MainActivity, supportFragmentManager, appUpdateDialogBuild)
                appUpdateDialog.setCancelVisibility(it.updateType)
                appUpdateDialog.setTextVersion(it.version)
                it.content?.let { it1 ->
                    appUpdateDialog.setTextContent(it1 as String)
                }
                appUpdateDialog.onClickSure {
                    manager = DownloadManager.Builder(this).run {
                        apkUrl(it.downloadUrl)
                        apkName("app-release.apk")
                        smallIcon(R.mipmap.ic_launcher)
                        build()
                    }
                    manager!!.download()
                }
            }
        })

        showNewbiePoliteDialog()
    }


    private fun nextDialog() {
        requestMainViewModel.isNeedCheckin()
        requestMainViewModel.isNeedCheckinLiveData.observe(this, {
            LogUtils.e("321", it.toJson())
            if (it) {
                requestMainViewModel.checkin()
            }
        })

        requestMainViewModel.checkinLiveData.observe(this, {
            LogUtils.e("321", it.toJson())
            WindowTaskManager.getInstance()
                .showWindow(this@MainActivity, supportFragmentManager, loginRewardDialogBuild)
            loginRewardDialog.setTextContent("x " + it.rewardNum)

        })

        requestCheatingViewModel.getAssistLiveData.observe(this, {
            WindowTaskManager.getInstance()
                .showWindow(this@MainActivity, supportFragmentManager, cheatingOtherDialogBuild)
            cheatingOtherDialog.loadImg(it.goodsIcon)
            cheatingOtherDialog.setTextContent(it.ownerName)
            cheatingOtherDialog.loadImgUserAvatar(it.ownerIcon)
            if (it.isJoinFlag) {
                cheatingOtherDialog.setTextContentTwo("今日为他助力过啦")
                cheatingOtherDialog.setTextContentThree("每位好友每日只能助力一次哦～")
//                cheatingOtherDialog.btnSure.isEnabled = false
                cheatingOtherDialog.btnSure.setBackgroundResource(R.drawable.shape_radius_f19b3f_7)
            } else {
                cheatingOtherDialog.setTextContentTwo("帮我点一点")
                cheatingOtherDialog.setTextContentThree("助我一臂之力吧！")
//                cheatingOtherDialog.btnSure.isEnabled = true
                cheatingOtherDialog.btnSure.setBackgroundResource(R.drawable.shape_radius_f19b3f)
            }

            cheatingOtherDialog.onClickSure { //帮Ta助力
                ClipboardUtil.clearFirstClipboard(this)
                if (it.isJoinFlag) {
                    ToastUtils.showShort("今日已为该用户助力过啦")
                    cheatingOtherDialog.dismissDialog()
                    return@onClickSure
                }
                requestCheatingViewModel.putAssist(it.groupCode, it.goodsId) {
                    val cheatingSuccDialog = CheatingSuccDialog(this)
                    cheatingSuccDialog.onClickSure {
                        with(Bundle()) {
                            putInt("goodsId", it.goodsId)
                            PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                                this@MainActivity,
                                ProductDetailsActivity::class.java, this, false
                            )
                        }
                        cheatingSuccDialog.dismissDialog()
                    }
                    cheatingSuccDialog.show()
                }
            }

            cheatingOtherDialog.onClickCancel {
                ClipboardUtil.clearFirstClipboard(this)
            }
        })
    }

    private fun initDialog() {
        appUpdateDialogBuild = WindowWrapper.Builder()
            .priority(UPDATE_PRIORITY)
            .windowType(WindowType.DIALOG)
            .window(appUpdateDialog)
            .setWindowName(appUpdateDialog.className)
            .setCanShow(true)
            .build()

        loginRewardDialogBuild = WindowWrapper.Builder()
            .priority(LOGINREWARD_PRIORITY)
            .windowType(WindowType.DIALOG)
            .window(loginRewardDialog)
            .setWindowName(loginRewardDialog.className)
            .setCanShow(true)
            .build()

        cheatingOtherDialogBuild = WindowWrapper.Builder()
            .priority(LOGINREWARD_PRIORITY)
            .windowType(WindowType.DIALOG)
            .window(cheatingOtherDialog)
            .setWindowName(cheatingOtherDialog.className)
            .setCanShow(true)
            .build()


        newbiePoliteDialogBuild = WindowWrapper.Builder()
            .priority(NEWBIEPOLITE_PRIORITY)
            .windowType(WindowType.DIALOG)
            .window(newbiePoliteDialog)
            .setWindowName(newbiePoliteDialog.className)
            .setCanShow(true)
            .build()

    }

    /**
     * 启动新人有礼弹框
     */
    private fun showNewbiePoliteDialog() {
        CacheUtil.getUser().isNotNull({
            LogUtils.e("123", CacheUtil.getUser()?.boolNewUserActivityExchange!!)
            if (!CacheUtil.getUser()?.boolNewUserActivityExchange!!) {
                mViewModel.newcomerCeremony()
                mViewModel.newcomerCeremonyLive.observe(this) { bean ->
                    LogUtils.e("123", CacheUtil.getUser()?.boolGuide!!)
                    if (!CacheUtil.getUser()?.boolGuide!!) {
                        LogUtils.e("123", CacheUtil.getUser()?.guideStageId)
                        if (CacheUtil.getUser()?.guideStageId == 1) {
                            EventBus.getDefault().post(
                                UniversalEvent(
                                    UniversalEvent.EVENT_NEWER_GUIDE_STEP1,
                                    bean
                                )
                            )
                        } else if (CacheUtil.getUser()?.guideStageId == 2 || CacheUtil.getUser()?.guideStageId == 3) {
                            mViewBind.navigationLayout.setClMaskLayerWhetherToDisplay(true)
                            mViewBind.navigationLayout.controlTabJump(1)
                        }
                    } else {
                        WindowTaskManager.getInstance()
                            .showWindow(
                                this@MainActivity,
                                supportFragmentManager,
                                newbiePoliteDialogBuild
                            )

                        newbiePoliteDialog.apply {
                            setIvCloseWhetherToDisplay(true)
                            setData(bean)
                            onclickTvExchange {
                                dismissDialog()
                                mViewModel.exchange { newbieBean ->
                                    CacheUtil.getUser()?.let {
                                        it.boolNewUserActivityExchange = true
                                        CacheUtil.setUser(it)
                                    }
                                    with(
                                        RewardCommodityDialog(
                                            this@MainActivity,
                                            ScreenTools.getInstance().dp2px(mContext, 260f, true),
                                            WindowManager.LayoutParams.WRAP_CONTENT
                                        )
                                    ) {

                                        setProductData(bean)
                                        setCancel(View.VISIBLE, "回收", "#EF874E", 14f) {
                                            with(Bundle()) {
                                                putInt("orderId", newbieBean.orderId)
                                                PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                                                    this@MainActivity,
                                                    RecoverGoodActivity::class.java,
                                                    this,
                                                    false
                                                )
                                            }
                                            dismissDialog()
                                        }
                                        setSure(View.VISIBLE, "提货", "#FFFFFF", 14f) {
                                            with(Bundle()) {
                                                putInt("orderId", newbieBean.orderId)
                                                PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                                                    this@MainActivity,
                                                    PickGoodsDetailsActivity::class.java,
                                                    this,
                                                    false
                                                )
                                            }
                                            dismissDialog()
                                        }
                                        show()
                                    }
                                }
                            }

                            onClickClose {
                                dismissDialog()
                                nextDialog()
                            }
                        }
                    }
                }
            } else {
                nextDialog()
            }
        })

    }

    /**
     * 初始化云动画
     */
    private fun initCloudAnimation() {
        pagview_cloud = PagAnimationTools.pagTools.getPagView(
            "cloud.pag",
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        mViewBind.rlPagCloud.apply {
            addView(pagview_cloud)
        }
        pagview_cloud?.setScaleMode(PAGScaleMode.Stretch)
        pagview_cloud?.setRepeatCount(1)
        pagview_cloud?.addListener(object : PAGView.PAGViewListener {
            override fun onAnimationStart(view: PAGView?) {
            }

            override fun onAnimationEnd(view: PAGView?) {
//                EventBus.getDefault().post(
//                    UniversalEvent(
//                        UniversalEvent.EVENT_MAINACTIVITY_ENDTRANSITIONS_ANIMATION,
//                        null
//                    )
//                )
                pagview_cloud?.progress = 1.0
            }

            override fun onAnimationCancel(view: PAGView?) {
            }

            override fun onAnimationRepeat(view: PAGView?) {
            }
        })
    }

    /**
     * 启动云动画
     */
    private fun startCloudAnimation() {
        pagview_cloud?.let {
            if (!it.isPlaying) it.play()
        }
    }

    /**
     * 停止云动画
     */
    private fun stopCloudAnimation() {
        pagview_cloud?.let {
            if (it.isPlaying) {
                it.stop()
                it.freeCache()
            }
        }
    }

    /**
     * 初始化工作中动画
     */
    private fun initWorkingAnimation() {
        pagview_working = PagAnimationTools.pagTools.getPagView(
            "working.pag",
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        mViewBind.rlPagWorking.addView(pagview_working)
        pagview_working?.setRepeatCount(0)
    }

    /**
     * 启动工作中动画
     */
    private fun startWorkingAnimation() {
        pagview_working?.let {
            if (!it.isPlaying) it.play()
        }
    }

    /**
     * 停止工作中动画
     */
    private fun stopWorkingAnimation() {
        pagview_working?.let {
            if (it.isPlaying) {
                it.stop()
                it.freeCache()
            }
        }
    }

    /**
     * 延迟3秒关闭动画
     */
    fun closeAnimations() {
        Observable.timer(3, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
//                LogUtils.iTag(ConfigConstants.CONSTANT.TAG_ALL, "=-= 我要关闭动画了。")
                stopWorkingAnimation()
                stopCloudAnimation()
                mViewBind.clPag.visibility = View.GONE
            }
    }


    //Event通知
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun handleEvent(event: UniversalEvent) {
        if (event.actionType == UniversalEvent.EVENT_MAINACTIVITY_STARTTRANSITIONS_ANIMATION) {
//            startAnimation()
            EventBus.getDefault().post(
                UniversalEvent(
                    UniversalEvent.EVENT_MAINACTIVITY_ENDTRANSITIONS_ANIMATION,
                    null
                )
            )
        } else if (event.actionType == UniversalEvent.event_mainactivity_start_cloud_working_animation) {
            mViewBind.clPag.visibility = View.VISIBLE
            startCloudAnimation()
            startWorkingAnimation()
            closeAnimations()
        } else if (event.actionType == UniversalEvent.EVENT_MAINACTIVITY_REFRESH_CLMASKLAYER) {
            mViewBind.navigationLayout.setClMaskLayerWhetherToDisplay(false)
        } else if (event.actionType == UniversalEvent.EVENT_JUMP_HOMEFRAGMENT) {
            mViewBind.navigationLayout.controlTabJump(0)
            showNewbiePoliteDialog()
        } else if (event.actionType == UniversalEvent.EVENT_JUMP_HOMEFRAGMENT2) {
            if (!PublicPracticalMethodFromKT.ppmfKT.isBlank(event.message)) {
                val topicId = event.message as Int
                if (topicListResponse.size > 0) {
                    topicListResponse.forEach {
                        if (topicId == it.id) {
                            val sort = it.sort - 1
                            eventViewModel.choiseIndex.value = sort
                        }
                    }
                }
            } else {
                mViewBind.navigationLayout.controlTabJump(0)
            }
        } else if (event.actionType == UniversalEvent.EVENT_NEWER_GUIDE_STEP1) {
            LogUtils.e("123  EVENT_NEWER_GUIDE_STEP1", CacheUtil.getUser()?.guideStageId)
            WindowTaskManager.getInstance()
                .showWindow(this@MainActivity, supportFragmentManager, newbiePoliteDialogBuild)
            val bean = event.message as NewbiePoliteBean
            newbiePoliteDialog.apply {
                setIvCloseWhetherToDisplay(false)
                setData(bean)
                startFingerAnimation()
                onclickTvExchange {
                    mViewModel.doUserGuide(CacheUtil.getUser()?.guideStageId!!) { isSuccess, bean, msg ->
                        if (isSuccess && !PublicPracticalMethodFromKT.ppmfKT.isBlank(bean)) {
                            CacheUtil.getUser()?.let {
                                it.guideStageId = bean!!.guideStageId
                                CacheUtil.setUser(it)
                            }
                            mViewBind.navigationLayout.setClMaskLayerWhetherToDisplay(true)
                            mViewBind.navigationLayout.controlTabJump(1)
                            dismissDialog()
                        }
                    }
                }
            }
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            if (!CacheUtil.isLogin()) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
                return
            }
            requestMainViewModel.gashaponHomepage {
                eventViewModel.gashaponHomepageEvent.value = it
            }

            meViewModel.getuserInfo()

            LogUtils.e("123", ClipboardUtil.getText(this))
            val text = ClipboardUtil.getText(this)
            if (!text.isNullOrEmpty() && text.contains("userCode") && text.contains("groupCode")) {
                groupCode = JSONObject(text).get("groupCode") as String
                userCode = JSONObject(text).get("userCode") as String

                CacheUtil.getUser()?.userId?.let {
                    ClipboardUtil.clearFirstClipboard(this)
                    requestCheatingViewModel.getAssist(
                        groupCode,
                        it
                    )
                }

            }
        }
    }

    override fun onNetworkStateChanged(netState: NetState) {
        super.onNetworkStateChanged(netState)
        if (netState.isSuccess) {
        } else {
            ToastUtil.showCenter("网络异常，请检查网络设置")
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }
}
