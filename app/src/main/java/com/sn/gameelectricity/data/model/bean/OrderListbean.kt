package com.sn.gameelectricity.data.model.bean

/**
 * Date:2022/5/30
 * Time:16:02
 * author:dimple
 * 订单列表 实体类
 */
data class OrderListbean(
    val payloadGoodsListVO: PayloadGoodsListVO,
    val payloadOrderSimpleInfoVO: PayloadOrderSimpleInfoVO
)

data class PayloadGoodsListVO(
    val defaultGoldCoinMax: Int,
    val defaultPrice: Double,
    val defaultScore: Int,
    val desireType: Int,
    val discountPrice: Double,
    val goldCoinPrice: Int,
    val goodsName: String,
    val groupSuccessPrice: Int,
    val icon: String,
    val id: Int,
    val patternName: String,
    val services: String,
    val topicId: Int
)

data class PayloadOrderSimpleInfoVO(
    val avatarUrlList: List<String>,
    val discountConsume: Int,
    val factAmt: Double,
    val groupAssistStatus: Int,
    val deliveryNo: String,
    val id: Int,
    val orderNo: String,
    val orderNumber: Int,
    val orderType: Int,//订单类型 0.普通订单 1.积分换购 2.金币抵扣 3.邀请半价 4.每日必抢 5.扭蛋 6.心愿购 7.活动赠与
    val payEndTime: Long,
    val currentTime: Long,
    val payStatus: Int,
    val status: Int,
    val groupUserNum: Int,
    val resultType: Int//	1-回收 2-提货
)