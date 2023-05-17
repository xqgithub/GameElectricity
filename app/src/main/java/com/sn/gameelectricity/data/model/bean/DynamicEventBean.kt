package com.sn.gameelectricity.data.model.bean

/**
 * Date:2022/6/13
 * Time:20:05
 * author:dimple
 * 动态事件 实体类
 */
data class DynamicEventBean(
    val createTime: Long,
    val dynamicEventCopyWriting: String,
    val dynamicEventCost: String?,
    val dynamicEventCostType: Int?,
    val dynamicEventProfit: String?,
    val dynamicEventProfitType: Int?,
    val dynamicEventType: Int?,
    val id: Int,
    val operateUserId: Int?,
    val targetUserId: Int?,
    var timeTimeHead: String = "更多",
    var isshowTimeHead: Boolean = false
)