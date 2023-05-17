package me.hgj.jetpackmvvm.util;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.util.ImageUtils;
import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.CosXmlSimpleService;
import com.tencent.cos.xml.model.object.PutObjectRequest;
import com.tencent.cos.xml.transfer.COSXMLUploadTask;
import com.tencent.cos.xml.transfer.TransferConfig;
import com.tencent.cos.xml.transfer.TransferManager;
import com.tencent.qcloud.core.auth.QCloudCredentialProvider;
import com.tencent.qcloud.core.auth.ShortTimeCredentialProvider;


public class Cos {
    private TransferManager transferManager;
    //    public static final String BIG_PATH = "https://pictrue01-1304083978.cos.ap-guangzhou.myqcloud.com/";
    public static final String BIG_PATH = "https://pictrue01-1304083978.file.myqcloud.com/";

    //    public static final String THUMBNAIL_PATH = "https://pictrue02-1304083978.cos.ap-guangzhou.myqcloud.com/";
    public static final String THUMBNAIL_PATH = "https://pictrue02-1304083978.file.myqcloud.com/";

    //    public static final String VIDEO_URL = "https://trends-video-1304083978.cos.ap-guangzhou.myqcloud.com/";
    public static final String VIDEO_URL = "https://trends-video-1304083978.file.myqcloud.com/";

    public Cos(Context context) {
        String secretId = "AKIDsY2LhrZMn22rRtTlrkMEvJyJQFEXKR2U"; //永久密钥 secretId
        String secretKey = "GG1gDh21ndLp95A7Yae07zccIFxQfhaC"; //永久密钥 secretKey

        // keyDuration 为请求中的密钥有效期，单位为秒
        QCloudCredentialProvider myCredentialProvider =
                new ShortTimeCredentialProvider(secretId, secretKey, 300);
        // 创建 CosXmlServiceConfig 对象，根据需要修改默认的配置参数
        // 存储桶所在地域简称，例如广州地区是 ap-guangzhou
        String region = "ap-guangzhou";
        CosXmlServiceConfig serviceConfig = new CosXmlServiceConfig.Builder()
                .setRegion(region)
                .isHttps(true) // 使用 HTTPS 请求, 默认为 HTTP 请求
                .builder();

        // 初始化 COS Service，获取实例
        CosXmlSimpleService cosXmlService = new CosXmlSimpleService(context,
                serviceConfig, myCredentialProvider);
        // 初始化 TransferConfig，这里使用默认配置，如果需要定制，请参考 SDK 接口文档
        TransferConfig transferConfig = new TransferConfig.Builder().build();
        // 初始化 TransferManager
        transferManager = new TransferManager(cosXmlService,
                transferConfig);
    }

//    public COSXMLUploadTask uploadGroupAvatar(Uri uri) {
//        String bucket = "aifun-1304083978"; //存储桶，格式：BucketName-APPID
//        String cosPath = "" + UserConfigCenter.getInstance().getClientUserUin() + TimeUtils.getNowMills(); //对象在存储桶中的位置标识符，即称对象键
//        COSXMLUploadTask cosxmlUploadTask = transferManager.upload(bucket, cosPath, uri, null);
//        return cosxmlUploadTask;
//    }

    /**
     * 上传动态图片
     *
     * @param uri 图片地址
     * @return COSXMLUploadTask
     */
    public COSXMLUploadTask uploadMomentBigPic(Uri uri, String cosPath) {
//        String cosPath = "" + UserConfigCenter.getInstance().getClientUserUin() + uri; //对象在存储桶中的位置标识符，即称对象键
        Log.d("COSXMLUploadTask", "uploadMomentBigPic: " + cosPath);
        String bucket = "pictrue01-1304083978";//存储桶，格式：BucketName-APPID
        COSXMLUploadTask cosxmlUploadTask = transferManager.upload(bucket, cosPath, uri, null);

        return cosxmlUploadTask;
    }

    public COSXMLUploadTask uploadMomentThumbnailPic(Uri uri, String cosPath) {
//        String cosPath = "" + UserConfigCenter.getInstance().getClientUserUin() + uri; //对象在存储桶中的位置标识符，即称对象键
        Log.d("COSXMLUploadTask", "uploadMomentThumbnailPic: " + cosPath);
        String bucket = "pictrue02-1304083978";//存储桶，格式：BucketName-APPID
        COSXMLUploadTask cosxmlUploadTask = transferManager.upload(bucket, cosPath, uri, null);
        return cosxmlUploadTask;
    }

    public COSXMLUploadTask uploadMomentVideo(Uri uri, String cosPath) {
        Log.d("COSXMLUploadTask", "uploadMomentVideo: " + cosPath);
        String bucket = "trends-video-1304083978";//存储桶，格式：BucketName-APPID
        PutObjectRequest objectRequest = new PutObjectRequest(bucket, cosPath, uri);
        return transferManager.upload(objectRequest, null);
    }

    public COSXMLUploadTask uploadMomentVideoThumbnailPic(View view, String cosPath) {
//        String cosPath = "" + UserConfigCenter.getInstance().getClientUserUin() + uri; //对象在存储桶中的位置标识符，即称对象键
        Log.d("COSXMLUploadTask", "uploadMomentThumbnailPic: " + cosPath);
        String bucket = "pictrue02-1304083978";//存储桶，格式：BucketName-APPID
        COSXMLUploadTask cosxmlUploadTask = transferManager.upload(bucket, cosPath, ImageUtils.drawable2Bytes(((ImageView) view).getDrawable()));

        return cosxmlUploadTask;
    }

}
