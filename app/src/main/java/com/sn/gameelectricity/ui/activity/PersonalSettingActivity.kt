package com.sn.gameelectricity.ui.activity

import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.azhon.appupdate.manager.DownloadManager
import com.blankj.utilcode.util.AppUtils
import com.sn.gameelectricity.R
import com.sn.gameelectricity.app.base.BaseActivity1
import com.sn.gameelectricity.app.network.TokenInterceptorHelper
import com.sn.gameelectricity.app.util.CacheUtil.setIsLogin
import com.sn.gameelectricity.app.util.LocalUtil
import com.sn.gameelectricity.app.weight.dialog.AppUpdateDialog
import com.sn.gameelectricity.app.weight.dialog.SelectionTooltipDialog
import com.sn.gameelectricity.data.model.bean.VersionResponse
import com.sn.gameelectricity.databinding.ActivityPersonalSettingBinding
import com.sn.gameelectricity.ui.activity.login.LoginActivity
import com.sn.gameelectricity.ui.activity.web.WebActivity
import com.sn.gameelectricity.viewmodel.PersonalSettingViewModel
import com.sn.gameelectricity.viewmodel.request.RequestMainViewModel
import me.hgj.jetpackmvvm.common.ConfigConstants
import me.hgj.jetpackmvvm.ext.lifecycle.KtxActivityManger
import me.hgj.jetpackmvvm.util.PublicPracticalMethodFromKT
import me.hgj.jetpackmvvm.util.ScreenTools
import me.hgj.jetpackmvvm.util.ToastUtil
import singleClick

/**
 * Date:2022/5/16
 * Time:17:37
 * author:dimple
 * 个人设置页面
 */
class PersonalSettingActivity :
    BaseActivity1<PersonalSettingViewModel, ActivityPersonalSettingBinding>() {

    private val requestMainViewModel: RequestMainViewModel by viewModels()

    private lateinit var localUtil: LocalUtil

    val appUpdateDialog by lazy {
        AppUpdateDialog(this)
    }

    private var versionResponse: VersionResponse? = null

    private var manager: DownloadManager? = null


    override fun initView(savedInstanceState: Bundle?) {
        initData()

        mViewBind.apply {

            PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
                tvLogout,
                -1,
                "",
                ScreenTools.getInstance().dp2px(this@PersonalSettingActivity, 24f, true).toFloat(),
                ScreenTools.getInstance().dp2px(this@PersonalSettingActivity, 24f, true).toFloat(),
                ScreenTools.getInstance().dp2px(this@PersonalSettingActivity, 24f, true).toFloat(),
                ScreenTools.getInstance().dp2px(this@PersonalSettingActivity, 24f, true).toFloat(),
                null,
                "#ECF0F8"
            )

            PublicPracticalMethodFromKT.ppmfKT.setDynamicShapeRectangle(
                tvNewVersion,
                -1,
                "",
                ScreenTools.getInstance().dp2px(this@PersonalSettingActivity, 10f, true).toFloat(),
                ScreenTools.getInstance().dp2px(this@PersonalSettingActivity, 10f, true).toFloat(),
                ScreenTools.getInstance().dp2px(this@PersonalSettingActivity, 10f, true).toFloat(),
                ScreenTools.getInstance().dp2px(this@PersonalSettingActivity, 10f, true).toFloat(),
                GradientDrawable.Orientation.LEFT_RIGHT,
                "#F19B3F", "#ED7A57"
            )

            //缓存的值
            tvClearCacheValue.text = localUtil.cacheSize.toString()

            ivBack.singleClick {
                finish()
            }


            clPersonalInformation.singleClick {
                PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                    this@PersonalSettingActivity,
                    PersonalInfoEditActivity::class.java,
                    null,
                    false
                )
            }

            clAboutUs.singleClick {
                PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                    this@PersonalSettingActivity,
                    AboutUSActivity::class.java,
                    null,
                    false
                )
            }

            clClearCache.singleClick {
                with(SelectionTooltipDialog(this@PersonalSettingActivity)) {
                    settitle(View.GONE)
                    setContent("是否清理缓存？")
                    setCancel {
                        dismissDialog()
                    }
                    setSure {
                        localUtil.clearAppCache()
                        tvClearCacheValue.text = localUtil.cacheSize.toString()
                        dismissDialog()
                    }
                    show()
                }
            }

            clAccountSecurity.singleClick {
                PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                    this@PersonalSettingActivity,
                    AccountSecurityActivity::class.java, null, false,
                    -1, -1,
                    Intent.FLAG_ACTIVITY_CLEAR_TOP
                )
            }


            clPrivacyPolicy.singleClick {
//                with(Bundle()) {
//                    putString(
//                        ConfigConstants.CONSTANT.WEBVIEWURL,
//                        ConfigConstants.VARIABLE.URL_PRIVACY_POLICY
//                    )
//                    putString(
//                        ConfigConstants.CONSTANT.WEBVIEW_PAGE_ID,
//                        ConfigConstants.CONSTANT.WEBVIEW_PRIVACY_POLICY
//                    )
//                    PublicPracticalMethodFromKT.ppmfKT.intentToJump(
//                        this@PersonalSettingActivity,
//                        UniversalWebViewActivity::class.java, this, false
//                    )
//                }
                with(Bundle()) {
                    putString(
                        "url",
                        ConfigConstants.VARIABLE.URL_PRIVACY_POLICY
                    )
                    putString(
                        "title",
                        "隐私政策"
                    )
                    PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                        this@PersonalSettingActivity,
                        WebActivity::class.java, this, false
                    )
                }
            }

            clUserAgreement.singleClick {
//                with(Bundle()) {
//                    putString(
//                        ConfigConstants.CONSTANT.WEBVIEWURL,
//                        ConfigConstants.VARIABLE.URL_USER_AGREEMENT
//                    )
//                    putString(
//                        ConfigConstants.CONSTANT.WEBVIEW_PAGE_ID,
//                        ConfigConstants.CONSTANT.WEBVIEW_USER_AGREEMENT
//                    )
//                    PublicPracticalMethodFromKT.ppmfKT.intentToJump(
//                        this@PersonalSettingActivity,
//                        UniversalWebViewActivity::class.java, this, false
//                    )
//                }
                with(Bundle()) {
                    putString(
                        "url",
                        ConfigConstants.VARIABLE.URL_USER_AGREEMENT
                    )
                    putString(
                        "title",
                        "用户协议"
                    )
                    PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                        this@PersonalSettingActivity,
                        WebActivity::class.java, this, false
                    )
                }
            }

            tvLogout.singleClick {
                with(SelectionTooltipDialog(this@PersonalSettingActivity)) {
                    settitle(View.GONE)
                    setContent("确认退出当前登录吗？")
                    setCancel {
                        dismissDialog()
                    }
                    setSure {
                        mViewModel.userLoginOut { isSuccess, msg ->
                            if (isSuccess) {
                                setIsLogin(false)
                                TokenInterceptorHelper.getInstance()
                                    .saveAppToken(context, "")
                                KtxActivityManger.finishActivity(MainActivity::class.java)
                                PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                                    this@PersonalSettingActivity,
                                    LoginActivity::class.java, null, true
                                )
                            } else {
                                ToastUtil.showCenter(msg)
                            }
                        }
                    }
                    show()
                }
            }


            clCheckUpdates.singleClick {
                versionResponse?.let {
                    appUpdateDialog.setCancelVisibility(it.updateType)
                    appUpdateDialog.setTextVersion(it.version)
                    appUpdateDialog.setTextContent(if (PublicPracticalMethodFromKT.ppmfKT.isBlank(it.content)) "" else it.content as String)
                    appUpdateDialog.onClickSure {
                        if (it.isNeedUpdate) {
                            manager = DownloadManager.Builder(this@PersonalSettingActivity).run {
                                apkUrl(it.downloadUrl)
                                apkName("app-release.apk")
                                smallIcon(R.mipmap.ic_launcher)
                                build()
                            }
                            manager!!.download()
                        }
                    }
                    appUpdateDialog.show()
                } ?: let {
                    ToastUtil.showCenter("当前版本为最新版本")
                }
            }

            clCollectionChecklist.singleClick {
                with(Bundle()) {
                    putString(
                        "url",
                        ConfigConstants.VARIABLE.URL_COLLECTION_CHECKLIST
                    )
                    putString(
                        "title",
                        "个人信息收集清单"
                    )
                    PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                        this@PersonalSettingActivity,
                        WebActivity::class.java, this, false
                    )
                }
            }
            clThirdPartySDK.singleClick {
                with(Bundle()) {
                    putString(
                        "url",
                        ConfigConstants.VARIABLE.URL_THIRDPARTY_SDK
                    )
                    putString(
                        "title",
                        "第三方SDK目录"
                    )
                    PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                        this@PersonalSettingActivity,
                        WebActivity::class.java, this, false
                    )
                }
            }
            clAppPermissions.singleClick {
                with(Bundle()) {
                    putString(
                        "url",
                        ConfigConstants.VARIABLE.URL_APP_PERMISSIONS
                    )
                    putString(
                        "title",
                        "应用权限说明"
                    )
                    PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                        this@PersonalSettingActivity,
                        WebActivity::class.java, this, false
                    )
                }
            }

            clFeedback.singleClick {
                PublicPracticalMethodFromKT.ppmfKT.intentToJump(
                    this@PersonalSettingActivity,
                    FeedBackActivity::class.java, null, false
                )
            }
        }
    }


    private fun initData() {
        localUtil = LocalUtil(this@PersonalSettingActivity)
        requestMainViewModel.getVersion(AppUtils.getAppVersionName())
        requestMainViewModel.versionLiveData.observe(this@PersonalSettingActivity) { bean ->
            if (bean.isNeedUpdate) {
                mViewBind.tvNewVersion.visibility = View.VISIBLE
                versionResponse = bean
            }
        }
    }

    //跳向网页
    private fun jumpToWeb(url: String, title: String, keyPage: Int) {
        val intent = Intent(this@PersonalSettingActivity, WebActivity::class.java)
        intent.putExtra("url", url)
        intent.putExtra("title", title)
        startActivity(intent)
    }

}