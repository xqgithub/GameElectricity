package com.sn.gameelectricity.ui.fragment.home

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.gyf.immersionbar.ImmersionBar
import com.sn.gameelectricity.R
import com.sn.gameelectricity.app.App
import com.sn.gameelectricity.app.base.BaseFragment1
import com.sn.gameelectricity.app.eventViewModel
import com.sn.gameelectricity.app.util.CacheUtil
import com.sn.gameelectricity.app.util.load
import com.sn.gameelectricity.app.weight.dialog.*
import com.sn.gameelectricity.data.model.bean.AddWishBus
import com.sn.gameelectricity.data.model.bean.GashaponHomepageResponse
import com.sn.gameelectricity.databinding.FragmentHomeBinding
import com.sn.gameelectricity.ui.activity.PickGoodsDetailsActivity
import com.sn.gameelectricity.ui.activity.PrizePoolActivity
import com.sn.gameelectricity.ui.activity.RecoverGoodActivity
import com.sn.gameelectricity.ui.activity.web.WebActivity
import com.sn.gameelectricity.ui.adapter.FragmentPagerSaveAdapter
import com.sn.gameelectricity.viewmodel.request.RequestHomeViewModel
import com.sn.gameelectricity.viewmodel.state.HomeViewModel
import com.sn.gameelectricity.viewmodel.state.MainViewModel
import com.sn.gameelectricity.viewmodel.state.MeViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.*
import me.hgj.jetpackmvvm.util.PublicPracticalMethodFromKT
import me.hgj.jetpackmvvm.util.ScreenTools
import org.libpag.PAGFile
import org.libpag.PAGImage
import org.libpag.PAGView
import singleClick
import java.lang.Math.abs
import java.util.concurrent.TimeUnit


/**
 * 兑好物
 */
class HomeFragment : BaseFragment1<HomeViewModel, FragmentHomeBinding>() {

    private var fragments: MutableList<Fragment> = mutableListOf()
    private var tabTitles: MutableList<String> = mutableListOf()

    private val requestHomeViewModel: RequestHomeViewModel by viewModels()
    private val meViewModel: MeViewModel by viewModels()
    private val mainViewModel: MainViewModel by viewModels()
    private var gashaponHomepageResponse: GashaponHomepageResponse? = null

    private var disposable: Disposable? = null

    private var pagGashapon: PAGView? = null
    private var pagAction: PAGView? = null
    private var pagGashaponFile: PAGFile? = null

    private val newbiePoliteDialog by lazy {
        NewbiePoliteDialog(
            requireActivity(),
            ScreenTools.getInstance().dp2px(requireActivity(), 240f, true),
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    val homeAddWishDialog by lazy {
        HomeAddWishDialog(
            activity,
            requireActivity(), requestHomeViewModel, viewLifecycleOwner,
            ScreenTools.getInstance().dp2px(requireActivity(), 375f, true)
        )
    }

    val goldLackDialog by lazy {
        GoldLackDialog(requireActivity())
    }

    val gainGoldDialog by lazy {
        GainGoldDialog(requireActivity())
    }

    val gainProductDialog by lazy {
        GainProductDialog(requireActivity())
    }

    val gainProductGoldDialog by lazy {
        GainProductGoldDialog(requireActivity())
    }

    override fun initView(savedInstanceState: Bundle?) {

        ImmersionBar.with(this)
            .transparentStatusBar()
            .titleBar(mViewBind.toolbar)
            .statusBarDarkFont(true)
            .navigationBarColorInt(Color.WHITE)
            .navigationBarDarkIcon(true)
            .init()

        mViewBind.apply {
            homeAPPbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
                val offset = abs(verticalOffset)
                when {
                    offset < appBarLayout.totalScrollRange.div(1) -> {
                        ivArrowUp.visibility = View.GONE
                    }
                    offset == appBarLayout.totalScrollRange -> {
                        ivArrowUp.visibility = View.VISIBLE
                    }
                }
            })

            clPrizePool.singleClick {
                with(Bundle()) {
                    PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                        requireActivity(),
                        PrizePoolActivity::class.java, this, false
                    )
                }
            }

            clHomeGold.singleClick {
                activity?.navigationLayout?.controlTabJump(1)
            }

            clHomeGoose.singleClick {
                activity?.navigationLayout?.controlTabJump(1)
            }

            ivHomeGift.singleClick {
                mainViewModel.newcomerCeremony()
            }

            ivHomeRule.singleClick {
                val intent = Intent(requireActivity(), WebActivity::class.java)
                intent.putExtra("url", "https://itest.aifun.com/#/Ruledesc")
                intent.putExtra("title", "规则说明")
                startActivity(intent)
            }

            tvWished.singleClick {
                homeAddWishDialog.apply {
                    show()
                }
            }

            rlHomeHandClick.singleClick {
                pagAction?.let {
                    it.setRepeatCount(1)
                    if (!it.isPlaying) {
                        it.play()
                    }
                }

                pagGashapon?.let {
                    it.setRepeatCount(1)
                    if (!it.isPlaying) {
                        it.play()
                    }
                }

            }

            startAnimator()
        }
    }

    private fun initGashaponPag() {
        pagGashapon = PAGView(App.instance)
        pagGashapon?.layoutParams = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        mViewBind.rlPagBg.addView(pagGashapon)
        pagGashaponFile = PAGFile.Load(App.instance.assets, "gashapon.pag")
        pagGashapon?.composition = pagGashaponFile
        pagGashapon?.addListener(object : PAGView.PAGViewListener {
            override fun onAnimationStart(view: PAGView?) {
            }

            override fun onAnimationEnd(view: PAGView?) {
                meViewModel.getuserInfo()
                pagAction?.progress = 0.0
                pagGashapon?.progress = 0.0
            }

            override fun onAnimationCancel(view: PAGView?) {
            }

            override fun onAnimationRepeat(view: PAGView?) {
            }
        })
    }

    private fun initActionPag() {
        pagAction = PAGView(App.instance)
        pagAction?.layoutParams = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        mViewBind.rlHomeHandClick.addView(pagAction)
        pagAction?.composition = PAGFile.Load(App.instance.assets, "action.pag")
        pagAction?.addListener(object : PAGView.PAGViewListener {
            override fun onAnimationStart(view: PAGView?) {
            }

            override fun onAnimationEnd(view: PAGView?) {

            }

            override fun onAnimationCancel(view: PAGView?) {
            }

            override fun onAnimationRepeat(view: PAGView?) {
            }
        })
    }

    private fun startAnimator() {
        val mObjectAnimatorX1 = ObjectAnimator.ofFloat(
            mViewBind.ivHomeHand,
            "translationX",
            mViewBind.ivHomeHand.translationX,
            -50.0f,
            mViewBind.ivHomeHand.translationX
        )
        val mObjectAnimatorY1 = ObjectAnimator.ofFloat(
            mViewBind.ivHomeHand,
            "translationY",
            mViewBind.ivHomeHand.translationY,
            -50.0f,
            mViewBind.ivHomeHand.translationY
        )

        mObjectAnimatorX1.repeatCount = -1
        mObjectAnimatorY1.repeatCount = -1

        val animatorSet = AnimatorSet()
        animatorSet.play(mObjectAnimatorX1).with(mObjectAnimatorY1)
        animatorSet.setDuration(2000)
        animatorSet.start()
    }


    override fun createObserver() {
        requestHomeViewModel.topicList()

        requestHomeViewModel.homeTopicList.observe(viewLifecycleOwner, Observer {
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

        })

        meViewModel.userInfoLive.observe(this) { beans ->
            mViewBind.apply {
                if (gashaponHomepageResponse != null && gashaponHomepageResponse?.gashaponPlayConsumerGoldCoinNum!! < beans.goldCoin) {
//                    ToastUtils.make().setGravity(Gravity.TOP or Gravity.LEFT, AdaptScreenUtils.pt2Px(
//                        70f
//                    ),AdaptScreenUtils.pt2Px(
//                        45f
//                    )).setTextSize(12).show("-  "+  gashaponHomepageResponse?.gashaponPlayConsumerGoldCoinNum!!)
                    requestHomeViewModel.gashaponPlay {
                        eventViewModel.refreshHomeDaily.value = 1
                        when (it.size) {
                            1 -> {
                                val gashaponPlayResponse = it[0]
                                when (gashaponPlayResponse.awardType) {//奖品类型：1-实物 2-虚拟
                                    1 -> {
                                        gainProductDialog.apply {
                                            setTextContent(gashaponPlayResponse.awardName)
                                            loadImg(gashaponPlayResponse.awardImg)
                                            onClickSure {
//                                                with(Bundle()) {
//                                                    putInt("orderId", gashaponPlayResponse.orderId)
//                                                    PublicPracticalMethodFromKT.ppmfKT.intentToJump(
//                                                        requireActivity(),
//                                                        PickGoodsDetailsActivity::class.java,
//                                                        this,
//                                                        false
//                                                    )
//                                                }
                                            }
                                            onClickRecovery {
                                                with(Bundle()) {
                                                    putInt("orderId", gashaponPlayResponse.orderId)
                                                    PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                                                        requireActivity(),
                                                        RecoverGoodActivity::class.java,
                                                        this,
                                                        false
                                                    )
                                                }
                                            }
                                            show()
                                        }
                                    }
                                    2 -> {
                                        gainGoldDialog.apply {
                                            when (gashaponPlayResponse.virtualGoodsType) {//虚拟商品类型：1-金币 3-饲料
                                                1 -> {
                                                    setTextContent("金币 x" + gashaponPlayResponse.virtualGoodsNum)
//                                                    ToastUtils.make().setGravity(Gravity.TOP or Gravity.LEFT, AdaptScreenUtils.pt2Px(
//                                                        70f
//                                                    ),AdaptScreenUtils.pt2Px(
//                                                        45f
//                                                    )).setTextSize(12).show("+  "+ gashaponPlayResponse.virtualGoodsNum)
                                                }
                                                3 -> {
                                                    setTextContent("饲料 x" + gashaponPlayResponse.virtualGoodsNum)
                                                }
                                            }
                                            loadImg(gashaponPlayResponse.awardImg)
                                            show()
                                        }
                                    }
                                }
                            }
                            2 -> {
                                gainProductGoldDialog.apply {
                                    it.forEach {
                                        when (it.awardType) {//奖品类型：1-实物 2-虚拟
                                            1 -> {
                                                setTextContent(it.awardName)
                                                loadImgProduct(it.awardImg)
                                                when (it.awardSource) {//奖励来源：1-扭蛋 2-每日必抢
                                                    1 -> {
                                                        setContentDes(
                                                            "扭蛋",
                                                            R.drawable.shape_radius_f19b3f_2
                                                        )
                                                    }
                                                    2 -> {
                                                        setContentDes(
                                                            "每日必抢",
                                                            R.drawable.shape_radius_6c72fd_2
                                                        )
                                                    }
                                                }
                                                onClickSure {
//                                                    with(Bundle()) {
//                                                        putInt("orderId", it.orderId)
//                                                        PublicPracticalMethodFromKT.ppmfKT.intentToJump(
//                                                            requireActivity(),
//                                                            PickGoodsDetailsActivity::class.java,
//                                                            this,
//                                                            false
//                                                        )
//                                                    }
                                                }
                                            }
                                            2 -> {
                                                when (it.virtualGoodsType) {//虚拟商品类型：1-金币 3-饲料
                                                    1 -> {
                                                        setGoldContent("金币 x" + it.virtualGoodsNum)
//                                                        ToastUtils.make().setGravity(Gravity.TOP or Gravity.LEFT, AdaptScreenUtils.pt2Px(
//                                                            70f
//                                                        ),AdaptScreenUtils.pt2Px(
//                                                            45f
//                                                        )).setTextSize(12).show("+  "+ it.virtualGoodsNum)
                                                    }
                                                    3 -> {
                                                        setGoldContent("饲料 x" + it.virtualGoodsNum)
                                                    }
                                                }
                                                when (it.awardSource) {//奖励来源：1-扭蛋 2-每日必抢
                                                    1 -> {
                                                        setGoldContentDes(
                                                            "扭蛋",
                                                            R.drawable.shape_radius_f19b3f_2
                                                        )
                                                    }
                                                    2 -> {
                                                        setGoldContentDes(
                                                            "每日必抢",
                                                            R.drawable.shape_radius_6c72fd_2
                                                        )
                                                    }
                                                }
                                                loadImgGold(it.awardImg)
                                            }
                                        }
                                    }
                                    show()
                                }
                            }
                        }

                    }
                } else {
                    goldLackDialog.apply {
                        onClickSure {
                            activity?.navigationLayout?.controlTabJump(1)
                        }
                        show()
                    }
                }

            }
        }

        CacheUtil.getWished()?.let {
            mViewBind.tvWished.visibility = View.GONE
            mViewBind.clHomeAddWish2.visibility = View.VISIBLE
            mViewBind.ivWishPic.load(it.resId)
        }

        eventViewModel.gashaponHomepageEvent.observeInFragment(this@HomeFragment, Observer {
            gashaponHomepageResponse = it
            mViewBind.tvUserGoldCoinNum.text =
                App.appViewModelInstance.goldCoinConversion(it.userGoldCoinNum)
            mViewBind.tvTis.text = "${it.gashaponPlayConsumerGoldCoinNum}金币/次"
            mViewBind.ivPrizePool.load(it.todayFirstAwardImg)
            if (it.isGooseIsWorking) {
                mViewBind.ivHomeGoose.background =
                    resources.getDrawable(R.drawable.ic_home_gooseing)
                mViewBind.tvHomeGoose.text = "工作中"
            } else {
                mViewBind.ivHomeGoose.background =
                    resources.getDrawable(R.drawable.ic_home_gooseing)
                mViewBind.tvHomeGoose.text = "休息中"
            }

            if (it.isHasWishShopping) {
                CacheUtil.setWished(AddWishBus(it.wishShoppingImg, true))
                mViewBind.tvWished.visibility = View.GONE
                mViewBind.clHomeAddWish2.visibility = View.VISIBLE
                mViewBind.ivWishPic.load(it.wishShoppingImg)

                Glide.with(this@HomeFragment)
                    .asBitmap()
                    .load(it.wishShoppingImg)
                    .into(object : SimpleTarget<Bitmap?>() {
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap?>?
                        ) {
                            pagGashaponFile?.replaceImage(4, PAGImage.FromBitmap(resource))
                            pagGashapon?.progress = 0.0
                        }
                    })
            } else {
                CacheUtil.setWished(AddWishBus("", false))
                mViewBind.tvWished.visibility = View.VISIBLE
                mViewBind.clHomeAddWish2.visibility = View.GONE
            }

            if (!CacheUtil.getUser()?.boolNewUserActivityExchange!!) {
                mViewBind.ivHomeGift.visibility = View.VISIBLE
            } else {
                mViewBind.ivHomeGift.visibility = View.GONE
            }
        })

        eventViewModel.choiseIndex.observeInFragment(this@HomeFragment, Observer {
            activity?.navigationLayout?.controlTabJump(0)
            mViewBind.fragmentHomeVp.setCurrentItem(it)
        })

        eventViewModel.earncoins.observeInFragment(this@HomeFragment, Observer {
            activity?.navigationLayout?.controlTabJump(1)
        })

        eventViewModel.choiseHomeTop.observeInFragment(this@HomeFragment, Observer {
            val layoutParams: ViewGroup.LayoutParams = mViewBind.homeAPPbar.getLayoutParams()
            val behavior = (layoutParams as CoordinatorLayout.LayoutParams).behavior
            if (behavior is AppBarLayout.Behavior) {
                val appBarLayoutBehavior = behavior
                val topAndBottomOffset = appBarLayoutBehavior.topAndBottomOffset
                if (topAndBottomOffset != 0) {
                    appBarLayoutBehavior.topAndBottomOffset = 0
                    mViewBind.homeAPPbar.setExpanded(true, true)
                }
            }
        })

        mainViewModel.newcomerCeremonyLive.observe(this) { bean ->
            newbiePoliteDialog.apply {
                setIvCloseWhetherToDisplay(true)
                setData(bean)
                onclickTvExchange {
                    dismissDialog()
                    mainViewModel.exchange { newbieBean ->
                        CacheUtil.getUser()?.let {
                            it.boolNewUserActivityExchange = true
                            CacheUtil.setUser(it)
                        }
                        with(
                            RewardCommodityDialog(
                                requireActivity(),
                                ScreenTools.getInstance().dp2px(mContext, 260f, true),
                                WindowManager.LayoutParams.WRAP_CONTENT
                            )
                        ) {
                            setProductData(bean)
                            setCancel(View.VISIBLE, "回收", "#EF874E", 14f) {
                                with(Bundle()) {
                                    putInt("orderId", newbieBean.orderId)
                                    PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                                        requireActivity(),
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
                                        requireActivity(),
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
                }
                show()
            }
        }

        var count = 0
        var isCountFrist = true
        disposable = Observable.interval(0, 5, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                count++
                if (count == 5 || isCountFrist) {
                    isCountFrist = false
                    count = 0
                    mViewBind.tvTis.visibility = View.VISIBLE
                } else {
                    mViewBind.tvTis.visibility = View.GONE
                }
            }.subscribe()

    }


    fun initViewPager() {

        mViewBind.apply {
            /** 初始化viewpager **/
            fragmentHomeVp.apply {
                adapter =
                    PageAdapter(requireActivity().supportFragmentManager, fragments, tabTitles)
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
                        requireContext(),
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
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.item_tablayout_home, null)
        val tv_title_name = view.findViewById<TextView>(R.id.tv_title_name)

        tv_title_name.apply {
            text = tabTitles[position]
        }


        if (position == 0) {
            tv_title_name.setTextColor(Color.parseColor("#FFFFFF"))
            tv_title_name.background = resources.getDrawable(R.drawable.shape_radius_6c72fd)
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

    fun stopPagGashapon() {
        pagGashapon?.let {
            if (it.isPlaying) {
                it.stop()
                it.freeCache()
            }
        }
    }

    fun stopPagAction() {
        pagAction?.let {
            if (it.isPlaying) {
                it.stop()
                it.freeCache()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        initGashaponPag()
        initActionPag()

    }

    override fun onDestroy() {
        disposable?.dispose()
        disposable = null
        stopPagGashapon()
        stopPagAction()
        super.onDestroy()
    }

}