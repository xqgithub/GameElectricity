package com.sn.gameelectricity.app.ext

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.constraintlayout.solver.state.State
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.sn.gameelectricity.R
import com.snai.aifun.utils.dp

class EmptyDefaultLayout : ConstraintLayout {

    private lateinit var imageView: ImageView
    private lateinit var textView: TextView
    private lateinit var buttonView: Button

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)


    init {
        addImageView()
        addTextView()
        addButtonView()
    }

    private fun addButtonView() {
        val layoutParams = LayoutParams(200.dp, 40.dp)
        layoutParams.topToBottom = R.id.emptyTextView
        layoutParams.startToStart = LayoutParams.PARENT_ID
        layoutParams.endToEnd = LayoutParams.PARENT_ID
        layoutParams.bottomToBottom = LayoutParams.PARENT_ID
        layoutParams.topMargin = 15.dp

        buttonView =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                createButtonView()
            } else {
                createButtonViewBy23()
            }
        buttonView.visibility = View.INVISIBLE
        addView(buttonView, layoutParams)
    }

    private fun addTextView() {
        val layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        layoutParams.topToBottom = R.id.emptyImageView
        layoutParams.startToStart = LayoutParams.PARENT_ID
        layoutParams.endToEnd = LayoutParams.PARENT_ID
        layoutParams.bottomToTop = R.id.emptyButtonView
        layoutParams.topMargin = 4.dp

        textView = createTextView()
        addView(textView, layoutParams)
    }

    private fun addImageView() {
        val layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        layoutParams.topToTop = LayoutParams.PARENT_ID
        layoutParams.startToStart = LayoutParams.PARENT_ID
        layoutParams.endToEnd = LayoutParams.PARENT_ID
        layoutParams.bottomToTop = R.id.emptyTextView
        layoutParams.verticalChainStyle = State.Chain.PACKED.ordinal

        imageView = createImageView()
        addView(imageView, layoutParams)
    }

    private fun createImageView(): ImageView {
        return ImageView(context).apply {
            id = R.id.emptyImageView
            scaleType = ImageView.ScaleType.CENTER_CROP
        }
    }

    private fun createTextView(): TextView {
        return TextView(context).apply {
            id = R.id.emptyTextView
            textSize = 12F
            setTextColor(ContextCompat.getColor(context, R.color.color_A1A7AF))
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun createButtonView(): Button {
        return Button(context, null, 0, R.style.S_Button).apply {
            id = R.id.emptyButtonView
        }
    }

    private fun createButtonViewBy23(): Button {
        return Button(context).apply {
            id = R.id.emptyButtonView
            setBackgroundResource(R.drawable.selector_btn_invalid)
            setTextAppearance(context, R.style.S_Button)
        }
    }

    /**
     * 设置占位图片
     * @param resId Int
     */
    fun setImageResource(@DrawableRes resId: Int) {
        imageView.setImageResource(resId)
    }

    /**
     * 设置提示文本
     * @param resId Int
     */
    fun setPromptText(@StringRes resId: Int) {
        textView.setText(resId)
    }

    /**
     * 设置按钮文本
     * @param resId Int
     */
    fun setButtonText(@StringRes resId: Int) {
        if (resId != 0) {
            buttonView.visibility = View.VISIBLE
            buttonView.setText(resId)
        }
    }

    fun setButtonClick(click: () -> Unit) {
        buttonView.setOnClickListener { click() }
    }

}