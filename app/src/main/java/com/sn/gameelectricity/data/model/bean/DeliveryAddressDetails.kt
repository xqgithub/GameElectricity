package com.sn.gameelectricity.data.model.bean

/**
 * Date:2022/5/28
 * Time:16:45
 * author:dimple
 * 收货地址详情
 */
data class DeliveryAddressDetails(
    val address: String,
    val addresseeName: String,
    val areaCode: Int,
    val areaName: String,
    val cityCode: Int,
    val cityName: String,
    val createTime: String,
    val id: Int,
    val phoneNumber: String,
    val provinceCode: Int,
    val provinceName: String,
    val streetCode: Int,
    val streetName: String,
    val type: Int,
    val userId: Int
)