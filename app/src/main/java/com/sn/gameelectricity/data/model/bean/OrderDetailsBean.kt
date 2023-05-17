package com.sn.gameelectricity.data.model.bean

/**
 * Date:2022/5/31
 * Time:14:09
 * author:dimple
 * 订单详情 实体类
 */
data class OrderDetailsBean(
    var groupUserNum: Int,
    val payloadGoodsInfoVO: PayloadGoodsInfoVO,
    val payloadGroupAssistMemberVOList: List<PayloadGroupAssistMemberVO>,
    val payloadOrderInfoVO: PayloadOrderInfoVO,
    val payloadUserDeliveryAddressVO: PayloadUserDeliveryAddressVO
)

data class PayloadGoodsInfoVO(
    val defaultGoldCoinMax: Int,
    val defaultPrice: Double,
    val defaultScore: Int,
    val delivery: Int,
    val desireType: Int,
    val discountPrice: Double,
    val goldCoinExchangeRate: Double,
    val goldCoinPrice: Double,
    val goodsDec: String,
    val goodsName: String,
    val goodsPicList: List<String>,
    val groupAssistVO: GroupAssistVO,
    val groupPriceOffRate: Double,
    val groupSuccessPrice: Double,
    val groupUserNum: Int,
    val icon: String,
    val id: Int,
    val mark: String,
    val maxBuyNum: Int,
    val patternName: String,
    val photoList: List<String>,
    val postage: Double,
    val publishTime: String,
    val publishUserId: Int,
    val rewardRate: Int,
    val services: String,
    val sort: Int,
    val status: Int,
    val supplierAddress: String,
    val supplierIcon: String,
    val supplierName: String,
    val supplierPhone: String,
    val supplierWebType: Int,
    val supplierWebUrl: String,
    val tap: String,
    val topicId: Int
)

data class PayloadGroupAssistMemberVO(
    var avatarUrl: String,
    var nickName: String,
    var userId: Int
)

data class PayloadOrderInfoVO(
    val addressId: Int,
    val contactPhone: String,
    val createTime: String,
    val currentTime: Long,
    val deliverTime: String,
    val deliveryNo: String,
    val discountAmt: Double,
    val discountConsume: Int,
    val discountType: Int,
    val distributionType: Int,
    val factAmt: Double,
    val goodsId: Int,
    val groupAssistId: Int,
    val id: Int,
    val orderAmt: Double,
    val orderNo: String,
    val orderNumber: Int,
    val orderType: Int,
    val payEndTime: Long,
    val payInfo: String,
    val payIp: String,
    val payStatus: Int,
    val payTime: String,
    val payType: String,
    val postageAmount: Double,
    val remark: String,
    val status: Int,
    val supplierAddress: String,
    val traceNo: String,
    val userId: Int,
    val resultType: Int?,//结果类型 1回收 2提货
    val recoverTime: String?,//回收时间
    val recoverScore: Int?,//回收积分
    val completionTime: String,//完成时间
)

data class PayloadUserDeliveryAddressVO(
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

data class GroupAssistVO(
    val endTime: String,
    val goodsIcon: String,
    val goodsId: Int,
    val goodsName: String,
    val groupCode: String,
    val groupMember: String,
    val groupMemberNum: Int,
    val id: Int,
    val inviteCode: String,
    val joinFlag: Boolean,
    val memberInfo: List<MemberInfo>,
    val ownerIcon: String,
    val orderId: Int,
    val ownerName: String,
    val patternId: Int,
    val requestUri: String,
    val startTime: String,
    val status: Int,
    val userId: Int
)

data class MemberInfo(
    val accountStatus: Int,
    val avatarUrl: String,
    val boolGuide: Boolean,
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
    val userId: Int
)