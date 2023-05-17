package com.sn.gameelectricity.ui.activity

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.core.widget.doAfterTextChanged
import com.sn.gameelectricity.R
import com.sn.gameelectricity.app.base.BaseActivity1
import com.sn.gameelectricity.data.model.bean.OrderListRefreshBean
import com.sn.gameelectricity.databinding.ActivityOrderSearchBinding
import com.sn.gameelectricity.ui.fragment.OrderListFragment
import com.sn.gameelectricity.viewmodel.OrderListViewModel
import me.hgj.jetpackmvvm.event.UniversalEvent
import me.hgj.jetpackmvvm.util.PublicPracticalMethodFromKT
import me.hgj.jetpackmvvm.util.ScreenTools
import org.greenrobot.eventbus.EventBus
import singleClick

/**
 * Date:2022/5/10
 * Time:19:03
 * author:dimple
 * 订单搜索页面
 */
class OrderSearchActivity : BaseActivity1<OrderListViewModel, ActivityOrderSearchBinding>() {

    private val type: OrderListFragment.OrderType by lazy {
        intent?.getSerializableExtra("type") as OrderListFragment.OrderType
    }

    //搜索关键字
    private var searchKeywords = ""


    override fun initView(savedInstanceState: Bundle?) {
        mViewBind.apply {
            tvCancel.singleClick {
                finish()
            }

            //设置搜索栏的背景
            PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
                clSearch, -1, "",
                ScreenTools.getInstance().dp2px(this@OrderSearchActivity, 30f, true).toFloat(),
                ScreenTools.getInstance().dp2px(this@OrderSearchActivity, 30f, true).toFloat(),
                ScreenTools.getInstance().dp2px(this@OrderSearchActivity, 30f, true).toFloat(),
                ScreenTools.getInstance().dp2px(this@OrderSearchActivity, 30f, true).toFloat(),
                null, "#F7F9FE"
            )


            etSearch.apply {
//                doAfterTextChanged {
//                    searchKeywords = it.toString()
//                }
                setOnEditorActionListener { v, actionId, event ->
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        searchKeywords = v.text.toString().trim()
                        EventBus.getDefault().post(
                            UniversalEvent(
                                UniversalEvent.EVENT_ORDERLIST_REFRESH,
                                OrderListRefreshBean(
                                    OrderListFragment.OrderType.SEARCH,
                                    searchKeywords
                                )
                            )
                        )
                    }
                    false
                }
            }
        }

        initFragmentLayout()
    }

    /**
     * 初始化Fragment
     */
    private fun initFragmentLayout() {
        val transaction = supportFragmentManager.beginTransaction()
        val orderListFragment = OrderListFragment.newInstance(type)
        transaction.add(R.id.fragmentContainer, orderListFragment)
        transaction.commit()
    }
}