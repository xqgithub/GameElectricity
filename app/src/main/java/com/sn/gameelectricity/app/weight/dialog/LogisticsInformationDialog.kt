package com.sn.gameelectricity.app.weight.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.sn.gameelectricity.R
import com.sn.gameelectricity.app.App
import com.sn.gameelectricity.app.util.ClipboardUtil
import com.sn.gameelectricity.app.weight.customview.ErrorPageView
import com.sn.gameelectricity.databinding.DialogLogisticsInformationBinding
import com.sn.gameelectricity.ui.adapter.LogisticsInformationAdapter
import com.sn.gameelectricity.viewmodel.OrderDetailsViewModel
import me.hgj.jetpackmvvm.ext.addMoreData
import me.hgj.jetpackmvvm.ext.inflate
import me.hgj.jetpackmvvm.ext.removeEmptyView
import me.hgj.jetpackmvvm.util.PublicPracticalMethodFromKT
import me.hgj.jetpackmvvm.util.ScreenTools
import me.hgj.jetpackmvvm.util.ToastUtil
import me.hgj.jetpackmvvm.util.load
import singleClick

/**
 * Date:2022/6/1
 * Time:14:06
 * author:dimple
 *  物流信息 弹框
 */
class LogisticsInformationDialog @JvmOverloads constructor(
    var mContext: Context,
    val mViewModel: OrderDetailsViewModel,
    var width: Int = ScreenTools.getInstance().dp2px(mContext, 375f, true),
    var height: Int = WindowManager.LayoutParams.WRAP_CONTENT,
    var themeResId: Int = R.style.TransparentDialog
) : Dialog(mContext, themeResId) {
    private val mBinding by inflate<DialogLogisticsInformationBinding>()

    private lateinit var logisticsInformationAdapter: LogisticsInformationAdapter

    init {
        setContentView(mBinding.root)
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        setBG()
    }

    override fun show() {
        super.show()
        /**
         * 设置宽度全屏，要设置在show的后面
         */
        val layoutParams = window!!.attributes
        layoutParams.width = width
        layoutParams.height = height
        layoutParams.gravity = Gravity.BOTTOM
        window!!.setWindowAnimations(R.style.BottomShowAnimation)
        window!!.decorView.setPadding(0, 0, 0, 0)
        window!!.attributes = layoutParams
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
    }

    override fun onStart() {
        super.onStart()
    }

    /**
     * 设置背景
     */
    private fun setBG() {
        PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
            mBinding.clDialog, -1, "",
            ScreenTools.getInstance().dp2px(mContext, 16f, true).toFloat(),
            ScreenTools.getInstance().dp2px(mContext, 16f, true).toFloat(),
            0f,
            0f,
            null, "#FFFFFFFF"
        )
    }

    private fun initData() {
        initList()
        dataEcho()
    }

    /**
     * 设置快递单号数据
     */
    fun setDeliveryNoData(deliveryNo: String, orderNo: String) {
        mViewModel.deliveryByDeliveryNo(deliveryNo, orderNo)
    }

    fun onClickCopyButton() {
    }


    /**
     * 初始化地区列表
     */
    private fun initList() {
        mBinding.apply {
            logisticsInformationAdapter = LogisticsInformationAdapter(context)
            recyclerView.apply {
                layoutManager = LinearLayoutManager(mContext)
                adapter = logisticsInformationAdapter

                clearAnimation()
                setLoadingMoreBottomHeight(36f)

                isRefreshEnabled = false
                isLoadMoreEnabled = false
            }
        }
    }


    /**
     * 数据回显
     */
    private fun dataEcho() {
        mBinding.apply {
            mViewModel.apply {
                deliveryByDeliveryNoLive.observe((mContext as AppCompatActivity)) { bean ->
                    sivAvatar.load(
                        bean.deliveryCompanyIcon,
                        R.drawable.ic_load_error,
                        R.drawable.ic_load_error
                    )
                    tvCourierName.text = bean.deliveryCompanyName
                    tvTrackingNumber.text = bean.deliveryNo
                    if (bean.traceVOList.isEmpty()) {
                        bindEmptyView()
                    } else {
                        recyclerView.removeEmptyView()
                        logisticsInformationAdapter.setNewDataIgnoreSize(
                            mViewModel.transformLogisticsInformationData(
                                bean.traceVOList
                            )
                        )

                        mBinding.tvCopy.singleClick {
                            ClipboardUtil.copyText(
                                mContext,
                                bean.deliveryNo
                            )
                            ToastUtil.showCenter("复制成功")
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
        val errorPageView = ErrorPageView(mContext)
        with(errorPageView) {
            post {
                val recyclerView_height = mBinding.recyclerView.height
                setErrorIcon(
                    R.drawable.ge_error,
                    ScreenTools.getInstance().dp2px(App.instance, 124f, true),
                    ScreenTools.getInstance().dp2px(App.instance, 124f, true)
                )
                setErrorContent("哦豁，一片荒芜～", 12f, "#A1A7AF")
                changeErrorIconPositionToTOP(
                    (0.2 * recyclerView_height).toInt()
                )
                changeErrorTextPositionToTOP(
                    ScreenTools.getInstance().dp2px(App.instance, 5f, true)
                )
            }
        }

        mBinding.recyclerView.apply {
            setEmptyView(errorPageView)
//            setLoadingMoreBottomHeight(0f)
            isLoadMoreEnabled = false
            logisticsInformationAdapter.setNewData(arrayListOf())
        }
    }


    /**
     * 关闭对话框并且销毁
     */
    fun closeDialog(onCallBack: () -> Unit) {
        mBinding.ivClose.singleClick {
            dismissDialog()
            onCallBack()
        }
    }


    /**
     * 关闭弹框
     */
    fun dismissDialog() {
        if (isShowing) {
            dismiss()
        }
    }


}