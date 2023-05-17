package com.sn.gameelectricity.data.model.bean

/**
 * Date:2022/6/9
 * Time:17:37
 * author:dimple
 * 做新手引导 实体类
 */
data class NewerGuideBean(
    val action: String,
    val content: String,
    val createTime: String,
    val eggNum: Int,
    val goldCoinNum: Int,
    val guideStageId: Int,
    val id: Int,
    val sysUserId: Int,
    val updateTime: String
)