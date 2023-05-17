package com.sn.gameelectricity.viewmodel

import android.content.Context
import android.net.Uri
import com.blankj.utilcode.util.LogUtils
import com.google.gson.JsonObject
import com.luck.picture.lib.entity.LocalMedia
import com.sn.gameelectricity.app.network.ApiService
import com.sn.gameelectricity.app.network.HttpFactory
import com.sn.gameelectricity.app.util.CacheUtil
import com.sn.gameelectricity.app.util.cos.CosUtil
import com.sn.gameelectricity.data.model.bean.CosBean
import com.sn.gameelectricity.ui.activity.PersonalInfoEditActivity
import com.tencent.cos.xml.exception.CosXmlClientException
import com.tencent.cos.xml.exception.CosXmlServiceException
import com.tencent.cos.xml.listener.CosXmlResultListener
import com.tencent.cos.xml.model.CosXmlRequest
import com.tencent.cos.xml.model.CosXmlResult
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.common.ConfigConstants
import me.hgj.jetpackmvvm.ext.convert
import me.hgj.jetpackmvvm.network.NettServerException
import me.hgj.jetpackmvvm.util.ToastUtil
import me.hgj.jetpackmvvm.util.delayToDoSometing
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

/**
 * Date:2022/5/12
 * Time:15:49
 * author:dimple
 * 个人设置
 */
class PersonalSettingViewModel : BaseViewModel() {


    /**
     * 登出
     */
    fun userLoginOut(onCallBack: (isSuccess: Boolean, msg: String) -> Unit) {
        HttpFactory.getInstance().APINew(ApiService.BASE_URL).userLoginOut()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.code == 0) {
                    onCallBack(true, "")
                } else {
                    onCallBack(false, it.msg)
                }
            }, {
                onCallBack(false, "登出失败!")
            })
    }

    /**
     * 编辑用户
     */
    fun userEdit(
        type: PersonalInfoEditActivity.PersonalInfoEditType,
        param: Any,
        onCallBack: (isSuccess: Boolean, msg: String) -> Unit
    ) {
        val requestBody = JsonObject().let {
            when (type) {
                PersonalInfoEditActivity.PersonalInfoEditType.birthday -> {
                    it.addProperty("birthday", param as String)
                }
                PersonalInfoEditActivity.PersonalInfoEditType.gender -> {
                    it.addProperty("gender", param as Int)
                }
                PersonalInfoEditActivity.PersonalInfoEditType.nickName -> {
                    it.addProperty("nickName", param as String)
                }
                PersonalInfoEditActivity.PersonalInfoEditType.avatarurl -> {
//                    val a = "https://gmall-1304083978.cos.ap-guangzhou.myqcloud.com/avatar/IMG_CMP_81756127.png"
                    //截取文件名
                    val param_index = (param as String).indexOfLast { _char ->
                        _char.toString() == "/"
                    }
                    val _param = param.substring(param_index + 1, param.length)
                    it.addProperty("avatarUrl", _param)
                }
            }
            it.addProperty("id", CacheUtil.getUser()?.userId)
            it.toString().toRequestBody("application/json".toMediaTypeOrNull())
        }

        showLoading(null)
        delayToDoSometing(500) {
            HttpFactory.getInstance().APINew(ApiService.BASE_URL).userEdit(requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    dismissLoading()
                    if (it.code == 0) {
                        onCallBack(true, "修改成功")
                    } else {
                        onCallBack(false, it.msg)
                    }
                }, {
                    dismissLoading()
                    onCallBack(false, "编辑失败!")
                })
        }
    }

    /**
     * 获取COS临时密钥
     */
    fun cosCredential(onCallBack: (bean: CosBean) -> Unit) {
        HttpFactory.getInstance().APINew(ApiService.BASE_URL).cosCredential()
            .convert().funSubscribe(false,
                {
                    if (it is NettServerException) {
                        ToastUtil.showCenter("${it.errorMessage}")
                    }
                    LogUtils.iTag(ConfigConstants.CONSTANT.TAG_ALL, "${it.message}")
                }, {
                    onCallBack(it)
                })
    }

    /**
     * 上传头像
     */
    fun uploadAvatar(
        context: Context,
        localMedia: MutableList<LocalMedia>,
        cosBean: CosBean
    ): Observable<List<String>> {
        val resultList = arrayListOf<String>()
        return Observable.create { emitter ->
            val sUrl = localMedia[0].compressPath
            val baseUri = Uri.fromFile(File(sUrl))
//            val cosPath = EncryptUtils.encryptMD5ToString(
//                "${cosBean.directoryList[0]}$baseUri"
//            )
//            val picfileName = sUrl.substring(sUrl.lastIndexOf("/") + 1, sUrl.length)
            val picfileName = "${CacheUtil.getUser()?.userId}_${System.currentTimeMillis()}${
                sUrl.substring(
                    sUrl.lastIndexOf("."), sUrl.length
                )
            }"
            val cosPath = "${cosBean.directoryList[0]}/$picfileName"
            val cosUtils =
                CosUtil(
                    context,
                    cosBean.tmpSecretId,
                    cosBean.tmpSecretKey,
                    cosBean.sessionToken,
                    cosBean.startTime,
                    cosBean.expireTime,
                    cosBean.region
                )
            cosUtils.uploadAvatar(baseUri, cosPath, cosBean.bucketName)
                .setCosXmlResultListener(object : CosXmlResultListener {
                    override fun onSuccess(p0: CosXmlRequest?, p1: CosXmlResult?) {
                        resultList.add(p1?.accessUrl ?: "")
                        emitter.onNext(resultList)
                        emitter.onComplete()
                    }

                    override fun onFail(
                        p0: CosXmlRequest?,
                        p1: CosXmlClientException?,
                        p2: CosXmlServiceException?
                    ) {
                        if (p2 != null)
                            emitter.onError(p2)
                        else if (p1 != null)
                            emitter.onError(p1)
                    }

                })
        }
    }
}