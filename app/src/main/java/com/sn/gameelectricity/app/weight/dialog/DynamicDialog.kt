package com.sn.gameelectricity.app.weight.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.sn.gameelectricity.R
import com.sn.gameelectricity.app.App
import com.sn.gameelectricity.app.weight.customview.ErrorPageView
import com.sn.gameelectricity.databinding.DialogDynamicBinding
import com.sn.gameelectricity.ui.adapter.DynamicAdapter
import com.sn.gameelectricity.viewmodel.state.MoneyViewModel
import me.hgj.jetpackmvvm.ext.addMoreData
import me.hgj.jetpackmvvm.ext.inflate
import me.hgj.jetpackmvvm.ext.removeEmptyView
import me.hgj.jetpackmvvm.util.PublicPracticalMethodFromKT
import me.hgj.jetpackmvvm.util.ScreenTools
import singleClick

/**
 * Date:2022/5/24
 * Time:9:34
 * author:dimple
 * 动态弹框
 */
class DynamicDialog @JvmOverloads constructor(
    var mContext: Context,
    var moneyViewModel: MoneyViewModel,
    var width: Int = ScreenTools.getInstance().dp2px(mContext, 375f, true),
    var height: Int = WindowManager.LayoutParams.WRAP_CONTENT,
    var themeResId: Int = R.style.TransparentDialog
) : Dialog(mContext, themeResId) {

    private val mBinding by inflate<DialogDynamicBinding>()

    private lateinit var dynamicAdapter: DynamicAdapter

    private var pageNum: Int = 1


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
        dataEcho()
        mBinding.apply {
            dynamicAdapter = DynamicAdapter(mContext)
            recyclerView.apply {
                layoutManager = LinearLayoutManager(mContext)
                adapter = dynamicAdapter


                isRefreshEnabled = false
                isLoadMoreEnabled = true
                setOnLoadMoreListener {
                    pageNum++
                    moneyViewModel.eventList(pageNum)
                }
                
                clearAnimation()
                setLoadingMoreBottomHeight(56f)
            }
        }
        moneyViewModel.eventList(pageNum)
    }

    override fun onStart() {
        super.onStart()
    }


    /**
     * 设置背景
     */
    private fun setBG() {
        PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
            mBinding.clBg, -1, "",
            ScreenTools.getInstance().dp2px(mContext, 16f, true).toFloat(),
            ScreenTools.getInstance().dp2px(mContext, 16f, true).toFloat(),
            0f,
            0f,
            GradientDrawable.Orientation.LEFT_RIGHT, "#F19B3F", "#ED7A57"
        )

        PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
            mBinding.clContent, -1, "",
            ScreenTools.getInstance().dp2px(mContext, 24f, true).toFloat(),
            ScreenTools.getInstance().dp2px(mContext, 24f, true).toFloat(),
            0f,
            0f,
            null, "#FFF6F0"
        )

    }


    /**
     * 数据回显
     */
    private fun dataEcho() {
        moneyViewModel.apply {
            eventListLive.observe((mContext as AppCompatActivity)) { beans ->
                if (pageNum == 1) {
                    if (beans.isEmpty()) {
                        bindEmptyView()
                    } else {
                        mBinding.recyclerView.removeEmptyView()
                        val a = moneyViewModel.transformDynamicData(beans)
                        dynamicAdapter.setNewData(beans)
                    }
                } else {
                    dynamicAdapter.addMoreData(beans)
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
                    R.drawable.ge_error2,
                    ScreenTools.getInstance().dp2px(App.instance, 124f, true),
                    ScreenTools.getInstance().dp2px(App.instance, 124f, true)
                )
                setErrorContent("哦豁，一片荒芜～", 12f, "#57493B")
                changeErrorIconPositionToTOP(
                    (0.3 * recyclerView_height).toInt()
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
            dynamicAdapter.setNewData(arrayListOf())
        }
    }


    fun setonClickIvClose(onCallBack: () -> Unit) {
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