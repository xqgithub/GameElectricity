package com.sn.gameelectricity.app.weight.dialog

import android.app.Dialog
import android.content.Context
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.RelativeLayout
import com.sn.gameelectricity.R
import com.sn.gameelectricity.app.App
import com.sn.gameelectricity.app.util.load
import com.sn.gameelectricity.databinding.DialogGainGoldBinding
import me.hgj.jetpackmvvm.ext.inflate
import me.hgj.jetpackmvvm.util.ScreenTools
import org.libpag.PAGFile
import org.libpag.PAGView
import singleClick


/**
 * 金币 x20 Dialog
 */
class GainGoldDialog @JvmOverloads constructor(
    var mContext: Context,
    var width: Int = ScreenTools.getInstance().dp2px(mContext, 260f, true),
    var height: Int = WindowManager.LayoutParams.WRAP_CONTENT,
    var themeResId: Int = R.style.TransparentDialog
) : Dialog(mContext, themeResId) {

    private val mBinding by inflate<DialogGainGoldBinding>()
    private var pagAction: PAGView? = null

    init {
        setContentView(mBinding.root)
        setCancelable(false)
        setCanceledOnTouchOutside(true)
        onClickSure()
        initActionPag()
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

    private fun initActionPag() {
        pagAction = PAGView(App.instance)
        pagAction?.layoutParams = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        mBinding.rlHomeHandClick.addView(pagAction)
        pagAction?.composition = PAGFile.Load(App.instance.assets, "glow.pag")
        pagAction?.let {
            it.setRepeatCount(0)
            if (!it.isPlaying) {
                it.play()
            }
        }
    }

    fun loadImg(text: String) {
        mBinding.sivImg.load(text)
    }

    fun setTextContent(text: String) {
        mBinding.tvContent.text = text
    }

    /**
     * 点击确定按钮
     */
    fun onClickSure() {
        mBinding.btnSure.singleClick {
            dismissDialog()
        }
    }

    fun stopPagAction() {
        pagAction?.let {
            if (it.isPlaying) {
                it.stop()
                it.freeCache()
            }
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