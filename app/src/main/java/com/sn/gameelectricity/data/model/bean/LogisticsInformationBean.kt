package com.sn.gameelectricity.data.model.bean

/**
 * Date:2022/6/1
 * Time:14:30
 * author:dimple
 */
data class LogisticsInformationBean(
    val contactPhone: String,
    val deliveryCompanyCode: String,
    val deliveryCompanyIcon: String,
    val deliveryCompanyName: String,
    val deliveryNo: String,
    val id: Int,
    val orderNo: String,
    val status: Int,
    val traceVOList: List<TraceVO>
)

data class TraceVO(
    val acceptStation: String,
    val acceptTime: String,
    val action: Int?,
    val location: String,
    var ishowaction: Boolean = false,
    var actionname: String = ""
)