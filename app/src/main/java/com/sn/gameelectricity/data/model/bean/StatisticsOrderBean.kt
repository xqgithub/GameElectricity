package com.sn.gameelectricity.data.model.bean

/**
 * Date:2022/6/15
 * Time:16:07
 * author:dimple
 * 统计订单实体类
 */
data class StatisticsOrderBean(
    val countGoodsToBePickedUp: Int,
    val countToBePaid: Int,
    val countToBeReceived: Int,
    val countToBeShipped: Int
)