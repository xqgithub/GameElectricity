package com.sn.gameelectricity.ui.fragment

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.LogUtils
import com.gyf.immersionbar.ImmersionBar
import com.sn.gameelectricity.R
import com.sn.gameelectricity.app.App
import com.sn.gameelectricity.app.base.BaseFragment1
import com.sn.gameelectricity.app.util.load
import com.sn.gameelectricity.app.weight.customview.CommentDialogDecoration
import com.sn.gameelectricity.app.weight.customview.ErrorPageView
import com.sn.gameelectricity.app.weight.dialog.LogisticsInformationDialog
import com.sn.gameelectricity.app.weight.dialog.ManorDialog
import com.sn.gameelectricity.app.weight.dialog.SelectionTooltipDialog
import com.sn.gameelectricity.data.model.bean.DeliveryAddressBean
import com.sn.gameelectricity.data.model.bean.OrderListRefreshBean
import com.sn.gameelectricity.databinding.FragmentOrderListBinding
import com.sn.gameelectricity.ui.activity.*
import com.sn.gameelectricity.ui.adapter.OrderListAdapter
import com.sn.gameelectricity.ui.enum.ActivityType
import com.sn.gameelectricity.viewmodel.OrderDetailsViewModel
import com.sn.gameelectricity.viewmodel.OrderListViewModel
import com.sn.gameelectricity.viewmodel.state.MainViewModel
import me.hgj.jetpackmvvm.common.ConfigConstants
import me.hgj.jetpackmvvm.event.UniversalEvent
import me.hgj.jetpackmvvm.ext.addMoreData
import me.hgj.jetpackmvvm.ext.removeEmptyView
import me.hgj.jetpackmvvm.util.PublicPracticalMethodFromKT
import me.hgj.jetpackmvvm.util.ScreenTools
import me.hgj.jetpackmvvm.util.ToastUtil
import me.hgj.jetpackmvvm.util.dataNullConvertToString
import me.jessyan.autosize.AutoSizeConfig
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * Date:2022/5/10
 * Time:11:06
 * author:dimple
 * 订单列表 页面
 */
class OrderListFragment : BaseFragment1<OrderListViewModel, FragmentOrderListBinding>() {

    enum class OrderType {
        All,//全部
        PENDING_PAYMENT,//待付款
        PENDING_DELIVERED,//待提货
        PENDING_DELIVERY,//待发货
        PENDING_RECEIPT,//待收货
        COMPLETED,//已完成
        SEARCH//搜索
    }

    private val orderDetailsViewModel: OrderDetailsViewModel by viewModels()

    private lateinit var type: OrderType

    private lateinit var orderListAdapter: OrderListAdapter

    private var pageNum: Int = 1

    private var searchKeywords = ""

    private var changeaddress_orderId = -1

    private var logisticsInformationDialog: LogisticsInformationDialog? = null

    companion object {
        fun newInstance(type: OrderType): OrderListFragment {
            val orderListFragment = OrderListFragment()
            with(Bundle()) {
                putSerializable("type", type)
                orderListFragment.arguments = this
            }
            return orderListFragment
        }
    }


    override fun initView(savedInstanceState: Bundle?) {
        arguments?.let {
            type = it.getSerializable("type") as OrderType
        }

        //注册EventBus
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }

        initOrderList()
        dataEcho()
    }

    /**
     * 初始化列表
     */
    private fun initOrderList() {
        mViewBind.apply {
            orderListAdapter = OrderListAdapter(requireActivity(), mViewModel)
            recyclerView.apply {
                layoutManager = LinearLayoutManager(requireActivity())
                adapter = orderListAdapter
                addItemDecoration(
                    CommentDialogDecoration().getInstance()
                        .setSpaceItemDecoration(
                            ScreenTools.getInstance().dp2px(requireActivity(), 12f, true),
                            ScreenTools.getInstance().dp2px(requireActivity(), 8f, true),
                            ScreenTools.getInstance().dp2px(requireActivity(), 12f, true),
                            ScreenTools.getInstance().dp2px(requireActivity(), 4f, true)
                        )
                )

                setOnRefreshListener {
                    pageNum = 1
                    orderListAdapter.cancelAllTimers()
                    if (type == OrderType.SEARCH) {
                        mViewModel.searchUserOrder(searchKeywords, pageNum)
                    } else {
                        mViewModel.orderList(type, pageNum)
                    }
                }

                setOnLoadMoreListener {
                    pageNum++
                    if (type == OrderType.SEARCH) {
                        mViewModel.searchUserOrder(searchKeywords, pageNum)
                    } else {
                        mViewModel.orderList(type, pageNum)
                    }
                }

                clearAnimation()
                setLoadingMoreBottomHeight(86f)


                setOnItemClickListener { v, position ->
                    with(Bundle()) {
                        var _type = type
                        val status =
                            orderListAdapter.getItemData(position).payloadOrderSimpleInfoVO.status
                        when (status) {
                            0 -> {
                                _type = OrderType.PENDING_PAYMENT
                            }
                            1 -> {
                                _type = OrderType.PENDING_DELIVERED
                            }
                            2 -> {
                                _type = OrderType.PENDING_DELIVERY
                            }
                            3 -> {
                                _type = OrderType.PENDING_RECEIPT
                            }
                            4 -> {
                                _type = OrderType.COMPLETED
                            }
                        }
                        putSerializable("type", _type)
                        putInt(
                            "orderId",
                            orderListAdapter.getItemData(position).payloadOrderSimpleInfoVO.id
                        )
                        PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                            requireActivity(),
                            OrderDetailsActivity::class.java, this, false
                        )
                    }
                }

                setOnItemChildClickListener { v, position ->
                    val bean = orderListAdapter.getItemData(position)
                    val status = bean.payloadOrderSimpleInfoVO.status
                    when (v.id) {
                        R.id.tv_order_operate -> {
                            when (status) {
                                0 -> {//待付款
                                }
                                1 -> {//待提货
                                    with(Bundle()) {
                                        putInt("orderId", bean.payloadOrderSimpleInfoVO.id)
                                        PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                                            requireActivity(),
                                            PickGoodsDetailsActivity::class.java,
                                            this,
                                            false
                                        )
                                    }
                                }
                                2 -> {//待发货
                                    with(Bundle()) {
                                        putSerializable(
                                            "activitytype",
                                            ActivityType.Order_Details_Activity
                                        )
                                        changeaddress_orderId = bean.payloadOrderSimpleInfoVO.id
                                        PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                                            requireActivity(),
                                            ShippingAddressActivity::class.java, this, false
                                        )
                                    }
                                }
                                3 -> {//待收货,完成
                                    if (dataNullConvertToString(bean.payloadOrderSimpleInfoVO.deliveryNo) == "") {
                                        ToastUtil.showCenter("快递单号为空！")
                                        return@setOnItemChildClickListener
                                    }
                                    logisticsInformationDialog = LogisticsInformationDialog(
                                        requireActivity(),
                                        orderDetailsViewModel,
                                        ScreenTools.getInstance()
                                            .dp2px(requireActivity(), 375f, true),
                                        (0.8 * AutoSizeConfig.getInstance().screenHeight).toInt()
                                    )
                                    logisticsInformationDialog?.apply {
                                        setDeliveryNoData(
                                            bean.payloadOrderSimpleInfoVO.deliveryNo,
                                            bean.payloadOrderSimpleInfoVO.orderNo
                                        )
                                        closeDialog {
                                            logisticsInformationDialog = null
                                        }
                                        show()
                                    }
                                }
                                4 -> {//完成
                                    with(SelectionTooltipDialog(requireActivity())) {
                                        settitle(View.GONE)
                                        setContent("确认删除该地址吗")
                                        setCancel {
                                            dismissDialog()
                                        }
                                        setSure {
                                            orderDetailsViewModel.deleteOrderById(bean.payloadOrderSimpleInfoVO.id) { isSuccess, msg ->
                                                if (isSuccess) {
                                                    EventBus.getDefault().post(
                                                        UniversalEvent(
                                                            UniversalEvent.EVENT_ORDERLIST_REFRESH,
                                                            OrderListRefreshBean(type, "")
                                                        )
                                                    )
                                                }
                                                ToastUtil.showCenter(msg)
                                                dismissDialog()
                                            }
                                        }
                                        show()
                                    }
                                }
                            }
                        }
                        R.id.tv_order_operate2 -> {
                            when (status) {
                                0 -> {//待付款
                                }
                                1 -> {//待提货
                                    with(Bundle()) {
                                        putInt("orderId", bean.payloadOrderSimpleInfoVO.id)
                                        PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                                            requireActivity(),
                                            RecoverGoodActivity::class.java,
                                            this,
                                            false
                                        )
                                    }
                                }
                                2 -> {//待发货

                                }
                                3 -> {//待收货

                                }
                                4 -> {//完成
                                    if (dataNullConvertToString(bean.payloadOrderSimpleInfoVO.deliveryNo) == "") {
                                        ToastUtil.showCenter("快递单号为空！")
                                        return@setOnItemChildClickListener
                                    }
                                    logisticsInformationDialog = LogisticsInformationDialog(
                                        requireActivity(),
                                        orderDetailsViewModel,
                                        ScreenTools.getInstance()
                                            .dp2px(requireActivity(), 375f, true),
                                        (0.8 * AutoSizeConfig.getInstance().screenHeight).toInt()
                                    )
                                    logisticsInformationDialog?.apply {
                                        setDeliveryNoData(
                                            bean.payloadOrderSimpleInfoVO.deliveryNo,
                                            bean.payloadOrderSimpleInfoVO.orderNo
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
        }


    }

    /**
     * 当列表数据为空的时候，显示
     */
    private fun bindEmptyView() {
        val errorPageView = ErrorPageView(requireContext())
        with(errorPageView) {
            post {
                var errorIcon = R.drawable.ic_img_emp
                var ErrorContent = "暂无数据"
                var offsetTop = 0
                if (type == OrderType.SEARCH) {
                    errorIcon = R.drawable.ge_error2
                    ErrorContent = "没有找到相关订单噢～"
                    offsetTop = ScreenTools.getInstance().dp2px(requireActivity(), 14f, true)
                }

                val recyclerView_height = mViewBind.recyclerView.height
                setErrorIcon(
                    errorIcon,
                    ScreenTools.getInstance().dp2px(App.instance, 124f, true),
                    ScreenTools.getInstance().dp2px(App.instance, 124f, true)
                )
                setErrorContent(ErrorContent, 12f, "#A1A7AF")
                changeErrorIconPositionToTOP((0.3 * recyclerView_height).toInt())

                changeErrorTextPositionToTOP(offsetTop)
            }

        }

        mViewBind.recyclerView.apply {
            setEmptyView(errorPageView)
//            setLoadingMoreBottomHeight(0f)
            isLoadMoreEnabled = false
            orderListAdapter.setNewData(arrayListOf())
        }
    }

    override fun lazyLoadData() {
        mViewBind.apply {
            when (type) {
                OrderType.All -> {
                }
                OrderType.PENDING_PAYMENT -> {
                }
                OrderType.PENDING_DELIVERED -> {
                }
                OrderType.PENDING_DELIVERY -> {
                }
                OrderType.PENDING_RECEIPT -> {
                }
                OrderType.COMPLETED -> {
                }
            }
        }
//        mViewBind.recyclerView.isRefreshing = true
        orderListAdapter.cancelAllTimers()
        if (type == OrderType.SEARCH) {
            mViewModel.searchUserOrder(searchKeywords, pageNum)
        } else {
            mViewModel.orderList(type, pageNum)
        }
    }

    /**
     * 数据回显
     */
    private fun dataEcho() {
        mViewModel.orderListLive.observe(viewLifecycleOwner) { beans ->
            if (pageNum == 1) {
                if (beans.isEmpty()) {
                    bindEmptyView()
                } else {
                    mViewBind.recyclerView.removeEmptyView()
                    orderListAdapter.setNewDataIgnoreSize(beans)
                }
            } else {
                orderListAdapter.addMoreData(beans)
            }
        }

        mViewModel.searchUserOrderListLive.observe(viewLifecycleOwner) { beans ->
            if (pageNum == 1) {
                if (beans.isEmpty()) {
                    bindEmptyView()
                } else {
                    mViewBind.recyclerView.removeEmptyView()
                    orderListAdapter.setNewDataIgnoreSize(beans)
                }
            } else {
                orderListAdapter.addMoreData(beans)
            }

        }
    }

    //Event通知
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun handleEvent(event: UniversalEvent) {
        if (event.actionType == UniversalEvent.EVENT_ORDERLIST_REFRESH
        ) {
            pageNum = 1
            orderListAdapter.cancelAllTimers()
            if (!PublicPracticalMethodFromKT.ppmfKT.isBlank(event.message)) {
                val bean = event.message as OrderListRefreshBean
                if (bean.orderType == type && type == OrderType.SEARCH) {
                    searchKeywords = bean.searchKeywords
                    mViewModel.searchUserOrder(searchKeywords, pageNum)
                } else if (bean.orderType == type) {
                    mViewModel.orderList(type, pageNum)
                }
            } else {
                if (type == OrderType.SEARCH) {
                    mViewModel.searchUserOrder(searchKeywords, pageNum)
                } else {
                    mViewModel.orderList(type, pageNum)
                }
            }
        } else if (event.actionType == UniversalEvent.EVENT_DELIVERY_ADDRESS) {
            if (!PublicPracticalMethodFromKT.ppmfKT.isBlank(event.message)
                && type == OrderType.PENDING_DELIVERY && changeaddress_orderId != -1
            ) {
                val _deliveryAddressBean = event.message as DeliveryAddressBean
                orderDetailsViewModel.updateOrderAddressId(
                    _deliveryAddressBean.id,
                    changeaddress_orderId!!
                ) { isSuccess, msg ->
                    ToastUtil.showCenter(msg)
                }
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        orderListAdapter?.let {
            it.cancelAllTimers()
        }
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }

}