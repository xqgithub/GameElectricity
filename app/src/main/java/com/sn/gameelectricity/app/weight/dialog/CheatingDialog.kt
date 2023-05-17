package com.sn.gameelectricity.app.weight.dialog

import android.app.Dialog
import android.content.Context
import android.view.WindowManager
import com.sn.gameelectricity.R
import kotlinx.android.synthetic.main.dialog_bottom_comfirm_order.*
import me.hgj.jetpackmvvm.util.ScreenTools
import singleClick

/**
 * 邀请助力 Dialog
 */
class CheatingDialog @JvmOverloads constructor(
    var mContext: Context,
    var width: Int = ScreenTools.getInstance().dp2px(mContext, 375f, true),
    var height: Int = WindowManager.LayoutParams.MATCH_PARENT,
    var themeResId: Int = R.style.BottomDialogStyle
) : Dialog(mContext, themeResId) {

    init {
        setContentView(R.layout.dialog_bottom_comfirm_order)
        setCancelable(true)
        setCanceledOnTouchOutside(true)
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

//
//    /**
//     * 设置提示内容
//     */
//    fun setTitleContent(text: String) {
//        tvtitle.text = text
//    }
//
//    /**
//     * 设置弹框内容
//     */
//    fun setTextContent(text: String) {
//        tvContent.text = text
//    }
//
//    /**
//     * 点击确定按钮
//     */
//    fun onClickSure(onCallBack: () -> Unit) {
//        btnSure.singleClick {
//            onCallBack()
//            dismissDialog()
//        }
//    }
//
    /**
     * 点击取消按钮
     */
    fun onClickCancel() {
        imgClose.singleClick {
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