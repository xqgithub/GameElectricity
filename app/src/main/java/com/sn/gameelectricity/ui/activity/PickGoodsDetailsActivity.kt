package com.sn.gameelectricity.ui.activity

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import com.gyf.immersionbar.ImmersionBar
import com.sn.gameelectricity.R
import com.sn.gameelectricity.app.base.BaseActivity1
import com.sn.gameelectricity.app.event.AddressAddEvent
import com.sn.gameelectricity.app.util.load
import com.sn.gameelectricity.app.weight.customview.emoji.EmojiconEditText
import com.sn.gameelectricity.app.weight.dialog.AddAddressDialog
import com.sn.gameelectricity.data.model.bean.DeliveryAddressBean
import com.sn.gameelectricity.data.model.bean.OrderDetailsBean
import com.sn.gameelectricity.databinding.ActivityPickgoodsDetailsBinding
import com.sn.gameelectricity.ui.enum.ActivityType
import com.sn.gameelectricity.ui.fragment.OrderListFragment
import com.sn.gameelectricity.viewmodel.OrderDetailsViewModel
import com.sn.gameelectricity.viewmodel.OrderPayDetailsViewModel
import com.sn.gameelectricity.viewmodel.ShippingAddressViewModel
import com.sn.gameelectricity.viewmodel.request.RequestOrderPayViewModel
import kotlinx.android.synthetic.main.activity_orderpay_details.*
import kotlinx.android.synthetic.main.activity_pickgoods_details.*
import kotlinx.android.synthetic.main.activity_pickgoods_details.clAbholung
import kotlinx.android.synthetic.main.activity_pickgoods_details.clAddress
import kotlinx.android.synthetic.main.activity_pickgoods_details.clAddressEmp
import kotlinx.android.synthetic.main.activity_pickgoods_details.tvAddress
import kotlinx.android.synthetic.main.activity_pickgoods_details.tvAddressDefault
import kotlinx.android.synthetic.main.activity_pickgoods_details.tvDistributionValue
import kotlinx.android.synthetic.main.activity_pickgoods_details.tvOrderRemarksValue
import kotlinx.android.synthetic.main.activity_pickgoods_details.tvSupplierAddress
import kotlinx.android.synthetic.main.activity_pickgoods_details.tvSupplierPhone
import me.hgj.jetpackmvvm.util.PublicPracticalMethodFromKT
import me.shaohui.bottomdialog.BottomDialog
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import singleClick
import java.text.DecimalFormat
import java.text.MessageFormat


/**
 * 提货 详情
 */
class PickGoodsDetailsActivity :
    BaseActivity1<OrderPayDetailsViewModel, ActivityPickgoodsDetailsBinding>() {

    private var actionPosition: Int = 0
    private var distributionType: Int = 1
    private var supplierAddress: String = ""
    private var remark: String = ""
    private var payType: String = "A"
    private var mPostage: Double = 0.00

    private lateinit var orderDetailsBean: OrderDetailsBean
    private var deliveryAddressBean: DeliveryAddressBean? = null
    private val orderDetailsViewModel: OrderDetailsViewModel by viewModels()
    private val requestOrderPayViewModel: RequestOrderPayViewModel by viewModels()
    private val shippingAddressViewModel: ShippingAddressViewModel by viewModels()

    private val orderId: Int by lazy {
        intent.getIntExtra("orderId", 0)
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
                        this@PickGoodsDetailsActivity,
                        ShippingAddressActivity::class.java, this, false
                    )
                }
            }

            clAddressEmp.singleClick {
                with(Bundle()) {
                    putSerializable("activitytype", ActivityType.OrderPayDetails_Activity)
                    PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                        this@PickGoodsDetailsActivity,
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


        distributionDialog.setViewListener {
            initDistributionDialog(it)
        }.setLayoutRes(R.layout.dialog_bottom_distribution)

        orderRemarksnDialog.setViewListener {
            initOrderRemarksDialog(it)
        }.setLayoutRes(R.layout.dialog_bottom_orderremarks)
    }

    override fun createObserver() {
        orderDetailsViewModel.orderById(orderId)
        orderDetailsViewModel.orderByIdLive.observe(this, Observer {
            orderDetailsBean = it
            loadProductData(it)
        })

        requestOrderPayViewModel.createTransactionOrderLive.observe(this, Observer {
            with(Bundle()) {
                putSerializable("activitytype", ActivityType.OrderPayDetails_Activity)
                putSerializable("type", OrderListFragment.OrderType.PENDING_PAYMENT)
                putInt("orderId", it.id)
                PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                    this@PickGoodsDetailsActivity,
                    OrderDetailsActivity::class.java, this, true
                )
            }

//            requestOrderPayViewModel.getCountDownTime((120 * 1000).toLong(), it.orderNo) {
//                with(Bundle()) {
//                    LogUtils.e("789")
//                    putInt("orderId", it.id)
//                    PublicPracticalMethodFromKT.ppmfKT.intentToJump(
//                        this@PickGoodsDetailsActivity,
//                        PaySuccActivity::class.java, this, true
//                    )
//                }
//            }
//            when (payType) {
//                "A" -> {
//                    requestOrderPayViewModel.aLiPay(this@PickGoodsDetailsActivity, it.payInfo)
//                }
//                "W" -> {
//                    requestOrderPayViewModel.wxPay(this@PickGoodsDetailsActivity, it.payInfo)
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

    private fun loadProductData(bean: OrderDetailsBean) {
        mViewBind.apply {
            ivIcon.load(
                bean.payloadGoodsInfoVO.icon
            )

            tvGoodsName.text = bean.payloadGoodsInfoVO.goodsName
            if (bean.payloadGoodsInfoVO.postage > 0) {
                tvDistributionValue.text =
                    "快递" + DecimalFormat("0.00").format( bean.payloadGoodsInfoVO.postage) + "元"
                distributionType = 1
                clAbholung.visibility = View.GONE
                mPostage = bean.payloadGoodsInfoVO.postage
            } else {
                tvDistributionValue.text = "包邮"
                clAbholung.visibility = View.GONE
                distributionType = 1
                mPostage = 0.00
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
                    if (mPostage > 0) {
                        requestOrderPayViewModel.createTransactionOrder(
                            deliveryAddressBean!!.id,
                            distributionType,
                            mPostage,
                            mPostage,
                            orderDetailsBean.payloadOrderInfoVO.orderNo,
                            "",
                            payType,
                            mPostage,
                            remark
                        )
                    } else {
                        requestOrderPayViewModel.specialOrderCompletion(
                            deliveryAddressBean!!.id,
                            distributionType,
                            mPostage,
                            mPostage,
                            orderDetailsBean.payloadOrderInfoVO.orderNo,
                            "",
                            payType,
                            mPostage,
                            remark

                        )
                        with(Bundle()) {
                            putInt("orderId", orderId)
                            PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                                this@PickGoodsDetailsActivity,
                                PickGoodsSuccActivity::class.java, this, true
                            )
                        }
                    }
                } else {
                    clAddress.visibility = View.GONE
                    clAddressEmp.visibility = View.VISIBLE
                    val addAddressDialog = AddAddressDialog(this@PickGoodsDetailsActivity)
                    addAddressDialog.onClickSure {
                        with(Bundle()) {
                            putSerializable(
                                "activitytype",
                                ActivityType.OrderPayDetails_Activity
                            )
                            PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                                this@PickGoodsDetailsActivity,
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
        var tvSure = view.findViewById<TextView>(R.id.tvSure)
        var tvPostage = view.findViewById<TextView>(R.id.tvPostage)
        if (orderDetailsBean.payloadGoodsInfoVO.postage > 0) {
            tvPostage.text =
                "快递" + DecimalFormat("0.00").format(orderDetailsBean.payloadGoodsInfoVO.postage) + "元"
        } else {
            tvPostage.text =
                "包邮"
        }

        orderDetailsBean.payloadGoodsInfoVO.supplierAddress?.let {
            contentEditView.text = "${orderDetailsBean.payloadGoodsInfoVO.supplierAddress}"
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
                    if (orderDetailsBean.payloadGoodsInfoVO.postage > 0) {
                        distributionType = 1
                    } else {
                        distributionType = 1
                    }
                    if (orderDetailsBean.payloadGoodsInfoVO.postage > 0) {
                        tvDistributionValue.text =
                            "快递" + DecimalFormat("0.00").format(orderDetailsBean.payloadGoodsInfoVO.postage) + "元"
                        mPostage = orderDetailsBean.payloadGoodsInfoVO.postage
                    } else {
                        tvDistributionValue.text =
                            "包邮"
                        mPostage = 0.00
                    }
                    clAbholung.visibility = View.GONE
                }
                1 -> {
                    distributionType = 2
                    clAbholung.visibility = View.VISIBLE
                    tvDistributionValue.text = "自提"
                    mViewBind.tvSupplierAddr.text = supplierAddress
                    mPostage = 0.00
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