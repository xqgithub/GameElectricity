package com.sn.gameelectricity.app.weight.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.WindowManager
import com.sn.gameelectricity.R
import com.sn.gameelectricity.databinding.DialogAddWishBinding
import me.hgj.jetpackmvvm.ext.inflate
import me.hgj.jetpackmvvm.util.ScreenTools
import singleClick


/**
 * 添加心愿购 Dialog
 */
class AddWishDialog @JvmOverloads constructor(
    var mContext: Context,
    var width: Int = ScreenTools.getInstance().dp2px(mContext, 240f, true),
    var height: Int = WindowManager.LayoutParams.WRAP_CONTENT,
    var themeResId: Int = R.style.TransparentDialog
) : Dialog(mContext, themeResId) {
    private val mBinding by inflate<DialogAddWishBinding>()

    init {
        setContentView(mBinding.root)
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        onClickCancel()

        val spannableString = SpannableString("有机会通过扭蛋机抽奖获得此商品，确定要将此商品添加为心愿购吗？")
        spannableString.setSpan(
            ForegroundColorSpan(Color.parseColor("#F19B3F")),
            5,
            10,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        mBinding.tvContent.setText(spannableString)
    }

    override fun show() {
        super.show()
        /**
         * 设置宽度全屏，要设置在show的后面
         */
        val layoutParams = window!!.attributes
        layoutParams.width = width
        layoutParams.height = height
        window!!.decorView.setPadding(0, 0, 0, 0)
        window!!.attributes = layoutParams
    }



    /**
     * 设置弹框内容
     */
    fun setTextContent(text: String) {
        mBinding.tvContent.text = text
    }

    /**
     * 点击确定按钮
     */
    fun onClickSure(onCallBack: () -> Unit) {
        mBinding.btnSure.singleClick {
            onCallBack()
            dismissDialog()
        }
    }

    /**
     * 点击取消按钮
     */
    fun onClickCancel() {
        mBinding.btnCancel.singleClick {
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