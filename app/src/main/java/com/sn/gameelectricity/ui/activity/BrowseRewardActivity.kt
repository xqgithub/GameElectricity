package com.sn.gameelectricity.ui.activity

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.gyf.immersionbar.ImmersionBar
import com.sn.gameelectricity.R
import com.sn.gameelectricity.app.base.BaseActivity1
import com.sn.gameelectricity.data.model.bean.PayloadGameTaskVO
import com.sn.gameelectricity.databinding.ActivityBrowseRewardBinding
import com.sn.gameelectricity.ui.adapter.FragmentPagerSaveAdapter
import com.sn.gameelectricity.ui.fragment.home.HomeDailyRobFragment
import com.sn.gameelectricity.ui.fragment.home.HomeEquityFragment
import com.sn.gameelectricity.ui.fragment.home.HomeHotFragment
import com.sn.gameelectricity.viewmodel.request.RequestHomeViewModel
import com.sn.gameelectricity.viewmodel.state.MainViewModel
import com.sn.gameelectricity.viewmodel.state.MoneyViewModel
import me.hgj.jetpackmvvm.event.UniversalEvent
import me.hgj.jetpackmvvm.util.ToastUtil
import me.hgj.jetpackmvvm.util.dataNullConvertToInt
import me.hgj.jetpackmvvm.util.dataNullConvertToString
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import singleClick


/**
 * 浏览得奖励 Activity
 */
class BrowseRewardActivity : BaseActivity1<MainViewModel, ActivityBrowseRewardBinding>() {

    private var fragments: MutableList<Fragment> = mutableListOf()
    private var tabTitles: MutableList<String> = mutableListOf()

    private val requestHomeViewModel: RequestHomeViewModel by viewModels()
    private val moneyViewModel: MoneyViewModel by viewModels()

//    private val choiceTopic: Int by lazy {
//        intent.getIntExtra("choiceTopic", 0)
//    }

    private var choiceTopic: Int = 0

    //是否开始计时
    private var isStartTimer = false

    private val payloadGameTaskVO: PayloadGameTaskVO by lazy {
        intent.getSerializableExtra("PayloadGameTaskVO") as PayloadGameTaskVO
    }


    private var countDownTimer: CountDownTimer? = null
    private var award = ""
    private var award_num = 0
    private var award2 = ""
    private var award_num2 = 0

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this)
            .transparentStatusBar()
            .statusBarDarkFont(true)
            .navigationBarColorInt(Color.WHITE)
            .navigationBarDarkIcon(true)
            .init()

        //注册EventBus
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }


        choiceTopic = dataNullConvertToInt(payloadGameTaskVO.eventType) as Int

        mViewBind.apply {
            ivBack.singleClick {
                finish()
            }

            tv002.text =
                mViewModel.countDownHighlight(
                    "浏览${dataNullConvertToString(payloadGameTaskVO.taskTarget) as String}s获得奖励，滑动即可开始浏览~",
                    dataNullConvertToString(payloadGameTaskVO.taskTarget) as String
                )

            if (payloadGameTaskVO.requestRewardInfoVOList.size == 1) {
                if (payloadGameTaskVO.requestRewardInfoVOList[0].rewardType == 1) {
                    award = "金币"
                } else {
                    award = "饲料"
                }
                award_num =
                    dataNullConvertToInt(payloadGameTaskVO.requestRewardInfoVOList[0].rewardNumber) as Int
            } else {
                payloadGameTaskVO.requestRewardInfoVOList.forEach {
                    if (it.rewardType == 1) {
                        award = "金币"
                        award_num =
                            dataNullConvertToInt(it.rewardNumber) as Int
                    } else if (it.rewardType == 3) {
                        award2 = "饲料"
                        award_num2 =
                            dataNullConvertToInt(it.rewardNumber) as Int
                    }
                }
            }
        }
    }

    override fun createObserver() {
        requestHomeViewModel.topicList()
        requestHomeViewModel.homeTopicList.observe(this, Observer {
            it.forEach {
                tabTitles.add(it.topicName)
                if (it.topicName.equals("每日必抢")) {
                    fragments.add(HomeDailyRobFragment())
                } else if (it.topicName.equals("权益兑换")) {
                    fragments.add(HomeEquityFragment())
                } else {
                    fragments.add(HomeHotFragment.newInstance(it.id))
                }
            }
            initViewPager()

            val resultCurrentItem = if (choiceTopic > 0) {
                choiceTopic - 1
            } else {
                0
            }
            mViewBind.fragmentHomeVp.setCurrentItem(resultCurrentItem)
        })
    }

    fun initViewPager() {

        mViewBind.apply {
            /** 初始化viewpager **/
            fragmentHomeVp.apply {
                adapter =
                    PageAdapter(
                        supportFragmentManager,
                        fragments,
                        tabTitles
                    )
                //预加载多少页
                offscreenPageLimit = 1
                addOnPageChangeListener(onPageChangeListener)
            }
            /** 初始化TabLayout **/
            fragmentHomeTb.apply {
                setupWithViewPager(fragmentHomeVp)

                for (i in 0 until fragmentHomeVp.adapter!!.count) {
                    val tab: TabLayout.Tab? = getTabAt(i)
                    tab?.let {
                        it.customView = getTabView(i)
                    }
                }

                //去掉tab点击效果
                tabRippleColor = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        this@BrowseRewardActivity,
                        R.color.transparent
                    )
                )
                //关联ViewPager
                addOnTabSelectedListener(
                    TabLayout.ViewPagerOnTabSelectedListener(
                        fragmentHomeVp
                    )
                )

                fragmentHomeVp.addOnPageChangeListener(
                    TabLayout.TabLayoutOnPageChangeListener(
                        this
                    )
                )

                addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                    override fun onTabSelected(tab: TabLayout.Tab?) {
                        val view = tab?.customView
                        view?.let {
                            val tv_title_name = view.findViewById<TextView>(R.id.tv_title_name)

                            tv_title_name.setTextColor(Color.parseColor("#FFFFFF"))
                            tv_title_name.background =
                                resources.getDrawable(R.drawable.shape_radius_6c72fd)

                        }
                    }

                    override fun onTabUnselected(tab: TabLayout.Tab?) {
                        val view = tab?.customView
                        view?.let {
                            val tv_title_name = view.findViewById<TextView>(R.id.tv_title_name)

                            tv_title_name.setTextColor(Color.parseColor("#233556"))
                            tv_title_name.background = null
                        }
                    }

                    override fun onTabReselected(tab: TabLayout.Tab?) {
                    }
                })
            }
        }
    }

    private fun getTabView(position: Int): View {
        val view = LayoutInflater.from(this).inflate(R.layout.item_tablayout_home, null)
        val tv_title_name = view.findViewById<TextView>(R.id.tv_title_name)

        tv_title_name.apply {
            text = tabTitles[position]
        }
        if (position == 0) {
            tv_title_name.setTextColor(Color.parseColor("#FFFFFF"))
            tv_title_name.background = getDrawable(R.drawable.shape_radius_6c72fd)
        } else {
            tv_title_name.setTextColor(Color.parseColor("#233556"))
            tv_title_name.background = null
        }

        return view
    }

    class PageAdapter(
        fm: FragmentManager,
        private val mFragment: MutableList<Fragment>,
        private val titles: MutableList<String>
    ) : FragmentPagerSaveAdapter(fm) {
        override fun getCount(): Int {
            return mFragment.size
        }

        override fun getItem(position: Int): Fragment {
            return mFragment[position]
        }

        override fun getPageTitle(position: Int): CharSequence {
            return titles[position]
        }
    }


    private var onPageChangeListener: ViewPager.OnPageChangeListener =
        object : ViewPager.OnPageChangeListener {

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                when (position) {
                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        }

    /**
     * 开始浏览计时
     */
    fun startBrowsingTime(time: Int) {
        countDownTimer = object : CountDownTimer(time * 1000L, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val time = millisUntilFinished / 1000L
                if (payloadGameTaskVO.requestRewardInfoVOList.size == 1) {
                    mViewBind.tv002.text =
                        mViewModel.countDownHighlight(
                            "${time}s继续浏览,即可以获得${award_num}${award}",
                            time.toString()
                        )
                } else {
                    mViewBind.tv002.text = mViewModel.countDownHighlight(
                        "${time}s继续浏览,即可以获得${award_num}${award},${award_num2}${award2}",
                        time.toString()
                    )
                }
            }

            override fun onFinish() {
                val time = 0L
                moneyViewModel.simpleTaskCompletion(dataNullConvertToInt(payloadGameTaskVO.id) as Int) { isSuccess, msg ->
                    if (isSuccess) {
//                        EventBus.getDefault().post(
//                            UniversalEvent(
//                                UniversalEvent.EVENT_TODAY_BUILDTASKLIST_REFRESH,
//                                null
//                            )
//                        )
                        mViewBind.iv002.visibility = View.VISIBLE
                        mViewBind.tv002.text = "浏览任务完成，快去领取奖励吧~"
                    }
                    ToastUtil.showCenter(msg)
                }
            }
        }
        countDownTimer?.start()
    }

    //Event通知
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun handleEvent(event: UniversalEvent) {
        if (event.actionType == UniversalEvent.EVENT_HOMEHOTFRAGMENT_HOMEDAILYROBFRAGMENT_BYRECYCLERVIEW_SLIDE
        ) {
            if (!isStartTimer) {
                isStartTimer = true
                startBrowsingTime((dataNullConvertToString(payloadGameTaskVO.taskTarget) as String).toInt())
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
        countDownTimer = null

        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }


}
