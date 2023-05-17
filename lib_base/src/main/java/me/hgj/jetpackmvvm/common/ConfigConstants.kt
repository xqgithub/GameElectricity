package me.hgj.jetpackmvvm.common

/**
 * Date:2022/5/7
 * Time:13:46
 * author:dimple
 * 全局【常量】或【变量】配置说明
 */
object ConfigConstants {

    /** 常量 **/
    object CONSTANT {
        /** TAG名称 **/
        const val TAG_ALL = "gameElectricity"

        /** 错误日志名称 **/
        const val ERROR_JOURNAL = "AGameElectricityErrorLog"

        /** 接口日志名称 **/
        const val interface_log = "interface_log"

        /** UI设计标准 **/
        //pad的设计图宽
        const val PAD_WIDTH = 1024

        //pad的设计图高
        const val PAD_HEIGHT = 768

        //phone的设计图宽
        const val PHONE_WIDTH = 375

        //phone的设计图高
        const val PHONE_HEIGHT = 812

        /** okhttp配置 **/
        //okhttp 写超时
        const val OKHTTP_WRITE_TIME_OUT = 15L

        //okhttp 连接超时
        const val OKHTTP_CONNECT_TIME_OUT = 15L

        //okhttp 读取超时
        const val OKHTTP_READ_TIME_OUT = 15L


        /** 建筑物任务序号 **/
        const val MAKEUPS_TASKNUMBER = "makeups_tasknumber"
        const val LEISURE_TASKNUMBER = "leisure_tasknumber"
        const val MOTHERBABY_TASKNUMBER = "motherbaby_tasknumber"
        const val DIGITAL_TASKNUMBER = "digital_tasknumber"

        /** ActivityResultCode **/
        const val RESULTCODE_NICKNAME = 0x9999
        const val RESULTCODE_PHONEALBUM = 0x4001
        const val RESULTCODE_PHOTOGRAPH = 0x4002


        /** webView 页面标识 **/
        //页面标识
        const val WEBVIEW_PAGE_ID = "WEBVIEW_PAGE_ID"

        //隐私政策
        const val WEBVIEW_PRIVACY_POLICY = "WEBVIEW_PRIVACY_POLICY"

        //用户协议
        const val WEBVIEW_USER_AGREEMENT = "WEBVIEW_USER_AGREEMENT"

        //页面地址标识
        const val WEBVIEWURL = "WEBVIEWURL"
    }

    /** 变量 **/
    object VARIABLE {
        //经度
        var longitude = ""

        //纬度
        var latitude = ""

        //修改手机号，旧验证码
        var old_verification_code = ""

        //隐私政策
        var URL_PRIVACY_POLICY = "https://itest.aifun.com/#/Privacy"

        //用户协议
        var URL_USER_AGREEMENT = "https://itest.aifun.com/#/Useragreement"

        //个人信息收集清单
        var URL_COLLECTION_CHECKLIST = "https://itest.aifun.com/#/PersonalInformation"

        //AIFUN接入第三方SDK目录
        var URL_THIRDPARTY_SDK = "https://itest.aifun.com/#/SDKdirectory"

        //AIFUN接入应用权限说明
        var URL_APP_PERMISSIONS = "https://itest.aifun.com/#/permissionDescription"

        //排行榜规则
        var URL_LEADERBOARD_RULE = "https://itest.aifun.com/#/Ranking"

        //当前时间
        var cur_time = 1653393630L

        //截止时间
        var end_time = 1653393690L

        //排行榜第一用户头像
        var leaderboard_first_avatar = "leaderboard_first_avatar"
    }

}