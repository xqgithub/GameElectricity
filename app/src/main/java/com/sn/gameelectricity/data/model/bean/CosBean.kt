package com.sn.gameelectricity.data.model.bean

/**
 * Date:2022/6/6
 * Time:15:30
 * author:dimple
 * cos实体类
 */
data class CosBean(
    val directoryList: List<String>,
    val expireTime: Long,
    val sessionToken: String,
    val startTime: Long,
    val tmpSecretId: String,
    val tmpSecretKey: String,
    val bucketName: String,
    val region: String
)