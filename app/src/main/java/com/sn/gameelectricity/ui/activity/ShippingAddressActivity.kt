package com.sn.gameelectricity.ui.activity

import android.app.Activity
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.gyf.immersionbar.ImmersionBar
import com.sn.gameelectricity.R
import com.sn.gameelectricity.app.App
import com.sn.gameelectricity.app.base.BaseActivity1
import com.sn.gameelectricity.app.event.AddressAddEvent
import com.sn.gameelectricity.app.util.ConstraintUtil
import com.sn.gameelectricity.app.weight.customview.ErrorPageView
import com.sn.gameelectricity.app.weight.dialog.SelectionTooltipDialog
import com.sn.gameelectricity.databinding.ActivityShippingAddressBinding
import com.sn.gameelectricity.databinding.FragmentMoneyBinding
import com.sn.gameelectricity.ui.adapter.ShippingAddressAdapter
import com.sn.gameelectricity.ui.enum.ActivityType
import com.sn.gameelectricity.viewmodel.ShippingAddressViewModel
import me.hgj.jetpackmvvm.event.UniversalEvent
import me.hgj.jetpackmvvm.ext.addMoreData
import me.hgj.jetpackmvvm.ext.removeEmptyView
import me.hgj.jetpackmvvm.util.PublicPracticalMethodFromKT
import me.hgj.jetpackmvvm.util.ScreenTools
import me.hgj.jetpackmvvm.util.ToastUtil
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import singleClick

/**
 * Date:2022/5/11
 * Time:19:04
 * author:dimple
 * 收货地址 页面
 */
class ShippingAddressActivity :
    BaseActivity1<ShippingAddressViewModel, ActivityShippingAddressBinding>() {

    private lateinit var shippingAddressAdapter: ShippingAddressAdapter

    private var pageNum: Int = 1

    val activityType by lazy {
        val serializable = intent?.getSerializableExtra("activitytype")
        if (!PublicPracticalMethodFromKT.ppmfKT.isBlank(serializable)) {
            serializable as ActivityType
        } else {
            null
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        //注册EventBus
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }


        mViewBind.apply {

            PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
                clAddAddress, -1, "",
                ScreenTools.getInstance().dp2px(this@ShippingAddressActivity, 24f, true).toFloat(),
                ScreenTools.getInstance().dp2px(this@ShippingAddressActivity, 24f, true).toFloat(),
                ScreenTools.getInstance().dp2px(this@ShippingAddressActivity, 24f, true).toFloat(),
                ScreenTools.getInstance().dp2px(this@ShippingAddressActivity, 24f, true).toFloat(),
                GradientDrawable.Orientation.LEFT_RIGHT, "#F19B3F", "#ED7A57"
            )


            ivBack.singleClick {
                finish()
            }

            clAddAddress.singleClick {
                PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                    this@ShippingAddressActivity,
                    ShippingAddressAddActivity::class.java, null, false
                )
            }


        }
        initShippingAddressList()
        dataEcho()
        mViewModel.deliveryAddressList()
    }

    /**
     * 初始化列表
     */
    private fun initShippingAddressList() {
        mViewBind.apply {
            shippingAddressAdapter =
                ShippingAddressAdapter(this@ShippingAddressActivity, mViewModel)
            recyclerView.apply {
                layoutManager = LinearLayoutManager(this@ShippingAddressActivity)
                adapter = shippingAddressAdapter

                isRefreshEnabled = false
                isLoadMoreEnabled = true

                clearAnimation()
                setLoadingMoreBottomHeight(36f)

//                setOnItemClickListener { v, position ->
//                    val itemData = shippingAddressAdapter.getItemData(position)
//                    LogUtils.e(position)
//                    EventBus.getDefault().post(
//                        AddressAddEvent(
//                            AddressAddEvent.EVENT_ADDRESSADD_CODE
//                        )
//                    )
//                    finish()
//                }

                setOnItemChildClickListener { view, position ->
                    when (view.id) {
                        R.id.btnDelete -> {
                            with(SelectionTooltipDialog(this@ShippingAddressActivity)) {
                                settitle(View.GONE)
                                setContent("确认删除该地址吗")
                                setCancel {
                                    dismissDialog()
                                }
                                setSure {
                                    mViewModel.deliveryAddressDelete(
                                        shippingAddressAdapter.getItemData(
                                            position
                                        ).id
                                    ) { isSuccess, msg ->
                                        if (isSuccess) {
                                            mViewModel.deliveryAddressList()
                                        }
                                        ToastUtil.showCenter(msg)
                                        dismissDialog()
                                    }
                                }
                                show()
                            }
                        }
                        R.id.iv_edit -> {
                            with(Bundle()) {
                                putInt("id", shippingAddressAdapter.getItemData(position).id)
                                PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                                    this@ShippingAddressActivity,
                                    ShippingAddressAddActivity::class.java, this, false
                                )
                            }
                        }
                        R.id.cl_content -> {
                            if (PublicPracticalMethodFromKT.ppmfKT.isBlank(activityType)) {
                                return@setOnItemChildClickListener
                            }
                            if (activityType == ActivityType.Order_Details_Activity) {
                                val bean = shippingAddressAdapter.getItemData(position)
                                EventBus.getDefault().post(
                                    UniversalEvent(
                                        UniversalEvent.EVENT_DELIVERY_ADDRESS,
                                        bean
                                    )
                                )
                                finish()
                            }
                            if (activityType == ActivityType.OrderPayDetails_Activity) {
                                EventBus.getDefault().post(
                                    AddressAddEvent(
                                        AddressAddEvent.EVENT_ADDRESSADD_CODE,
                                        position
                                    )
                                )
                                finish()
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 数据回显
     */
    private fun dataEcho() {
        mViewModel.apply {
            deliveryAddressLive.observe(this@ShippingAddressActivity) { beans ->
                if (pageNum == 1) {
                    if (beans.isEmpty()) {
                        bindEmptyView()
                    } else {
                        mViewBind.recyclerView.removeEmptyView()
                        shippingAddressAdapter.setNewDataIgnoreSize(beans)
                    }
                } else {
                    shippingAddressAdapter.addMoreData(beans)
                }
            }
        }
    }


    /**
     * 当列表数据为空的时候，显示
     */
    private fun bindEmptyView() {
        val errorPageView = ErrorPageView(this@ShippingAddressActivity)
        with(errorPageView) {
            post {
                val recyclerView_height = mViewBind.recyclerView.height

                setErrorIcon(
                    R.drawable.ge_error,
                    ScreenTools.getInstance().dp2px(App.instance, 124f, true),
                    ScreenTools.getInstance().dp2px(App.instance, 124f, true)
                )
                setErrorContent("您还没有收货地址哦，赶快设置一个吧～", 12f, "#A1A7AF")
                changeErrorIconPositionToTOP(
                    (0.4 * recyclerView_height).toInt()
                )
                changeErrorTextPositionToTOP(
                    ScreenTools.getInstance().dp2px(App.instance, 5f, true)
                )
            }
        }

        mViewBind.recyclerView.apply {
            setEmptyView(errorPageView)
//            setLoadingMoreBottomHeight(0f)
            isLoadMoreEnabled = false
            shippingAddressAdapter.setNewData(arrayListOf())
        }
    }


    /**
     * 根据状态栏高度设置控件高度
     */
    fun setTopViewHeight(mActivity: Activity, mBinding: FragmentMoneyBinding) {
        var StatusBarHeight = ImmersionBar.getStatusBarHeight(mActivity)
        val constraintUtil = ConstraintUtil(mBinding.clMain)
        with(constraintUtil.begin()) {
            setMarginTop(
                R.id.siv_avatar,
                StatusBarHeight + ScreenTools.getInstance().dp2px(mActivity, 25f, true)
            )
            commit()
        }
    }


    //Event通知
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun handleEvent(event: UniversalEvent) {
        if (event.actionType == UniversalEvent.EVENT_DELIVERYADDRESSLIST_REFRESH
        ) {
            mViewModel.deliveryAddressList()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }

}