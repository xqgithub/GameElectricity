package com.sn.gameelectricity.data.model.bean

/**
 * Date:2022/5/26
 * Time:11:20
 * author:dimple
 * 收货地址
 */
data class DeliveryAddressBean(
    val address: String,
    val addresseeName: String,
    val areaCode: Int,
    val areaName: String,
    val cityCode: Int,
    val cityName: String,
    val id: Int,
    val phoneNumber: String,
    val provinceCode: Int,
    val provinceName: String,
    val streetCode: Int,
    val streetName: String,
    val type: Int,
    val userId: Int
)