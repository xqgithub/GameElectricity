package com.sn.gameelectricity.data.model.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class UserCouponResponse(
    var goldCouponNum: Int,
    var silverCouponNum: Int,
    var bronzeCouponNum: Int
) : Parcelable
