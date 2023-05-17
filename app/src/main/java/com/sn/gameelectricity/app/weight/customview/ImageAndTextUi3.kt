package com.sn.gameelectricity.app.weight.customview

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.PathInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.sn.gameelectricity.R
import com.sn.gameelectricity.app.App
import com.sn.gameelectricity.app.util.pag.PagAnimationTools
import com.sn.gameelectricity.databinding.ViewImgTextUi3Binding
import me.hgj.jetpackmvvm.util.PublicPracticalMethodFromKT
import me.hgj.jetpackmvvm.util.ScreenTools
import org.libpag.PAGView
import singleClick


/**
 * Date:2022/5/9
 * Time:14:03
 * author:dimple
 * 自定义图片和文字控件 首页任务状态
 */
class ImageAndTextUi3 @JvmOverloads constructor(
    private var mContext: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(mContext, attrs, defStyleAttr) {

    private lateinit var mBinding: ViewImgTextUi3Binding

    //Item点击事件
    private lateinit var itemOnClickListener: () -> Unit

    private var pag_halo: PAGView? = null

    init {
        val root = View.inflate(context, R.layout.view_img_text_ui3, this)
        mBinding = ViewImgTextUi3Binding.bind(root)
    }

    /**
     * 设置背景图信息
     */
    fun setBgPicFromRes(
        imgId: Int,
        width: Int = ScreenTools.getInstance().dp2px(App.instance, 64f, true),
        height: Int = ScreenTools.getInstance().dp2px(App.instance, 64f, true)
    ) {
        mBinding.ivBg.setImageDrawable(ContextCompat.getDrawable(mContext, imgId))
        val params = mBinding.ivBg.layoutParams
        params.height = height
        params.width = width
        mBinding.ivBg.layoutParams = params
    }

    /**
     * 设置任务状态图信息
     */
    fun setTaskStatusFromRes(
        imgId: Int,
        width: Int = ScreenTools.getInstance().dp2px(App.instance, 44f, true),
        height: Int = ScreenTools.getInstance().dp2px(App.instance, 44f, true)
    ) {
        mBinding.ivTaskStatus.setImageDrawable(ContextCompat.getDrawable(mContext, imgId))
        val params = mBinding.ivTaskStatus.layoutParams
        params.height = height
        params.width = width
        mBinding.ivTaskStatus.layoutParams = params
    }

    /**
     * 设置文字信息
     */
    fun setTextData(
        content: String = "",
        isShow: Boolean = true,
        _textSize: Float = 10f,
        _textColor: String = "#864F17"
    ) {
        mBinding.tvNum.apply {
            if (isShow) this.visibility = View.VISIBLE else this.visibility = View.GONE
            text = content
            textSize = _textSize
            setTextColor(Color.parseColor(_textColor))
        }
    }

    /**
     * 修改文字距离高度
     */
    fun changeTextPositionToTOP(offsetTop: Int) {
        val layout = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )
        layout.startToStart = R.id.cl_main
        layout.endToEnd = R.id.cl_main
        layout.topToTop = R.id.cl_main
        layout.topMargin = offsetTop
        mBinding.tvNum.layoutParams = layout
    }

    /**
     * 开始动画
     */
    fun startAnimation() {
        if (PublicPracticalMethodFromKT.ppmfKT.isBlank(pag_halo)) {
            pag_halo = PagAnimationTools.pagTools.getPagView(
                "glow.pag",
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            mBinding.rlPagHalo.addView(pag_halo)
        }
        pag_halo?.let {
            it.setRepeatCount(0)
            if (!it.isPlaying) it.play()
        }
    }

    /**
     * 停止动画
     */
    fun stopAnimation() {
        pag_halo?.let {
            if (it.isPlaying) {
                it.stop()
                it.freeCache()
            }
        }
    }

    /**
     * 启动移动动画
     */
    private var animator: ObjectAnimator? = null

    fun startMoveAnimation(view: View, offsetY: Float = 0f) {
        if (PublicPracticalMethodFromKT.ppmfKT.isBlank(animator)) {
            animator = ObjectAnimator.ofFloat(
                this,
                "translationY",
                view.translationY,
                offsetY,
                view.translationY
            )
            animator?.apply {
                duration = 2000
                repeatCount = -1
                repeatMode = ObjectAnimator.RESTART
                start()
            }
        } else {
            animator?.apply {
                if (!isRunning) start()
            }
        }
    }

    /**
     * 停止移动动画
     */
    fun stopMoveAnimation() {
        animator?.let {
            if (it.isRunning) {
                it.pause()
                it.end()
            }
        }
    }

    /**
     * item点击事件
     */
    fun setItemOnClickListener(itemOnClickListener: () -> Unit) {
        this.itemOnClickListener = itemOnClickListener
        mBinding.clMain.singleClick {
            this.itemOnClickListener.invoke()
        }
    }

    override fun onWindowVisibilityChanged(visibility: Int) {
        super.onWindowVisibilityChanged(visibility)
        if (visibility == View.GONE) {
            pag_halo?.let {
                if (it.isPlaying) {
                    it.stop()
                    it.freeCache()
                }
            }

            animator?.let {
                if (it.isRunning) {
                    it.pause()
                    it.end()
                }
            }
        }
    }

    private var mStartPointX = 0f
    private var mStartPointY = 0f
    private var mEndPointX = 0f
    private var mEndPointY = 0f

    private var mMovePointX = 0f
    private var mMovePointY = 0f

    private var mControlPointX = 0f
    private var mControlPointY = 0f

    private var mPath: Path? = null
    private var mPathPaint: Paint? = null


}