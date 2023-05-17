package com.sn.gameelectricity.data.model.bean

/**
 * Date:2022/5/30
 * Time:9:37
 * author:dimple
 * 个人中心用户信息 实体类
 */
data class PersonalInfo(
    val accountStatus: Int,
    val avatarUrl: String,
    val email: String,
    val gender: Int,
    val goldCoin: Int,
    val guideStageId: Int,
    val idCard: String,
    val mobile: String,
    val money: Int,
    val nickName: String,
    val realName: String,
    val score: Int,
    val token: String,
    val userCode: String,
    val userId: Int,
    val birthday: String,
    var boolGuide: Boolean,
    var boolNewUserActivityExchange: Boolean,
    var iv: String,
    var key: String,
    var loadingUrl: String,
    var userRightsCouponId: Int
)