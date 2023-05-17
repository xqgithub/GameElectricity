package com.sn.gameelectricity.data.model.bean

/**
 * Date:2022/4/8
 * Time:10:21
 * author:dimple
 * 我的关注实体类
 */
data class FocusListBean(
    val userId: Long,
    val avatar: String,
    val nickname: String,
    var focusState: Int//1 = 未关注，2 = 已关注，3 = 相互关注
)
