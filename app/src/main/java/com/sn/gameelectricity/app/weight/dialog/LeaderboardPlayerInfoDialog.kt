package com.sn.gameelectricity.app.weight.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.just.agentweb.AgentWeb
import com.sn.gameelectricity.R
import com.sn.gameelectricity.app.App
import com.sn.gameelectricity.app.weight.customview.CommentDialogDecoration
import com.sn.gameelectricity.app.weight.customview.ErrorPageView
import com.sn.gameelectricity.data.model.bean.LeaderBoardBean
import com.sn.gameelectricity.databinding.DialogPlayerinfoBinding
import com.sn.gameelectricity.databinding.DialogRuleBinding
import com.sn.gameelectricity.ui.adapter.LeaderboardPlayerInfoAdapter
import com.sn.gameelectricity.viewmodel.request.RequestEquityViewModel
import com.sn.gameelectricity.viewmodel.state.MoneyViewModel
import kotlinx.android.synthetic.main.activity_leaderboard.*
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.common.ConfigConstants
import me.hgj.jetpackmvvm.ext.addMoreData
import me.hgj.jetpackmvvm.ext.inflate
import me.hgj.jetpackmvvm.ext.removeEmptyView
import me.hgj.jetpackmvvm.ext.util.toHtml
import me.hgj.jetpackmvvm.util.PublicPracticalMethodFromKT
import me.hgj.jetpackmvvm.util.ScreenTools
import me.hgj.jetpackmvvm.util.ToastUtil
import me.hgj.jetpackmvvm.util.load
import me.jessyan.autosize.AutoSizeConfig
import singleClick

/**
 * Date:2022/7/7
 * Time:9:42
 * author:dimple
 * 排行榜玩家信息
 */
class LeaderboardPlayerInfoDialog @JvmOverloads constructor(
    var mContext: Context,
    var moneyViewModel: MoneyViewModel,
    var requestEquityViewModel: RequestEquityViewModel,
    var width: Int = ScreenTools.getInstance().dp2px(mContext, 375f, true),
    var height: Int = WindowManager.LayoutParams.WRAP_CONTENT,
    var themeResId: Int = R.style.TransparentDialog
) : Dialog(mContext, themeResId) {

    private val mBinding by inflate<DialogPlayerinfoBinding>()

    private var pageNum: Int = 1

    private var userId: Long = 0

    private lateinit var leaderBoardBean: LeaderBoardBean

    //权益列表
    private var leaderboardPlayerInfoAdapter: LeaderboardPlayerInfoAdapter? = null

    //权益商品信息弹框
    private var equityProductDialog: EquityProductDialog? = null

    init {
        setContentView(mBinding.root)
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        setBG()
    }

    override fun show() {
        super.show()
        /**
         * 设置宽度全屏，要设置在show的后面
         */
        val layoutParams = window!!.attributes
        layoutParams.width = width
        layoutParams.height = height
        layoutParams.gravity = Gravity.BOTTOM
        window!!.setWindowAnimations(R.style.BottomShowAnimation)
        window!!.decorView.setPadding(0, 0, 0, 0)
        window!!.attributes = layoutParams
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initList()
        dataEcho()

        mBinding.apply {
            ivTitle.load(
                leaderBoardBean.avatar,
                R.drawable.default_user_avatar,
                R.drawable.default_user_avatar
            )
            tvName.text = leaderBoardBean.nickName


            if (leaderBoardBean.rank > 3) {
                ivRank.setImageDrawable(
                    ContextCompat.getDrawable(
                        mContext,
                        R.drawable.ge_leaderboard_rank_more
                    )
                )
                tvRank.apply {
                    visibility = View.VISIBLE
                    text = "${leaderBoardBean.rank}"
                }
            } else {
                tvRank.visibility = View.GONE
                var rankIcon = R.drawable.ge_leaderboard_rank_first
                when (leaderBoardBean.rank) {
                    2 -> {
                        rankIcon = R.drawable.ge_leaderboard_rank_sencond
                    }
                    3 -> {
                        rankIcon = R.drawable.ge_leaderboard_rank_third
                    }
                }
                ivRank.setImageDrawable(
                    ContextCompat.getDrawable(
                        mContext,
                        rankIcon
                    )
                )
            }
        }

        userId = leaderBoardBean.userId
        moneyViewModel.couponRightList(pageNum, 10, userId, true)//434
    }

    override fun onStart() {
        super.onStart()
    }

    /**
     * 初始化权益中心列表
     */
    private fun initList() {
        mBinding.apply {
            leaderboardPlayerInfoAdapter = LeaderboardPlayerInfoAdapter(mContext)
            recyclerView.apply {
                layoutManager = GridLayoutManager(mContext, 3)
                adapter = leaderboardPlayerInfoAdapter
                clearAnimation()
                setLoadingMoreBottomHeight(10f)

                addItemDecoration(
                    CommentDialogDecoration().getInstance()
                        .setSpaceItemDecoration(
                            0,
                            0,
                            0,
                            ScreenTools.getInstance().dp2px(mContext, 8f, true)
                        )
                )

                setOnLoadMoreListener {
                    pageNum++
                    moneyViewModel.couponRightList(pageNum, 10, userId)
                }

                setOnItemClickListener { v, position ->
                    val bean = leaderboardPlayerInfoAdapter?.getItemData(position)

                    equityProductDialog = EquityProductDialog(
                        mContext,
                        bean!!.goodsId,
                        bean.userId,
                        requestEquityViewModel,
                        WindowManager.LayoutParams.MATCH_PARENT,
                        (0.8 * AutoSizeConfig.getInstance().screenHeight).toInt()
                    )
                    equityProductDialog?.apply {
                        setonClickIvClose {
                            equityProductDialog = null
                        }
                        tvFriendOnclick()
                        tvEnemyOnclick()
                        toSure()
                        isShowCLShare(View.GONE)
                        show()
                    }
                }
            }
        }
    }

    /**
     * 数据回显
     */
    private fun dataEcho() {
        moneyViewModel.couponRightListLive.observe((mContext as AppCompatActivity)) { bean ->
            mBinding.tvEquityNums.text = "${bean.totalGoodsNum}"
            if (pageNum == 1) {
                if (bean.goodsVOList.isEmpty()) {
                    bindEmptyView()
                } else {
                    mBinding.recyclerView.removeEmptyView()
                    leaderboardPlayerInfoAdapter?.setNewDataIgnoreSize(bean.goodsVOList)
                }
            } else {
                leaderboardPlayerInfoAdapter?.addMoreData(bean.goodsVOList)
            }
        }
    }

    /**
     * 当列表数据为空的时候，显示
     */
    private fun bindEmptyView() {
        val errorPageView = ErrorPageView(mContext)
        with(errorPageView) {
            post {
                val recyclerView_height = mBinding.recyclerView.height
                setErrorIcon(
                    R.drawable.ge_error2,
                    ScreenTools.getInstance().dp2px(App.instance, 124f, true),
                    ScreenTools.getInstance().dp2px(App.instance, 124f, true)
                )
                setErrorContent("哦豁，一片荒芜～", 12f, "#57493B")
                changeErrorIconPositionToTOP(
                    (0.25 * recyclerView_height).toInt()
                )
                changeErrorTextPositionToTOP(
                    ScreenTools.getInstance().dp2px(App.instance, 5f, true)
                )
            }
        }

        mBinding.recyclerView.apply {
            setEmptyView(errorPageView)
            isLoadMoreEnabled = false
            leaderboardPlayerInfoAdapter?.setNewData(arrayListOf())
        }
    }

    /**
     * 设置背景
     */
    private fun setBG() {
        PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
            mBinding.clBg, -1, "",
            ScreenTools.getInstance().dp2px(mContext, 16f, true).toFloat(),
            ScreenTools.getInstance().dp2px(mContext, 16f, true).toFloat(),
            0f,
            0f,
            GradientDrawable.Orientation.LEFT_RIGHT, "#6C72FD", "#C973FE"
        )

        PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
            mBinding.clContent, -1, "",
            ScreenTools.getInstance().dp2px(mContext, 24f, true).toFloat(),
            ScreenTools.getInstance().dp2px(mContext, 24f, true).toFloat(),
            0f,
            0f,
            null, "#FFFFFF"
        )
    }

    /**
     * 关闭按钮
     */
    fun onClickClose(onCallBack: () -> Unit) {
        mBinding.ivClose.singleClick {
            dismissDialog()
            onCallBack()
        }
    }

    /**
     * 设置用户数据
     */
    fun setUserDatas(leaderBoardBean: LeaderBoardBean) {
        this.leaderBoardBean = leaderBoardBean
    }

    /**
     * 关闭弹框
     */
    fun dismissDialog() {
        if (isShowing) {
            dismiss()
        }
    }
}