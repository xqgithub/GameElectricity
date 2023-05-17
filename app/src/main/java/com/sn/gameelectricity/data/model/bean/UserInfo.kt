package com.sn.gameelectricity.data.model.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * 描述　: 账户信息
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class UserInfo(
    var avatarUrl: String,
    var email: String,
    var gender: Int,
    var guideStageId: Int,
    var goldCoin: Int,
    var score: Int,
    var idCard: String,
    var mobile: String,
    var nickName: String,
    var realName: String,
    var token: String,
    var userId: Int,
    var birthday: String,
    var boolGuide: Boolean,
    var boolNewUserActivityExchange: Boolean,
    var iv: String,
    var userCode: String,
    var key: String,
    var loadingUrl: String,
    var userRightsCouponId: Int
) : Parcelable
