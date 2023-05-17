package com.sn.gameelectricity.app.weight.customview

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.sn.gameelectricity.R
import com.sn.gameelectricity.app.App
import com.sn.gameelectricity.databinding.ViewImgTextUi2Binding
import com.sn.gameelectricity.databinding.ViewImgTextUiBinding
import me.hgj.jetpackmvvm.util.ScreenTools
import me.hgj.jetpackmvvm.util.load
import singleClick

/**
 * Date:2022/5/9
 * Time:14:03
 * author:dimple
 * 自定义图片和文字控件 左图右文字
 */
class ImageAndTextUi2 @JvmOverloads constructor(
    private var mContext: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(mContext, attrs, defStyleAttr) {

    private lateinit var mBinding: ViewImgTextUi2Binding

    //Item点击事件
    private lateinit var itemOnClickListener: () -> Unit

    init {
        val root = View.inflate(context, R.layout.view_img_text_ui2, this)
        mBinding = ViewImgTextUi2Binding.bind(root)
    }

    /**
     * 设置图片信息
     */
    fun setAvatarDataFromRes(
        imgId: Int,
        width: Int = ScreenTools.getInstance().dp2px(App.instance, 36f, true),
        height: Int = ScreenTools.getInstance().dp2px(App.instance, 36f, true)
    ) {
        mBinding.sivAvatar.setImageDrawable(ContextCompat.getDrawable(mContext, imgId))

        val params = mBinding.sivAvatar.layoutParams
        params.height = height
        params.width = width
        mBinding.sivAvatar.layoutParams = params
    }

    /**
     * 设置文字信息
     */
    fun setTextData(content: String, _textSize: Float = 12f, _textColor: String = "#57493B") {
        mBinding.tvContent.apply {
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