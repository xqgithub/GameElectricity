package com.sn.gameelectricity.app.weight.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.text.SpannableString
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.sn.gameelectricity.R
import com.sn.gameelectricity.app.App
import com.sn.gameelectricity.databinding.DialogSelectionToolTipBinding
import me.hgj.jetpackmvvm.ext.inflate
import me.hgj.jetpackmvvm.util.PublicPracticalMethodFromKT
import me.hgj.jetpackmvvm.util.ScreenTools
import singleClick

/**
 * Date:2022/5/12
 * Time:19:44
 * author:dimple
 * 选择提示框
 */
class SelectionTooltipDialog @JvmOverloads constructor(
    var mContext: Context,
    var width: Int = ScreenTools.getInstance().dp2px(mContext, 240f, true),
    var height: Int = WindowManager.LayoutParams.WRAP_CONTENT,
    var themeResId: Int = R.style.TransparentDialog
) : Dialog(mContext, themeResId) {

    private val mBinding by inflate<DialogSelectionToolTipBinding>()


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
//        PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
//            mBinding.clContent, -1, "",
//            ScreenTools.getInstance().dp2px(mContext, 16f, true).toFloat(),
//            ScreenTools.getInstance().dp2px(mContext, 16f, true).toFloat(),
//            ScreenTools.getInstance().dp2px(mContext, 16f, true).toFloat(),
//            ScreenTools.getInstance().dp2px(mContext, 16f, true).toFloat(),
//            null, "#0DF19643"
//        )
//
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

    /**
     * 设置标题内容
     */
    fun settitle(
        isShow: Int = View.VISIBLE,
        content: String = "",
        textcolor: String = "#57493B",
        textsize: Float = 13f
    ) {
        mBinding.tvtitle.apply {
            visibility = isShow
            text = content
            setTextColor(Color.parseColor(textcolor))
            textSize = textsize
        }
    }


    /**
     * 设置弹框内容
     */
    fun setContent(content: String, textcolor: String = "#57493B", textsize: Float = 13f) {
        mBinding.tvContent.apply {
            text = content
            setTextColor(Color.parseColor(textcolor))
            textSize = textsize
        }
    }

    /**
     * 设置弹框内容高亮显示
     */
    fun setContentHighlight(
        content: SpannableString,
        textcolor: String = "#57493B",
        textsize: Float = 13f
    ) {
        mBinding.tvContent.apply {
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
     * 设置确定按钮的背景
     */
    fun setSureBg(
        cornerRadius: Float,
        orientation: GradientDrawable.Orientation? = null,
        vararg bgColor: String
    ) {
        PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
            mBinding.tvSure, -1, "",
            ScreenTools.getInstance().dp2px(mContext, cornerRadius, true).toFloat(),
            ScreenTools.getInstance().dp2px(mContext, cornerRadius, true).toFloat(),
            ScreenTools.getInstance().dp2px(mContext, cornerRadius, true).toFloat(),
            ScreenTools.getInstance().dp2px(mContext, cornerRadius, true).toFloat(),
            orientation, *bgColor
        )
    }


    /**
     * 设置图片信息
     */
    fun setRewardedIcon(
        imgId: Int,
        isShow: Boolean = false,
        width: Int = ScreenTools.getInstance().dp2px(App.instance, 92f, true),
        height: Int = ScreenTools.getInstance().dp2px(App.instance, 57f, true)
    ) {

        mBinding.sivIcon.apply {
            if (isShow) this.visibility = View.VISIBLE else this.visibility = View.GONE
            this.setImageDrawable(ContextCompat.getDrawable(mContext, imgId))
            val params = this.layoutParams
            params.height = height
            params.width = width
            this.layoutParams = params
        }
    }

    /**
     * 设置金币数量
     */
    fun setTvPrizeSnumber(
        content: String = "",
        isShow: Boolean = false,
        textcolor: String = "#EF874E",
        textsize: Float = 12f,
    ) {
        mBinding.tvPrizesNumber.apply {
            visibility = if (isShow) View.VISIBLE else View.GONE
            text = content
            setTextColor(Color.parseColor(textcolor))
            textSize = textsize

            PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
                mBinding.tvPrizesNumber, -1, "",
                ScreenTools.getInstance().dp2px(mContext, 24f, true).toFloat(),
                ScreenTools.getInstance().dp2px(mContext, 24f, true).toFloat(),
                ScreenTools.getInstance().dp2px(mContext, 24f, true).toFloat(),
                ScreenTools.getInstance().dp2px(mContext, 24f, true).toFloat(),
                null, "#FFF6F0"
            )

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