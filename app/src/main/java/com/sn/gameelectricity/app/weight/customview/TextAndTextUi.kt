package com.sn.gameelectricity.app.weight.customview

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.sn.gameelectricity.R
import com.sn.gameelectricity.app.App
import com.sn.gameelectricity.databinding.ViewImgTextUiBinding
import com.sn.gameelectricity.databinding.ViewTextTextUiBinding
import kotlinx.android.synthetic.main.view_text_text_ui.view.*
import me.hgj.jetpackmvvm.util.ScreenTools
import me.hgj.jetpackmvvm.util.load
import singleClick

/**
 * Date:2022/5/9
 * Time:14:03
 * author:dimple
 * 文字描述+金额数字 ui
 */
class TextAndTextUi @JvmOverloads constructor(
    private var mContext: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(mContext, attrs, defStyleAttr) {

    private lateinit var mBinding: ViewTextTextUiBinding

    //Item点击事件
    private lateinit var itemOnClickListener: () -> Unit

    init {
        val root = View.inflate(context, R.layout.view_text_text_ui, this)
        mBinding = ViewTextTextUiBinding.bind(root)
    }

    /**
     * 设置描述文字信息
     */
    fun setDescribeText(
        content: String, _textSize: Float = 12f, _textColor: String = "#061925"
    ) {
        mBinding.tvDescribe.apply {
            text = content
            textSize = _textSize
            setTextColor(Color.parseColor(_textColor))
        }
    }

    /**
     * 设置数字文字信息
     */
    fun setNumText(content: String, _textSize: Float = 14f, _textColor: String = "#061925") {
        mBinding.tvPrice.apply {
            text = content
            textSize = _textSize
            setTextColor(Color.parseColor(_textColor))
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
}