package com.sn.gameelectricity.ui.activity

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.LogUtils
import com.gyf.immersionbar.ImmersionBar
import com.sn.gameelectricity.R
import com.sn.gameelectricity.app.App
import com.sn.gameelectricity.app.appViewModel
import com.sn.gameelectricity.app.base.BaseActivity1
import com.sn.gameelectricity.app.util.SharedpreferencesUtil
import com.sn.gameelectricity.app.weight.customview.ErrorPageView
import com.sn.gameelectricity.app.weight.dialog.LeaderboardPlayerInfoDialog
import com.sn.gameelectricity.app.weight.dialog.LeaderboardRuleDialog
import com.sn.gameelectricity.app.weight.dialog.ShareDialog
import com.sn.gameelectricity.data.model.bean.LeaderBoardBean
import com.sn.gameelectricity.databinding.ActivityLeaderboardBinding
import com.sn.gameelectricity.databinding.ViewLeaderboardListHeaderBinding
import com.sn.gameelectricity.ui.adapter.LeaderBoardListAdapter
import com.sn.gameelectricity.viewmodel.request.RequestEquityViewModel
import com.sn.gameelectricity.viewmodel.state.MoneyViewModel
import kotlinx.android.synthetic.main.activity_leaderboard.*
import me.hgj.jetpackmvvm.common.ConfigConstants
import me.hgj.jetpackmvvm.ext.addMoreData
import me.hgj.jetpackmvvm.ext.removeEmptyView
import me.hgj.jetpackmvvm.util.PublicPracticalMethodFromKT
import me.hgj.jetpackmvvm.util.ScreenTools
import me.hgj.jetpackmvvm.util.isNotNull
import me.hgj.jetpackmvvm.util.load
import me.jessyan.autosize.AutoSizeConfig
import singleClick

/**
 * Date:2022/7/6
 * Time:16:11
 * author:dimple
 * 排行榜
 */
class LeaderboardActivity : BaseActivity1<MoneyViewModel, ActivityLeaderboardBinding>() {

    private var pageNum: Int = 1

    private lateinit var leaderBoardListAdapter: LeaderBoardListAdapter

    //行程头view
    private lateinit var headerBinding: ViewLeaderboardListHeaderBinding

    //头部数据
    private var header_datas = mutableListOf<LeaderBoardBean>()

    //中心数据
    private var center_datas = mutableListOf<LeaderBoardBean>()

    //排行榜规则
    private var leaderboardRuleDialog: LeaderboardRuleDialog? = null

    //玩家信息
    private var leaderboardPlayerInfoDialog: LeaderboardPlayerInfoDialog? = null

    //排行榜列表滑动距离
    private var rvScrollY = 0

    //排行榜的头高度
    private var leaderboardhead_height = 0

    private val requestEquityViewModel: RequestEquityViewModel by viewModels()


    val shareDialog by lazy {
        ShareDialog(
            this,
            ScreenTools.getInstance().dp2px(this, 375f, true)
        )
    }

    override fun initView(savedInstanceState: Bundle?) {

        ImmersionBar.with(this)
            .statusBarColor(R.color.transparent)
            .statusBarDarkFont(true)
            .navigationBarColorInt(Color.WHITE)
            .navigationBarDarkIcon(true)
            .init()


        mViewModel.setTopViewHeight3(
            this@LeaderboardActivity,
            mViewBind.clMain,
            R.id.ivBack,
            ScreenTools.getInstance().dp2px(App.instance, 20f, true)
        )
        mViewModel.setTopViewHeight3(
            this@LeaderboardActivity,
            mViewBind.clMain,
            R.id.ivLogo,
            0
        )

        PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
            mViewBind.tvRule,
            -1,
            "",
            ScreenTools.getInstance().dp2px(App.instance, 8f, true).toFloat(),
            0f,
            ScreenTools.getInstance().dp2px(App.instance, 8f, true).toFloat(),
            0f,
            GradientDrawable.Orientation.LEFT_RIGHT,
            "#DBEAFF"
        )
        initData()
        initListener()
    }

    private fun initData() {
        initList()
        dataEcho()
        mViewModel.getRankList(1, 100, true)

        appViewModel.uiShowHide(arrayOf(mViewBind.clMyRank), View.GONE)
        mViewModel.getMyRank()
    }

    private fun initListener() {
        mViewBind.apply {
            ivBack.singleClick {
                finish()
            }
            tvRule.singleClick {
                leaderboardRuleDialog = LeaderboardRuleDialog(
                    this@LeaderboardActivity,
                    ScreenTools.getInstance().dp2px(this@LeaderboardActivity, 375f, true),
                    (0.7 * AutoSizeConfig.getInstance().screenHeight).toInt()
                )
                leaderboardRuleDialog?.apply {
                    show()
                }
            }

            ivInvite.singleClick {
                shareDialog.apply {
                    show()
                }
            }
        }
    }

    /**
     * 初始化排行榜列表
     */


    private fun initList() {
        mViewBind.apply {
            leaderBoardListAdapter = LeaderBoardListAdapter(this@LeaderboardActivity)
            recyclerView.apply {
                layoutManager = LinearLayoutManager(this@LeaderboardActivity)
                adapter = leaderBoardListAdapter
                clearAnimation()
                setLoadingMoreBottomHeight(10f)

                setOnRefreshListener {
                    header_datas.clear()
                    center_datas.clear()

                    if (!this@LeaderboardActivity::headerBinding.isLateinit) {
                        headerBinding.isNotNull({
                            appViewModel.uiShowHide(arrayOf(it.clMain), View.GONE)
                        })
                    }

                    pageNum = 1
                    mViewModel.getRankList(pageNum, 100, false)
                }

//                setOnLoadMoreListener {
//                    pageNum++
//                    mViewModel.getRankList(pageNum)
//                }

                setOnItemClickListener { v, position ->
                    val bean = leaderBoardListAdapter.getItemData(position)
                    showPlayerInfoDialog(bean)
//                    //因为有头部view，所以这里需要+2
//                    val _position = position + 2
//                    LogUtils.iTag(ConfigConstants.CONSTANT.TAG_ALL, "_position =-=$_position")
//                    val view_item = recyclerView.layoutManager!!.findViewByPosition(_position)
//                    val _clMain = view_item!!.findViewById<ConstraintLayout>(R.id.cl_main)
//
//                    val height = view_item.height
//                    val scrollY = view_item.y
//                    val a = leaderboardhead_height
//
//
//                    PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
//                        _clMain,
//                        -1,
//                        "",
//                        0f,
//                        0f,
//                        0f,
//                        0f,
//                        null,
//                        "#44CFB3"
//                    )
                }
                var _position = -1
                lateinit var view_item: View
                lateinit var _clMain: ConstraintLayout
                var item_height = 1
                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(_recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(_recyclerView, dx, dy)
                        rvScrollY += dy
                        var clItemBgColor = ""
                        var CornerRadiusLeftTop = 0f
                        var CornerRadiusRightTop = 0f
                        var CornerRadiusLeftBottom = 0f
                        var CornerRadiusRightBottom = 0f
//                        if (rvScrollY > 0) {
//                            _position = if ((rvScrollY - leaderboardhead_height) < 0) {
//                                0
//                            } else {
//                                (rvScrollY - leaderboardhead_height + item_height) / item_height
//                            }
//
//                            view_item =
//                                recyclerView.layoutManager?.findViewByPosition(_position + 2)!!
//                            item_height = view_item.height
//                            _clMain = view_item!!.findViewById(R.id.cl_main)
//
//
//                            if (view_item.y < 30) {
//                                clItemBgColor = "#FFFFFF"
//                                CornerRadiusLeftTop =
//                                    ScreenTools.getInstance().dp2px(App.instance, 20f, true)
//                                        .toFloat()
//                                CornerRadiusRightTop =
//                                    ScreenTools.getInstance().dp2px(App.instance, 20f, true)
//                                        .toFloat()
//                                CornerRadiusLeftBottom = 0f
//                                CornerRadiusRightBottom = 0f
//                            } else {
//                                clItemBgColor = "#FFFFFF"
//                                CornerRadiusLeftTop = 0f
//                                CornerRadiusRightTop = 0f
//                                CornerRadiusLeftBottom = 0f
//                                CornerRadiusRightBottom = 0f
//                            }
//
//                            PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
//                                _clMain,
//                                -1,
//                                "",
//                                CornerRadiusLeftTop,
//                                CornerRadiusRightTop,
//                                CornerRadiusLeftBottom,
//                                CornerRadiusRightBottom,
//                                null,
//                                clItemBgColor
//                            )
//
//                            LogUtils.iTag(
//                                ConfigConstants.CONSTANT.TAG_ALL, "rvScrollY =-=$rvScrollY",
//                                "_position =-= $_position",
//                                "view_item.y =-= ${view_item.y}"
//                            )
//                        }
                    }
                })
            }
        }
    }

    /**
     * 排行榜添加headerview
     */
    private fun bindHeaderView() {
        if (mViewBind.recyclerView.headerViewCount == 0) {
            headerBinding =
                ViewLeaderboardListHeaderBinding.inflate(
                    layoutInflater,
                    mViewBind.recyclerView,
                    false
                )
            mViewBind.recyclerView.addHeaderView(headerBinding.root)
        }
        appViewModel.uiShowHide(arrayOf(headerBinding.clMain), View.VISIBLE)
        headerBinding.apply {
            when (header_datas.size) {
                0 -> {
                    clFirst.visibility = View.GONE
                    clSecond.visibility = View.GONE
                    clthird.visibility = View.GONE
                }
                1 -> {
                    clFirst.visibility = View.VISIBLE
                    clSecond.visibility = View.INVISIBLE
                    clthird.visibility = View.INVISIBLE

                    sivAvatarFirst.load(
                        header_datas[0].avatar,
                        R.drawable.default_user_avatar,
                        R.drawable.default_user_avatar
                    )
                    tvNameFirst.text = header_datas[0].nickName
                    tvInviteesNum.text = "${header_datas[0].num}"

                    SharedpreferencesUtil.putString(
                        this@LeaderboardActivity,
                        ConfigConstants.VARIABLE.leaderboard_first_avatar,
                        header_datas[0].avatar
                    )
                }
                2 -> {
                    clFirst.visibility = View.VISIBLE
                    clSecond.visibility = View.VISIBLE
                    clthird.visibility = View.INVISIBLE

                    sivAvatarFirst.load(
                        header_datas[0].avatar,
                        R.drawable.default_user_avatar,
                        R.drawable.default_user_avatar
                    )
                    tvNameFirst.text = header_datas[0].nickName
                    tvInviteesNum.text = "${header_datas[0].num}"

                    SharedpreferencesUtil.putString(
                        this@LeaderboardActivity,
                        ConfigConstants.VARIABLE.leaderboard_first_avatar,
                        header_datas[0].avatar
                    )


                    sivAvatarSecond.load(
                        header_datas[1].avatar,
                        R.drawable.default_user_avatar,
                        R.drawable.default_user_avatar
                    )
                    tvNameSecond.text = header_datas[1].nickName
                    tvInviteesNum2.text = "${header_datas[1].num}"


                }
                3 -> {
                    clFirst.visibility = View.VISIBLE
                    clSecond.visibility = View.VISIBLE
                    clthird.visibility = View.VISIBLE

                    sivAvatarFirst.load(
                        header_datas[0].avatar,
                        R.drawable.default_user_avatar,
                        R.drawable.default_user_avatar
                    )
                    tvNameFirst.text = header_datas[0].nickName
                    tvInviteesNum.text = "${header_datas[0].num}"

                    SharedpreferencesUtil.putString(
                        this@LeaderboardActivity,
                        ConfigConstants.VARIABLE.leaderboard_first_avatar,
                        header_datas[0].avatar
                    )

                    sivAvatarSecond.load(
                        header_datas[1].avatar,
                        R.drawable.default_user_avatar,
                        R.drawable.default_user_avatar
                    )
                    tvNameSecond.text = header_datas[1].nickName
                    tvInviteesNum2.text = "${header_datas[1].num}"

                    sivAvatarthird.load(
                        header_datas[2].avatar,
                        R.drawable.default_user_avatar,
                        R.drawable.default_user_avatar
                    )
                    tvNamethird.text = header_datas[2].nickName
                    tvInviteesNum3.text = "${header_datas[2].num}"

                }
            }

            clMain.post {
                leaderboardhead_height = clMain.height
            }

            clFirst.singleClick {
                showPlayerInfoDialog(
                    header_datas[0]
                )
            }

            clSecond.singleClick {
                showPlayerInfoDialog(
                    header_datas[1]
                )
            }

            clthird.singleClick {
                showPlayerInfoDialog(
                    header_datas[2]
                )
            }
        }
    }

    /**
     * 数据回显
     */
    private fun dataEcho() {
        mViewModel.rankListLive.observe(this) { beans ->
            if (pageNum == 1) {
                if (beans.isEmpty()) {
                    bindEmptyView()
                    SharedpreferencesUtil.putString(
                        this@LeaderboardActivity,
                        ConfigConstants.VARIABLE.leaderboard_first_avatar,
                        ""
                    )
                } else {

                    if (beans.size <= 3) {
                        header_datas.addAll(beans)
                    } else {
                        header_datas.addAll(beans.slice(0..2))
                        center_datas.addAll(beans.slice(3..beans.lastIndex))
                    }
                    mViewBind.recyclerView.removeEmptyView()
                    bindHeaderView()
                    leaderBoardListAdapter.setNewDataIgnoreSize(center_datas)
                }
            } else {
                center_datas.addAll(beans)
                leaderBoardListAdapter.addMoreData(beans)
            }
        }

        mViewModel.myRankLive.observe(this) { bean ->
            appViewModel.uiShowHide(arrayOf(mViewBind.clMyRank), View.VISIBLE)
            mViewBind.sivMyRankAvatar.load(
                bean.avatar,
                R.drawable.default_user_avatar,
                R.drawable.default_user_avatar
            )
            mViewBind.tvMyRank.apply {
                if (bean.rank == -1) {
                    text = "未上榜"
                    setTextColor(Color.parseColor("#EF874E"))
                } else {
                    text = mViewModel.myRankHighlight("我的排名 ${bean.rank}", bean.rank)
                }
            }
            mViewBind.tvInviteesNum.text = "${bean.num}"
        }
    }

    /**
     * 当列表数据为空的时候，显示
     */
    private fun bindEmptyView() {
        val errorPageView = ErrorPageView(this@LeaderboardActivity)
        with(errorPageView) {
            post {
                var errorIcon = R.drawable.ic_img_emp
                var ErrorContent = "暂无数据～"
                var offsetTop = 0
                val recyclerView_height = mViewBind.recyclerView.height
                setErrorIcon(
                    errorIcon,
                    ScreenTools.getInstance().dp2px(App.instance, 124f, true),
                    ScreenTools.getInstance().dp2px(App.instance, 124f, true)
                )
                setErrorContent(ErrorContent, 12f, "#A1A7AF")
                changeErrorIconPositionToTOP((0.3 * recyclerView_height).toInt())
                changeErrorTextPositionToTOP(offsetTop)
            }
        }

        mViewBind.recyclerView.apply {
            setEmptyView(errorPageView)
//            setLoadingMoreBottomHeight(0f)
            isLoadMoreEnabled = false
            leaderBoardListAdapter.setNewData(arrayListOf())
        }
    }

    /**
     * 显示弹框
     */
    private fun showPlayerInfoDialog(leaderBoardBean: LeaderBoardBean) {
        leaderboardPlayerInfoDialog =
            LeaderboardPlayerInfoDialog(
                this@LeaderboardActivity,
                mViewModel,
                requestEquityViewModel,
                ScreenTools.getInstance().dp2px(this@LeaderboardActivity, 375f, true),
                (0.75 * AutoSizeConfig.getInstance().screenHeight).toInt()
            )
        leaderboardPlayerInfoDialog?.apply {
            setUserDatas(leaderBoardBean)
            onClickClose {
                leaderboardPlayerInfoDialog = null
            }
            show()
        }
    }
}