package com.sn.gameelectricity.data.model.bean

/**
 * Date:2022/7/7
 * Time:14:19
 * author:dimple
 * 排行榜 实体类
 */
data class LeaderBoardBean(
    val avatar: String,
    val nickName: String,
    val num: Int,
    val rank: Int,
    val score: Double,
    val userId: Long
)