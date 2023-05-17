package com.sn.gameelectricity.ui.activity

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.view.KeyEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.LogUtils
import com.gyf.immersionbar.ImmersionBar
import com.sn.gameelectricity.R
import com.sn.gameelectricity.app.App
import com.sn.gameelectricity.app.appViewModel
import com.sn.gameelectricity.app.base.BaseActivity1
import com.sn.gameelectricity.app.util.ClipboardUtil
import com.sn.gameelectricity.app.weight.customview.emoji.EmojiconEditText
import com.sn.gameelectricity.app.weight.dialog.LogisticsInformationDialog
import com.sn.gameelectricity.app.weight.dialog.MoneyTaskDialog
import com.sn.gameelectricity.app.weight.dialog.SelectionTooltipDialog
import com.sn.gameelectricity.data.model.bean.DeliveryAddressBean
import com.sn.gameelectricity.data.model.bean.OrderDetailsBean
import com.sn.gameelectricity.data.model.bean.OrderListRefreshBean
import com.sn.gameelectricity.data.model.bean.PayloadGroupAssistMemberVO
import com.sn.gameelectricity.databinding.ActivityOrderDetailsBinding
import com.sn.gameelectricity.ui.adapter.AreaAdapter
import com.sn.gameelectricity.ui.adapter.OrderDetailsHelperAdapter
import com.sn.gameelectricity.ui.enum.ActivityType
import com.sn.gameelectricity.ui.fragment.OrderListFragment
import com.sn.gameelectricity.viewmodel.OrderDetailsViewModel
import com.sn.gameelectricity.viewmodel.request.RequestOrderPayViewModel
import kotlinx.android.synthetic.main.activity_orderpay_details.*
import me.hgj.jetpackmvvm.common.ConfigConstants
import me.hgj.jetpackmvvm.event.UniversalEvent
import me.hgj.jetpackmvvm.ext.addMoreData
import me.hgj.jetpackmvvm.ext.removeEmptyView
import me.hgj.jetpackmvvm.ext.view.gone
import me.hgj.jetpackmvvm.util.*
import me.jessyan.autosize.AutoSizeConfig
import me.shaohui.bottomdialog.BottomDialog
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import singleClick
import java.text.MessageFormat

/**
 * Date:2022/5/10
 * Time:19:51
 * author:dimple
 * 订单详情页面
 */
class OrderDetailsActivity : BaseActivity1<OrderDetailsViewModel, ActivityOrderDetailsBinding>() {

    private val type: OrderListFragment.OrderType by lazy {
        intent?.getSerializableExtra("type") as OrderListFragment.OrderType
    }
    private val orderId by lazy {
        intent?.getIntExtra("orderId", -1)
    }

    val activityType by lazy {
        val _activitytype = intent?.getSerializableExtra("activitytype")
        if (_activitytype != null) {
            _activitytype as ActivityType
        }
        _activitytype
    }

    private val requestOrderPayViewModel: RequestOrderPayViewModel by viewModels()

    private var logisticsInformationDialog: LogisticsInformationDialog? = null

    //支付方式
    private var payType = "A"

    //收货地址id
    private var addressId = -1

    private var deliveryAddressBean: DeliveryAddressBean? = null

    private var orderDetailsBean: OrderDetailsBean? = null


    private val orderRemarksnDialog: BottomDialog by lazy {
        BottomDialog.create(supportFragmentManager)
    }
    private var remark: String = ""

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

        initRecyclerViewHelper()
        dataEcho()

        orderRemarksnDialog.setViewListener {
            initOrderRemarksDialog(it)
        }.layoutRes = R.layout.dialog_bottom_orderremarks

        mViewBind.apply {
            appViewModel.uiShowHide(arrayOf(nsvContent, clOperate), View.INVISIBLE)
            ivBack.singleClick {
                if (!PublicPracticalMethodFromKT.ppmfKT.isBlank(activityType)) {
                    when (activityType) {
                        ActivityType.OrderPayDetails_Activity -> {
                            EventBus.getDefault().post(
                                UniversalEvent(
                                    UniversalEvent.EVENT_ORDERLIST_REFRESH,
                                    null
                                )
                            )
                        }
                    }
                }
                finish()
            }

            PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
                clBg, -1, "",
                0f,
                0f,
                0f,
                0f,
                GradientDrawable.Orientation.LEFT_RIGHT, "#F19B3F", "#ED7A57"
            )

            PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
                clAddress, -1, "",
                ScreenTools.getInstance().dp2px(this@OrderDetailsActivity, 8f, true).toFloat(),
                ScreenTools.getInstance().dp2px(this@OrderDetailsActivity, 8f, true).toFloat(),
                ScreenTools.getInstance().dp2px(this@OrderDetailsActivity, 8f, true).toFloat(),
                ScreenTools.getInstance().dp2px(this@OrderDetailsActivity, 8f, true).toFloat(),
                null, "#FFFFFF"
            )

            PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
                clProductInfo, -1, "",
                ScreenTools.getInstance().dp2px(this@OrderDetailsActivity, 8f, true).toFloat(),
                ScreenTools.getInstance().dp2px(this@OrderDetailsActivity, 8f, true).toFloat(),
                ScreenTools.getInstance().dp2px(this@OrderDetailsActivity, 8f, true).toFloat(),
                ScreenTools.getInstance().dp2px(this@OrderDetailsActivity, 8f, true).toFloat(),
                null, "#FFFFFF"
            )

            PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
                clTotalPrice, -1, "",
                ScreenTools.getInstance().dp2px(this@OrderDetailsActivity, 8f, true).toFloat(),
                ScreenTools.getInstance().dp2px(this@OrderDetailsActivity, 8f, true).toFloat(),
                ScreenTools.getInstance().dp2px(this@OrderDetailsActivity, 8f, true).toFloat(),
                ScreenTools.getInstance().dp2px(this@OrderDetailsActivity, 8f, true).toFloat(),
                null, "#FFFFFF"
            )

            PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
                clRealPayment, -1, "",
                ScreenTools.getInstance().dp2px(this@OrderDetailsActivity, 8f, true).toFloat(),
                ScreenTools.getInstance().dp2px(this@OrderDetailsActivity, 8f, true).toFloat(),
                ScreenTools.getInstance().dp2px(this@OrderDetailsActivity, 8f, true).toFloat(),
                ScreenTools.getInstance().dp2px(this@OrderDetailsActivity, 8f, true).toFloat(),
                null, "#FFFFFF"
            )

            PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
                clPaymentMethod, -1, "",
                ScreenTools.getInstance().dp2px(this@OrderDetailsActivity, 8f, true).toFloat(),
                ScreenTools.getInstance().dp2px(this@OrderDetailsActivity, 8f, true).toFloat(),
                ScreenTools.getInstance().dp2px(this@OrderDetailsActivity, 8f, true).toFloat(),
                ScreenTools.getInstance().dp2px(this@OrderDetailsActivity, 8f, true).toFloat(),
                null, "#FFFFFF"
            )

            PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
                tvAddressDefault, -1, "",
                ScreenTools.getInstance().dp2px(this@OrderDetailsActivity, 4f, true).toFloat(),
                ScreenTools.getInstance().dp2px(this@OrderDetailsActivity, 4f, true).toFloat(),
                ScreenTools.getInstance().dp2px(this@OrderDetailsActivity, 4f, true).toFloat(),
                ScreenTools.getInstance().dp2px(this@OrderDetailsActivity, 4f, true).toFloat(),
                null, "#FDEDE4"
            )

            PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
                clHelp, -1, "",
                ScreenTools.getInstance().dp2px(this@OrderDetailsActivity, 8f, true).toFloat(),
                ScreenTools.getInstance().dp2px(this@OrderDetailsActivity, 8f, true).toFloat(),
                ScreenTools.getInstance().dp2px(this@OrderDetailsActivity, 8f, true).toFloat(),
                ScreenTools.getInstance().dp2px(this@OrderDetailsActivity, 8f, true).toFloat(),
                null, "#FFFFFF"
            )

            PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
                clOrderNotes, -1, "",
                ScreenTools.getInstance().dp2px(this@OrderDetailsActivity, 8f, true).toFloat(),
                ScreenTools.getInstance().dp2px(this@OrderDetailsActivity, 8f, true).toFloat(),
                ScreenTools.getInstance().dp2px(this@OrderDetailsActivity, 8f, true).toFloat(),
                ScreenTools.getInstance().dp2px(this@OrderDetailsActivity, 8f, true).toFloat(),
                null, "#FFFFFF"
            )
            ituiGoldConsume.apply {
                setAvatarDataFromRes(
                    R.drawable.ge_me_gold,
                    ScreenTools.getInstance().dp2px(App.instance, 24f, true),
                    ScreenTools.getInstance().dp2px(App.instance, 24f, true)
                )
                setTextData("金币抵扣", 14f, "#061925")
            }
            ituiCouponConsume.apply {
                setAvatarDataFromRes(
                    R.drawable.ge_order_details_coupon,
                    ScreenTools.getInstance().dp2px(App.instance, 24f, true),
                    ScreenTools.getInstance().dp2px(App.instance, 24f, true)
                )
                setTextData("优惠券", 14f, "#061925")
            }
            ituiScoresConsume.apply {
                setAvatarDataFromRes(
                    R.drawable.ge_me_order,
                    ScreenTools.getInstance().dp2px(App.instance, 24f, true),
                    ScreenTools.getInstance().dp2px(App.instance, 24f, true)
                )
                setTextData("积分消耗", 14f, "#061925")
            }



            clAddress.singleClick {
                if (type == OrderListFragment.OrderType.PENDING_RECEIPT || type == OrderListFragment.OrderType.COMPLETED) {
                    return@singleClick
                }
                with(Bundle()) {
                    putSerializable("activitytype", ActivityType.Order_Details_Activity)
                    PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                        this@OrderDetailsActivity,
                        ShippingAddressActivity::class.java, this, false
                    )
                }
            }

            ituiDeliveryAddressAdd.singleClick {
                if (type == OrderListFragment.OrderType.PENDING_RECEIPT || type == OrderListFragment.OrderType.COMPLETED) {
                    return@singleClick
                }
                with(Bundle()) {
                    putSerializable("activitytype", ActivityType.Order_Details_Activity)
                    PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                        this@OrderDetailsActivity,
                        ShippingAddressActivity::class.java, this, false
                    )
                }
            }

            clAlipay.singleClick {
                payType = "A"
                ivCheckboxAlipay.setImageDrawable(
                    ContextCompat.getDrawable(
                        this@OrderDetailsActivity,
                        R.drawable.ge_selected
                    )
                )
                ivCheckboxUnion.setImageDrawable(
                    ContextCompat.getDrawable(
                        this@OrderDetailsActivity,
                        R.drawable.ge_unselected
                    )
                )
            }
            clUnion.singleClick {
                payType = "W"
                ivCheckboxAlipay.setImageDrawable(
                    ContextCompat.getDrawable(
                        this@OrderDetailsActivity,
                        R.drawable.ge_unselected
                    )
                )
                ivCheckboxUnion.setImageDrawable(
                    ContextCompat.getDrawable(
                        this@OrderDetailsActivity,
                        R.drawable.ge_selected
                    )
                )
            }
        }
        mViewModel.setTopViewHeight(this@OrderDetailsActivity, mViewBind)
    }

    /**
     * 初始化助力好友头像列表
     */

    private lateinit var orderDetailsHelperAdapter: OrderDetailsHelperAdapter

    fun initRecyclerViewHelper() {
        mViewBind.apply {
            orderDetailsHelperAdapter = OrderDetailsHelperAdapter(this@OrderDetailsActivity)
            recyclerViewHelper.apply {
                val linearLayoutManager = LinearLayoutManager(this@OrderDetailsActivity)
                layoutManager = linearLayoutManager
                linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
                adapter = orderDetailsHelperAdapter
                clearAnimation()
            }
        }
    }

    /**
     * 数据回显
     */
    private fun dataEcho() {
        mViewBind.apply {
            mViewModel.apply {
                orderByIdLive.observe(this@OrderDetailsActivity) { bean ->
                    appViewModel.uiShowHide(arrayOf(nsvContent, clOperate), View.VISIBLE)
                    orderDetailsBean = bean
                    setOperateUI(bean)
                    if (!PublicPracticalMethodFromKT.ppmfKT.isBlank(activityType)) {
                        when (activityType) {
                            ActivityType.OrderPayDetails_Activity -> {
                                toPay(bean)
                            }
                        }
                    }

                    clProductInfo.singleClick {
                        //跳转到商品详情页
                        with(Bundle()) {
                            putInt("goodsId", bean.payloadOrderInfoVO.goodsId)
                            PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                                this@OrderDetailsActivity,
                                ProductDetailsActivity::class.java, this, false
                            )
                        }
                    }

                    val payloadGroupAssistMemberVOList =
                        mutableListOf<PayloadGroupAssistMemberVO>()
                    if (!PublicPracticalMethodFromKT.ppmfKT.isBlank(bean.payloadGroupAssistMemberVOList)) {
                        for (i in bean.payloadGroupAssistMemberVOList.indices) {
                            payloadGroupAssistMemberVOList.add(bean.payloadGroupAssistMemberVOList[i])
                        }
                        val poordata = bean.groupUserNum - bean.payloadGroupAssistMemberVOList.size
                        for (i in 0 until poordata) {
                            payloadGroupAssistMemberVOList.add(
                                PayloadGroupAssistMemberVO(
                                    "",
                                    "",
                                    -99
                                )
                            )
                        }
                    } else {
                        for (i in 0 until bean.groupUserNum) {
                            payloadGroupAssistMemberVOList.add(
                                PayloadGroupAssistMemberVO(
                                    "",
                                    "",
                                    -99
                                )
                            )
                        }
                    }
                    orderDetailsHelperAdapter.setNewData(payloadGroupAssistMemberVOList)
                }
            }
            requestOrderPayViewModel.createTransactionOrderLive.observe(this@OrderDetailsActivity) { bean ->
                when (bean.payType) {
                    "A" -> requestOrderPayViewModel.aLiPay(
                        this@OrderDetailsActivity,
                        bean.payInfo
                    )
                    "W" -> requestOrderPayViewModel.wxPay(
                        this@OrderDetailsActivity,
                        bean.payInfo
                    )
                }
                requestOrderPayViewModel.getCountDownTime(
                    2 * 60 * 1000L,
                    bean.orderNo
                ) {
                    with(Bundle()) {
                        putInt("orderId", bean.id)
                        PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                            this@OrderDetailsActivity,
                            PaySuccActivity::class.java, this, true
                        )
                    }
                }
            }
        }
    }

    /**
     * 根据类型设置操作按钮的UI
     */
    private var pay_color_start = "#F19B3F"
    private var pay_color_end = "#ED7A57"


    private fun setOperateUI(bean: OrderDetailsBean) {
        mViewBind.apply {
            //商品信息
            sivImg.load(
                bean.payloadGoodsInfoVO.icon,
                R.drawable.img_empty,
                R.drawable.img_empty
            )
            tvOrderName.text = bean.payloadGoodsInfoVO.goodsName
            tvOrderIntroduce.text = bean.payloadGoodsInfoVO.patternName
            tvOrderPrice.text = "¥${amountDataConvert(bean.payloadGoodsInfoVO.discountPrice)}"
            tvOrderNum.text = "${bean.payloadOrderInfoVO.orderNumber}"
            //商品总价
            tvProductPrice.text = "¥${amountDataConvert(bean.payloadGoodsInfoVO.discountPrice)}"
            tvGoldConsumePrice.text =
                "${amountDataConvert(bean.payloadOrderInfoVO.discountConsume)}金币抵扣¥${
                    amountDataConvert(
                        bean.payloadOrderInfoVO.discountAmt
                    )
                }"
            tvScoresConsumePrice.text =
                "${amountDataConvert(bean.payloadOrderInfoVO.discountConsume)}"
            tvOrderNotesContent.text =
                if (PublicPracticalMethodFromKT.ppmfKT.isBlank(remark)) {
                    if (PublicPracticalMethodFromKT.ppmfKT.isBlank(bean.payloadOrderInfoVO.remark)) {
                        "无备注"
                    } else {
                        bean.payloadOrderInfoVO.remark
                    }
                } else {
                    remark
                }
//            if (PublicPracticalMethodFromKT.ppmfKT.isBlank(bean.payloadOrderInfoVO.remark)) "无备注" else bean.payloadOrderInfoVO.remark
            //实付款
            tvRealPaymentPrice.text = "¥${amountDataConvert(bean.payloadOrderInfoVO.factAmt)}"

            tvOrderSerial.apply {
                text =
                    mViewModel.orderSerialHighlight("复制", "${bean.payloadOrderInfoVO.orderNo} | 复制")
                singleClick {
                    ClipboardUtil.copyText(
                        this@OrderDetailsActivity,
                        bean.payloadOrderInfoVO.orderNo
                    )
                    ToastUtil.showCenter("复制成功")
                }
            }
            tvOrderPaymentNumber.text = bean.payloadOrderInfoVO.orderNo
            tvOrderCreateTime.text = bean.payloadOrderInfoVO.createTime
            tvOrderPaymentTime.text = bean.payloadOrderInfoVO.payTime
            tvOrderDeliveryTime.text = bean.payloadOrderInfoVO.deliverTime
            tvOrderCompleteTime.text = bean.payloadOrderInfoVO.completionTime


            //收货地址
            if (PublicPracticalMethodFromKT.ppmfKT.isBlank(bean.payloadUserDeliveryAddressVO)) {
                groupAddress.visibility = View.INVISIBLE

                clAddress.visibility =
                    if (type != OrderListFragment.OrderType.PENDING_DELIVERED) View.VISIBLE else View.GONE
                ituiDeliveryAddressAdd.apply {
                    visibility = View.VISIBLE
                    setAvatarDataFromRes(
                        R.drawable.ge_address_add,
                        ScreenTools.getInstance().dp2px(App.instance, 24f, true),
                        ScreenTools.getInstance().dp2px(App.instance, 24f, true)
                    )
                    setTextData("添加收货地址", 14f, "#061925")
                }
            } else {
                groupAddress.visibility = View.VISIBLE
                ituiDeliveryAddressAdd.visibility = View.GONE
                addressId = bean.payloadUserDeliveryAddressVO.id

                if (bean.payloadUserDeliveryAddressVO.type == 0) {
                    tvAddressDefault.visibility = View.VISIBLE
                } else {
                    tvAddressDefault.visibility = View.GONE
                }
                tvAddress.text =
                    "${bean.payloadUserDeliveryAddressVO.provinceName}" +
                            "${bean.payloadUserDeliveryAddressVO.cityName}" +
                            "${bean.payloadUserDeliveryAddressVO.areaName}" +
                            "${bean.payloadUserDeliveryAddressVO.streetName}"
                tvAddress2.text =
                    "${bean.payloadUserDeliveryAddressVO.address}"
                tvNamePhone.text =
                    "${bean.payloadUserDeliveryAddressVO.addresseeName} ${bean.payloadUserDeliveryAddressVO.phoneNumber}"

                clAddress.visibility =
                    if (type != OrderListFragment.OrderType.PENDING_DELIVERED) View.VISIBLE else View.GONE
            }

            //自提地址判断
            when (bean.payloadOrderInfoVO.distributionType) {
                2 -> {
//                    splitline.visibility = View.VISIBLE
                    tvPickUpAddress.visibility = View.VISIBLE
                    tvPickUpAddressContent.visibility = View.VISIBLE

                    tvFreight.text = "配送服务"
                    tvFreightPrice.text = "自提"

                    tvPickUpAddressContent.text =
                        bean.payloadGoodsInfoVO.supplierAddress
                }
                else -> {
                    splitline.visibility = View.GONE
                    tvPickUpAddress.visibility = View.GONE
                    tvPickUpAddressContent.visibility = View.GONE

                    tvFreight.text = "运费"
                    tvFreightPrice.text = "¥${amountDataConvert(bean.payloadGoodsInfoVO.postage)}"
                }
            }


            //支付方式判断
            if (PublicPracticalMethodFromKT.ppmfKT.isBlank(bean.payloadOrderInfoVO.payType)) {
                clAlipay.visibility = View.VISIBLE
                clUnion.visibility = View.VISIBLE
                ivCheckboxAlipay.apply {
                    visibility = View.VISIBLE
                    setImageDrawable(
                        ContextCompat.getDrawable(
                            this@OrderDetailsActivity,
                            R.drawable.ge_selected
                        )
                    )
                }
                ivCheckboxUnion.visibility = View.VISIBLE
            } else {
                when (bean.payloadOrderInfoVO.payType) {
                    "A" -> {
                        clAlipay.visibility = View.VISIBLE
                        clUnion.visibility = View.GONE
                        ivCheckboxAlipay.visibility = View.GONE
                    }
                    "W" -> {
                        clAlipay.visibility = View.GONE
                        clUnion.visibility = View.VISIBLE
                        ivCheckboxUnion.visibility = View.GONE
                    }
                }
                payType = bean.payloadOrderInfoVO.payType
            }

            //金币抵扣
            ituiGoldConsume.visibility = View.GONE
            tvGoldConsumePrice.visibility = View.GONE
            //优惠券
            ituiCouponConsume.visibility = View.GONE
            tvCouponConsumePrice.visibility = View.GONE
            //消耗积分
            ituiScoresConsume.visibility = View.GONE
            tvScoresConsumePrice.visibility = View.GONE
            //支付单号
            tvOrderPaymentNumberName.visibility = View.GONE
            tvOrderPaymentNumber.visibility = View.GONE
            //创建时间
            tvOrderCreateTimeName.visibility = View.GONE
            tvOrderCreateTime.visibility = View.GONE
            //付款时间
            tvOrderPaymentTimeName.visibility = View.GONE
            tvOrderPaymentTime.visibility = View.GONE
            //发货时间
            tvOrderDeliveryTimeName.visibility = View.GONE
            tvOrderDeliveryTime.visibility = View.GONE
            //完成时间
            tvOrderCompleteTimeName.visibility = View.GONE
            tvOrderCompleteTime.visibility = View.GONE
            //根据订单类型 判断是否显示助力
            when (bean.payloadOrderInfoVO.orderType) {
                0 -> {
                    clHelp.visibility = View.GONE
                }
                1 -> {
                    clHelp.visibility = View.GONE
                    //消耗积分
                    ituiScoresConsume.visibility = View.VISIBLE
                    tvScoresConsumePrice.visibility = View.VISIBLE
                }
                2 -> {
                    clHelp.visibility = View.GONE
                    //金币抵扣
                    ituiGoldConsume.visibility = View.VISIBLE
                    tvGoldConsumePrice.visibility = View.VISIBLE
                }
                3 -> {
                    clHelp.visibility = View.VISIBLE
                    tvPayDisplay.visibility = View.INVISIBLE
                    ituiTitle.apply {
                        if (!PublicPracticalMethodFromKT.ppmfKT.isBlank(bean.payloadGoodsInfoVO.groupAssistVO)) {
                            if (bean.payloadGoodsInfoVO.groupAssistVO.groupMemberNum == bean.groupUserNum) {
                                setAvatarDataFromRes(
                                    R.drawable.ge_helped,
                                    ScreenTools.getInstance().dp2px(App.instance, 24f, true),
                                    ScreenTools.getInstance().dp2px(App.instance, 24f, true)
                                )
                                tvOrderOperate.isEnabled = true
                                setTextData(
                                    "助力成功(${bean.payloadGroupAssistMemberVOList.size}/${bean.groupUserNum})",
                                    16f,
                                    "#FFFFFF"
                                )
                                visibility = View.VISIBLE
                                return@apply
                            }
                        }
                        tvOrderOperate.isEnabled = false
                        setAvatarDataFromRes(
                            R.drawable.ge_helping,
                            ScreenTools.getInstance().dp2px(App.instance, 24f, true),
                            ScreenTools.getInstance().dp2px(App.instance, 24f, true)
                        )
                        setTextData(
                            "正在好友助力中(${if (PublicPracticalMethodFromKT.ppmfKT.isBlank(bean.payloadGroupAssistMemberVOList)) 0 else bean.payloadGroupAssistMemberVOList.size}/${bean.groupUserNum})...",
                            16f,
                            "#FFFFFF"
                        )

                        isEnabled = false
                        pay_color_start = "#33F19B3F"
                        pay_color_end = "#33ED7A57"
                        visibility = View.VISIBLE


                        clHelp.singleClick {
                            with(Bundle()) {
                                putString(
                                    "groupCode",
                                    bean.payloadGoodsInfoVO.groupAssistVO.groupCode
                                )
                                PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                                    this@OrderDetailsActivity,
                                    CheatingActivity::class.java, this, false
                                )
                            }
                        }
                    }
                }
                4, 5, 7 -> {
                    clHelp.visibility = View.GONE
                }
            }

            //根据类型显示订单详情UI
            when (type) {
                OrderListFragment.OrderType.PENDING_PAYMENT -> {
                    tvOrderOperate.apply {
                        text = "立即支付"
                        setTextColor(Color.parseColor("#FFFFFF"))
                        visibility = View.VISIBLE

                        mViewModel.startCountdownTask(
                            bean.payloadOrderInfoVO.currentTime,
                            bean.payloadOrderInfoVO.payEndTime
                        ) {
//                        val hour = it["hour"]!!.times(60).times(60)
                            val minute = it["minute"]!!.times(60)
                            val second = it["second"]!!
                            val countTime = if ((minute + second) < 0L) 1L else (minute + second)

                            val countDownTimer = object : CountDownTimer(countTime * 1000L, 1000) {
                                override fun onTick(millisUntilFinished: Long) {
                                    val time = millisUntilFinished / 1000L
                                    text = "立即支付 ${mViewModel.showCountTimeContent(time)}"
                                    tvPayDisplay.text =
                                        "请在 ${mViewModel.showCountTimeContent(time)} 内支付"
                                }

                                override fun onFinish() {
//                                    ToastUtil.showCenter("订单结束")
                                    isEnabled = false
                                    pay_color_start = "#33F19B3F"
                                    pay_color_end = "#33ED7A57"

                                    PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
                                        tvOrderOperate,
                                        -1,
                                        "",
                                        ScreenTools.getInstance()
                                            .dp2px(this@OrderDetailsActivity, 17f, true)
                                            .toFloat(),
                                        ScreenTools.getInstance()
                                            .dp2px(this@OrderDetailsActivity, 17f, true)
                                            .toFloat(),
                                        ScreenTools.getInstance()
                                            .dp2px(this@OrderDetailsActivity, 17f, true)
                                            .toFloat(),
                                        ScreenTools.getInstance()
                                            .dp2px(this@OrderDetailsActivity, 17f, true)
                                            .toFloat(),
                                        GradientDrawable.Orientation.LEFT_RIGHT,
                                        pay_color_start,
                                        pay_color_end
                                    )
                                    this.cancel()
                                }
                            }.start()
                        }

                        PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
                            tvOrderOperate,
                            -1,
                            "",
                            ScreenTools.getInstance()
                                .dp2px(this@OrderDetailsActivity, 17f, true)
                                .toFloat(),
                            ScreenTools.getInstance()
                                .dp2px(this@OrderDetailsActivity, 17f, true)
                                .toFloat(),
                            ScreenTools.getInstance()
                                .dp2px(this@OrderDetailsActivity, 17f, true)
                                .toFloat(),
                            ScreenTools.getInstance()
                                .dp2px(this@OrderDetailsActivity, 17f, true)
                                .toFloat(),
                            GradientDrawable.Orientation.LEFT_RIGHT,
                            pay_color_start,
                            pay_color_end
                        )

                        singleClick {
                            toPay(bean)
                        }
                    }

                    //助力购的单子
                    if (bean.payloadOrderInfoVO.orderType == 3 &&
                        PublicPracticalMethodFromKT.ppmfKT.isBlank(bean.payloadOrderInfoVO.payInfo)
                    ) {
                        ivNotesMore.visibility = View.VISIBLE
                        clOrderNotes.singleClick {
                            orderRemarksnDialog.show()
                        }
                    }

                    tvOrderOperate2.apply {
                        text = "重新下单"
                        setTextColor(Color.parseColor("#6A7079"))
                        PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
                            this,
                            ScreenTools.getInstance()
                                .dp2px(this@OrderDetailsActivity, 1f, true),
                            "#ECF0F8",
                            ScreenTools.getInstance()
                                .dp2px(this@OrderDetailsActivity, 17f, true)
                                .toFloat(),
                            ScreenTools.getInstance()
                                .dp2px(this@OrderDetailsActivity, 17f, true)
                                .toFloat(),
                            ScreenTools.getInstance()
                                .dp2px(this@OrderDetailsActivity, 17f, true)
                                .toFloat(),
                            ScreenTools.getInstance()
                                .dp2px(this@OrderDetailsActivity, 17f, true)
                                .toFloat(),
                            null,
                            "#00000000"
                        )
                        visibility =
                            if (bean.payloadOrderInfoVO.orderType == 3 &&
                                !PublicPracticalMethodFromKT.ppmfKT.isBlank(bean.payloadOrderInfoVO.payInfo)
                            ) View.VISIBLE else View.GONE
                        singleClick {
                            mViewModel.cancelOrderById(orderId!!) { isSuccess, msg ->
                                if (isSuccess) {
                                    requestOrderPayViewModel.countDownTimer?.cancel()
                                    mViewModel.orderById(orderId!!)
                                } else {
                                    ToastUtil.showCenter(msg)
                                }
                            }
                        }
                    }
                }

                OrderListFragment.OrderType.PENDING_DELIVERED -> {
                    tvPayDisplay.visibility = View.INVISIBLE

                    ituiTitle.apply {
                        visibility = View.VISIBLE
                        this.setAvatarDataFromRes(
                            R.drawable.ge_order_details_pending_delivery,
                            ScreenTools.getInstance().dp2px(App.instance, 24f, true),
                            ScreenTools.getInstance().dp2px(App.instance, 24f, true)
                        )
                        this.setTextData("待提货", 16f, "#FFFFFF")
                    }
                    clPaymentMethod.visibility = View.GONE

                    //运费
                    tvFreight.visibility = View.GONE
                    tvFreightPrice.visibility = View.GONE
                    splitline.visibility = View.GONE
                    tvPickUpAddress.visibility = View.GONE
                    tvPickUpAddressContent.visibility = View.GONE
                    //订单备注
                    clOrderNotes.visibility = View.GONE
                    //实际付款
                    tvRealPaymentName.visibility = View.GONE
                    tvRealPaymentPrice.visibility = View.GONE


                    tvOrderOperate.apply {
                        text = "提货"
                        setTextColor(Color.parseColor("#FFFFFF"))
                        PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
                            this, -1, "",
                            ScreenTools.getInstance()
                                .dp2px(this@OrderDetailsActivity, 17f, true)
                                .toFloat(),
                            ScreenTools.getInstance()
                                .dp2px(this@OrderDetailsActivity, 17f, true)
                                .toFloat(),
                            ScreenTools.getInstance()
                                .dp2px(this@OrderDetailsActivity, 17f, true)
                                .toFloat(),
                            ScreenTools.getInstance()
                                .dp2px(this@OrderDetailsActivity, 17f, true)
                                .toFloat(),
                            GradientDrawable.Orientation.LEFT_RIGHT, "#F19B3F", "#ED7A57"
                        )
                        visibility = View.VISIBLE

                        singleClick {
                            with(Bundle()) {
                                putInt("orderId", orderId!!)
                                PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                                    this@OrderDetailsActivity,
                                    PickGoodsDetailsActivity::class.java,
                                    this,
                                    false
                                )
                            }
                        }
                    }
                    tvOrderOperate2.apply {
                        text = "回收"
                        setTextColor(Color.parseColor("#EF874E"))

                        PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
                            this,
                            ScreenTools.getInstance()
                                .dp2px(this@OrderDetailsActivity, 1f, true),
                            "#EF874E",
                            ScreenTools.getInstance()
                                .dp2px(this@OrderDetailsActivity, 17f, true)
                                .toFloat(),
                            ScreenTools.getInstance()
                                .dp2px(this@OrderDetailsActivity, 17f, true)
                                .toFloat(),
                            ScreenTools.getInstance()
                                .dp2px(this@OrderDetailsActivity, 17f, true)
                                .toFloat(),
                            ScreenTools.getInstance()
                                .dp2px(this@OrderDetailsActivity, 17f, true)
                                .toFloat(),
                            null,
                            "#00000000"
                        )
                        visibility = View.VISIBLE

                        singleClick {
                            with(Bundle()) {
                                putInt("orderId", orderId!!)
                                PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                                    this@OrderDetailsActivity,
                                    RecoverGoodActivity::class.java,
                                    this,
                                    false
                                )
                            }
                        }

                    }
                }
                OrderListFragment.OrderType.PENDING_DELIVERY -> {
                    tvPayDisplay.visibility = View.INVISIBLE
                    clPaymentMethod.visibility = View.GONE
                    ituiTitle.apply {
                        visibility = View.VISIBLE
                        this.setAvatarDataFromRes(
                            R.drawable.ge_order_details_prepare_shipment,
                            ScreenTools.getInstance().dp2px(App.instance, 24f, true),
                            ScreenTools.getInstance().dp2px(App.instance, 24f, true)
                        )
                        this.setTextData("待发货", 16f, "#FFFFFF")
                    }
                    //支付单号
                    tvOrderPaymentNumberName.visibility = View.VISIBLE
                    tvOrderPaymentNumber.visibility = View.VISIBLE
                    //创建时间
                    tvOrderCreateTimeName.visibility = View.VISIBLE
                    tvOrderCreateTime.visibility = View.VISIBLE
                    //付款时间
                    tvOrderPaymentTimeName.visibility = View.VISIBLE
                    tvOrderPaymentTime.visibility = View.VISIBLE


                    tvOrderOperate.apply {
                        text = "变更地址"
                        setTextColor(Color.parseColor("#6A7079"))
                        PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
                            this,
                            ScreenTools.getInstance()
                                .dp2px(this@OrderDetailsActivity, 1f, true),
                            "#ECF0F8",
                            ScreenTools.getInstance()
                                .dp2px(this@OrderDetailsActivity, 17f, true)
                                .toFloat(),
                            ScreenTools.getInstance()
                                .dp2px(this@OrderDetailsActivity, 17f, true)
                                .toFloat(),
                            ScreenTools.getInstance()
                                .dp2px(this@OrderDetailsActivity, 17f, true)
                                .toFloat(),
                            ScreenTools.getInstance()
                                .dp2px(this@OrderDetailsActivity, 17f, true)
                                .toFloat(),
                            null,
                            "#00000000"
                        )
                        visibility = View.VISIBLE
                        tvOrderOperate.isEnabled = true
                        setOnClickListener {
                            with(Bundle()) {
                                putSerializable(
                                    "activitytype",
                                    ActivityType.Order_Details_Activity
                                )
                                PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                                    this@OrderDetailsActivity,
                                    ShippingAddressActivity::class.java, this, false
                                )
                            }
                        }
                    }
                    tvOrderOperate2.visibility = View.GONE
                }
                OrderListFragment.OrderType.PENDING_RECEIPT -> {
                    tvPayDisplay.visibility = View.INVISIBLE
                    ituiTitle.apply {
                        visibility = View.VISIBLE
                        this.setAvatarDataFromRes(
                            R.drawable.ge_order_details_pending_receipt,
                            ScreenTools.getInstance().dp2px(App.instance, 24f, true),
                            ScreenTools.getInstance().dp2px(App.instance, 24f, true)
                        )
                        this.setTextData("待收货", 16f, "#FFFFFF")
                    }
                    clPaymentMethod.visibility = View.GONE
                    ivMore.visibility = View.GONE


                    //支付单号
                    tvOrderPaymentNumberName.visibility = View.VISIBLE
                    tvOrderPaymentNumber.visibility = View.VISIBLE
                    //创建时间
                    tvOrderCreateTimeName.visibility = View.VISIBLE
                    tvOrderCreateTime.visibility = View.VISIBLE
                    //付款时间
                    tvOrderPaymentTimeName.visibility = View.VISIBLE
                    tvOrderPaymentTime.visibility = View.VISIBLE
                    //发货时间
                    tvOrderDeliveryTimeName.visibility = View.VISIBLE
                    tvOrderDeliveryTime.visibility = View.VISIBLE



                    tvOrderOperate.apply {
                        text = "查看物流"
                        setTextColor(Color.parseColor("#6A7079"))
                        PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
                            this,
                            ScreenTools.getInstance()
                                .dp2px(this@OrderDetailsActivity, 1f, true),
                            "#ECF0F8",
                            ScreenTools.getInstance()
                                .dp2px(this@OrderDetailsActivity, 17f, true)
                                .toFloat(),
                            ScreenTools.getInstance()
                                .dp2px(this@OrderDetailsActivity, 17f, true)
                                .toFloat(),
                            ScreenTools.getInstance()
                                .dp2px(this@OrderDetailsActivity, 17f, true)
                                .toFloat(),
                            ScreenTools.getInstance()
                                .dp2px(this@OrderDetailsActivity, 17f, true)
                                .toFloat(),
                            null,
                            "#00000000"
                        )
                        visibility = View.VISIBLE
                        tvOrderOperate.isEnabled = true
                        setOnClickListener {
                            if (dataNullConvertToString(bean.payloadOrderInfoVO.deliveryNo) == "") {
                                ToastUtil.showCenter("快递单号为空！")
                                return@setOnClickListener
                            }
                            logisticsInformationDialog = LogisticsInformationDialog(
                                this@OrderDetailsActivity,
                                mViewModel,
                                ScreenTools.getInstance()
                                    .dp2px(this@OrderDetailsActivity, 375f, true),
                                (0.8 * AutoSizeConfig.getInstance().screenHeight).toInt()
                            )
                            logisticsInformationDialog?.apply {
                                setDeliveryNoData(
                                    bean.payloadOrderInfoVO.deliveryNo,
                                    bean.payloadOrderInfoVO.orderNo
                                )
                                closeDialog {
                                    logisticsInformationDialog = null
                                }
                                show()
                            }
                        }
                    }
                    tvOrderOperate2.visibility = View.GONE
                }
                OrderListFragment.OrderType.COMPLETED -> {
                    tvPayDisplay.visibility = View.INVISIBLE
                    ituiTitle.apply {
                        visibility = View.VISIBLE
                        this.setAvatarDataFromRes(
                            R.drawable.ge_order_details_completed,
                            ScreenTools.getInstance().dp2px(App.instance, 24f, true),
                            ScreenTools.getInstance().dp2px(App.instance, 24f, true)
                        )
                        this.setTextData("已完成", 16f, "#FFFFFF")
                    }
                    clPaymentMethod.visibility = View.GONE
                    ivMore.visibility = View.GONE

                    //支付单号
                    tvOrderPaymentNumberName.visibility = View.VISIBLE
                    tvOrderPaymentNumber.visibility = View.VISIBLE
                    //创建时间
                    tvOrderCreateTimeName.visibility = View.VISIBLE
                    tvOrderCreateTime.visibility = View.VISIBLE
                    //付款时间
                    tvOrderPaymentTimeName.visibility = View.VISIBLE
                    tvOrderPaymentTime.visibility = View.VISIBLE
                    //发货时间
                    tvOrderDeliveryTimeName.visibility = View.VISIBLE
                    tvOrderDeliveryTime.visibility = View.VISIBLE
                    //完成时间
                    tvOrderCompleteTimeName.visibility = View.VISIBLE
                    tvOrderCompleteTime.visibility = View.VISIBLE


                    if (!PublicPracticalMethodFromKT.ppmfKT.isBlank(bean.payloadOrderInfoVO.resultType)
                        && bean.payloadOrderInfoVO.resultType == 1
                    ) {//回收订单
                        clAddress.visibility = View.GONE
                        clHelp.visibility = View.GONE

                        tvFreight.visibility = View.GONE
                        tvFreightPrice.visibility = View.GONE
                        tvOrderPaymentNumberName.visibility = View.GONE
                        tvOrderPaymentNumber.visibility = View.GONE
                        tvOrderPaymentTimeName.visibility = View.GONE
                        tvOrderPaymentTime.visibility = View.GONE
                        tvOrderDeliveryTimeName.visibility = View.GONE
                        tvOrderDeliveryTime.visibility = View.GONE
                        tvOrderDeliveryTimeName.visibility = View.GONE
                        tvOrderDeliveryTime.visibility = View.GONE
                        tvOrderNotes.visibility = View.GONE
                        tvOrderNotesContent.visibility = View.GONE
                        clOrderNotes.visibility = View.GONE

                        tvRealPaymentName.text = "获得积分"
                        tvRealPaymentPrice.text =
                            "${amountDataConvert(bean.payloadOrderInfoVO.recoverScore)}"
                        tvOrderCompleteTimeName.text = "回收时间"
                        tvOrderCompleteTime.text = bean.payloadOrderInfoVO.recoverTime

                    }



                    tvOrderOperate.apply {
                        text = "删除订单"
                        setTextColor(Color.parseColor("#6A7079"))
                        PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
                            this,
                            ScreenTools.getInstance()
                                .dp2px(this@OrderDetailsActivity, 1f, true),
                            "#ECF0F8",
                            ScreenTools.getInstance()
                                .dp2px(this@OrderDetailsActivity, 17f, true)
                                .toFloat(),
                            ScreenTools.getInstance()
                                .dp2px(this@OrderDetailsActivity, 17f, true)
                                .toFloat(),
                            ScreenTools.getInstance()
                                .dp2px(this@OrderDetailsActivity, 17f, true)
                                .toFloat(),
                            ScreenTools.getInstance()
                                .dp2px(this@OrderDetailsActivity, 17f, true)
                                .toFloat(),
                            null,
                            "#00000000"
                        )
                        visibility = View.VISIBLE
                        tvOrderOperate.isEnabled = true

                        singleClick {
                            with(SelectionTooltipDialog(this@OrderDetailsActivity)) {
                                settitle(View.GONE)
                                setContent("确认删除该地址吗")
                                setCancel {
                                    dismissDialog()
                                }
                                setSure {
                                    mViewModel.deleteOrderById(orderId!!) { isSuccess, msg ->
                                        if (isSuccess) {
                                            EventBus.getDefault().post(
                                                UniversalEvent(
                                                    UniversalEvent.EVENT_ORDERLIST_REFRESH,
                                                    OrderListRefreshBean(type, "")
                                                )
                                            )
                                            finish()
                                        }
                                        ToastUtil.showCenter(msg)
                                    }
                                }
                                show()
                            }


                        }

                    }
                    tvOrderOperate2.apply {
                        text = "查看物流"
                        setTextColor(Color.parseColor("#6A7079"))
                        PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
                            this,
                            ScreenTools.getInstance()
                                .dp2px(this@OrderDetailsActivity, 1f, true),
                            "#ECF0F8",
                            ScreenTools.getInstance()
                                .dp2px(this@OrderDetailsActivity, 17f, true)
                                .toFloat(),
                            ScreenTools.getInstance()
                                .dp2px(this@OrderDetailsActivity, 17f, true)
                                .toFloat(),
                            ScreenTools.getInstance()
                                .dp2px(this@OrderDetailsActivity, 17f, true)
                                .toFloat(),
                            ScreenTools.getInstance()
                                .dp2px(this@OrderDetailsActivity, 17f, true)
                                .toFloat(),
                            null,
                            "#00000000"
                        )
                        if (!PublicPracticalMethodFromKT.ppmfKT.isBlank(bean.payloadOrderInfoVO.resultType)
                            && bean.payloadOrderInfoVO.resultType == 1
                        ) {
                            visibility = View.GONE
                        } else {
                            visibility = View.VISIBLE
                        }
                        singleClick {
                            if (dataNullConvertToString(bean.payloadOrderInfoVO.deliveryNo) == "") {
                                ToastUtil.showCenter("快递单号为空！")
                                return@singleClick
                            }
                            logisticsInformationDialog = LogisticsInformationDialog(
                                this@OrderDetailsActivity,
                                mViewModel,
                                ScreenTools.getInstance()
                                    .dp2px(this@OrderDetailsActivity, 375f, true),
                                (0.8 * AutoSizeConfig.getInstance().screenHeight).toInt()
                            )
                            logisticsInformationDialog?.apply {
                                setDeliveryNoData(
                                    bean.payloadOrderInfoVO.deliveryNo,
                                    bean.payloadOrderInfoVO.orderNo
                                )
                                closeDialog {
                                    logisticsInformationDialog = null
                                }
                                show()
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 设置助力好友信息
     */
    private fun setHelpFriendAvatar(
        avatar: String,
        name: String,
        avatar2: String,
        name2: String,
        avatar3: String,
        name3: String
    ) {
        mViewBind.apply {
            ituiHelpFriend.apply {
                setAvatarDataFromUrl(
                    avatar,
                    R.drawable.ge_help_friend_add,
                    ScreenTools.getInstance().dp2px(App.instance, 44f, true),
                    ScreenTools.getInstance().dp2px(App.instance, 44f, true)
                )
                setTextData(name, 12f, "#061925")
            }
            ituiHelpFriend2.apply {
                setAvatarDataFromUrl(
                    avatar2,
                    R.drawable.ge_help_friend_add,
                    ScreenTools.getInstance().dp2px(App.instance, 44f, true),
                    ScreenTools.getInstance().dp2px(App.instance, 44f, true)
                )
                setTextData(name, 12f, "#061925")
            }
            ituiHelpFriend3.apply {
                setAvatarDataFromUrl(
                    avatar3,
                    R.drawable.ge_help_friend_add,
                    ScreenTools.getInstance().dp2px(App.instance, 44f, true),
                    ScreenTools.getInstance().dp2px(App.instance, 44f, true)
                )
                setTextData(name, 12f, "#061925")
            }
        }
    }

    /**
     * 校验是否符合提交规则
     */
    fun checkSubRules(): Boolean {
        var result = true
        if (addressId == -1) {
            result = false
            ToastUtil.showCenter("收货地址不能为空！")
        }
        return result
    }

    /**
     * 去支付
     */
    private fun toPay(bean: OrderDetailsBean) {
        mViewBind.apply {
            if (!checkSubRules()) return
            if (!PublicPracticalMethodFromKT.ppmfKT.isBlank(bean.payloadOrderInfoVO.payInfo)) {
                when (payType) {
                    "A" -> requestOrderPayViewModel.aLiPay(
                        this@OrderDetailsActivity,
                        bean.payloadOrderInfoVO.payInfo
                    )
                    "W" -> requestOrderPayViewModel.wxPay(
                        this@OrderDetailsActivity,
                        bean.payloadOrderInfoVO.payInfo
                    )
                }
                requestOrderPayViewModel.getCountDownTime(
                    2 * 60 * 1000L,
                    bean.payloadOrderInfoVO.orderNo
                ) {
                    with(Bundle()) {
                        putInt("orderId", bean.payloadOrderInfoVO.id)
                        PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                            this@OrderDetailsActivity,
                            PaySuccActivity::class.java, this, true
                        )
                    }
                }
            } else {
                requestOrderPayViewModel.createTransactionOrder(
                    addressId,
                    1,
                    PublicPracticalMethodFromKT.ppmfKT.doubleAdd(
                        bean.payloadGoodsInfoVO.groupSuccessPrice,
                        bean.payloadGoodsInfoVO.postage
                    ),
                    bean.payloadOrderInfoVO.orderAmt,
                    bean.payloadOrderInfoVO.orderNo,
                    "",
                    payType,
                    bean.payloadGoodsInfoVO.postage,
                    remark
                )
            }
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
            mViewBind.tvOrderNotesContent.text = if (remark.isEmpty()) "无备注" else remark
            orderRemarksnDialog.dismiss()
        }

        imgClose.singleClick {
            orderRemarksnDialog.dismiss()
        }

    }


    //Event通知
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun handleEvent(event: UniversalEvent) {
        if (event.actionType == UniversalEvent.EVENT_DELIVERY_ADDRESS
        ) {
            if (!PublicPracticalMethodFromKT.ppmfKT.isBlank(event.message)) {
                val _deliveryAddressBean = event.message as DeliveryAddressBean
                mViewModel.updateOrderAddressId(
                    _deliveryAddressBean.id,
                    orderId!!
                ) { isSuccess, msg ->
                    if (isSuccess) {
                        val bean = event.message as DeliveryAddressBean
                        deliveryAddressBean = bean
                        addressId = deliveryAddressBean!!.id
                        mViewBind.apply {
                            groupAddress.visibility = View.VISIBLE
                            ituiDeliveryAddressAdd.visibility = View.GONE
                            if (bean.type == 0) {
                                tvAddressDefault.visibility = View.VISIBLE
                            } else {
                                tvAddressDefault.visibility = View.GONE
                            }
                            tvAddress.text =
                                "${bean.provinceName}" +
                                        "${bean.cityName}" +
                                        "${bean.areaName}" +
                                        "${bean.streetName}"
                            tvAddress2.text =
                                "${bean.address}"
                            tvNamePhone.text =
                                "${bean.addresseeName} ${bean.phoneNumber}"
                        }
                    }
                    ToastUtil.showCenter(msg)
                }
            }

        }
    }

    /**
     * 物理返回键 监听
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!PublicPracticalMethodFromKT.ppmfKT.isBlank(activityType)) {
                when (activityType) {
                    ActivityType.OrderPayDetails_Activity -> {
                        EventBus.getDefault().post(
                            UniversalEvent(
                                UniversalEvent.EVENT_ORDERLIST_REFRESH,
                                null
                            )
                        )
                    }
                }
            }
            finish()
        }
        return false
    }


    override fun onResume() {
        super.onResume()
        if (PublicPracticalMethodFromKT.ppmfKT.isBlank(orderDetailsBean) ||
            orderDetailsBean?.payloadOrderInfoVO?.orderType == 3
        ) {
            mViewModel.orderById(orderId!!)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }

    override fun handleResponseError(errorCode: Int) {
        //加载错误页面
        App.appViewModelInstance.initErrorPage(mViewBind.errorpage, R.drawable.ic_img_emp) {
            finish()
        }
    }
}