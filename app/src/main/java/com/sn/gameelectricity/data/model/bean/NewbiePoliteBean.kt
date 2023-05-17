package com.sn.gameelectricity.data.model.bean

/**
 * Date:2022/6/9
 * Time:9:19
 * author:dimple
 * 新人有礼活动 实体类
 */
data class NewbiePoliteBean(
    val createTime: String,
    val exchangeGoodsIcon: String,
    val exchangeGoodsId: Int,
    val exchangeGoodsName: String,
    val id: Int,
    val limitNum: Int,
    val payNum: Int,
    val payType: Int,
    val status: Int,
    val sysUserId: Int,
    val sysUserName: Any,
    val totalNum: Int,
    val updateTime: String,
    val usedNum: Int,
    val validlyTimeEnd: String,
    val validlyTimeStart: String
)