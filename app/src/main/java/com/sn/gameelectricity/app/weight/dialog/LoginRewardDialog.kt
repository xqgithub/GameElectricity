package com.sn.gameelectricity.app.weight.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Handler
import android.view.WindowManager
import androidx.fragment.app.FragmentManager
import com.sn.gameelectricity.R
import com.sn.gameelectricity.app.weight.prioritywindow.IWindow
import com.sn.gameelectricity.app.weight.prioritywindow.OnWindowDismissListener
import com.sn.gameelectricity.databinding.DialogLoginRewardBinding
import me.hgj.jetpackmvvm.ext.inflate
import me.hgj.jetpackmvvm.util.ScreenTools
import singleClick


/**
 * 登录奖励 Dialog
 */
class LoginRewardDialog @JvmOverloads constructor(
    var mContext: Context,
    var width: Int = ScreenTools.getInstance().dp2px(mContext, 240f, true),
    var height: Int = WindowManager.LayoutParams.WRAP_CONTENT,
    var themeResId: Int = R.style.TransparentDialog
) : Dialog(mContext, themeResId), IWindow {

    private var onWindowDismissListener: OnWindowDismissListener? = null
    private val mBinding by inflate<DialogLoginRewardBinding>()
    private val handler = Handler()

    init {
        setContentView(mBinding.root)
        setCancelable(true)
        setCanceledOnTouchOutside(true)
        onClickCancel()
        handler?.let {
            handler.postDelayed({ dismissDialog() }, 5000)
        }
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

    fun setTextContent(text: String) {
        mBinding.tvConten.text = text
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

    fun onClickCancel() {
        mBinding.btnSure.singleClick {
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
        return LoginRewardDialog::class.java.simpleName
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