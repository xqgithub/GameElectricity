package com.sn.gameelectricity.app.weight.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.WindowManager
import androidx.fragment.app.FragmentManager
import com.sn.gameelectricity.R
import com.sn.gameelectricity.app.util.load
import com.sn.gameelectricity.app.util.loadUserAvatar
import com.sn.gameelectricity.app.weight.prioritywindow.IWindow
import com.sn.gameelectricity.app.weight.prioritywindow.OnWindowDismissListener
import com.sn.gameelectricity.databinding.DialogCheatingOtherBinding
import me.hgj.jetpackmvvm.ext.inflate
import me.hgj.jetpackmvvm.util.ScreenTools
import singleClick


/**
 * 帮Ta助力 Dialog
 */
class CheatingOtherDialog @JvmOverloads constructor(
    var mContext: Context,
    var width: Int = ScreenTools.getInstance().dp2px(mContext, 260f, true),
    var height: Int = WindowManager.LayoutParams.WRAP_CONTENT,
    var themeResId: Int = R.style.TransparentDialog
) : Dialog(mContext, themeResId), IWindow {

    private val mBinding by inflate<DialogCheatingOtherBinding>()
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
     * 设置弹框内容
     */
    fun loadImg(text: String) {
        mBinding.sivImg.load(text)
    }

    /**
     * 设置弹框内容
     */
    fun loadImgUserAvatar(text: String) {
        mBinding.ivAvatar.loadUserAvatar(text)
    }

    /**
     * 设置弹框内容
     */
    fun setTextContent(text: String) {
        mBinding.tvContent.text = text + "的助力邀请"
    }

    /**
     * 设置弹框内容
     */
    fun setTextContentTwo(text: String) {
        mBinding.tvContentTwo.text = text
    }

    /**
     * 设置弹框内容
     */
    fun setTextContentThree(text: String) {
        mBinding.tvContentThree.text = text
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
    fun onClickCancel(onCallBack: () -> Unit) {
        mBinding.ivClose.singleClick {
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
        return CheatingOtherDialog::class.java.simpleName
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