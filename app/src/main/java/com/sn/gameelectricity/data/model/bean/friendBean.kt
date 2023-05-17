package com.sn.gameelectricity.data.model.bean

/**
 * Date:2022/6/14
 * Time:9:48
 * author:dimple
 * 好友列表 实体类
 */
data class friendBean(
    val contribute: Int?,
    val payloadUserFriendsVOList: List<PayloadUserFriendsVO>,
    val userId: Int?
)

data class PayloadUserFriendsVO(
    val avatarUrl: String?,
    var boolReceiveReward: Int?,
    val contribute: Int?,
    val id: Int?,
    val nickName: String?,
    val requestRewardInfoVO: RequestRewardInfoVO,
    val userId: Int?
)