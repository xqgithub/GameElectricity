package com.sn.gameelectricity.ui.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.sn.gameelectricity.app.base.BaseActivity1
import com.sn.gameelectricity.app.util.tabs.TabLayoutMediatorFun
import com.sn.gameelectricity.databinding.ActivityOrderListBinding
import com.sn.gameelectricity.ui.adapter.SimpleActivityPageAdapter
import com.sn.gameelectricity.ui.adapter.SimpleFragmentPageAdapter
import com.sn.gameelectricity.ui.fragment.OrderListFragment
import com.sn.gameelectricity.viewmodel.OrderListViewModel
import me.hgj.jetpackmvvm.util.PublicPracticalMethodFromKT
import me.hgj.jetpackmvvm.util.ScreenTools
import singleClick

/**
 * Date:2022/5/9
 * Time:17:29
 * author:dimple
 *  订单列表页面
 */
class OrderListActivity : BaseActivity1<OrderListViewModel, ActivityOrderListBinding>() {

    private val type: OrderListFragment.OrderType by lazy {
        intent?.getSerializableExtra("type") as OrderListFragment.OrderType
    }

    private var fragmentList = mutableListOf<Fragment>()
    private val nameList = mutableListOf<String>()

    override fun initView(savedInstanceState: Bundle?) {

        mViewBind.apply {
            //设置搜索栏的背景
            PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
                clSearch, -1, "",
                ScreenTools.getInstance().dp2px(this@OrderListActivity, 30f, true).toFloat(),
                ScreenTools.getInstance().dp2px(this@OrderListActivity, 30f, true).toFloat(),
                ScreenTools.getInstance().dp2px(this@OrderListActivity, 30f, true).toFloat(),
                ScreenTools.getInstance().dp2px(this@OrderListActivity, 30f, true).toFloat(),
                null, "#F7F9FE"
            )

            ivBack.singleClick {
                finish()
            }

            clSearch.singleClick {
                with(Bundle()) {
                    putSerializable("type", OrderListFragment.OrderType.SEARCH)
                    PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                        this@OrderListActivity,
                        OrderSearchActivity::class.java, this, false
                    )
                }
            }
        }

        initData()
    }

    private fun initData() {

        nameList.add("全部")
        nameList.add("待付款")
        nameList.add("待提货")
        nameList.add("待发货")
        nameList.add("待收货")
        nameList.add("已完成")

        fragmentList.add(OrderListFragment.newInstance(OrderListFragment.OrderType.All))
        fragmentList.add(OrderListFragment.newInstance(OrderListFragment.OrderType.PENDING_PAYMENT))
        fragmentList.add(OrderListFragment.newInstance(OrderListFragment.OrderType.PENDING_DELIVERED))
        fragmentList.add(OrderListFragment.newInstance(OrderListFragment.OrderType.PENDING_DELIVERY))
        fragmentList.add(OrderListFragment.newInstance(OrderListFragment.OrderType.PENDING_RECEIPT))
        fragmentList.add(OrderListFragment.newInstance(OrderListFragment.OrderType.COMPLETED))

        mViewBind.apply {

            viewPager.adapter = SimpleActivityPageAdapter(this@OrderListActivity, fragmentList)
            viewPager.offscreenPageLimit = nameList.size
            TabLayoutMediatorFun(nameList, tabLayout, viewPager)

            when (type) {
                OrderListFragment.OrderType.All -> {
                    viewPager.currentItem = 0
                }
                OrderListFragment.OrderType.PENDING_PAYMENT -> {
                    viewPager.currentItem = 1
                }
                OrderListFragment.OrderType.PENDING_DELIVERED -> {
                    viewPager.currentItem = 2
                }
                OrderListFragment.OrderType.PENDING_DELIVERY -> {
                    viewPager.currentItem = 3
                }
                OrderListFragment.OrderType.PENDING_RECEIPT -> {
                    viewPager.currentItem = 4
                }
                OrderListFragment.OrderType.COMPLETED -> {
                    viewPager.currentItem = 5
                }
            }
        }
    }
}