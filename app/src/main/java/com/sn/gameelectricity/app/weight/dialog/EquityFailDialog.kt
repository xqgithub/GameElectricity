package com.sn.gameelectricity.app.weight.dialog

import android.app.Dialog
import android.content.Context
import android.view.WindowManager
import com.sn.gameelectricity.R
import com.sn.gameelectricity.databinding.DialogEquityFailBinding
import me.hgj.jetpackmvvm.ext.inflate
import me.hgj.jetpackmvvm.util.ScreenTools
import singleClick


/**
 * 恭喜您冲成功啦，
快去抽奖吧！ Dialog
 */
class EquityFailDialog @JvmOverloads constructor(
    var mContext: Context,
    var width: Int = ScreenTools.getInstance().dp2px(mContext, 240f, true),
    var height: Int = WindowManager.LayoutParams.WRAP_CONTENT,
    var themeResId: Int = R.style.TransparentDialog
) : Dialog(mContext, themeResId) {

    private val mBinding by inflate<DialogEquityFailBinding>()

    init {
        setContentView(mBinding.root)
        setCancelable(true)
        setCanceledOnTouchOutside(true)
        onClickSure()
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
     * 设置内容
     */
    fun setContent(text: String) {
        mBinding.tvContent.text = text
    }

    /**
     * 点击确定按钮
     */
    fun onClickSure() {
        mBinding.btnCancel.singleClick {
            dismissDialog()
        }
    }

    /**
     * 点击获取奖券按钮
     */
    fun onClickTickets(onCallBack: () -> Unit) {
        mBinding.btnSure.singleClick {
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