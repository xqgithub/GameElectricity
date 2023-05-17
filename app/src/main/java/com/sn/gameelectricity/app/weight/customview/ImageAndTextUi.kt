package com.sn.gameelectricity.app.weight.customview

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.google.android.material.imageview.ShapeableImageView
import com.sn.gameelectricity.R
import com.sn.gameelectricity.app.App
import com.sn.gameelectricity.databinding.ViewImgTextUiBinding
import me.hgj.jetpackmvvm.util.PublicPracticalMethodFromKT
import me.hgj.jetpackmvvm.util.ScreenTools
import me.hgj.jetpackmvvm.util.load
import singleClick

/**
 * Date:2022/5/9
 * Time:14:03
 * author:dimple
 * 自定义图片和文字控件 上下格式
 */
class ImageAndTextUi @JvmOverloads constructor(
    private var mContext: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(mContext, attrs, defStyleAttr) {

    private lateinit var mBinding: ViewImgTextUiBinding

    //Item点击事件
    private lateinit var itemOnClickListener: () -> Unit

    init {
        val root = View.inflate(context, R.layout.view_img_text_ui, this)
        mBinding = ViewImgTextUiBinding.bind(root)
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
     * 设置图片信息
     */
    fun setAvatarDataFromUrl(
        imgUrl: String,
        imgDefault: Int,
        width: Int = ScreenTools.getInstance().dp2px(App.instance, 36f, true),
        height: Int = ScreenTools.getInstance().dp2px(App.instance, 36f, true)
    ) {
        val params = mBinding.sivAvatar.layoutParams
        params.height = height
        params.width = width
        mBinding.sivAvatar.layoutParams = params
        mBinding.sivAvatar.load(imgUrl, imgDefault, imgDefault)
    }

    /**
     * 设置图片信息
     */
    fun setAvatarDataFromUrl2(
        imgUrl: String,
        imgDefault: Int,
        width: Int = ScreenTools.getInstance().dp2px(App.instance, 36f, true),
        height: Int = ScreenTools.getInstance().dp2px(App.instance, 36f, true)
    ) {
        mBinding.sivAvatar.visibility = View.GONE
        mBinding.sivAvatar2.visibility = View.VISIBLE
        val params = mBinding.sivAvatar2.layoutParams
        params.height = height
        params.width = width
        mBinding.sivAvatar2.layoutParams = params
        mBinding.sivAvatar2.load(imgUrl, imgDefault, imgDefault)
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
     * 设置文字信息
     */
    fun setTextData2(content: String, _textSize: Float = 12f, _textColor: String = "#57493B") {
        mBinding.tvContent2.apply {
            visibility = View.VISIBLE
            mBinding.tvContent.visibility = View.GONE
            text = content
            textSize = _textSize
            setTextColor(Color.parseColor(_textColor))
        }
    }

    /**
     * 设置角标数据
     */
    fun setDotData(
        content: String,
        _textSize: Float = 10f,
        _textColor: String = "#ffffff",
        isShow: Boolean = true
    ) {
        mBinding.tvDot.apply {
            if (isShow) visibility = View.VISIBLE else visibility = View.GONE
            text = content
            textSize = _textSize
            setTextColor(Color.parseColor(_textColor))
            PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
                this, ScreenTools.getInstance().dp2px(mContext, 2f, true), "#FFFFFF",
                ScreenTools.getInstance().dp2px(mContext, 9f, true).toFloat(),
                ScreenTools.getInstance().dp2px(mContext, 9f, true).toFloat(),
                ScreenTools.getInstance().dp2px(mContext, 9f, true).toFloat(),
                ScreenTools.getInstance().dp2px(mContext, 9f, true).toFloat(),
                null, "#F85542"
            )
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