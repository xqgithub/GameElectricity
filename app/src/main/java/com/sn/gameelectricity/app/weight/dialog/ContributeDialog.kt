package com.sn.gameelectricity.app.weight.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.sn.gameelectricity.R
import com.sn.gameelectricity.app.weight.customview.CommentDialogDecoration
import com.sn.gameelectricity.app.weight.customview.ErrorPageView
import com.sn.gameelectricity.databinding.DialogContributeBinding
import com.sn.gameelectricity.ui.adapter.ContributeAdapter
import com.sn.gameelectricity.viewmodel.state.MoneyViewModel
import me.hgj.jetpackmvvm.ext.inflate
import me.hgj.jetpackmvvm.ext.removeEmptyView
import me.hgj.jetpackmvvm.util.PublicPracticalMethodFromKT
import me.hgj.jetpackmvvm.util.ScreenTools
import singleClick

/**
 * Date:2022/5/20
 * Time:9:34
 * author:dimple
 * 贡献明细弹框
 */
class ContributeDialog @JvmOverloads constructor(
    var mContext: Context,
    val moneyViewModel: MoneyViewModel,
    var width: Int = ScreenTools.getInstance().dp2px(mContext, 375f, true),
    var height: Int = WindowManager.LayoutParams.WRAP_CONTENT,
    var themeResId: Int = R.style.TransparentDialog
) : Dialog(mContext, themeResId) {

    private val mBinding by inflate<DialogContributeBinding>()

    private lateinit var contributeAdapter: ContributeAdapter


    init {
        setContentView(mBinding.root)
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        setBG()

        mBinding.ivBack.singleClick {
            dismissDialog()
        }
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
        window!!.setWindowAnimations(R.style.RightShowAnimation)
        window!!.decorView.setPadding(0, 0, 0, 0)
        window!!.attributes = layoutParams
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding.apply {
            contributeAdapter = ContributeAdapter(mContext)
            recyclerView.apply {
                layoutManager = LinearLayoutManager(mContext)
                adapter = contributeAdapter

                addItemDecoration(
                    CommentDialogDecoration().getInstance()
                        .setSpaceItemDecoration(
                            ScreenTools.getInstance().dp2px(mContext, 0f, true),
                            ScreenTools.getInstance().dp2px(mContext, 0f, true),
                            ScreenTools.getInstance().dp2px(mContext, 0f, true),
                            ScreenTools.getInstance().dp2px(mContext, 10f, true)
                        )
                )

                clearAnimation()
                setLoadingMoreBottomHeight(56f)

                val a = mutableListOf<String>()
                for (i in 0..14) {
                    a.add(i.toString())
                }
                contributeAdapter.setNewData(a)

            }
        }
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
     * 当列表数据为空的时候，显示
     */
    private fun bindEmptyView() {
        val errorPageView = ErrorPageView(mContext)
//        errorPageView.post {
//            val height = mBinding.recyclerView.height
//            errorPageView.changeErrorIconPositionToTOP((0.25 * height).toInt())
//        }
        mBinding.recyclerView.apply {
            setEmptyView(errorPageView)
//            setLoadingMoreBottomHeight(0f)
            isLoadMoreEnabled = false
            contributeAdapter.setNewData(arrayListOf())
        }
    }

    /**
     * 关闭对话框
     */
    fun closeDialog(onCallBack: () -> Unit) {
        mBinding.ivClose.singleClick {
            onCallBack()
            dismissDialog()
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