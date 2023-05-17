package com.sn.gameelectricity.app.util.cos

import com.tencent.qcloud.core.auth.BasicLifecycleCredentialProvider
import com.tencent.qcloud.core.auth.QCloudLifecycleCredentials
import com.tencent.qcloud.core.auth.SessionQCloudCredentials

/**
 * Date:2022/6/7
 * Time:10:36
 * author:dimple
 */
class MyCredentialProvider(
    val tmpSecretId: String,
    val tmpSecretKey: String,
    val sessionToken: String,
    val beginTime: Long,
    val expiredTime: Long
) : BasicLifecycleCredentialProvider() {

    override fun fetchNewCredentials(): QCloudLifecycleCredentials {
        return SessionQCloudCredentials(
            tmpSecretId,
            tmpSecretKey,
            sessionToken,
            beginTime,
            expiredTime
        )
    }
}