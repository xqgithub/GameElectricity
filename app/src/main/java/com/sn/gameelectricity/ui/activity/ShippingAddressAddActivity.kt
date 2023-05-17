package com.sn.gameelectricity.ui.activity

import android.Manifest
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.Editable
import android.view.KeyEvent
import android.view.View
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import com.sn.gameelectricity.app.base.BaseActivity1
import com.sn.gameelectricity.app.event.AddressAddEvent
import com.sn.gameelectricity.app.util.permission.PermissionRequestManager
import com.sn.gameelectricity.app.weight.dialog.AddressTargetingDialog
import com.sn.gameelectricity.app.weight.dialog.AreaDialog
import com.sn.gameelectricity.app.weight.dialog.SelectionTooltipDialog
import com.sn.gameelectricity.data.model.bean.AreaSelectedBean
import com.sn.gameelectricity.data.model.bean.OrderListRefreshBean
import com.sn.gameelectricity.databinding.ActivityShippingAddressAddBinding
import com.sn.gameelectricity.viewmodel.LocationViewModel
import com.sn.gameelectricity.viewmodel.ShippingAddressViewModel
import me.hgj.jetpackmvvm.event.UniversalEvent
import me.hgj.jetpackmvvm.util.PublicPracticalMethodFromKT
import me.hgj.jetpackmvvm.util.ScreenTools
import me.hgj.jetpackmvvm.util.ToastUtil
import me.jessyan.autosize.AutoSizeConfig
import org.greenrobot.eventbus.EventBus
import singleClick

/**
 * Date:2022/5/12
 * Time:10:44
 * author:dimple
 * 添加收货地址页面/编辑收货地址页面
 */
class ShippingAddressAddActivity :
    BaseActivity1<ShippingAddressViewModel, ActivityShippingAddressAddBinding>() {
    //    private val shippingAddressType: ShippingAddressType by lazy {
//        intent?.getSerializableExtra("ShippingAddressType") as ShippingAddressType
//    }
    //收货地址id
    private val shippingAddressId by lazy {
        intent?.getIntExtra("id", -1)
    }


    private val locationViewModel by viewModels<LocationViewModel>()

    val addressTargetingDialog by lazy {
        AddressTargetingDialog(
            this@ShippingAddressAddActivity,
            mViewModel,
            ScreenTools.getInstance().dp2px(this@ShippingAddressAddActivity, 375f, true),
            (0.77 * AutoSizeConfig.getInstance().screenHeight).toInt()
        )
    }

    private var areaDialog: AreaDialog? = null
    private var areaSelectedBean: AreaSelectedBean? = null
    private var isDefaultAddress: Boolean = false

    override fun initView(savedInstanceState: Bundle?) {
        mViewBind.apply {
            //滑动按钮设置
            sbDefaultAddress.apply {
                setSmallCircleModel(
                    Color.parseColor("#00000000"),
                    Color.parseColor("#F19B3F"),
                    Color.parseColor("#CED3DE"),
                    Color.parseColor("#ffffffff"),
                    Color.parseColor("#ffffffff")
                )
                setOnCheckedListener {
                    isDefaultAddress = it
                }
            }

            PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
                tvConfirm, -1, "",
                ScreenTools.getInstance().dp2px(this@ShippingAddressAddActivity, 24f, true)
                    .toFloat(),
                ScreenTools.getInstance().dp2px(this@ShippingAddressAddActivity, 24f, true)
                    .toFloat(),
                ScreenTools.getInstance().dp2px(this@ShippingAddressAddActivity, 24f, true)
                    .toFloat(),
                ScreenTools.getInstance().dp2px(this@ShippingAddressAddActivity, 24f, true)
                    .toFloat(),
                GradientDrawable.Orientation.LEFT_RIGHT, "#66F19B3F", "#66ED7A57"
            )

            PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
                tvDelete,
                ScreenTools.getInstance().dp2px(this@ShippingAddressAddActivity, 1f, true),
                "#EF874E",
                ScreenTools.getInstance().dp2px(this@ShippingAddressAddActivity, 24f, true)
                    .toFloat(),
                ScreenTools.getInstance().dp2px(this@ShippingAddressAddActivity, 24f, true)
                    .toFloat(),
                ScreenTools.getInstance().dp2px(this@ShippingAddressAddActivity, 24f, true)
                    .toFloat(),
                ScreenTools.getInstance().dp2px(this@ShippingAddressAddActivity, 24f, true)
                    .toFloat(),
                null,
                "#00000000"
            )

            ivBack.singleClick {
                isShowRenounceEditDialog()
            }

            etArea.singleClick {
                if (etArea.text.toString().trim().isEmpty()) {
                    areaSelectedBean = AreaSelectedBean()
                }

                areaDialog = AreaDialog(
                    this@ShippingAddressAddActivity,
                    mViewModel,
                    areaSelectedBean!!,
                    ScreenTools.getInstance().dp2px(this@ShippingAddressAddActivity, 375f, true),
                    (0.65 * AutoSizeConfig.getInstance().screenHeight).toInt()
                )
                areaDialog?.apply {
                    setAreaSelectListener {
                        etArea.text = "${it.mProvince} ${it.mCity} ${it.mArea} ${it.mStreet}"
                        ivClear3.visibility = View.VISIBLE
                        checkNextButton()
                        areaSelectedBean.apply {
                            mProvince = it.mProvince
                            mCity = it.mCity
                            mArea = it.mArea
                            mStreet = it.mStreet
                            areaCode_Province = it.areaCode_Province
                            areaCode_City = it.areaCode_City
                            areaCode_Area = it.areaCode_Area
                            areaCode_Street = it.areaCode_Street
                        }
                        areaDialog!!.dismissDialog()
                        areaDialog = null
                    }
                    closeDialog {
                        areaDialog = null
                    }
                    show()
                }
            }

            ivPosition.singleClick {
                PermissionRequestManager.request(
                    this@ShippingAddressAddActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                    .toSetting()
                    .request { allGranted, _, _ ->
                        if (allGranted) {
                            locationViewModel.fetchLocation()
                                .observe(this@ShippingAddressAddActivity) {
                                    addressTargetingDialog.apply {
                                        setData(it.latitude, it.longitude, "公司")
                                        setItemOnClickListener { poiInfo ->
                                            etArea.text =
                                                "${poiInfo.province} ${poiInfo.city} ${poiInfo.area}"
                                            etAddress.text =
                                                Editable.Factory.getInstance()
                                                    .newEditable("${poiInfo.address}")
                                        }
                                        show()
                                    }
                                }
                        }
                    }
            }

            tvConfirm.singleClick {
                if (etPhone.text.toString().trim().length < 11) {
                    ToastUtil.showCenter("请输入11位有效手机号")
                    return@singleClick
                }

                if (shippingAddressId != -1) {
                    mViewModel.deliveryAddressupdateById(
                        etAddress.text.toString().trim(),
                        etReceiver.text.toString().trim(),
                        etPhone.text.toString().trim(),
                        areaSelectedBean!!.areaCode_Street,
                        if (isDefaultAddress) 0 else -1,
                        shippingAddressId!!
                    ) { isSuccess, msg ->
                        if (isSuccess) {
                            EventBus.getDefault().post(
                                UniversalEvent(
                                    UniversalEvent.EVENT_DELIVERYADDRESSLIST_REFRESH,
                                    null
                                )
                            )
                            finish()
                        }
                        ToastUtil.showCenter(msg)
                    }
                } else {
                    mViewModel.deliveryAddressSave(
                        etAddress.text.toString().trim(),
                        etReceiver.text.toString().trim(),
                        etPhone.text.toString().trim(),
                        areaSelectedBean!!.areaCode_Street,
                        if (isDefaultAddress) 0 else -1
                    ) { isSuccess, msg, addressSaveResponse ->
                        if (isSuccess) {
                            EventBus.getDefault().post(
                                UniversalEvent(
                                    UniversalEvent.EVENT_DELIVERYADDRESSLIST_REFRESH,
                                    null
                                )
                            )
                            EventBus.getDefault().post(
                                AddressAddEvent(
                                    AddressAddEvent.EVENT_ADDRESSADD_CODE, 0
                                )
                            )
                            finish()
                        }
                        ToastUtil.showCenter(msg)
                    }
                }
            }

            tvDelete.singleClick {
                with(SelectionTooltipDialog(this@ShippingAddressAddActivity)) {
                    settitle(View.GONE)
                    setContent("确认删除该地址吗")
                    setCancel {
                        dismissDialog()
                    }
                    setSure {
                        mViewModel.deliveryAddressDelete(shippingAddressId!!) { isSuccess, msg ->
                            if (isSuccess) {
                                EventBus.getDefault().post(
                                    UniversalEvent(
                                        UniversalEvent.EVENT_DELIVERYADDRESSLIST_REFRESH,
                                        null
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
            ivClear.singleClick {
                etReceiver.text = Editable.Factory.getInstance().newEditable("")
            }
            ivClear2.singleClick {
                etPhone.text = Editable.Factory.getInstance().newEditable("")
            }
            ivClear3.singleClick {
                etArea.text = ""
                ivClear3.visibility = View.GONE
                checkNextButton()
            }
            ivClear4.singleClick {
                etAddress.text = Editable.Factory.getInstance().newEditable("")
            }

            etReceiver.doAfterTextChanged { ed ->
                ed?.let {
                    if (it.isNotEmpty()) ivClear.visibility = View.VISIBLE else ivClear.visibility =
                        View.GONE
                }
                checkNextButton()
            }
            etPhone.doAfterTextChanged { ed ->
                ed?.let {
                    if (it.isNotEmpty()) ivClear2.visibility =
                        View.VISIBLE else ivClear2.visibility =
                        View.GONE
                }
                checkNextButton()
            }
            etAddress.doAfterTextChanged { ed ->
                ed?.let {
                    if (it.isNotEmpty()) ivClear4.visibility =
                        View.VISIBLE else ivClear4.visibility =
                        View.GONE
                }
                checkNextButton()
            }
        }
        dataEcho()
        if (shippingAddressId != -1) {
            mViewBind.tvDelete.visibility = View.VISIBLE
            mViewModel.deliveryAddressDetails(shippingAddressId!!)
            mViewBind.tvTitle.text = "编辑收货地址"
        } else {
            mViewBind.tvDelete.visibility = View.GONE
        }
        checkNextButton()
    }

    /**
     * 数据回显
     */
    private fun dataEcho() {
        mViewModel.apply {
            deliveryAddressDetailsLive.observe(this@ShippingAddressAddActivity) { bean ->
                mViewBind.apply {
                    etReceiver.text =
                        Editable.Factory.getInstance().newEditable("${bean.addresseeName}")
                    etPhone.text =
                        Editable.Factory.getInstance().newEditable("${bean.phoneNumber}")
                    etArea.text =
                        "${bean.provinceName} ${bean.cityName} ${bean.areaName} ${bean.streetName}"
                    ivClear3.visibility = View.VISIBLE
                    etAddress.text =
                        Editable.Factory.getInstance().newEditable("${bean.address}")
                    if (bean.type == 0) {
                        isDefaultAddress = true
                        sbDefaultAddress.setChecked(true)
                    } else {
                        isDefaultAddress = false
                        sbDefaultAddress.setChecked(false)
                    }
                    areaSelectedBean = AreaSelectedBean(
                        bean.provinceCode,
                        bean.cityCode,
                        bean.areaCode,
                        bean.streetCode,
                        bean.provinceName,
                        bean.cityName,
                        bean.areaName,
                        bean.streetName
                    )
                }
            }
        }
    }


    /**
     * 检查是否可以点击确认按钮
     */
    private fun checkNextButton() {
        mViewBind.apply {
            var next_bg_color = "#66EF874E"

            val receiver = etReceiver.text.toString().trim()
            val phone = etPhone.text.toString().trim()
            val area = etArea.text.toString().trim()
            val address = etAddress.text.toString().trim()



            if (receiver.isNotEmpty() &&
                phone.isNotEmpty() &&
                area.isNotEmpty() &&
                address.isNotEmpty()
            ) {
                tvConfirm.isEnabled = true
                next_bg_color = "#EF874E"
            } else {
                tvConfirm.isEnabled = false
            }

            PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
                tvConfirm,
                -1,
                "",
                ScreenTools.getInstance().dp2px(this@ShippingAddressAddActivity, 24f, true)
                    .toFloat(),
                ScreenTools.getInstance().dp2px(this@ShippingAddressAddActivity, 24f, true)
                    .toFloat(),
                ScreenTools.getInstance().dp2px(this@ShippingAddressAddActivity, 24f, true)
                    .toFloat(),
                ScreenTools.getInstance().dp2px(this@ShippingAddressAddActivity, 24f, true)
                    .toFloat(),
                null,
                next_bg_color
            )
        }
    }

    /**
     * 确认是否弹出放弃编辑弹框
     */
    private fun isShowRenounceEditDialog() {
        mViewBind.apply {
            val receiver = etReceiver.text.toString().trim()
            val phone = etPhone.text.toString().trim()
            val area = etArea.text.toString().trim()
            val address = etAddress.text.toString().trim()

            if (receiver.isNotEmpty() ||
                phone.isNotEmpty() ||
                area.isNotEmpty() ||
                address.isNotEmpty()
            ) {
                with(SelectionTooltipDialog(this@ShippingAddressAddActivity)) {
                    settitle(View.GONE)
                    setContent("确定放弃编辑当前地址吗")
                    setCancel {
                        dismissDialog()
                    }
                    setSure {
                        dismissDialog()
                        finish()
                    }
                    show()
                }
            } else {
                finish()
            }
        }
    }


    /**
     * 物理返回键 监听
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            isShowRenounceEditDialog()
        }
        return false
    }
}