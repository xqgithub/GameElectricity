package com.sn.gameelectricity.app.weight.customview

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.sn.gameelectricity.R
import com.sn.gameelectricity.app.App
import com.sn.gameelectricity.databinding.ViewErrorpageBinding
import me.hgj.jetpackmvvm.util.PublicPracticalMethodFromKT
import me.hgj.jetpackmvvm.util.ScreenTools
import singleClick

/**
 * Date:2022/3/28
 * Time:16:45
 * author:dimple
 * 自定义错误页面，可以根据值显示不同的信息
 */
class ErrorPageView @JvmOverloads constructor(
    private var mContext: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(mContext, attrs, defStyleAttr) {

    private lateinit var mBinding: ViewErrorpageBinding

    //图像点击
    private lateinit var viewOnClickListener: () -> Unit

    init {
        val root = View.inflate(context, R.layout.view_errorpage, this)
        mBinding = ViewErrorpageBinding.bind(root)
    }

    /**
     * 设置图片
     */
    fun setErrorIcon(
        img: Int,
        width: Int = ScreenTools.getInstance().dp2px(App.instance, 36f, true),
        height: Int = ScreenTools.getInstance().dp2px(App.instance, 36f, true)
    ) {
        mBinding.ivError.setImageDrawable(ContextCompat.getDrawable(mContext, img))
        val params = mBinding.ivError.layoutParams
        params.height = height
        params.width = width
        mBinding.ivError.layoutParams = params
    }

    /**
     * 设置内容
     */
    fun setErrorContent(
        content: String = "",
        _textSize: Float = 12f,
        _textColor: String = "#57493B"
    ) {
        mBinding.tvError.apply {
            text = content
            textSize = _textSize
            setTextColor(Color.parseColor(_textColor))
        }
    }

    /**
     * 设置去操作
     */
    fun setToOperation(content: String) {
        if (!PublicPracticalMethodFromKT.ppmfKT.isBlank(content)) {
            mBinding.tvToOperate.apply {
                visibility = View.VISIBLE
                text = content
                PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
                    this,
                    -1,
                    "",
                    ScreenTools.getInstance().dp2px(App.instance, 24f, true).toFloat(),
                    ScreenTools.getInstance().dp2px(App.instance, 24f, true).toFloat(),
                    ScreenTools.getInstance().dp2px(App.instance, 24f, true).toFloat(),
                    ScreenTools.getInstance().dp2px(App.instance, 24f, true).toFloat(),
                    null,
                    "#4EEAC4"
                )
            }
        } else {
            mBinding.tvToOperate.visibility = View.GONE
        }
    }

    /**
     * 点击事件
     */
    fun toOperateOnClickListener(viewOnClickListener: () -> Unit) {
        this.viewOnClickListener = viewOnClickListener
        mBinding.tvToOperate.singleClick {
            this.viewOnClickListener.invoke()
        }
    }

    /**
     * 修改图片距离高度
     */
    fun changeErrorIconPositionToTOP(offsetTop: Int) {
        val layout = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )
        layout.startToStart = R.id.cl_main
        layout.endToEnd = R.id.cl_main
        layout.topToTop = R.id.cl_main
        layout.topMargin = offsetTop
        mBinding.ivError.layoutParams = layout
    }


    /**
     * 修改文字距离高度
     */
    fun changeErrorTextPositionToTOP(offsetTop: Int) {
        val layout = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )
        layout.startToStart = R.id.cl_main
        layout.endToEnd = R.id.cl_main
        layout.topToBottom = R.id.iv_error
        layout.topMargin = offsetTop
        mBinding.tvError.layoutParams = layout
    }


}