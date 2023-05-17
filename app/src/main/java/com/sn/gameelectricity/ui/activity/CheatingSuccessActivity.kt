package com.sn.gameelectricity.ui.activity

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.gyf.immersionbar.ImmersionBar
import com.sn.gameelectricity.R
import com.sn.gameelectricity.app.base.BaseActivity1
import com.sn.gameelectricity.app.event.AddressAddEvent
import com.sn.gameelectricity.app.util.load
import com.sn.gameelectricity.app.weight.dialog.AddAddressDialog
import com.sn.gameelectricity.data.model.bean.DeliveryAddressBean
import com.sn.gameelectricity.data.model.bean.GoodsGetgoodsResponse
import com.sn.gameelectricity.databinding.ActivityCheatingDetailsBinding
import com.sn.gameelectricity.ui.enum.ActivityType
import com.sn.gameelectricity.viewmodel.CheatingDetailsViewModel
import com.sn.gameelectricity.viewmodel.ShippingAddressViewModel
import com.sn.gameelectricity.viewmodel.request.RequestCheatingViewModel
import com.sn.gameelectricity.viewmodel.request.RequestProductDetailsViewModel
import kotlinx.android.synthetic.main.activity_cheating_details.*
import me.hgj.jetpackmvvm.util.PublicPracticalMethodFromKT
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import singleClick
import java.text.DecimalFormat


/**
 * 助力成功详情
 */
class CheatingSuccessActivity :
    BaseActivity1<CheatingDetailsViewModel, ActivityCheatingDetailsBinding>() {

    private val requestProductDetailsViewModel: RequestProductDetailsViewModel by viewModels()
    private val requestCheatingViewModel: RequestCheatingViewModel by viewModels()
    private val shippingAddressViewModel: ShippingAddressViewModel by viewModels()

    private lateinit var goodsGetgoodsResponse: GoodsGetgoodsResponse
    private lateinit var deliveryAddressBean: DeliveryAddressBean

    private var actionPosition: Int = 0
    private var payType: String = "A"
    private var distributionType: Int = 1
    private var supplierAddress: String = ""

    private val groupCode: String by lazy {
        intent.getStringExtra("groupCode") as String
    }

    private val goodsId: Int by lazy {
        intent.getIntExtra("goodsId", 0)
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this)
            .transparentStatusBar()
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
                        this@CheatingSuccessActivity,
                        ShippingAddressActivity::class.java, this, false
                    )
                }
            }

            clAddressEmp.singleClick {
                PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                    this@CheatingSuccessActivity,
                    ShippingAddressAddActivity::class.java, null, false
                )
            }

        }
    }

    override fun createObserver() {
        requestProductDetailsViewModel.goodsGetGoods(goodsId)
        requestProductDetailsViewModel.productGetGoods.observe(this, Observer {
            goodsGetgoodsResponse = it
            loadProductData(it)
        })

        requestCheatingViewModel.assistLiveData.observe(this, Observer {
            with(Bundle()) {
                putString("groupCode", it.groupCode)
                PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                    this@CheatingSuccessActivity,
                    CheatingActivity::class.java, this, true
                )
            }
        })

        shippingAddressViewModel.deliveryAddressList()
        shippingAddressViewModel.deliveryAddressLive.observe(this) { beans ->
            beans?.let {
                clAddress.visibility = View.VISIBLE
                clAddressEmp.visibility = View.GONE
                for ((index, it) in beans.withIndex()) {
                    if (it.type == 0) {
                        tvAddressDefault.visibility = View.VISIBLE
                        deliveryAddressBean = it
                        tvSupplierPhone.text = "${it.addresseeName} ${it.phoneNumber}"
                        tvAddress.text =
                            "${it.provinceName} ${it.cityName} ${it.areaName} ${it.streetName}"
                        tvSupplierAddress.text =
                            "${it.address}"
                    } else {
                        tvAddressDefault.visibility = View.GONE
                        deliveryAddressBean = beans.get(actionPosition)
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
            } ?: let {
                clAddress.visibility = View.GONE
                clAddressEmp.visibility = View.VISIBLE
                val addAddressDialog = AddAddressDialog(this@CheatingSuccessActivity)
                addAddressDialog.onClickSure {
                    PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                        this@CheatingSuccessActivity,
                        ShippingAddressAddActivity::class.java, null, false
                    )
                    addAddressDialog.dismissDialog()
                }
                addAddressDialog.show()
            }
        }
    }

    private fun loadProductData(goodsGetgoodsResponse: GoodsGetgoodsResponse) {
        mViewBind.apply {
            ivIcon.load(goodsGetgoodsResponse.icon)
            tvGoodsName.text = goodsGetgoodsResponse.goodsName
            tvPatternName.text = goodsGetgoodsResponse.patternName
            tvDiscountPrice.text = "¥" + goodsGetgoodsResponse.discountPrice

            if (goodsGetgoodsResponse.postage > 0) {
                tvDistributionValue.text =
                    "¥" + DecimalFormat("#.##").format(goodsGetgoodsResponse.postage)
            } else {
                tvDistributionValue.text =
                    "包邮"
            }

            if (goodsGetgoodsResponse.postage > 0) {
                distributionType = 2
            } else {
                distributionType = 1
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

            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun handleEvent(addressAddEvent: AddressAddEvent) {
        if (addressAddEvent.actionType == AddressAddEvent.EVENT_ADDRESSADD_CODE
        ) {
            shippingAddressViewModel.deliveryAddressList()
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