package com.sn.gameelectricity.app.weight.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.WindowManager
import com.sn.gameelectricity.R
import com.sn.gameelectricity.databinding.DialogEquitySuccBinding
import me.hgj.jetpackmvvm.ext.inflate
import me.hgj.jetpackmvvm.util.ScreenTools
import singleClick


/**
 * 兑换成功 Dialog
 */
class EquitySuccDialog @JvmOverloads constructor(
    var mContext: Context,
    var width: Int = ScreenTools.getInstance().dp2px(mContext, 240f, true),
    var height: Int = WindowManager.LayoutParams.WRAP_CONTENT,
    var themeResId: Int = R.style.TransparentDialog
) : Dialog(mContext, themeResId) {

    private val mBinding by inflate<DialogEquitySuccBinding>()

    init {
        setContentView(mBinding.root)
        setCancelable(true)
        setCanceledOnTouchOutside(true)

        val spannableString = SpannableString("已为您保存至权益中心")
        spannableString.setSpan(
            ForegroundColorSpan(Color.parseColor("#F19B3F")),
            6,
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
     * 点击确定按钮
     */
    fun onClickSure(onCallBack: () -> Unit) {
        mBinding.btnSure.singleClick {
            onCallBack()
            dismissDialog()
        }
    }

    /**
     * 点击查看按钮
     */
    fun onClickSee(onCallBack: () -> Unit) {
        mBinding.btnSee.singleClick {
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