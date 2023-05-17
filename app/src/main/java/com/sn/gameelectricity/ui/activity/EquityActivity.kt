package com.sn.gameelectricity.ui.activity

import android.graphics.Color
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.blankj.utilcode.util.NetworkUtils
import com.gyf.immersionbar.ImmersionBar
import com.sn.gameelectricity.app.base.BaseActivity1
import com.sn.gameelectricity.app.eventViewModel
import com.sn.gameelectricity.app.util.CacheUtil
import com.sn.gameelectricity.app.weight.customview.CommentDialogDecoration
import com.sn.gameelectricity.app.weight.dialog.EquityDescribeDialog
import com.sn.gameelectricity.app.weight.dialog.EquityProductDialog
import com.sn.gameelectricity.data.model.bean.GoodsRightListResponse
import com.sn.gameelectricity.databinding.ActivityEquitylBinding
import com.sn.gameelectricity.ui.adapter.EquityAdapter
import com.sn.gameelectricity.viewmodel.request.RequestEquityViewModel
import com.sn.gameelectricity.viewmodel.state.HomeViewModel
import kotlinx.android.synthetic.main.activity_main.*
import me.hgj.jetpackmvvm.util.ScreenTools
import me.hgj.jetpackmvvm.util.ToastUtil
import me.jessyan.autosize.AutoSizeConfig
import singleClick


/**
 * 权益中心 Activity
 */
class EquityActivity : BaseActivity1<HomeViewModel, ActivityEquitylBinding>() {

    private val requestEquityViewModel: RequestEquityViewModel by viewModels()
    private lateinit var equityAdapter: EquityAdapter
    private var goodsRightResponseList: MutableList<GoodsRightListResponse.GoodsVOListDTO> =
        mutableListOf()
    private var equityProductDialog: EquityProductDialog? = null
    private var equityDescribeDialog: EquityDescribeDialog? = null

    private var pageNum: Int = 1

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this)
            .transparentStatusBar()
            .titleBar(mViewBind.toolbar)
            .statusBarDarkFont(true)
            .navigationBarColorInt(Color.WHITE)
            .navigationBarDarkIcon(true)
            .init()

        mViewBind.apply {

//            appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
//                val offset = Math.abs(verticalOffset)
//                if (offset < appBarLayout.totalScrollRange.div(2) ?: 0) {
//                    BarUtils.setStatusBarLightMode(this@EquityActivity, false)
//                    toolbarBack.setImageResource(R.drawable.ge_arrow_back_white)
//                    tvTitle.setTextColor(Color.parseColor("#FFFFFF"))
//                } else if (offset > appBarLayout.totalScrollRange / 2) {
//                    BarUtils.setStatusBarLightMode(this@EquityActivity, true)
//                    toolbarBack.setImageResource(R.drawable.ge_arrow_back_black)
//                    tvTitle.setTextColor(Color.BLACK)
//                }
//            })

            toolbarBack.singleClick {
                finish()
            }

            btnSure.singleClick {
                eventViewModel.choiseIndex.value = 0
                finish()
            }

            ivEquityRule.singleClick {
                equityDescribeDialog = EquityDescribeDialog(
                    this@EquityActivity,
                    WindowManager.LayoutParams.MATCH_PARENT,
                    (0.7 * AutoSizeConfig.getInstance().screenHeight).toInt()
                )
                equityDescribeDialog?.apply {
                    setonClickIvClose {
                        equityDescribeDialog = null
                    }
                    show()
                }
            }

            equityAdapter = EquityAdapter(this@EquityActivity)
            recyclerView.apply {
                layoutManager = GridLayoutManager(this@EquityActivity, 3)
                adapter = equityAdapter
                addItemDecoration(
                    CommentDialogDecoration().getInstance()
                        .setSpaceItemDecoration(
                            ScreenTools.getInstance().dp2px(this@EquityActivity, 4f, true),
                            ScreenTools.getInstance().dp2px(this@EquityActivity, 4f, true),
                            ScreenTools.getInstance().dp2px(this@EquityActivity, 4f, true),
                            ScreenTools.getInstance().dp2px(this@EquityActivity, 4f, true)
                        )
                )

                setOnRefreshListener {
                    if (!NetworkUtils.isConnected()) {
                        this.isRefreshing = false
                        ToastUtil.showCenter("网络异常，请检查网络设置")
                        return@setOnRefreshListener
                    }
                    pageNum = 1
                    requestEquityViewModel.goodsRightList(pageNum, 50)
                }

//                setOnLoadMoreListener {
//                    pageNum++
//                    requestEquityViewModel.goodsRightList(pageNum, 50)
//                }

                clearAnimation()
                setLoadingMoreBottomHeight(46f)

                setOnItemClickListener { v, position ->
                    val itemData = equityAdapter.getItemData(position)
                    if (itemData.goodsId > 0) {
                        equityProductDialog = EquityProductDialog(
                            this@EquityActivity,
                            itemData.goodsId,
                            CacheUtil.getUser()?.userId!!,
                            requestEquityViewModel,
                            WindowManager.LayoutParams.MATCH_PARENT,
                            (0.7 * AutoSizeConfig.getInstance().screenHeight).toInt()
                        )
                        equityProductDialog?.apply {
                            setonClickIvClose {
                            }
                            tvFriendOnclick()
                            tvEnemyOnclick()
                            toSure()
                            toRecover()
                            show()
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        requestEquityViewModel.goodsRightList(pageNum, 50)
        requestEquityViewModel.userCoupon()
    }

    override fun createObserver() {
        requestEquityViewModel.goodsRightList.observe(this, Observer {
            goodsRightResponseList.clear()
            if (it.goodsVOList.size < 12) {
                for (index in 1..12) {
                    goodsRightResponseList.add(GoodsRightListResponse.GoodsVOListDTO())
                }
//                for (index in 1..it.totalGoodsNum) {
//                    goodsRightResponseList.add(GoodsRightListResponse.GoodsVOListDTO())
//                }
            } else {
                for (index in 1..(it.goodsVOList.size + it.goodsVOList.size % 3 + 3)) {
                    goodsRightResponseList.add(GoodsRightListResponse.GoodsVOListDTO())
                }
            }

            if (pageNum == 1) {
                if (!it.goodsVOList.isNullOrEmpty()) {
                    for ((index2, value) in it.goodsVOList.withIndex()) {
                        goodsRightResponseList[index2] = GoodsRightListResponse.GoodsVOListDTO(
                            value.goodsId,
                            value.goodsIcon,
                            value.number
                        )
                    }
                }

                mViewBind.recyclerView.isLoadMoreEnabled = !it.goodsVOList.isNullOrEmpty()
                equityAdapter.setNewData(goodsRightResponseList)
            } else {
                mViewBind.recyclerView.loadMoreEnd()
//                equityAdapter.addMoreData(it.goodsVOList)
            }

        })

        requestEquityViewModel.userCouponLiveData.observe(this, Observer {
            mViewBind.tvGoldCouponNum.text = "${it.goldCouponNum}/1"
            mViewBind.tvSilverCouponNum.text = "${it.silverCouponNum}/1"
            mViewBind.tvBronzeCouponNum.text = "${it.bronzeCouponNum}/1"
        })

    }

}