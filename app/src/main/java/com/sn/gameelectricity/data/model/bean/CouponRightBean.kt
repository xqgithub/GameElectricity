package com.sn.gameelectricity.data.model.bean

/**
 * Date:2022/7/8
 * Time:16:00
 * author:dimple
 * 权益中心列表 实体类
 */
data class CouponRightBean(
    val goodsVOList: List<GoodsVO>,
    val total: Int,
    val totalGoodsNum: Int
)

data class GoodsVO(
    val defaultPrice: Int,
    val discountPrice: Int,
    val goodsIcon: String,
    val goodsId: Int,
    val goodsName: String,
    val goodsUrl: String,
    val id: Int,
    val lastExchangeTime: String,
    val number: Int,
    val postage: Int,
    val recoverScore: Int,
    val storyUrl: String,
    val supplierAddress: String,
    val supplierPhone: String,
    val userId: Int
)