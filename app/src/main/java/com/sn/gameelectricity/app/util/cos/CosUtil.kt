package com.sn.gameelectricity.app.util.cos

import android.content.Context
import android.net.Uri
import android.util.Log
import com.tencent.cos.xml.CosXmlServiceConfig
import com.tencent.cos.xml.CosXmlSimpleService
import com.tencent.cos.xml.transfer.COSXMLUploadTask
import com.tencent.cos.xml.transfer.TransferConfig
import com.tencent.cos.xml.transfer.TransferManager
import com.tencent.qcloud.core.auth.QCloudCredentialProvider
import com.tencent.qcloud.core.auth.SessionCredentialProvider
import com.tencent.qcloud.core.auth.SessionQCloudCredentials
import com.tencent.qcloud.core.auth.ShortTimeCredentialProvider

/**
 * Date:2022/6/6
 * Time:15:50
 * author:dimple
 */
class CosUtil(context: Context) {
    private var transferManager: TransferManager? = null

    constructor(
        context: Context,
        secretId: String,
        secretKey: String,
        sessionToken: String,
        beginTime: Long,
        expiredTime: Long,
        region: String = "ap-guangzhou"
    ) : this(context) {
        // keyDuration 为请求中的密钥有效期，单位为秒
//        val myCredentialProvider: QCloudCredentialProvider =
//            ShortTimeCredentialProvider(secretId, secretKey, 300)

        val myCredentialProvider: QCloudCredentialProvider =
            MyCredentialProvider(secretId, secretKey, sessionToken, beginTime, expiredTime)

        // 创建 CosXmlServiceConfig 对象，根据需要修改默认的配置参数
        // 存储桶所在地域简称，例如广州地区是 ap-guangzhou
        val serviceConfig = CosXmlServiceConfig.Builder()
            .setRegion(region)
            .isHttps(true) // 使用 HTTPS 请求, 默认为 HTTP 请求
            .builder()
        // 初始化 COS Service，获取实例
        val cosXmlService = CosXmlSimpleService(
            context,
            serviceConfig, myCredentialProvider
        )
        // 初始化 TransferConfig，这里使用默认配置，如果需要定制，请参考 SDK 接口文档
        val transferConfig = TransferConfig.Builder().build()
        // 初始化 TransferManager
        transferManager = TransferManager(
            cosXmlService,
            transferConfig
        )
    }

    //    public COSXMLUploadTask uploadGroupAvatar(Uri uri) {
    //        String bucket = "aifun-1304083978"; //存储桶，格式：BucketName-APPID
    //        String cosPath = "" + UserConfigCenter.getInstance().getClientUserUin() + TimeUtils.getNowMills(); //对象在存储桶中的位置标识符，即称对象键
    //        COSXMLUploadTask cosxmlUploadTask = transferManager.upload(bucket, cosPath, uri, null);
    //        return cosxmlUploadTask;
    //    }
    /**
     * 上传头像
     */
    fun uploadAvatar(uri: Uri, cosPath: String, bucket: String): COSXMLUploadTask {
//        String cosPath = "" + UserConfigCenter.getInstance().getClientUserUin() + uri; //对象在存储桶中的位置标识符，即称对象键
//        val bucket = "pictrue01-1304083978" //存储桶，格式：BucketName-APPID
        return transferManager!!.upload(bucket, cosPath, uri, null)
    }
}