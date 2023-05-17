package com.sn.gameelectricity.data.model.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class GoodsGetDiscountResponse(
    var goldCoinPrice: Float,
    var goldNum: Int,
    var score: Int
) : Parcelable
