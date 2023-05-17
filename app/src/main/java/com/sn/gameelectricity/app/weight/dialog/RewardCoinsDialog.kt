package com.sn.gameelectricity.app.weight.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import com.sn.gameelectricity.R
import com.sn.gameelectricity.databinding.DialogRewardCoinsBinding
import me.hgj.jetpackmvvm.ext.inflate
import me.hgj.jetpackmvvm.util.PublicPracticalMethodFromKT
import me.hgj.jetpackmvvm.util.ScreenTools
import singleClick

/**
 * Date:2022/5/18
 * Time:17:36
 * author:dimple
 * 收获金币弹框
 */
class RewardCoinsDialog @JvmOverloads constructor(
    var mContext: Context,
    var width: Int = ScreenTools.getInstance().dp2px(mContext, 260f, true),
    var height: Int = WindowManager.LayoutParams.WRAP_CONTENT,
    var themeResId: Int = R.style.TransparentDialog
) : Dialog(mContext, themeResId) {

    private val mBinding by inflate<DialogRewardCoinsBinding>()

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
            mBinding.tvSure, -1, "",
            ScreenTools.getInstance().dp2px(mContext, 24f, true).toFloat(),
            ScreenTools.getInstance().dp2px(mContext, 24f, true).toFloat(),
            ScreenTools.getInstance().dp2px(mContext, 24f, true).toFloat(),
            ScreenTools.getInstance().dp2px(mContext, 24f, true).toFloat(),
            GradientDrawable.Orientation.LEFT_RIGHT, "#F19B3F", "#ED7A57"
        )
    }

    /**
     * 设置金币内容
     */
    fun setCoinsContent(content: String, textcolor: String = "#57493B", textsize: Float = 14f) {
        mBinding.tvMyGoldNum.apply {
            text = content
            setTextColor(Color.parseColor(textcolor))
            textSize = textsize
        }
    }


    /**
     * 设置弹框内容
     */
    fun setContent(content: String, textcolor: String = "#57493B", textsize: Float = 16f) {
        mBinding.tvContent.apply {
            text = content
            setTextColor(Color.parseColor(textcolor))
            textSize = textsize
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
     * 关闭弹框
     */
    fun dismissDialog() {
        if (isShowing) {
            dismiss()
        }
    }

}