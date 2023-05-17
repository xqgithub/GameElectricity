package com.sn.gameelectricity.app.weight.dialog

import android.app.Dialog
import android.content.Context
import android.view.WindowManager
import com.sn.gameelectricity.R
import com.sn.gameelectricity.databinding.DialogBerewardFailBinding
import com.sn.gameelectricity.databinding.DialogCheatingSuccBinding
import me.hgj.jetpackmvvm.ext.inflate
import me.hgj.jetpackmvvm.util.ScreenTools
import singleClick


/**
 * 助力成功 Dialog
 */
class CheatingSuccDialog @JvmOverloads constructor(
    var mContext: Context,
    var width: Int = ScreenTools.getInstance().dp2px(mContext, 240f, true),
    var height: Int = WindowManager.LayoutParams.WRAP_CONTENT,
    var themeResId: Int = R.style.TransparentDialog
) : Dialog(mContext, themeResId) {

    private val mBinding by inflate<DialogCheatingSuccBinding>()

    init {
        setContentView(mBinding.root)
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        onClickCancel()
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
     * 点击取消按钮
     */
    fun onClickCancel() {
        mBinding.ivClose.singleClick {
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