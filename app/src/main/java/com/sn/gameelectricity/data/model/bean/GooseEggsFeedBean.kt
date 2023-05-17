package com.sn.gameelectricity.data.model.bean

import me.hgj.jetpackmvvm.util.PublicPracticalMethodFromKT

/**
 * Date:2022/6/11
 * Time:9:17
 * author:dimple
 * 鹅蛋和饲料 实体类
 */
data class GooseEggsFeedBean(
    var addFeedNum: Int?,
    var eggNum: Int?,
    var endTime: Long?,
    var feedPoolCapacityFlag: Int?,
    var id: Int?,
    var layEggFrequency: Long?,
    var roleId: Int?,
    var startTime: Long?,
    var totalFeedNum: Int?,
    var userId: Int?,
    var maxEggNum: Int?
)