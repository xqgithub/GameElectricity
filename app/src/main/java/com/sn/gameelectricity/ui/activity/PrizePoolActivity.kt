package com.sn.gameelectricity.ui.activity

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.BarUtils
import com.google.android.material.appbar.AppBarLayout
import com.gyf.immersionbar.ImmersionBar
import com.sn.gameelectricity.app.base.BaseActivity1
import com.sn.gameelectricity.app.eventViewModel
import com.sn.gameelectricity.app.weight.customview.CommentDialogDecoration
import com.sn.gameelectricity.app.weight.dialog.GoldLackDialog
import com.sn.gameelectricity.data.model.bean.AwardPoolListResponse
import com.sn.gameelectricity.databinding.ActivityPrizePoolBinding
import com.sn.gameelectricity.ui.adapter.PrizePoolAdapter
import com.sn.gameelectricity.viewmodel.request.RequestHomeViewModel
import com.sn.gameelectricity.viewmodel.state.HomeViewModel
import com.sn.gameelectricity.viewmodel.state.MeViewModel
import kotlinx.android.synthetic.main.activity_main.*
import me.hgj.jetpackmvvm.util.PublicPracticalMethodFromKT
import me.hgj.jetpackmvvm.util.ScreenTools
import me.hgj.jetpackmvvm.util.ToastUtil
import singleClick
import java.text.DecimalFormat
import java.text.MessageFormat


/**
 * 奖品池 Activity
 */
class PrizePoolActivity : BaseActivity1<HomeViewModel, ActivityPrizePoolBinding>() {
    private val requestHomeViewModel: RequestHomeViewModel by viewModels()
    private val meViewModel: MeViewModel by viewModels()
    private lateinit var prizePoolAdapter: PrizePoolAdapter

    private val allList = arrayListOf<AwardPoolListResponse>()
    private val legendList = arrayListOf<AwardPoolListResponse>()
    private val epicList = arrayListOf<AwardPoolListResponse>()
    private val rareList = arrayListOf<AwardPoolListResponse>()
    private val valuableList = arrayListOf<AwardPoolListResponse>()
    private var poolType: Int = 1
    private var isRefresh: Boolean = false
    private var refreshGoldCoinNum: Int = 0

    val goldLackDialog by lazy {
        GoldLackDialog(
            this,
            ScreenTools.getInstance().dp2px(this, 240f, true)
        )
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

            appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
                val offset = Math.abs(verticalOffset)
                if (offset < appBarLayout.totalScrollRange.div(2) ?: 0) {
                    BarUtils.setStatusBarLightMode(this@PrizePoolActivity, false)
                } else if (offset > appBarLayout.totalScrollRange / 2) {
                    BarUtils.setStatusBarLightMode(this@PrizePoolActivity, true)
                }
            })

            toolbarBack.singleClick {
                finish()
            }

            prizePoolAdapter = PrizePoolAdapter(this@PrizePoolActivity)
            recyclerView.apply {
                layoutManager = LinearLayoutManager(this@PrizePoolActivity)
                adapter = prizePoolAdapter
                addItemDecoration(
                    CommentDialogDecoration().getInstance()
                        .setSpaceItemDecoration(
                            ScreenTools.getInstance().dp2px(this@PrizePoolActivity, 12f, true),
                            ScreenTools.getInstance().dp2px(this@PrizePoolActivity, 8f, true),
                            ScreenTools.getInstance().dp2px(this@PrizePoolActivity, 12f, true),
                            ScreenTools.getInstance().dp2px(this@PrizePoolActivity, 4f, true)
                        )
                )

                clearAnimation()
                setLoadingMoreBottomHeight(86f)

                setOnItemClickListener { v, position ->
                    val itemData = prizePoolAdapter.getItemData(position)
                    when (itemData.awardType) {
                        1 -> {
                            with(Bundle()) {
                                putInt("goodsId", itemData.awardId)
                                PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                                    this@PrizePoolActivity,
                                    ProductDetailsActivity::class.java, this, false
                                )
                            }
                        }
                    }
                }

            }

            clAll.singleClick {
                poolType = 1
                refurbishData()
                initColor()
                tvAllTex.setTextColor(Color.parseColor("#EF874E"))
                tvAll.setTextColor(Color.parseColor("#EF874E"))
            }

            clLegend.singleClick {
                poolType = 2
                refurbishData()
                initColor()
                tvLegendTex.setTextColor(Color.parseColor("#EF874E"))
                tvLegend.setTextColor(Color.parseColor("#EF874E"))
            }

            clEpic.singleClick {
                poolType = 3
                refurbishData()
                initColor()
                tvEpicTex.setTextColor(Color.parseColor("#EF874E"))
                tvEpic.setTextColor(Color.parseColor("#EF874E"))
            }

            clRare.singleClick {
                poolType = 4
                refurbishData()
                initColor()
                tvRareTex.setTextColor(Color.parseColor("#EF874E"))
                tvRare.setTextColor(Color.parseColor("#EF874E"))
            }

            clValuable.singleClick {
                poolType = 5
                refurbishData()
                initColor()
                tvValuableTex.setTextColor(Color.parseColor("#EF874E"))
                tvValuable.setTextColor(Color.parseColor("#EF874E"))
            }

            clRefresh.singleClick {
                meViewModel.getuserInfo()
            }

        }
    }

    private fun ActivityPrizePoolBinding.initColor() {
        tvAllTex.setTextColor(Color.parseColor("#57493B"))
        tvAll.setTextColor(Color.parseColor("#57493B"))

        tvLegendTex.setTextColor(Color.parseColor("#57493B"))
        tvLegend.setTextColor(Color.parseColor("#57493B"))

        tvEpicTex.setTextColor(Color.parseColor("#57493B"))
        tvEpic.setTextColor(Color.parseColor("#57493B"))

        tvRareTex.setTextColor(Color.parseColor("#57493B"))
        tvRare.setTextColor(Color.parseColor("#57493B"))

        tvValuableTex.setTextColor(Color.parseColor("#57493B"))
        tvValuable.setTextColor(Color.parseColor("#57493B"))
    }

    override fun onResume() {
        super.onResume()
        requestHomeViewModel.awardPool()
    }

    override fun createObserver() {
        requestHomeViewModel.awardPoolResponseLiveData.observe(this, Observer {
            if (isRefresh) {
                ToastUtil.showCenter("刷新成功")
                isRefresh = false
            }
            allList.clear()
            legendList.clear()
            epicList.clear()
            rareList.clear()
            valuableList.clear()
            //传说
            it.legend?.let { it1 ->
                it1.awardList.forEach { it2 ->
                    legendList.add(
                        AwardPoolListResponse(
                            it2.awardId,
                            it2.awardType,
                            it2.goodsName,
                            it2.icon,
                            it2.defaultPrice,
                            it2.discountPrice, 2
                        )
                    )
                    allList.add(
                        AwardPoolListResponse(
                            it2.awardId,
                            it2.awardType,
                            it2.goodsName,
                            it2.icon,
                            it2.defaultPrice,
                            it2.discountPrice, 2
                        )
                    )
                }
                mViewBind.tvLegend.text = MessageFormat.format("{0}%", it1.ratio)
            }
            //史诗
            it.epic?.let { it1 ->
                it1.awardList.forEach { it2 ->
                    epicList.add(
                        AwardPoolListResponse(
                            it2.awardId,
                            it2.awardType,
                            it2.goodsName,
                            it2.icon,
                            it2.defaultPrice,
                            it2.discountPrice, 3
                        )
                    )
                    allList.add(
                        AwardPoolListResponse(
                            it2.awardId,
                            it2.awardType,
                            it2.goodsName,
                            it2.icon,
                            it2.defaultPrice,
                            it2.discountPrice, 3
                        )
                    )
                }
                mViewBind.tvEpic.text = MessageFormat.format("{0}%", it1.ratio)
            }
            //稀有
            it.rare?.let { it1 ->
                it1.awardList.forEach { it2 ->
                    rareList.add(
                        AwardPoolListResponse(
                            it2.awardId,
                            it2.awardType,
                            it2.goodsName,
                            it2.icon,
                            it2.defaultPrice,
                            it2.discountPrice, 4
                        )
                    )
                    allList.add(
                        AwardPoolListResponse(
                            it2.awardId,
                            it2.awardType,
                            it2.goodsName,
                            it2.icon,
                            it2.defaultPrice,
                            it2.discountPrice, 4
                        )
                    )
                }
                mViewBind.tvRare.text = MessageFormat.format("{0}%", it1.ratio)
            }
            //尊贵
            it.valuable?.let { it1 ->
                it1.awardList.forEach { it2 ->
                    valuableList.add(
                        AwardPoolListResponse(
                            it2.awardId,
                            it2.awardType,
                            it2.goodsName,
                            it2.icon,
                            it2.defaultPrice,
                            it2.discountPrice, 5
                        )
                    )
                    allList.add(
                        AwardPoolListResponse(
                            it2.awardId,
                            it2.awardType,
                            it2.goodsName,
                            it2.icon,
                            it2.defaultPrice,
                            it2.discountPrice, 5
                        )
                    )
                }
                mViewBind.tvValuable.text = MessageFormat.format("{0}%", it1.ratio)
            }
            //心愿购
            it.wishShopping?.let { it1 ->
                it1.awardList.forEach { it2 ->
                    allList.add(
                        AwardPoolListResponse(
                            it2.awardId,
                            it2.awardType,
                            it2.goodsName,
                            it2.icon,
                            it2.defaultPrice,
                            it2.discountPrice, 1
                        )
                    )
                }
                mViewBind.tvWishShopping.visibility = View.VISIBLE
                mViewBind.tvContext.text = MessageFormat.format(
                    "含心愿购共{0}款({1}款传说 {2}款史诗 {3}款稀有 {4}款尊贵 合计{5}款)",
                    allList.size,
                    legendList.size,
                    epicList.size,
                    rareList.size,
                    valuableList.size,
                    (legendList.size + epicList.size + rareList.size + valuableList.size)
                )
            } ?: let {
                mViewBind.tvWishShopping.visibility = View.GONE
                mViewBind.tvContext.text = MessageFormat.format(
                    "共{0}款({1}款传说 {2}款史诗 {3}款稀有 {4}款尊贵 合计{5}款)",
                    allList.size,
                    legendList.size,
                    epicList.size,
                    rareList.size,
                    valuableList.size,
                    (legendList.size + epicList.size + rareList.size + valuableList.size)
                )
            }

            mViewBind.tvGoldCoinPrice.text = MessageFormat.format(
                "x{0}",
                it.refreshNeedGoldCoinNum
            )

            it.refreshNeedGoldCoinNum?.let {
                refreshGoldCoinNum = it.toString().toInt()
            }

            if(refreshGoldCoinNum == 700){
                mViewBind.tvGoldCoinPrice.visibility = View.GONE
                mViewBind.clGold.visibility = View.VISIBLE
            }

            refurbishData()

        })

        meViewModel.userInfoLive.observe(this) { beans ->
            mViewBind.apply {
                if (refreshGoldCoinNum < beans.goldCoin) {
                    requestHomeViewModel.putAwardPool {
                        tvGoldCoinPrice.visibility = View.GONE
                        clGold.visibility = View.VISIBLE
                    }
                    isRefresh = true
                } else {
                    goldLackDialog.apply {
                        onClickSure {
                            finish()
                            eventViewModel.earncoins.value = 1
                        }
                        show()
                    }
                }
            }
        }
    }

    private fun refurbishData() {
        when (poolType) {
            1 -> prizePoolAdapter.setNewData(allList)
            2 -> prizePoolAdapter.setNewData(legendList)
            3 -> prizePoolAdapter.setNewData(epicList)
            4 -> prizePoolAdapter.setNewData(rareList)
            5 -> prizePoolAdapter.setNewData(valuableList)
        }
    }


}