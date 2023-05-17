package com.sn.gameelectricity.data.model.bean

import com.sn.gameelectricity.ui.fragment.OrderListFragment

/**
 * Date:2022/6/16
 * Time:20:07
 * author:dimple
 */
data class OrderListRefreshBean(
    val orderType: OrderListFragment.OrderType,
    val searchKeywords: String
)
