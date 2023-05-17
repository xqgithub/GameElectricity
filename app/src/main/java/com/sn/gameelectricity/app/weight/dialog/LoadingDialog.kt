package com.sn.gameelectricity.app.weight.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.sn.gameelectricity.R
import com.sn.gameelectricity.app.App
import com.sn.gameelectricity.app.ext.showLoadingExt
import com.sn.gameelectricity.app.util.SettingUtil
import com.sn.gameelectricity.app.util.pag.PagAnimationTools
import com.sn.gameelectricity.databinding.DialogLoadingBinding
import me.hgj.jetpackmvvm.ext.inflate
import me.hgj.jetpackmvvm.util.PublicPracticalMethodFromKT
import me.hgj.jetpackmvvm.util.ScreenTools
import org.libpag.PAGView

/**
 * Date:2022/5/26
 * Time:17:03
 * author:dimple
 * loading 弹框
 */
class LoadingDialog @JvmOverloads constructor(
    var mContext: Context,
    var width: Int = WindowManager.LayoutParams.WRAP_CONTENT,
    var height: Int = WindowManager.LayoutParams.WRAP_CONTENT,
    var themeResId: Int = R.style.CustomDialog
) : Dialog(mContext, themeResId) {

    private val mBinding by inflate<DialogLoadingBinding>()

    //弹框种类 1 动画 2普通弹框 3LoadingIndicatorView
    private var dialog_type = 3

    private var pag_loading: PAGView? = null

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
        layoutParams.gravity = Gravity.CENTER
//        window!!.setWindowAnimations(R.style.BottomShowAnimation)
        window!!.decorView.setPadding(0, 0, 0, 0)
        window!!.attributes = layoutParams
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        mBinding.progressBar.run {
//            this.indeterminateTintList = SettingUtil.getOneColorStateList(mContext)
//        }
    }

    override fun onStart() {
        super.onStart()
        if (dialog_type == 1) {
            mBinding.rlPagLoading.visibility = View.VISIBLE
            mBinding.clLoading.visibility = View.GONE
            mBinding.aviLoading.visibility = View.GONE
            strartLoadingAnimation()
        } else if (dialog_type == 2) {
            mBinding.clLoading.visibility = View.VISIBLE
            mBinding.rlPagLoading.visibility = View.GONE
            mBinding.aviLoading.visibility = View.GONE
        } else if (dialog_type == 3) {
            mBinding.aviLoading.visibility = View.VISIBLE
            mBinding.rlPagLoading.visibility = View.GONE
            mBinding.clLoading.visibility = View.GONE
            mBinding.aviLoading.show()
        }
    }

    /**
     * 设置弹框类型
     */
    fun setDialogType(dialog_type: Int) {
        this.dialog_type = dialog_type
    }

    /**
     * 设置弹框的背景色
     */
    fun setDialogBg(color: String = "#00000000") {
        mBinding.clDialog.setBackgroundColor(Color.parseColor(color))
    }

    /**
     * 设置弹框loading的icon
     */
    fun setIcon(
        imgId: Int = R.drawable.ge_loading_chong,
        width: Int = ScreenTools.getInstance().dp2px(App.instance, 88f, true),
        height: Int = ScreenTools.getInstance().dp2px(App.instance, 88f, true)
    ) {
        mBinding.ivIcon.setImageDrawable(ContextCompat.getDrawable(mContext, imgId))
        val params = mBinding.ivIcon.layoutParams
        params.height = height
        params.width = width
        mBinding.ivIcon.layoutParams = params
    }

    /**
     * 设置弹框loading文字内容
     */
    fun setContent(
        content: String = "",
        isShow: Boolean = true,
        _textSize: Float = 13f,
        _textColor: String = "#57493B"
    ) {
        mBinding.tvContent.apply {
            if (isShow) this.visibility = View.VISIBLE else this.visibility = View.GONE
            text = content
            textSize = _textSize
            setTextColor(Color.parseColor(_textColor))
        }
    }

    /**
     * 开始动画
     */
    private fun strartLoadingAnimation() {
        if (PublicPracticalMethodFromKT.ppmfKT.isBlank(pag_loading)) {
            pag_loading = PagAnimationTools.pagTools.getPagView(
                "chong.pag",
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            mBinding.rlPagLoading.addView(pag_loading)
        }
        pag_loading?.let {
            it.setRepeatCount(0)
            if (!it.isPlaying) it.play()
        }
    }

    /**
     * 停止动画
     */
    private fun stopLoadingAnimation() {
        pag_loading?.let {
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
            stopLoadingAnimation()
            mBinding.aviLoading.hide()
        }
    }


}