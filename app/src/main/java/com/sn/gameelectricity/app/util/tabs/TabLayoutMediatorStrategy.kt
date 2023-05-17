package com.sn.gameelectricity.app.util.tabs

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Rect
import android.graphics.Typeface
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.core.animation.doOnStart
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.blankj.utilcode.util.LogUtils
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.sn.gameelectricity.R
import com.sn.gameelectricity.app.util.ColorUtil
import me.hgj.jetpackmvvm.common.ConfigConstants
import java.time.format.TextStyle

/**
 * Date:2022/5/10
 * Time:10:17
 * author:dimple
 * 自定义 TabLayout配置策略,后面可以自行添加
 */
class TabLayoutMediatorStrategy(
    private val context: Context,
    private val tabTitleList: MutableList<String>,
    private val viewPager: ViewPager2
) : TabLayoutMediator.TabConfigurationStrategy, OnTabPageChangeCallback {


    override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
        createTabView(tab, position)
    }

    private fun createTabView(tab: TabLayout.Tab, position: Int): TabLayout.Tab {
        val view = LayoutInflater.from(context).inflate(R.layout.item_tablayout_orderlist, null)
        val iv_title_line = view.findViewById<ImageView>(R.id.iv_title_line)
        val tv_title_name = view.findViewById<TextView>(R.id.tv_title_name)

        iv_title_line.apply {
            alpha = if (viewPager.currentItem == position) 1f else 0f
        }

        tv_title_name.apply {
            text = tabTitleList[position]
            setTextColor(
                ContextCompat.getColor(
                    context,
                    if (viewPager.currentItem == position) R.color.color_061925 else R.color.color_6A7079
                )
            )
        }
        tab.customView = view
        return tab
    }


    override fun onTabSelected(view: View) {
        val iv_title_line = view.findViewById<ImageView>(R.id.iv_title_line)
        val tv_title_name = view.findViewById<TextView>(R.id.tv_title_name)


        iv_title_line.post {
            val defaultLeft = (iv_title_line.width * 0.2f).toInt()
            val offset = iv_title_line.width - defaultLeft
            val animator = ValueAnimator.ofFloat(0f, 1f)
            animator.duration = 200
            animator.interpolator = DecelerateInterpolator()
            animator.doOnStart {
                iv_title_line.alpha = 1f
                iv_title_line.clipBounds = Rect(0, 0, defaultLeft, iv_title_line.height)
            }
            animator.addUpdateListener {
                val value = it.animatedValue as Float
                iv_title_line.clipBounds =
                    Rect(0, 0, (defaultLeft + (offset * value)).toInt(), iv_title_line.height)
            }
            animator.start()
        }

        tv_title_name.apply {
            setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.color_061925
                )
            )
            setTypeface(null, Typeface.BOLD)
        }


    }

    override fun onTabUnselected(view: View) {
        val iv_title_line = view.findViewById<ImageView>(R.id.iv_title_line)
        val tv_title_name = view.findViewById<TextView>(R.id.tv_title_name)
        iv_title_line.alpha = 0f

        tv_title_name.apply {
            setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.color_6A7079
                )
            )
            setTypeface(null, Typeface.NORMAL)
        }
    }

    override fun onPageScrolled(selectedChild: View, nextTabView: View, positionOffset: Float) {
        val selected_iv_title_line = selectedChild.findViewById<ImageView>(R.id.iv_title_line)
        val selected_tv_title_name = selectedChild.findViewById<TextView>(R.id.tv_title_name)

        val next_iv_title_line = nextTabView.findViewById<ImageView>(R.id.iv_title_line)
        val next_tv_title_name = nextTabView.findViewById<TextView>(R.id.tv_title_name)

        selected_tv_title_name.setTextColor(
            ColorUtil.evaluate(
                1f - positionOffset, ContextCompat.getColor(
                    context,
                    R.color.color_6A7079
                ), ContextCompat.getColor(
                    context,
                    R.color.color_061925
                )
            )
        )

        next_tv_title_name.setTextColor(
            ColorUtil.evaluate(
                1f - positionOffset, ContextCompat.getColor(
                    context,
                    R.color.color_061925
                ), ContextCompat.getColor(
                    context,
                    R.color.color_6A7079
                )
            )
        )

        if (selected_iv_title_line.alpha != 0f)
            selected_iv_title_line.alpha = (1f - positionOffset)

        if (next_iv_title_line.alpha != 0f)
            next_iv_title_line.alpha = positionOffset
    }
}