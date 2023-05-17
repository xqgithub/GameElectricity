package com.sn.gameelectricity.app.weight.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.text.Editable
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.FragmentManager
import com.blankj.utilcode.util.LogUtils
import com.sn.gameelectricity.R
import com.sn.gameelectricity.app.weight.prioritywindow.IWindow
import com.sn.gameelectricity.app.weight.prioritywindow.OnWindowDismissListener
import com.sn.gameelectricity.databinding.DialogAppUpdateBinding
import me.hgj.jetpackmvvm.ext.inflate
import me.hgj.jetpackmvvm.util.ScreenTools
import singleClick


/**
 * 发现新版本 Dialog
 */
class AppUpdateDialog @JvmOverloads constructor(
    var mContext: Context,
    var width: Int = ScreenTools.getInstance().dp2px(mContext, 285f, true),
    var height: Int = WindowManager.LayoutParams.WRAP_CONTENT,
    var themeResId: Int = R.style.TransparentDialog
) : Dialog(mContext, themeResId), IWindow {

    private val mBinding by inflate<DialogAppUpdateBinding>()
    private var onWindowDismissListener: OnWindowDismissListener? = null

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
     * 设置弹框内容
     */
    fun setTextContent(text: String) {
        mBinding.tvContent.text = Editable.Factory.getInstance().newEditable(text)
    }

    /**
     * 设置版本内容
     */
    fun setTextVersion(text: String) {
        mBinding.tvVersion.text = text
    }

    /**
     * 设置版本内容
     */
    fun setCancelVisibility(updateType: Int) {
        when (updateType) {
            1 -> mBinding.tvCancel.visibility = View.VISIBLE
            2 -> mBinding.tvCancel.visibility = View.GONE
        }
    }

    /**
     * 更新
     */
    fun onClickSure(onCallBack: () -> Unit) {
        LogUtils.e()
        mBinding.btnSure.singleClick {
            onCallBack()
            dismissDialog()
        }
    }

    /**
     * 点击取消按钮
     */
    fun onClickCancel() {
        mBinding.tvCancel.singleClick {
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
        return AppUpdateDialog::class.java.simpleName
    }

    /**
     * 弹窗展示
     */
    override fun show(activity: Activity?, manager: androidx.fragment.app.FragmentManager?) {
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