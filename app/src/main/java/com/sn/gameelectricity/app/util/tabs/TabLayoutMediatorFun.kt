package com.sn.gameelectricity.app.util.tabs

import android.view.View
import android.widget.FrameLayout
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.sn.gameelectricity.data.model.bean.OrderListRefreshBean
import com.sn.gameelectricity.ui.fragment.OrderListFragment
import me.hgj.jetpackmvvm.event.UniversalEvent
import org.greenrobot.eventbus.EventBus

/**
 * 自定义TabLayout
 */
class TabLayoutMediatorFun(
    tabTitleList: MutableList<String>,
    private val tabLayout: TabLayout,
    private val viewPager: ViewPager2,
    private val tabLayoutMediatorStrategy: TabLayoutMediator.TabConfigurationStrategy = TabLayoutMediatorStrategy(
        tabLayout.context,
        tabTitleList,
        viewPager
    )
) : TabLayout.OnTabSelectedListener, ViewPager2.OnPageChangeCallback() {

    private var previousScrollState = ViewPager2.SCROLL_STATE_IDLE
    private var scrollState = ViewPager2.SCROLL_STATE_IDLE

    init {
        TabLayoutMediator(tabLayout, viewPager, tabLayoutMediatorStrategy).attach()
        tabLayout.addOnTabSelectedListener(this)
        viewPager.registerOnPageChangeCallback(this)
    }

    //tab
    override fun onTabSelected(tab: TabLayout.Tab?) {
        tab?.apply {
            if (tabLayoutMediatorStrategy is OnTabPageChangeCallback && customView is View) {
                tabLayoutMediatorStrategy.onTabSelected(customView as View)
            }
        }

    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
        tab?.apply {
            if (tabLayoutMediatorStrategy is OnTabPageChangeCallback && customView is View) {
                tabLayoutMediatorStrategy.onTabUnselected(customView as View)
            }
        }
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {
    }

    //viewpager
    override fun onPageSelected(position: Int) {
        val tab = tabLayout.getTabAt(position)
        val customView = tab?.customView
        if (tabLayoutMediatorStrategy is OnTabPageChangeCallback && customView is View) {
            tabLayoutMediatorStrategy.onTabSelected(customView)
        }

        var type = OrderListFragment.OrderType.All
        when (position) {
            0 -> {
                type = OrderListFragment.OrderType.All
            }
            1 -> {
                type = OrderListFragment.OrderType.PENDING_PAYMENT
            }
            2 -> {
                type = OrderListFragment.OrderType.PENDING_DELIVERED
            }
            3 -> {
                type = OrderListFragment.OrderType.PENDING_DELIVERY
            }
            4 -> {
                type = OrderListFragment.OrderType.PENDING_RECEIPT
            }
            5 -> {
                type = OrderListFragment.OrderType.COMPLETED
            }
        }
        EventBus.getDefault().post(
            UniversalEvent(
                UniversalEvent.EVENT_ORDERLIST_REFRESH,
                OrderListRefreshBean(type, "")
            )
        )
    }

    override fun onPageScrollStateChanged(state: Int) {
        previousScrollState = scrollState
        scrollState = state
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        super.onPageScrolled(position, positionOffset, positionOffsetPixels)
        //当previousScrollState == 0 && scrollState == 2 时表示该次滚动为用户点击TabLayout造成的滚动，忽略掉。。。。
        if (!(previousScrollState == 0 && scrollState == 2)) {
            val formatPositionOffset = Math.round(positionOffset * 100).toFloat() / 100
            val selectedChild = tabLayout.getTabAt(position)?.customView
            val nextChild = tabLayout.getTabAt(position + 1)?.customView
            if (selectedChild is View && nextChild is View && tabLayoutMediatorStrategy is OnTabPageChangeCallback) {
                tabLayoutMediatorStrategy.onPageScrolled(
                    selectedChild,
                    nextChild,
                    formatPositionOffset
                )
            }
        }
    }
}