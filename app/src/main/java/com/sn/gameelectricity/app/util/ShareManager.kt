package com.sn.gameelectricity.app.util

import android.graphics.Bitmap
import cn.jiguang.share.android.api.JShareInterface
import cn.jiguang.share.android.api.PlatActionListener
import cn.jiguang.share.android.api.Platform
import cn.jiguang.share.android.api.ShareParams
import cn.jiguang.share.qqmodel.QQ
import cn.jiguang.share.wechat.Wechat
import cn.jiguang.share.wechat.WechatFavorite
import cn.jiguang.share.wechat.WechatMoments
import cn.jiguang.share.weibo.SinaWeibo
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.sn.gameelectricity.R
import com.sn.gameelectricity.app.App
import me.hgj.jetpackmvvm.util.PublicPracticalMethodFromKT
import java.io.File
import java.util.*


/**
 * 分享管理器
 */
class ShareManager private constructor() {

    companion object {
        val shareManager: ShareManager by lazy {
            ShareManager()
        }
    }

    /**
     * 分享图片到微信好友
     */
    fun shareImage2WxFriend(file: File) {
        if (IsInstallApp.isWeixinAvilible()) {
            val shareParams = ShareParams()
            shareParams.shareType = Platform.SHARE_IMAGE
            shareParams.imagePath = file.getAbsolutePath()

            JShareInterface.share(Wechat.Name, shareParams, mShareListener)
        } else {
            ToastUtils.showShort("请安装微信！")
        }

    }

    /**
     * 分享链接到微信好友
     */
    fun shareWebpage2WxFriend(
        share_title: String,
        share_text: String,
        share_url: String,
        file: Bitmap
    ) {
        if (IsInstallApp.isWeixinAvilible()) {
            val shareParams = ShareParams()
            shareParams.title = share_title
            shareParams.text = share_text
            shareParams.shareType = Platform.SHARE_WEBPAGE
            shareParams.url = share_url //必须
            shareParams.imageData = file

            JShareInterface.share(Wechat.Name, shareParams, mShareListener)
        } else {
            ToastUtils.showShort("请安装微信！")
        }

    }

    /**
     * 分享链接到微信朋友圈
     */
    fun shareWebpage2WxMoments(
        share_title: String,
        share_text: String,
        share_url: String,
        file: Bitmap
    ) {
        if (IsInstallApp.isWeixinAvilible()) {
            val shareParams = ShareParams()
            shareParams.title = share_title
            shareParams.text = share_text
            shareParams.shareType = Platform.SHARE_WEBPAGE
            shareParams.url = share_url //必须
            shareParams.imageData = file

            JShareInterface.share(WechatMoments.Name, shareParams, mShareListener)
        } else {
            ToastUtils.showShort("请安装微信！")
        }

    }

    /**
     * 分享图片到微信朋友圈
     */
    fun shareImage2WxMoments(file: File) {
        if (IsInstallApp.isWeixinAvilible()) {
            val shareParams = ShareParams()
            shareParams.shareType = Platform.SHARE_IMAGE
            shareParams.imagePath = file.getAbsolutePath()

            JShareInterface.share(WechatMoments.Name, shareParams, mShareListener)
        } else {
            ToastUtils.showShort("请安装微信！")
        }

    }


    /**
     * 分享图片到QQ
     */
    fun shareImage2QQ(file: File) {
        if (IsInstallApp.isQQClientAvaolable()) {
            val shareParams = ShareParams()
            shareParams.shareType = Platform.SHARE_IMAGE
            shareParams.imagePath = file.getAbsolutePath()
            JShareInterface.share(QQ.Name, shareParams, mShareListener)
        } else {
            ToastUtils.showShort("请安装QQ！")
        }

    }

    /**
     * 分享链接到QQ
     */
    fun shareWebpage2QQ(
        share_url: String,
        share_title: String,
        share_text: String,
    ) {
        if (IsInstallApp.isQQClientAvaolable()) {
            val shareParams = ShareParams()
            shareParams.shareType = Platform.SHARE_WEBPAGE
            shareParams.title = share_title
            shareParams.text = share_text
            shareParams.url = share_url
            shareParams.imageUrl =
                "https://gmall-1304083978.cos.ap-guangzhou.myqcloud.com/app/android/20220630-100849.png"

//            PublicPracticalMethodFromKT.ppmfKT.samplingRateCompress(
//                mContext,
//                R.drawable.ge_friends_share
//            )

            JShareInterface.share(QQ.Name, shareParams, mShareListener)
        } else {
            ToastUtils.showShort("请安装QQ！")
        }

    }

    /**
     * 分享链接到QQ
     */
    fun shareWebpage2QQImg(
        share_url: String,
        share_title: String,
        share_text: String,
        imageUrl: String
    ) {
        if (IsInstallApp.isQQClientAvaolable()) {
            val shareParams = ShareParams()
            shareParams.shareType = Platform.SHARE_WEBPAGE
            shareParams.title = share_title
            shareParams.text = share_text
            shareParams.imageUrl = imageUrl
            shareParams.url = share_url
            JShareInterface.share(QQ.Name, shareParams, mShareListener)
        } else {
            ToastUtils.showShort("请安装QQ！")
        }

    }

    /**
     * 分享图片到微博
     */
    fun shareImage2Weibo(text: String, file: File) {
        if (IsInstallApp.isWeiBoInstalled()) {
            val shareParams = ShareParams()
            shareParams.shareType = Platform.SHARE_IMAGE
            shareParams.text = text
            shareParams.imagePath = file.getAbsolutePath()
            JShareInterface.share(SinaWeibo.Name, shareParams, mShareListener)
        } else {
            ToastUtils.showShort("请安装微博！")
        }
    }

    /**
     * 分享链接到微博
     */
    fun shareWebpage2Weibo(share_url: String, text: String) {
        if (IsInstallApp.isWeiBoInstalled()) {
            val shareParams = ShareParams()
            shareParams.shareType = Platform.SHARE_WEBPAGE
            shareParams.text = text
            shareParams.url = share_url
            JShareInterface.share(SinaWeibo.Name, shareParams, mShareListener)
        } else {
            ToastUtils.showShort("请安装微博！")
        }
    }

    private val mShareListener: PlatActionListener = object : PlatActionListener {
        override fun onComplete(platform: Platform, action: Int, data: HashMap<String, Any>) {
//            ToastUtils.showShort("分享成功")
        }

        override fun onError(platform: Platform, action: Int, errorCode: Int, error: Throwable) {
            LogUtils.e(error)
            LogUtils.e(errorCode)
//            ToastUtils.showShort("分享失败")
        }

        override fun onCancel(platform: Platform, action: Int) {
//            ToastUtils.showShort("分享取消")
        }
    }

}