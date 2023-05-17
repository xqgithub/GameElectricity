package com.sn.gameelectricity.app.weight.dialog

import android.app.Dialog
import android.content.Context
import android.view.WindowManager
import com.sn.gameelectricity.R
import com.sn.gameelectricity.databinding.DialogAddWishAwardBinding
import kotlinx.android.synthetic.main.dialog_add_wish.*
import me.hgj.jetpackmvvm.ext.inflate
import me.hgj.jetpackmvvm.util.ScreenTools
import singleClick

/**
 * 添加心愿购-去抽奖 Dialog
 */
class AddWishAwardDialog @JvmOverloads constructor(
    var mContext: Context,
    var width: Int = ScreenTools.getInstance().dp2px(mContext, 240f, true),
    var height: Int = WindowManager.LayoutParams.WRAP_CONTENT,
    var themeResId: Int = R.style.TransparentDialog
) : Dialog(mContext, themeResId) {
    private val mBinding by inflate<DialogAddWishAwardBinding>()

    init {
        setContentView(mBinding.root)
        setCancelable(false)
        setCanceledOnTouchOutside(true)
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
        tvContent.text = text
    }

    /**
     * 点击确定按钮
     */
    fun onClickSure(onCallBack: () -> Unit) {
        btnSure.singleClick {
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