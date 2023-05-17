package com.sn.gameelectricity.app.weight.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.FragmentManager
import com.sn.gameelectricity.R
import com.sn.gameelectricity.app.util.CacheUtil
import com.sn.gameelectricity.app.util.load
import com.sn.gameelectricity.app.util.pag.PagAnimationTools
import com.sn.gameelectricity.app.weight.prioritywindow.IWindow
import com.sn.gameelectricity.app.weight.prioritywindow.OnWindowDismissListener
import com.sn.gameelectricity.data.model.bean.NewbiePoliteBean
import com.sn.gameelectricity.databinding.DialogNewbiePoliteBinding
import me.hgj.jetpackmvvm.ext.inflate
import me.hgj.jetpackmvvm.util.PublicPracticalMethodFromKT
import me.hgj.jetpackmvvm.util.ScreenTools
import org.libpag.PAGView
import singleClick

/**
 * Date:2022/5/18
 * Time:17:36
 * author:dimple
 * 新手引导
 */
class NewbiePoliteDialog @JvmOverloads constructor(
    var mContext: Context,
    var width: Int = ScreenTools.getInstance().dp2px(mContext, 240f, true),
    var height: Int = WindowManager.LayoutParams.WRAP_CONTENT,
    var themeResId: Int = R.style.TransparentDialog2
) : Dialog(mContext, themeResId), IWindow {

    private val mBinding by inflate<DialogNewbiePoliteBinding>()
    private var onWindowDismissListener: OnWindowDismissListener? = null
    private var pagview_finger: PAGView? = null

    init {
        setContentView(mBinding.root)
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        setBG()
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
        pagview_finger = PagAnimationTools.pagTools.getPagView(
            "hand.pag",
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        mBinding.rlPagFinger.addView(pagview_finger)
        
//        onClickClose()
    }

    override fun onStart() {
        super.onStart()
    }


    /**
     * 设置背景
     */
    private fun setBG() {
        PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
            mBinding.clGold, -1, "",
            ScreenTools.getInstance().dp2px(mContext, 12f, true).toFloat(),
            ScreenTools.getInstance().dp2px(mContext, 12f, true).toFloat(),
            ScreenTools.getInstance().dp2px(mContext, 12f, true).toFloat(),
            ScreenTools.getInstance().dp2px(mContext, 12f, true).toFloat(),
            null, "#FDEDE4"
        )
    }

    /**
     * 设置数据
     */
    fun setData(newbiePoliteBean: NewbiePoliteBean) {
        newbiePoliteBean?.let { bean ->
            mBinding.apply {
                sivCommodity.load(bean.exchangeGoodsIcon)
                tvGold.text = tvGoldHighlight("${CacheUtil.getUser()?.goldCoin} / ${bean.payNum}")
            }
        }
    }


    /**
     * 设置关闭按钮是否显示
     */
    fun setIvCloseWhetherToDisplay(mVisible: Boolean = true) {
        if (mVisible) mBinding.ivClose.visibility =
            View.VISIBLE else mBinding.ivClose.visibility = View.INVISIBLE
    }

    /**
     * 启动动画
     */
    fun startFingerAnimation() {
        mBinding.rlPagFinger.visibility = View.VISIBLE
        pagview_finger?.let {
            it.setRepeatCount(0)
            if (!it.isPlaying) {
                it.play()
            }
        }
    }

    fun stopFingerAnimation() {
        mBinding.rlPagFinger.visibility = View.GONE
        pagview_finger?.let {
            if (it.isPlaying) {
                it.stop()
                it.freeCache()
            }
        }
    }


    /**
     * 高亮显示
     */
    private fun tvGoldHighlight(content: String): SpannableString {
        val msp = SpannableString(content)
        val startIndex = 0
        val endIndex = content.indexOf("/")
        val colorSpan = ForegroundColorSpan(Color.parseColor("#EF874E"))
        msp.setSpan(colorSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        return msp
    }

    /**
     * 立即兑换按钮点击
     */
    fun onclickTvExchange(onCallBack: () -> Unit) {
        mBinding.tvExchange.singleClick {
            onCallBack()
        }
    }

    /**
     * 点击关闭按钮
     */
    fun onClickClose(onCallBack: () -> Unit) {
        mBinding.ivClose.singleClick {
            onCallBack()
        }
    }


    /**
     * 关闭弹框
     */
    fun dismissDialog() {
        if (isShowing) {
            dismiss()
            stopFingerAnimation()
        }
    }

    override fun getClassName(): String {
        return NewbiePoliteDialog::class.java.simpleName
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