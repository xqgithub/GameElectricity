package com.sn.gameelectricity.app.weight.customview

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import com.sn.gameelectricity.R
import com.sn.gameelectricity.databinding.LayoutNavigationMainBinding
import singleClick

/**
 *
 * @ProjectName:    AIFun
 * @Package:        com.shannon.aifun.components.widgets
 * @ClassName:      MainNavigationLayout
 * @Description:    首页底部导航栏
 * @Author:         czhen
 * @CreateDate:     2021/6/23 9:52
 */
class MainNavigationLayout : RelativeLayout {

    private val rootViewBinding: LayoutNavigationMainBinding =
        LayoutNavigationMainBinding.inflate(LayoutInflater.from(context), this, true)
    private var selectedPosition = 0
    private var itemSelectedListener: OnNavigationItemSelectedListener? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        setBackgroundColor(Color.TRANSPARENT)
        bindListener()
    }


    private fun bindListener() {
        with(rootViewBinding) {
            tabHome.singleClick {
                if (rootViewBinding.navigationRoot.alpha != 1.0f) return@singleClick
                releaseSelected()
                changeTabHome(true)
                callbackListener()
            }
            tabMoney.singleClick {
                if (rootViewBinding.navigationRoot.alpha != 1.0f) return@singleClick
                releaseSelected()
                changeTabMoney(true)
                callbackListener()
            }
            tabMine.singleClick {
                if (rootViewBinding.navigationRoot.alpha != 1.0f) return@singleClick
                releaseSelected()
                changeTabMine(true)
                callbackListener()
            }
        }
    }

    /**
     * 将已选中的恢复到默认状态
     */
    private fun releaseSelected() {
        when (selectedPosition) {
            0 -> changeTabHome(false)
            1 -> changeTabMoney(false)
            2 -> changeTabMine(false)
        }
    }

    private fun getColorValue(colorId: Int): Int {
        return ContextCompat.getColor(context, colorId)
    }

    private fun changeTabHome(isSelected: Boolean) {
        with(rootViewBinding) {
            if (isSelected) {
                selectedPosition = 0
                tabHome.setBackgroundResource(R.drawable.selector_btn_ef874e)
                tabHomeIconView.setImageResource(R.drawable.navigation_tab_home)
                tabHomeTextView.setTextColor(getColorValue(R.color.navigation_text_selected))
            } else {
                tabHome.setBackgroundResource(R.drawable.selector_btn_ffffff)
                tabHomeIconView.setImageResource(R.drawable.navigation_tab_home_normal)
                tabHomeTextView.setTextColor(getColorValue(R.color.navigation_text_unselected))
            }
        }
    }

    private fun changeTabMoney(isSelected: Boolean) {
        with(rootViewBinding) {
            if (isSelected) {
                selectedPosition = 1
                tabMoney.setBackgroundResource(R.drawable.selector_btn_ef874e)
                tabMoneyIconView.setImageResource(R.drawable.navigation_tab_money)
                tabMoneyTextView.setTextColor(getColorValue(R.color.navigation_text_selected))
            } else {
                tabMoney.setBackgroundResource(R.drawable.selector_btn_ffffff)
                tabMoneyIconView.setImageResource(R.drawable.navigation_tab_money_normal)
                tabMoneyTextView.setTextColor(getColorValue(R.color.navigation_text_unselected))
            }
        }
    }

    private fun changeTabMine(isSelected: Boolean) {
        with(rootViewBinding) {
            if (isSelected) {
                selectedPosition = 2
                tabMine.setBackgroundResource(R.drawable.selector_btn_ef874e)
                tabMineIconView.setImageResource(R.drawable.navigation_tab_mine)
                tabMineTextView.setTextColor(getColorValue(R.color.navigation_text_selected))
            } else {
                tabMine.setBackgroundResource(R.drawable.selector_btn_ffffff)
                tabMineIconView.setImageResource(R.drawable.navigation_tab_mine_normal)
                tabMineTextView.setTextColor(getColorValue(R.color.navigation_text_unselected))
            }
        }
    }

    private fun callbackListener() {
        itemSelectedListener?.apply {
            onNavigationItemSelected(selectedPosition)
        }
    }

    fun setOnNavigationItemSelectedListener(listener: OnNavigationItemSelectedListener) {
        this.itemSelectedListener = listener
    }

    interface OnNavigationItemSelectedListener {
        fun onNavigationItemSelected(position: Int)
    }

    /**
     * 控制tab跳转
     */
    fun controlTabJump(position: Int) {
        when (position) {
            0 -> {
                if (rootViewBinding.navigationRoot.alpha != 1.0f) return
                releaseSelected()
                changeTabHome(true)
                callbackListener()
            }
            1 -> {
                if (rootViewBinding.navigationRoot.alpha != 1.0f) return
                releaseSelected()
                changeTabMoney(true)
                callbackListener()
            }
            2 -> {
                if (rootViewBinding.navigationRoot.alpha != 1.0f) return
                releaseSelected()
                changeTabMine(true)
                callbackListener()
            }
        }
    }

    /**
     * 设置遮罩层
     */
    fun setClMaskLayerWhetherToDisplay(mVisible: Boolean = false) {
        if (mVisible) {
            rootViewBinding.clMaskLayer.visibility = View.VISIBLE
            rootViewBinding.clMaskLayer.singleClick {

            }
        } else {
            rootViewBinding.clMaskLayer.visibility = View.GONE
        }
    }
}