package com.sn.gameelectricity.ui.activity

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.ToastUtils
import com.gyf.immersionbar.ImmersionBar
import com.sn.gameelectricity.R
import com.sn.gameelectricity.app.base.BaseActivity1
import com.sn.gameelectricity.app.event.AddressAddEvent
import com.sn.gameelectricity.app.util.load
import com.sn.gameelectricity.app.weight.customview.emoji.EmojiconEditText
import com.sn.gameelectricity.app.weight.dialog.AddAddressDialog
import com.sn.gameelectricity.data.model.bean.DeliveryAddressBean
import com.sn.gameelectricity.data.model.bean.GoodsGetDiscountResponse
import com.sn.gameelectricity.data.model.bean.GoodsGetgoodsResponse
import com.sn.gameelectricity.databinding.ActivityOrderpayDetailsBinding
import com.sn.gameelectricity.ui.enum.ActivityType
import com.sn.gameelectricity.ui.fragment.OrderListFragment
import com.sn.gameelectricity.viewmodel.OrderPayDetailsViewModel
import com.sn.gameelectricity.viewmodel.ShippingAddressViewModel
import com.sn.gameelectricity.viewmodel.request.RequestOrderPayViewModel
import com.sn.gameelectricity.viewmodel.request.RequestProductDetailsViewModel
import kotlinx.android.synthetic.main.activity_orderpay_details.*
import me.hgj.jetpackmvvm.util.PublicPracticalMethodFromKT
import me.shaohui.bottomdialog.BottomDialog
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import singleClick
import java.text.DecimalFormat
import java.text.MessageFormat


/**
 * 订单支付详情
 */
class OrderPayDetailsActivity :
    BaseActivity1<OrderPayDetailsViewModel, ActivityOrderpayDetailsBinding>() {

    private var actionPosition: Int = 0
    private var distributionType: Int = 1 //配送方式 1.快递 2.自提
    private var supplierAddress: String = ""
    private var remark: String = ""
    private var payType: String = "A"
    private var mFactAmt: Float = 0.0F
    private var mPostage: Float = 0.0F
    private var mDiscountConsume: Int = 0
    private lateinit var goodsGetgoodsResponse: GoodsGetgoodsResponse
    private lateinit var goodsGetDiscountResponse: GoodsGetDiscountResponse
    private var deliveryAddressBean: DeliveryAddressBean? = null
    private val requestProductDetailsViewModel: RequestProductDetailsViewModel by viewModels()
    private val requestOrderPayViewModel: RequestOrderPayViewModel by viewModels()
    private val shippingAddressViewModel: ShippingAddressViewModel by viewModels()

    private val buyNum: String by lazy {
        intent.getStringExtra("buyNum") as String
    }

    private val goodsId: Int by lazy {
        intent.getIntExtra("goodsId", 0)
    }

    private val buyType: Int by lazy {
        intent.getIntExtra("buyType", 0)
    }

    private val distributionDialog: BottomDialog by lazy {
        BottomDialog.create(supportFragmentManager)
    }

    private val orderRemarksnDialog: BottomDialog by lazy {
        BottomDialog.create(supportFragmentManager)
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this)
            .transparentStatusBar()
            .statusBarDarkFont(true)
            .navigationBarColorInt(Color.WHITE)
            .navigationBarDarkIcon(true)
            .init()

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }

        mViewBind.apply {

            ivBack.singleClick {
                finish()
            }

            clAddress.singleClick {
                with(Bundle()) {
                    putSerializable("activitytype", ActivityType.OrderPayDetails_Activity)
                    PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                        this@OrderPayDetailsActivity,
                        ShippingAddressActivity::class.java, this, false
                    )
                }
            }

            clAddressEmp.singleClick {
                with(Bundle()) {
                    putSerializable("activitytype", ActivityType.OrderPayDetails_Activity)
                    PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                        this@OrderPayDetailsActivity,
                        ShippingAddressActivity::class.java, this, false
                    )
                }
            }

            clDistribution.singleClick {
                distributionDialog.show()
            }

            clOrderRemarks.singleClick {
                orderRemarksnDialog.show()
            }
        }

        if (buyType == 1) {
            clGold.visibility = View.GONE
            tvGoldCoinPrice.visibility = View.GONE
            tvGoldTotalPrice.visibility = View.GONE
            tvIntegral.visibility = View.VISIBLE
        } else {
            clGold.visibility = View.VISIBLE
            tvGoldCoinPrice.visibility = View.VISIBLE
            tvGoldTotalPrice.visibility = View.VISIBLE
            tvIntegral.visibility = View.GONE
        }

        distributionDialog.setViewListener {
            initDistributionDialog(it)
        }.setLayoutRes(R.layout.dialog_bottom_distribution)

        orderRemarksnDialog.setViewListener {
            initOrderRemarksDialog(it)
        }.setLayoutRes(R.layout.dialog_bottom_orderremarks)
    }

    override fun createObserver() {
        requestProductDetailsViewModel.goodsGetGoods(goodsId)
        requestProductDetailsViewModel.productGetGoods.observe(this, Observer {
            goodsGetgoodsResponse = it
            loadProductData(it)
            requestOrderPayViewModel.goodsGetDiscount(goodsId)
        })

        requestOrderPayViewModel.orderGetDiscount.observe(this, Observer {
            goodsGetDiscountResponse = it
            if (buyType == 1) {
                tvIntegralPrice.text =
                    it.score.toString() + "/" + goodsGetgoodsResponse.defaultScore.toString()
                if (goodsGetgoodsResponse.defaultScore < it.score) {
                    tvPay.visibility = View.VISIBLE
                    tvIntegration.visibility = View.GONE
                } else {
                    tvPay.visibility = View.GONE
                    tvIntegration.visibility = View.VISIBLE
                }
                mDiscountConsume = goodsGetgoodsResponse.defaultScore
                when (distributionType) {
                    2 -> {//配送方式 1.快递 2.自提
                        mFactAmt = 0.0F
                    }
                    1 -> {//配送方式 1.快递 2.自提
                        mFactAmt =
                            (goodsGetgoodsResponse.postage).toFloat()
                    }

                }
                tvActualMoney.text = "¥" + DecimalFormat("0.00").format(mFactAmt)

            } else {
                tvGoldPrice.text =
                    MessageFormat.format(
                        "{0}金币抵扣 ¥{1}",
                        Math.min(it.goldNum, goodsGetgoodsResponse.defaultGoldCoinMax),
                        DecimalFormat("0.00").format(
                            Math.min(
                                it.goldCoinPrice,
                                goodsGetgoodsResponse.goldCoinPrice
                            )
                        )
                    )

                mDiscountConsume = Math.min(it.goldNum, goodsGetgoodsResponse.defaultGoldCoinMax)
                when (distributionType) {
                    2 -> {//配送方式 1.快递 2.自提
                        mFactAmt =
                            (goodsGetgoodsResponse.discountPrice * tvBuyNumber.text.toString()
                                .toInt() - Math.min(
                                goodsGetDiscountResponse.goldCoinPrice,
                                goodsGetgoodsResponse.goldCoinPrice
                            ))
                    }
                    1 -> {//配送方式 1.快递 2.自提
                        mFactAmt =
                            (goodsGetgoodsResponse.discountPrice * tvBuyNumber.text.toString()
                                .toInt() - Math.min(
                                goodsGetDiscountResponse.goldCoinPrice,
                                goodsGetgoodsResponse.goldCoinPrice
                            )) + goodsGetgoodsResponse.postage
                    }

                }
                tvActualMoney.text = "¥" + DecimalFormat("0.00").format(mFactAmt)

                tvGoldTotalPrice.text =
                    "共优惠 ¥" + DecimalFormat("0.00").format(
                        Math.min(
                            it.goldCoinPrice,
                            goodsGetgoodsResponse.goldCoinPrice
                        )
                    )
            }
        })

        requestOrderPayViewModel.orderCreatOrder.observe(this, Observer {
            if (it.payStatus == 4) {
                with(Bundle()) {
                    putInt("orderId", it.id)
                    PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                        this@OrderPayDetailsActivity,
                        PaySuccActivity::class.java, this, true
                    )
                }
            } else {
                with(Bundle()) {
                    putSerializable("activitytype", ActivityType.OrderPayDetails_Activity)
                    putSerializable("type", OrderListFragment.OrderType.PENDING_PAYMENT)
                    putInt("orderId", it.id)
                    PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                        this@OrderPayDetailsActivity,
                        OrderDetailsActivity::class.java, this, true
                    )
                }
            }

//            requestOrderPayViewModel.getCountDownTime((120 * 1000).toLong(), it.orderNo) {
//                with(Bundle()) {
//                    putInt("orderId", it.id)
//                    PublicPracticalMethodFromKT.ppmfKT.intentToJump(
//                        this@OrderPayDetailsActivity,
//                        PaySuccActivity::class.java, this, true
//                    )
//                }
//            }
//            when (payType) {
//                "A" -> {
//                    requestOrderPayViewModel.aLiPay(this@OrderPayDetailsActivity, it.payInfo)
//                }
//                "W" -> {
//                    requestOrderPayViewModel.wxPay(this@OrderPayDetailsActivity, it.payInfo)
//                }
//            }
        })

        shippingAddressViewModel.deliveryAddressList()
        shippingAddressViewModel.deliveryAddressLive.observe(this) { beans ->
            if (beans != null && beans.isNotEmpty()) {
                for ((index, it) in beans.withIndex()) {
                    if (it.type == 0) {
                        clAddress.visibility = View.VISIBLE
                        clAddressEmp.visibility = View.GONE
                        tvAddressDefault.visibility = View.VISIBLE
                        deliveryAddressBean = it
                        tvSupplierPhone.text = "${it.addresseeName} ${it.phoneNumber}"
                        tvAddress.text =
                            "${it.provinceName} ${it.cityName} ${it.areaName} ${it.streetName}"
                        tvSupplierAddress.text =
                            "${it.address}"
                        break
                    } else {
                        clAddress.visibility = View.GONE
                        clAddressEmp.visibility = View.VISIBLE
                    }
                }
            }
        }

        shippingAddressViewModel.deliveryAdLive.observe(this) { beans ->
            if (beans != null && beans.isNotEmpty()) {
                deliveryAddressBean = beans.get(actionPosition)
                clAddress.visibility = View.VISIBLE
                clAddressEmp.visibility = View.GONE
                if (beans.get(actionPosition).type == 0) {
                    tvAddressDefault.visibility = View.VISIBLE
                } else {
                    tvAddressDefault.visibility = View.GONE
                }

                tvSupplierPhone.text =
                    "${beans.get(actionPosition).addresseeName} ${beans.get(actionPosition).phoneNumber}"
                tvAddress.text =
                    "${beans.get(actionPosition).provinceName} ${beans.get(actionPosition).cityName} ${
                        beans.get(
                            actionPosition
                        ).areaName
                    } ${
                        beans.get(
                            actionPosition
                        ).streetName
                    }"
                tvSupplierAddress.text =
                    "${beans.get(actionPosition).address}"
            }
        }
    }

    private fun loadProductData(goodsGetgoodsResponse: GoodsGetgoodsResponse) {
        mViewBind.apply {
            ivIcon.load(goodsGetgoodsResponse.icon)
            tvGoodsName.text = goodsGetgoodsResponse.goodsName
            tvPatternName.text = goodsGetgoodsResponse.patternName
            tvDiscountPrice.text =
                "¥" + DecimalFormat("0.00").format(goodsGetgoodsResponse.discountPrice)

            tvGoldCoinPrice.text =
                "金币抵扣后 ¥" + DecimalFormat("0.00").format((goodsGetgoodsResponse.discountPrice - goodsGetgoodsResponse.goldCoinPrice))

            if (goodsGetgoodsResponse.postage > 0) {
                tvDistributionValue.text =
                    "快递" + DecimalFormat("0.00").format(goodsGetgoodsResponse.postage) + "元"
                distributionType = 1
                clAbholung.visibility = View.GONE
            } else {
                tvDistributionValue.text = "包邮"
                clAbholung.visibility = View.GONE
                distributionType = 1
            }
            mPostage = goodsGetgoodsResponse.postage

            tvBuyNumber.text = buyNum
            if (buyType == 1) {
                imgAdd.isEnabled = false
                imgSub.isEnabled = false
                imgSub.setImageResource(R.drawable.af_chat_buy_cut_new)
                imgAdd.setImageResource(R.drawable.af_chat_buy_add_new_gary)
            } else {
                imgAdd.isEnabled = true
                imgSub.isEnabled = true
                if (buyNum.toInt() == 1) {
                    imgSub.setImageResource(R.drawable.af_chat_buy_cut_new)
                } else {
                    imgSub.setImageResource(R.drawable.af_chat_buy_cut_new_green)
                }

                if (buyNum.toInt() == goodsGetgoodsResponse.maxBuyNum) {
                    imgAdd.setImageResource(R.drawable.af_chat_buy_add_new_gary)
                } else {
                    imgAdd.setImageResource(R.drawable.af_chat_buy_add_new)
                }
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

                    when (distributionType) {
                        2 -> {//配送方式 1.快递 2.自提
                            mFactAmt =
                                (goodsGetgoodsResponse.discountPrice * tvBuyNumber.text.toString()
                                    .toInt() - Math.min(
                                    goodsGetDiscountResponse.goldCoinPrice,
                                    goodsGetgoodsResponse.goldCoinPrice
                                ))
                        }
                        1 -> {//配送方式 1.快递 2.自提
                            mFactAmt =
                                (goodsGetgoodsResponse.discountPrice * tvBuyNumber.text.toString()
                                    .toInt() - Math.min(
                                    goodsGetDiscountResponse.goldCoinPrice,
                                    goodsGetgoodsResponse.goldCoinPrice
                                )) + goodsGetgoodsResponse.postage
                        }

                    }
                    tvActualMoney.text = "¥" + DecimalFormat("0.00").format(mFactAmt)
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
                    when (distributionType) {
                        2 -> {//配送方式 1.快递 2.自提
                            mFactAmt =
                                (goodsGetgoodsResponse.discountPrice * tvBuyNumber.text.toString()
                                    .toInt() - Math.min(
                                    goodsGetDiscountResponse.goldCoinPrice,
                                    goodsGetgoodsResponse.goldCoinPrice
                                ))
                        }
                        1 -> {//配送方式 1.快递 2.自提
                            mFactAmt =
                                (goodsGetgoodsResponse.discountPrice * tvBuyNumber.text.toString()
                                    .toInt() - Math.min(
                                    goodsGetDiscountResponse.goldCoinPrice,
                                    goodsGetgoodsResponse.goldCoinPrice
                                )) + goodsGetgoodsResponse.postage
                        }
                    }
                    tvActualMoney.text = "¥" + DecimalFormat("0.00").format(mFactAmt)
                }
            }

            ivAliPay.singleClick {
                payType = "A"
                ivAliPay.setImageResource(R.drawable.ic_checking)
                ivWxPay.setImageResource(R.drawable.ge_unselected)
            }

            ivWxPay.singleClick {
                payType = "W"
                ivAliPay.setImageResource(R.drawable.ge_unselected)
                ivWxPay.setImageResource(R.drawable.ic_checking)
            }

            tvPay.singleClick {
                if (deliveryAddressBean != null) {
                    if (buyType == 1) {//积分
                        requestOrderPayViewModel.createOrder(
                            deliveryAddressBean!!.id,
                            deliveryAddressBean!!.phoneNumber,
                            goodsGetgoodsResponse.discountPrice,
                            mDiscountConsume,
                            buyType,
                            distributionType,
                            mFactAmt,
                            goodsId,
                            0,
                            0,
                            (mFactAmt + goodsGetgoodsResponse.discountPrice).toFloat(),
                            tvBuyNumber.text.toString().toInt(),
                            buyType,
                            1,
                            payType,
                            mPostage,
                            0,
                            mViewBind.tvSupplierAddr.text.toString(),
                            remark
                        )
                    } else {
                        requestOrderPayViewModel.createOrder(
                            deliveryAddressBean!!.id,
                            deliveryAddressBean!!.phoneNumber,
                            Math.min(
                                goodsGetDiscountResponse.goldCoinPrice,
                                goodsGetgoodsResponse.goldCoinPrice
                            ),
                            Math.min(
                                goodsGetDiscountResponse.goldNum,
                                goodsGetgoodsResponse.defaultGoldCoinMax
                            ),
                            buyType,
                            distributionType,
                            DecimalFormat("0.00").format(mFactAmt).toFloat(),
                            goodsId,
                            0,
                            0,
                            (mFactAmt + Math.min(
                                goodsGetDiscountResponse.goldCoinPrice,
                                goodsGetgoodsResponse.goldCoinPrice
                            )).toFloat(),
                            tvBuyNumber.text.toString().toInt(),
                            buyType,
                            1,
                            payType,
                            mPostage,
                            0,
                            mViewBind.tvSupplierAddr.text.toString(),
                            remark
                        )
                    }
                } else {
                    clAddress.visibility = View.GONE
                    clAddressEmp.visibility = View.VISIBLE
                    val addAddressDialog = AddAddressDialog(this@OrderPayDetailsActivity)
                    addAddressDialog.onClickSure {
                        with(Bundle()) {
                            putSerializable("activitytype", ActivityType.OrderPayDetails_Activity)
                            PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                                this@OrderPayDetailsActivity,
                                ShippingAddressActivity::class.java, this, false
                            )
                        }
                        addAddressDialog.dismissDialog()
                    }
                    addAddressDialog.show()
                }
            }
        }
    }


    private fun initDistributionDialog(view: View) {
        val imgClose = view.findViewById<ImageView>(R.id.imgClose)
        var contentEditView = view.findViewById<TextView>(R.id.contentEditView)
        var ivPostage = view.findViewById<ImageView>(R.id.ivPostage)
        var ivAbholung = view.findViewById<ImageView>(R.id.ivAbholung)
        var clAbhol = view.findViewById<ConstraintLayout>(R.id.clAbhol)
        var tvSure = view.findViewById<TextView>(R.id.tvSure)
        var tvPostage = view.findViewById<TextView>(R.id.tvPostage)

        if (goodsGetgoodsResponse.postage > 0) {
            tvPostage.text =
                "快递" + DecimalFormat("0.00").format(goodsGetgoodsResponse.postage) + "元"
        } else {
            tvPostage.text =
                "包邮"
        }
        goodsGetgoodsResponse.supplierAddress?.let {
            contentEditView.text = "${goodsGetgoodsResponse.supplierAddress}"
        }

        when (distributionType) {
            1 -> {
                ivPostage.setImageResource(R.drawable.ic_checking)
                ivAbholung.setImageResource(R.drawable.ge_unselected)
            }
            2 -> {
                ivPostage.setImageResource(R.drawable.ge_unselected)
                ivAbholung.setImageResource(R.drawable.ic_checking)
            }

        }

        imgClose.singleClick {
            distributionDialog.dismiss()
        }

        var choiseType = 0
        ivPostage.singleClick {
            ivPostage.setImageResource(R.drawable.ic_checking)
            ivAbholung.setImageResource(R.drawable.ge_unselected)
            choiseType = 0
        }

        ivAbholung.singleClick {
            ivPostage.setImageResource(R.drawable.ge_unselected)
            ivAbholung.setImageResource(R.drawable.ic_checking)
            choiseType = 1
        }

        tvSure.singleClick {
            supplierAddress = contentEditView.text.toString()
            when (choiseType) {
                0 -> {
                    if (goodsGetgoodsResponse.postage > 0) {
                        distributionType = 1
                        tvDistributionValue.text =
                            "快递" + DecimalFormat("0.00").format(goodsGetgoodsResponse.postage) + "元"
                        if (buyType == 1) {
                            mFactAmt =
                                (goodsGetgoodsResponse.postage)
                        } else {
                            mFactAmt =
                                (goodsGetgoodsResponse.discountPrice * tvBuyNumber.text.toString()
                                    .toInt() - Math.min(
                                    goodsGetDiscountResponse.goldCoinPrice,
                                    goodsGetgoodsResponse.goldCoinPrice
                                )) + goodsGetgoodsResponse.postage
                        }


                        tvActualMoney.text = "¥" + DecimalFormat("0.00").format(mFactAmt)
                        mPostage = goodsGetgoodsResponse.postage
                    } else {
                        distributionType = 1
                        tvDistributionValue.text =
                            "包邮"
                        if (buyType == 1) {
                            mFactAmt = 0.0F
                        } else {
                            mFactAmt =
                                (goodsGetgoodsResponse.discountPrice * tvBuyNumber.text.toString()
                                    .toInt() - Math.min(
                                    goodsGetDiscountResponse.goldCoinPrice,
                                    goodsGetgoodsResponse.goldCoinPrice
                                ))
                        }

                        mViewBind.tvSupplierAddr.visibility = View.GONE
                        tvActualMoney.text = "¥" + DecimalFormat("0.00").format(mFactAmt)
                        mPostage = 0.0F
                    }

                    clAbholung.visibility = View.GONE
                }
                1 -> {
                    distributionType = 2
                    clAbholung.visibility = View.VISIBLE
                    tvDistributionValue.text = "自提"
                    mViewBind.tvSupplierAddr.visibility = View.VISIBLE
                    mViewBind.tvSupplierAddr.text = "${supplierAddress}"
                    if (buyType == 1) {
                        mFactAmt = 0.0F
                    } else {
                        mFactAmt =
                            (goodsGetgoodsResponse.discountPrice * tvBuyNumber.text.toString()
                                .toInt() - Math.min(
                                goodsGetDiscountResponse.goldCoinPrice,
                                goodsGetgoodsResponse.goldCoinPrice
                            ))
                    }
                    mPostage = 0.0F
                    tvActualMoney.text = "¥" + DecimalFormat("0.00").format(mFactAmt)
                }
            }

            distributionDialog.dismiss()
        }

    }

    private fun initOrderRemarksDialog(view: View) {
        val imgClose = view.findViewById<ImageView>(R.id.imgClose)
        var contentEditView = view.findViewById<EmojiconEditText>(R.id.contentEditView)
        var tvLave = view.findViewById<TextView>(R.id.tvLave)
        var tvSure = view.findViewById<TextView>(R.id.tvSure)
        contentEditView.addTextChangedListener {
            val textLength = it?.length ?: 0
            tvLave.text = MessageFormat.format("{0}", textLength)
        }
        tvSure.singleClick {
            remark = contentEditView.text.toString()
            tvOrderRemarksValue.text = if (remark.isEmpty()) "无备注" else remark
            orderRemarksnDialog.dismiss()
        }

        imgClose.singleClick {
            orderRemarksnDialog.dismiss()
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun handleEvent(addressAddEvent: AddressAddEvent) {
        if (addressAddEvent.actionType == AddressAddEvent.EVENT_ADDRESSADD_CODE
        ) {
            shippingAddressViewModel.deliveryAdList()
            actionPosition = addressAddEvent.actionPosition
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }
}