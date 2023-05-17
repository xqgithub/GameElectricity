package com.sn.gameelectricity.app.weight.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import com.sn.gameelectricity.R
import com.sn.gameelectricity.app.App
import com.sn.gameelectricity.databinding.DialogShareProductBinding
import me.hgj.jetpackmvvm.ext.inflate
import me.hgj.jetpackmvvm.util.PublicPracticalMethodFromKT
import me.hgj.jetpackmvvm.util.ScreenTools
import singleClick

class ShareProductDialog @JvmOverloads constructor(
    var mContext: Context,
    var width: Int = ScreenTools.getInstance().dp2px(mContext, 375f, true),
    var height: Int = WindowManager.LayoutParams.WRAP_CONTENT,
    var themeResId: Int = R.style.TransparentDialog
) : Dialog(mContext, themeResId) {

    private val mBinding by inflate<DialogShareProductBinding>()

    init {
        setContentView(mBinding.root)
        setCancelable(false)
        setCanceledOnTouchOutside(true)
        setBG()

        mBinding.ivClose.singleClick {
            dismissDialog()
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
        layoutParams.gravity = Gravity.BOTTOM
        window!!.setWindowAnimations(R.style.BottomShowAnimation)
        window!!.decorView.setPadding(0, 0, 0, 0)
        window!!.attributes = layoutParams
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    /**
     * 设置背景
     */
    private fun setBG() {
        PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
            mBinding.clDialog, -1, "",
            ScreenTools.getInstance().dp2px(mContext, 16f, true).toFloat(),
            ScreenTools.getInstance().dp2px(mContext, 16f, true).toFloat(),
            0f,
            0f,
            null, "#FFFFFF"
        )

        mBinding.apply {
            itDown.apply {
                setAvatarDataFromRes(
                    R.drawable.ic_share_wx,
                    ScreenTools.getInstance().dp2px(App.instance, 52f, true),
                    ScreenTools.getInstance().dp2px(App.instance, 52f, true)
                )
                setTextData("微信", 12f, "#233556")
            }

            itWx.apply {
                setAvatarDataFromRes(
                    R.drawable.ic_share_wx2,
                    ScreenTools.getInstance().dp2px(App.instance, 52f, true),
                    ScreenTools.getInstance().dp2px(App.instance, 52f, true)
                )
                setTextData("朋友圈", 12f, "#233556")
            }

            itQq.apply {
                setAvatarDataFromRes(
                    R.drawable.ic_share_qq,
                    ScreenTools.getInstance().dp2px(App.instance, 52f, true),
                    ScreenTools.getInstance().dp2px(App.instance, 52f, true)
                )
                setTextData("QQ", 12f, "#233556")
            }

            itWb.apply {
                setAvatarDataFromRes(
                    R.drawable.ic_share_wb,
                    ScreenTools.getInstance().dp2px(App.instance, 52f, true),
                    ScreenTools.getInstance().dp2px(App.instance, 52f, true)
                )
                setTextData("微博", 12f, "#233556")
            }
        }
    }

    fun onWxClick(onCallBack: () -> Unit) {
        mBinding.itDown.singleClick {
            onCallBack()
            dismissDialog()
        }
    }

    fun onPyqClick(onCallBack: () -> Unit) {
        mBinding.itWx.singleClick {
            onCallBack()
            dismissDialog()
        }
    }

    fun onQqClick(onCallBack: () -> Unit) {
        mBinding.itQq.singleClick {
            onCallBack()
            dismissDialog()
        }
    }

    fun onWbClick(onCallBack: () -> Unit) {
        mBinding.itWb.singleClick {
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