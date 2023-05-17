package com.sn.gameelectricity.app.weight.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import com.sn.gameelectricity.R
import com.sn.gameelectricity.app.util.load
import com.sn.gameelectricity.data.model.bean.NewbiePoliteBean
import com.sn.gameelectricity.databinding.DialogRewardCoinsBinding
import com.sn.gameelectricity.databinding.DialogRewardCommodityBinding
import me.hgj.jetpackmvvm.ext.inflate
import me.hgj.jetpackmvvm.util.PublicPracticalMethodFromKT
import me.hgj.jetpackmvvm.util.ScreenTools
import singleClick

/**
 * Date:2022/5/18
 * Time:17:36
 * author:dimple
 *
 */
class RewardCommodityDialog @JvmOverloads constructor(
    var mContext: Context,
    var width: Int = ScreenTools.getInstance().dp2px(mContext, 260f, true),
    var height: Int = WindowManager.LayoutParams.WRAP_CONTENT,
    var themeResId: Int = R.style.TransparentDialog
) : Dialog(mContext, themeResId) {

    private val mBinding by inflate<DialogRewardCommodityBinding>()

    init {
        setContentView(mBinding.root)
        setCancelable(false)
        setCanceledOnTouchOutside(true)
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
        layoutParams.gravity = Gravity.CENTER
//        window!!.setWindowAnimations(R.style.BottomShowAnimation)
        window!!.decorView.setPadding(0, 0, 0, 0)
        window!!.attributes = layoutParams
    }

    /**
     * 设置背景
     */
    private fun setBG() {
        PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
            mBinding.tvCancel, -1, "",
            ScreenTools.getInstance().dp2px(mContext, 24f, true).toFloat(),
            ScreenTools.getInstance().dp2px(mContext, 24f, true).toFloat(),
            ScreenTools.getInstance().dp2px(mContext, 24f, true).toFloat(),
            ScreenTools.getInstance().dp2px(mContext, 24f, true).toFloat(),
            null, "#F7F9FE"
        )

        PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
            mBinding.tvSure, -1, "",
            ScreenTools.getInstance().dp2px(mContext, 24f, true).toFloat(),
            ScreenTools.getInstance().dp2px(mContext, 24f, true).toFloat(),
            ScreenTools.getInstance().dp2px(mContext, 24f, true).toFloat(),
            ScreenTools.getInstance().dp2px(mContext, 24f, true).toFloat(),
            GradientDrawable.Orientation.LEFT_RIGHT, "#F19B3F", "#ED7A57"
        )

    }

    fun setIntroduceContent(content: String, textcolor: String = "#57493B", textsize: Float = 16f) {
        mBinding.tvIntroduce.apply {
            text = content
            setTextColor(Color.parseColor(textcolor))
            textSize = textsize
        }
    }

    /**
     * 设置取消按钮
     */
    fun setCancel(
        isShow: Int = View.VISIBLE,
        content: String = "取消",
        textcolor: String = "#A1A7AF",
        textsize: Float = 14f,
        onClick: () -> Unit
    ) {
        mBinding.tvCancel.apply {
            visibility = isShow
            text = content
            setTextColor(Color.parseColor(textcolor))
            textSize = textsize
            singleClick {
                onClick()
            }
        }
    }

    /**
     * 设置确定按钮
     */
    fun setSure(
        isShow: Int = View.VISIBLE,
        content: String = "确定",
        textcolor: String = "#FFFFFF",
        textsize: Float = 14f,
        onClick: () -> Unit
    ) {
        mBinding.tvSure.apply {
            visibility = isShow
            text = content
            setTextColor(Color.parseColor(textcolor))
            textSize = textsize
            singleClick {
                onClick()
            }
        }
    }

    /**
     * 设置商品数据
     */
    fun setProductData(newbiePoliteBean: NewbiePoliteBean) {
        mBinding.ivIcon.load(newbiePoliteBean.exchangeGoodsIcon)
        setIntroduceContent(newbiePoliteBean.exchangeGoodsName)
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