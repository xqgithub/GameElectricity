package com.sn.gameelectricity.app.weight.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.WindowManager
import androidx.fragment.app.FragmentManager
import com.sn.gameelectricity.R
import com.sn.gameelectricity.app.weight.prioritywindow.IWindow
import com.sn.gameelectricity.app.weight.prioritywindow.OnWindowDismissListener
import com.sn.gameelectricity.databinding.DialogCouplePoliteBinding
import me.hgj.jetpackmvvm.ext.inflate
import me.hgj.jetpackmvvm.util.ScreenTools
import singleClick


/**
 * 新人有礼 Dialog
 */
class CouplePoliteDialog @JvmOverloads constructor(
    var mContext: Context,
    var width: Int = ScreenTools.getInstance().dp2px(mContext, 240f, true),
    var height: Int = WindowManager.LayoutParams.WRAP_CONTENT,
    var themeResId: Int = R.style.TransparentDialog
) : Dialog(mContext, themeResId), IWindow {

    private val mBinding by inflate<DialogCouplePoliteBinding>()
    private var onWindowDismissListener: OnWindowDismissListener? = null

    init {
        setContentView(mBinding.root)
        setCancelable(false)
        setCanceledOnTouchOutside(false)
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
     * 立即兑换
     */
    fun onClickSure(onCallBack: () -> Unit) {
        mBinding.btnSure.singleClick {
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

    override fun getClassName(): String {
        return CouplePoliteDialog::class.java.simpleName
    }

    /**
     * 弹窗展示
     */
    override fun show(activity: Activity?, manager: FragmentManager?) {
        if (activity == null || activity.isFinishing || activity.isDestroyed) {
            return
        }
        show()
    }

    /**
     * 设置窗口关闭监听
     */
    override fun setOnWindowDismissListener(listener: OnWindowDismissListener?) {
        onWindowDismissListener = listener
        setOnDismissListener { onWindowDismissListener?.onDismiss() }
    }
}