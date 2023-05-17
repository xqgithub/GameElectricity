package com.sn.gameelectricity.data.model.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class TopicListResponse(
    var id: Int,
    var topicName: String,
    var sort: Int
) : Parcelable
