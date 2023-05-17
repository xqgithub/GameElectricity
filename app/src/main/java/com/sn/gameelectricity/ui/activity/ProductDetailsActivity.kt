package com.sn.gameelectricity.ui.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.os.CountDownTimer
import android.text.SpannableString
import android.text.style.LeadingMarginSpan
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.imageview.ShapeableImageView
import com.gyf.immersionbar.ImmersionBar
import com.sn.gameelectricity.R
import com.sn.gameelectricity.app.base.BaseActivity1
import com.sn.gameelectricity.app.eventViewModel
import com.sn.gameelectricity.app.util.CacheUtil
import com.sn.gameelectricity.app.util.ShareManager
import com.sn.gameelectricity.app.util.load
import com.sn.gameelectricity.app.weight.customview.FigureIndicatorView
import com.sn.gameelectricity.app.weight.dialog.*
import com.sn.gameelectricity.data.model.bean.AddWishBus
import com.sn.gameelectricity.data.model.bean.GoodsGetgoodsResponse
import com.sn.gameelectricity.databinding.ActivityProductDetailsBinding
import com.sn.gameelectricity.ui.activity.web.WebActivity
import com.sn.gameelectricity.ui.adapter.BannerAdapter
import com.sn.gameelectricity.ui.adapter.ProductPicAdapter
import com.sn.gameelectricity.ui.fragment.OrderListFragment
import com.sn.gameelectricity.viewmodel.request.RequestCheatingViewModel
import com.sn.gameelectricity.viewmodel.request.RequestEquityViewModel
import com.sn.gameelectricity.viewmodel.request.RequestProductDetailsViewModel
import com.sn.gameelectricity.viewmodel.state.ProductDetailsViewModel
import com.zhpan.bannerview.constants.IndicatorGravity
import com.zhpan.indicator.base.IIndicator
import kotlinx.android.synthetic.main.activity_product_details.view.*
import me.hgj.jetpackmvvm.util.PublicPracticalMethodFromKT
import me.hgj.jetpackmvvm.util.ScreenTools
import me.shaohui.bottomdialog.BottomDialog
import singleClick
import java.lang.ref.WeakReference
import java.text.DecimalFormat


/**
 * 商品详情
 */
class ProductDetailsActivity :
    BaseActivity1<ProductDetailsViewModel, ActivityProductDetailsBinding>() {

    private var mPictureList: MutableList<String> = ArrayList()
    private lateinit var goodsGetgoodsResponse: GoodsGetgoodsResponse
    private var buyType = 1
    private val requestProductDetailsViewModel: RequestProductDetailsViewModel by viewModels()
    private val requestCheatingViewModel: RequestCheatingViewModel by viewModels()
    private val requestEquityViewModel: RequestEquityViewModel by viewModels()

    val shareProductDialog by lazy {
        ShareProductDialog(
            this,
            ScreenTools.getInstance().dp2px(this, 375f, true)
        )
    }

    val equitySuccDialog by lazy {
        EquitySuccDialog(this)
    }

    val equityFailDialog by lazy {
        EquityFailDialog(this)
    }

    val beRewardFailDialog by lazy {
        BeRewardFailDialog(this)
    }

    private val goodsId: Int by lazy {
        intent.getIntExtra("goodsId", 0)
    }

    private val scoreDialog: BottomDialog by lazy {
        BottomDialog.create(supportFragmentManager)
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
                    toolbarBack.setImageDrawable(resources?.getDrawable(R.drawable.ic_pro_back))
                    toolbarShare.setImageDrawable(resources?.getDrawable(R.drawable.ic_pro_share))
                    BarUtils.setStatusBarLightMode(this@ProductDetailsActivity, false)
                } else if (offset > appBarLayout.totalScrollRange / 2) {
                    toolbarBack.setImageDrawable(resources?.getDrawable(R.drawable.ic_nav_arrow_icon_left))
                    toolbarShare.setImageDrawable(resources?.getDrawable(R.drawable.ic_af_share_black))
                    BarUtils.setStatusBarLightMode(this@ProductDetailsActivity, true)
                }
            })

            toolbarBack.singleClick {
                finish()
            }

            toolbarShare.singleClick {

                shareProductDialog.apply {

                    onWxClick {
                        Glide.with(this@ProductDetailsActivity)
                            .asBitmap()
                            .load(goodsGetgoodsResponse.photoList[0])
                            .into(object : SimpleTarget<Bitmap?>() {
                                override fun onResourceReady(
                                    resource: Bitmap,
                                    transition: Transition<in Bitmap?>?
                                ) {
                                    ShareManager.shareManager.shareWebpage2WxFriend(
                                        "前方有羊毛可薅",
                                        "小可爱，我发现一个薅羊毛的APP，来不及解释快上车！！！",
                                        "${CacheUtil.getUser()?.loadingUrl}?userCode=${CacheUtil.getUser()?.userCode}",
                                        resource
                                    )
                                }
                            })

                    }
                    onPyqClick {
                        Glide.with(this@ProductDetailsActivity)
                            .asBitmap()
                            .load(goodsGetgoodsResponse.photoList[0])
                            .into(object : SimpleTarget<Bitmap?>() {
                                override fun onResourceReady(
                                    resource: Bitmap,
                                    transition: Transition<in Bitmap?>?
                                ) {
                                    ShareManager.shareManager.shareWebpage2WxMoments(
                                        "前方有羊毛可薅",
                                        "小可爱，我发现一个薅羊毛的APP，来不及解释快上车！！！",
                                        "${CacheUtil.getUser()?.loadingUrl}?userCode=${CacheUtil.getUser()?.userCode}",
                                        resource
                                    )
                                }
                            })
                    }
                    onQqClick {
                        ShareManager.shareManager.shareWebpage2QQImg(
                            "${CacheUtil.getUser()?.loadingUrl}?userCode=${CacheUtil.getUser()?.userCode}",
                            "前方有羊毛可薅",
                            "小可爱，我发现一个薅羊毛的APP，来不及解释快上车！！！", goodsGetgoodsResponse.photoList[0]
                        )
                    }
                    onWbClick {
                        ShareManager.shareManager.shareWebpage2Weibo(
                            "${CacheUtil.getUser()?.loadingUrl}?userCode=${CacheUtil.getUser()?.userCode}",
                            "前方有羊毛可薅"
                        )
                    }
                    show()
                }
            }


            clScore.singleClick {
                buyType = 1
                scoreDialog.show()
            }

            clGoldCoin.singleClick {
                buyType = 2
                scoreDialog.show()
            }

            clCheating.singleClick {
                LogUtils.e(goodsGetgoodsResponse.groupAssistVO)
                goodsGetgoodsResponse.groupAssistVO?.let {
                    jumpToCheating(it.status, it.groupCode, it.orderId)

                } ?: let {
                    //创建助力
                    requestCheatingViewModel.assist(goodsId)
                }
            }

            clAddWish.singleClick {
                if (CacheUtil.getWished()?.isWish == true) {
                    val addWishedDialog = AddWishedDialog(this@ProductDetailsActivity)
                    addWishedDialog.onClickSure {
                        requestProductDetailsViewModel.wish(goodsId)
                    }
                    addWishedDialog.show()
                } else {
                    val addWishDialog = AddWishDialog(this@ProductDetailsActivity)
                    addWishDialog.onClickSure {
                        requestProductDetailsViewModel.wish(goodsId)
                    }
                    addWishDialog.show()
                }
            }

            clAddWishNow.singleClick {
                eventViewModel.choiseHomeTop.value = 1
                finish()
            }

            tvOperate.singleClick {
                requestEquityViewModel.rightsActivity(goodsGetgoodsResponse.rightsActivityId, {
                    equitySuccDialog.show()
                    equitySuccDialog.onClickSure {
                    }
                    equitySuccDialog.onClickSee {
                        with(Bundle()) {
                            PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                                this@ProductDetailsActivity,
                                EquityActivity::class.java, this, false
                            )
                        }
                    }
                }, {
                    if (it == 1879113784) {
                        beRewardFailDialog.setContent("这个商品太火被换光啦！")
                        beRewardFailDialog.show()
                    } else if (it == 1879113785) {
                        equityFailDialog.setContent("你的奖券数量不足")
                        equityFailDialog.onClickTickets {
                            PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                                this@ProductDetailsActivity,
                                LeaderboardActivity::class.java, null, false
                            )
                        }
                        equityFailDialog.show()
                    } else {
                        beRewardFailDialog.setContent("活动君走神了！")
                        beRewardFailDialog.show()
                    }
                })
            }
        }
    }

    private fun jumpToCheating(status: Int, groupCode: String, orderId: Int) {
        when (status) {
            2 -> {
                with(Bundle()) {
                    putSerializable("type", OrderListFragment.OrderType.PENDING_PAYMENT)
                    putInt("orderId", orderId)
                    PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                        this@ProductDetailsActivity,
                        OrderDetailsActivity::class.java, this, false
                    )
                }
            }
            1 -> {
                with(Bundle()) {
                    putString("groupCode", groupCode)
                    PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                        this@ProductDetailsActivity,
                        CheatingActivity::class.java, this, false
                    )
                }
            }
            0 -> {
                //创建助力
                requestCheatingViewModel.assist(goodsId)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        requestProductDetailsViewModel.goodsGetGoods(goodsId)
    }

    override fun createObserver() {
        requestProductDetailsViewModel.productGetGoods.observe(this, Observer {
            mPictureList.clear()
            it.photoList.forEach {
                mPictureList.add(it)
            }

            goodsGetgoodsResponse = it
            loadProductData(it)

            scoreDialog.setViewListener {
                initPayDialog(it)
            }.setLayoutRes(R.layout.dialog_bottom_score_label)

        })

        requestProductDetailsViewModel.wishLiveData.observe(this, Observer {
            CacheUtil.setWished(AddWishBus(goodsGetgoodsResponse.icon, true))
            eventViewModel.addWishEvent.value = AddWishBus(goodsGetgoodsResponse.icon, true)
            mViewBind.clAddWish.visibility = View.GONE
            mViewBind.clAddWishNow.visibility = View.VISIBLE
            val addWishAwardDialog = AddWishAwardDialog(this@ProductDetailsActivity)
            addWishAwardDialog.onClickSure {
                eventViewModel.choiseHomeTop.value = 1
                finish()
            }
            addWishAwardDialog.show()
        })

        requestCheatingViewModel.assistLiveData.observe(this, Observer {
            with(Bundle()) {
                putString("groupCode", it.groupCode)
                PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                    this@ProductDetailsActivity,
                    CheatingActivity::class.java, this, false
                )
            }
        })
    }

    private lateinit var productPicAdapter: ProductPicAdapter

    fun loadProductData(goodsGetgoodsResponse: GoodsGetgoodsResponse) {

        mViewBind.imageBanner
            .setLifecycleRegistry(lifecycle)
            .setAdapter(BannerAdapter())
            .setIndicatorView(setupIndicatorView())
            .setIndicatorGravity(IndicatorGravity.END)
            .create(mPictureList)

        mViewBind.apply {
            if (goodsGetgoodsResponse.topicId == 3) {
                clCoupon.visibility = View.VISIBLE
                cl002.visibility = View.VISIBLE
                cl001.visibility = View.GONE
                if (goodsGetgoodsResponse.usedNum == goodsGetgoodsResponse.totalNum) {
                    tvOperate.isEnabled = false
                    tvOperate.text = "已抢完"
                    tvOperate.setTextColor(Color.parseColor("#FFFFFF"))
                    tvOperate.setBackgroundResource(R.drawable.shape_radius_ced3de)
                } else {
                    tvOperate.isEnabled = true
                    tvOperate.text = "立即兑换"
                    tvOperate.setTextColor(Color.parseColor("#FFFFFF"))
                    tvOperate.setBackgroundResource(R.drawable.shape_radius_f19b3f)
                }
                when (goodsGetgoodsResponse.couponType) {//权益兑换类型 1-金券 2-银券 3-青铜券
                    1 -> {
                        tvCouponNum.text = "黄金奖券 x${goodsGetgoodsResponse.couponNum}"
                        sivImgCouponImg.setImageResource(R.drawable.ic_equity_level_golden)
                    }
                    2 -> {
                        tvCouponNum.text = "白银奖券 x${goodsGetgoodsResponse.couponNum}"
                        sivImgCouponImg.setImageResource(R.drawable.ic_equity_level_golden2)
                    }
                    3 -> {
                        tvCouponNum.text = "青铜奖券 x${goodsGetgoodsResponse.couponNum}"
                        sivImgCouponImg.setImageResource(R.drawable.ic_equity_level_golden3)
                    }
                }
            } else {
                clCoupon.visibility = View.GONE
                cl002.visibility = View.GONE
                cl001.visibility = View.VISIBLE
            }


            if (goodsGetgoodsResponse.isEveryDayActivity) {
                tvDailyRob.visibility = View.VISIBLE
            } else {
                tvDailyRob.visibility = View.GONE
            }
            if (goodsGetgoodsResponse.mark.isNullOrEmpty()) {
                tvMark.visibility = View.GONE
            } else {
                tvMark.text = goodsGetgoodsResponse.mark
                tvMark.visibility = View.VISIBLE
            }

            if (tvDailyRob.isVisible && tvMark.isVisible) {
                val standard = LeadingMarginSpan.Standard(
                    (SizeUtils.getMeasuredWidth(tvDailyRob) + SizeUtils.getMeasuredWidth(tvMark) + AdaptScreenUtils.pt2Px(
                        8f
                    )),
                    0
                )
                val goodsNameDes = SpannableString(goodsGetgoodsResponse.goodsName)
                goodsNameDes.setSpan(
                    standard,
                    0,
                    goodsNameDes.length,
                    SpannableString.SPAN_INCLUSIVE_INCLUSIVE
                )
                tvGoodsName.text = goodsNameDes
            } else if (tvDailyRob.isVisible) {
                val standard =
                    LeadingMarginSpan.Standard(
                        SizeUtils.getMeasuredWidth(tvDailyRob) + AdaptScreenUtils.pt2Px(
                            8f
                        ), 0
                    )
                val goodsNameDes = SpannableString(goodsGetgoodsResponse.goodsName)
                goodsNameDes.setSpan(
                    standard,
                    0,
                    goodsNameDes.length,
                    SpannableString.SPAN_INCLUSIVE_INCLUSIVE
                )
                tvGoodsName.text = goodsNameDes
            } else if (tvMark.isVisible) {
                val standard = LeadingMarginSpan.Standard(
                    SizeUtils.getMeasuredWidth(tvMark) + AdaptScreenUtils.pt2Px(8f), 0
                )
                val goodsNameDes = SpannableString(goodsGetgoodsResponse.goodsName)
                goodsNameDes.setSpan(
                    standard,
                    0,
                    goodsNameDes.length,
                    SpannableString.SPAN_INCLUSIVE_INCLUSIVE
                )
                tvGoodsName.text = goodsNameDes
            } else {
                tvGoodsName.text = goodsGetgoodsResponse.goodsName
            }

            tvDiscountPrice.text =
                "¥" + DecimalFormat("#.##").format(goodsGetgoodsResponse.discountPrice)
            tvDefaultPrice.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
            tvDefaultPrice.text =
                "原价 ¥" + DecimalFormat("#.##").format(goodsGetgoodsResponse.defaultPrice)
            tvPatternName.text = goodsGetgoodsResponse.patternName
            tvSupplierName.text = goodsGetgoodsResponse.supplierName
            sivImg01.load(goodsGetgoodsResponse.supplierIcon)

            goodsGetgoodsResponse.groupAssistVO?.let {
                when (goodsGetgoodsResponse.groupAssistVO.status) {
                    2 -> {
                        tvCheatingDes.text = "助力成功"
                        tvGroupSuccessPrice.visibility = View.VISIBLE
                        tvGroupTime.visibility = View.GONE
                    }
                    1 -> {
                        tvGroupSuccessPrice.visibility = View.GONE
                        tvGroupTime.visibility = View.VISIBLE
                        tvCheatingDes.text = "助力中..."
                        val differ =
                            TimeUtils.string2Millis(goodsGetgoodsResponse.groupAssistVO.endTime) - System.currentTimeMillis()
                        getCountDownTime(differ)
                    }
                    else -> {
                        tvGroupSuccessPrice.visibility = View.VISIBLE
                        tvGroupTime.visibility = View.GONE
                        tvCheatingDes.text = "邀请好友助力"
                    }
                }
            }

            if (goodsGetgoodsResponse.supplierName.isNullOrEmpty()) {
                clSupplier.visibility = View.GONE
            } else {
                clSupplier.visibility = View.VISIBLE
            }

            clSupplier.singleClick {
                goodsGetgoodsResponse.supplierWebUrl?.let {
                    val intent = Intent(this@ProductDetailsActivity, WebActivity::class.java)
                    intent.putExtra("url", goodsGetgoodsResponse.supplierWebUrl)
                    intent.putExtra("title", goodsGetgoodsResponse.supplierName)
                    startActivity(intent)
                }
            }

            tvGoodsDec.text = goodsGetgoodsResponse.goodsDec

            productPicAdapter = ProductPicAdapter(this@ProductDetailsActivity)
            recyclerView.apply {
                layoutManager = LinearLayoutManager(this@ProductDetailsActivity)
                adapter = productPicAdapter
            }

            productPicAdapter.setNewData(goodsGetgoodsResponse.goodsPicList as List<String>)

            tvDefaultScore.text = goodsGetgoodsResponse.defaultScore.toString()

            tvGroupSuccessPrice.text =
                "¥" + DecimalFormat("0.00").format(goodsGetgoodsResponse.groupSuccessPrice)
            tvGoldCoinPrice.text =
                "¥" + DecimalFormat("0.00").format(goodsGetgoodsResponse.goldCoinPrice)

            if (goodsGetgoodsResponse.desireType == 1) {
                clAddWish.visibility = View.VISIBLE
                clAddWishNow.visibility = View.GONE
            } else {
                clAddWish.visibility = View.GONE
                clAddWishNow.visibility = View.VISIBLE
            }
        }
    }

    private fun setupIndicatorView(): IIndicator? {
        val indicatorView = FigureIndicatorView(this)
        indicatorView.setRadius(resources!!.getDimensionPixelOffset(R.dimen.dp18))
        indicatorView.setTextSize(resources!!.getDimensionPixelSize(R.dimen.sp13))
        indicatorView.setBackgroundColor(Color.parseColor("#66000000"))
        return indicatorView
    }

    private var timer: CountDownTimer? = null
    private fun getCountDownTime(time: Long) {
        if (time > 0) {
            timer = MakeCountDown(mViewBind.root, time, 1000)
            timer?.start()
        }
    }

    private class MakeCountDown(
        targetView: View,
        millisInFuture: Long,
        countDownInterval: Long
    ) : CountDownTimer(millisInFuture, countDownInterval) {
        private val weakTargetView: WeakReference<View> = WeakReference(targetView)
        private val numberFormat = DecimalFormat("00")

        override fun onTick(millisUntilFinished: Long) {
            val targetView = weakTargetView.get()
            if (targetView != null) {
                val day = millisUntilFinished / (1000 * 24 * 60 * 60) //单位天
                val hour =
                    (millisUntilFinished - day * (1000 * 24 * 60 * 60)) / (1000 * 60 * 60) //单位时
                val minute =
                    (millisUntilFinished - day * (1000 * 24 * 60 * 60) - hour * (1000 * 60 * 60)) / (1000 * 60) //单位分
                val second =
                    (millisUntilFinished - day * (1000 * 24 * 60 * 60) - hour * (1000 * 60 * 60) - minute * (1000 * 60)) / 1000 //单位秒
                targetView.tvGroupTime.text =
                    numberFormat.format(minute) + ":" + numberFormat.format(second)
            }
        }

        override fun onFinish() {
            val targetView = weakTargetView.get()
            if (targetView != null) {
                targetView.tvGroupSuccessPrice.visibility = View.VISIBLE
                targetView.tvGroupTime.visibility = View.GONE
                targetView.tvCheatingDes.text = "邀请好友助力"
            }
        }
    }

    private fun initPayDialog(view: View) {
        val imgClose = view.findViewById<ImageView>(R.id.imgClose)
        val ivIcon = view.findViewById<ShapeableImageView>(R.id.ivIcon)
        val tvDiscount = view.findViewById<TextView>(R.id.tvDiscount)
        val tvPatternName = view.findViewById<TextView>(R.id.tvPatternName)
        val tvPrice = view.findViewById<TextView>(R.id.tvPrice)
        val tvGroupPrice = view.findViewById<TextView>(R.id.tvGroupPrice)
        val tvMark = view.findViewById<TextView>(R.id.tvMark)
        val tvBuyNumber = view.findViewById<TextView>(R.id.tvBuyNumber)
        val tvGroupUserNum = view.findViewById<TextView>(R.id.tvGroupUserNum)
        val imgSub = view.findViewById<ImageView>(R.id.imgSub)
        val imgAdd = view.findViewById<ImageView>(R.id.imgAdd)

        val clShare = view.findViewById<ConstraintLayout>(R.id.clShare)
        val tvConfirm = view.findViewById<TextView>(R.id.tvConfirm)

        imgClose.singleClick {
            scoreDialog.dismiss()
        }

        ivIcon.load(goodsGetgoodsResponse.icon)

        tvPatternName.text = goodsGetgoodsResponse.patternName

        if (buyType == 1) {
            tvDiscount.text = "¥ 0"
            tvPrice.text = "消耗积分 " + goodsGetgoodsResponse.defaultScore
            clShare.visibility = View.GONE
        } else {
            tvDiscount.text =
                "¥ " + DecimalFormat("0.00").format(goodsGetgoodsResponse.discountPrice)
            tvPrice.text =
                "金币可抵扣 ¥" + DecimalFormat("0.00").format(goodsGetgoodsResponse.goldCoinPrice)
            clShare.visibility = View.VISIBLE
        }

        tvMark.text = goodsGetgoodsResponse.services
        tvGroupUserNum.text = "${goodsGetgoodsResponse.groupUserNum}"
        tvGroupPrice.text =
            DecimalFormat("0.00").format((goodsGetgoodsResponse.discountPrice * goodsGetgoodsResponse.groupPriceOffRate))

        if (buyType == 1) {
            imgAdd.isEnabled = false
            imgSub.isEnabled = false
            imgSub.setImageResource(R.drawable.af_chat_buy_cut_new)
            imgAdd.setImageResource(R.drawable.af_chat_buy_add_new_gary)
        } else {
            imgAdd.isEnabled = true
            imgSub.isEnabled = true
            imgSub.setImageResource(R.drawable.af_chat_buy_cut_new)
            imgAdd.setImageResource(R.drawable.af_chat_buy_add_new)
        }

        imgAdd.singleClick {
            var buyNumStr = tvBuyNumber.getText().toString().trim { it <= ' ' }
            var num = buyNumStr.toInt()

            imgSub.setImageResource(R.drawable.af_chat_buy_cut_new_green)
            if (num >= goodsGetgoodsResponse.maxBuyNum) {
                ToastUtils.showShort("最多买" + goodsGetgoodsResponse.maxBuyNum + "个")
                return@singleClick
            } else {
                num++
                tvBuyNumber.text = num.toString()
                if (num == goodsGetgoodsResponse.maxBuyNum) {
                    imgAdd.setImageResource(R.drawable.af_chat_buy_add_new_gary)
                } else {
                    imgAdd.setImageResource(R.drawable.af_chat_buy_add_new)
                }
            }
        }

        imgSub.singleClick {
            var buyNumStr = tvBuyNumber.getText().toString().trim { it <= ' ' }
            var num = buyNumStr.toInt()

            imgAdd.setImageResource(R.drawable.af_chat_buy_add_new)
            if (num <= 1) {
                ToastUtils.showShort("最少买1个")
                return@singleClick
            } else {
                num--
                tvBuyNumber.text = num.toString()
                if (num == 1) {
                    imgSub.setImageResource(R.drawable.af_chat_buy_cut_new)
                } else {
                    imgSub.setImageResource(R.drawable.af_chat_buy_cut_new_green)
                }
            }
        }

        tvConfirm.singleClick {
            with(Bundle()) {
                putInt("buyType", buyType)
                putInt("goodsId", goodsId)
                putString("buyNum", tvBuyNumber.getText().toString())
                PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                    this@ProductDetailsActivity,
                    OrderPayDetailsActivity::class.java, this, false
                )
            }
            scoreDialog.dismiss()
        }

        clShare.singleClick {
            goodsGetgoodsResponse.groupAssistVO?.let {
                jumpToCheating(it.status, it.groupCode, it.orderId)

            } ?: let {
                //创建助力
                requestCheatingViewModel.assist(goodsId)
            }
            scoreDialog.dismiss()
        }
    }
}