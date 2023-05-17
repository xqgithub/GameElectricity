package com.sn.gameelectricity.app.weight.customview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.sn.gameelectricity.R
import com.sn.gameelectricity.app.App
import com.sn.gameelectricity.databinding.ViewCountdownProgressbarBinding
import me.hgj.jetpackmvvm.util.ScreenTools

/**
 * Date:2022/5/24
 * Time:15:58
 * author:dimple
 * 倒计时进度条
 */
class CountdownProgressBar @JvmOverloads constructor(
    private var mContext: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(mContext, attrs, defStyleAttr) {

    private lateinit var mBinding: ViewCountdownProgressbarBinding

    private var pageLocation: PageLocation? = null

    init {
        val root = View.inflate(context, R.layout.view_countdown_progressbar, this)
        mBinding = ViewCountdownProgressbarBinding.bind(root)

    }

    /**
     * 页面位置
     */
    enum class PageLocation {
        MoneyTaskDialog, MoneyFragment
    }

    var getMagnifierUi = {
        mBinding.ivMagnifier
    }


    /**
     * 设置进度条最大值
     */
    fun setProgressMax(max: Int) {
        if (pageLocation == PageLocation.MoneyTaskDialog) {
            mBinding.pb.max = max
        } else {
            mBinding.pb2.max = max
        }
    }


    /**
     * 设置进度条
     */
    fun setProgress(progress: Int) {
        if (pageLocation == PageLocation.MoneyTaskDialog) {
            mBinding.pb.progress = progress
        } else {
            mBinding.pb2.progress = progress
        }
    }

    /**
     * 设置时间显示
     */
    fun setTvText(content: String) {
        if (pageLocation == PageLocation.MoneyTaskDialog) {
            mBinding.tvTime.text = content
        } else {
            mBinding.tvTime2.text = content
        }


    }

    fun init(pageLocation: PageLocation) {
        this.pageLocation = pageLocation
        if (pageLocation == PageLocation.MoneyTaskDialog) {
            mBinding.ivIcon.visibility = View.VISIBLE
            mBinding.pb.visibility = View.VISIBLE
            mBinding.tvTime.visibility = View.VISIBLE


            mBinding.ivIcon2.visibility = View.GONE
            mBinding.pb2.visibility = View.GONE
            mBinding.ivMagnifier.visibility = View.GONE
            mBinding.tvTime2.visibility = View.GONE

        } else {
            mBinding.ivIcon.visibility = View.GONE
            mBinding.pb.visibility = View.GONE
            mBinding.tvTime.visibility = View.GONE

            mBinding.ivIcon2.visibility = View.VISIBLE
            mBinding.pb2.visibility = View.VISIBLE
            mBinding.ivMagnifier.visibility = View.VISIBLE
            mBinding.tvTime2.visibility = View.VISIBLE
        }
    }

    /**
     * 设置icon
     */
    fun setIcon2FromRes(
        imgId: Int,
        width: Int = ScreenTools.getInstance().dp2px(App.instance, 24f, true),
        height: Int = ScreenTools.getInstance().dp2px(App.instance, 24f, true)
    ) {
        mBinding.ivIcon2.setImageDrawable(ContextCompat.getDrawable(mContext, imgId))
        val params = mBinding.ivIcon2.layoutParams
        params.height = height
        params.width = width
        mBinding.ivIcon2.layoutParams = params
    }

    /**
     * 设置进度条的高度
     */
    fun setPb2Height(height: Int = ScreenTools.getInstance().dp2px(App.instance, 24f, true)) {
        val params = mBinding.pb2.layoutParams
        params.height = height
        params.width = width
        mBinding.pb2.layoutParams = params
    }
}