package com.sn.gameelectricity.app.event

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Path
import android.view.View
import android.view.animation.PathInterpolator
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.LogUtils
import com.kunminx.architecture.ui.callback.UnPeekLiveData
import com.sn.gameelectricity.app.App
import com.sn.gameelectricity.app.network.ApiService
import com.sn.gameelectricity.app.network.HttpFactory
import com.sn.gameelectricity.app.util.CacheUtil
import com.sn.gameelectricity.app.weight.customview.ErrorPageView
import com.sn.gameelectricity.data.model.bean.PersonalInfo
import com.sn.gameelectricity.data.model.bean.UserInfo
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.common.ConfigConstants
import me.hgj.jetpackmvvm.ext.convert
import me.hgj.jetpackmvvm.network.NettServerException
import me.hgj.jetpackmvvm.util.ScreenTools
import me.hgj.jetpackmvvm.util.ToastUtil
import me.hgj.jetpackmvvm.util.dataNullConvert
import me.hgj.jetpackmvvm.util.dataNullConvertToString
import me.jessyan.autosize.AutoSizeConfig
import java.text.DecimalFormat

/**
 * 描述　:APP全局的ViewModel，可以存放公共数据，当他数据改变时，所有监听他的地方都会收到回调,也可以做发送消息
 * 比如 全局可使用的 地理位置信息，账户信息,App的基本配置等等，
 */
class AppViewModel : BaseViewModel() {

    //App的账户信息
    var userInfo = UnPeekLiveData.Builder<UserInfo>().setAllowNullValue(true).create()

    init {
        //默认值保存的账户信息，没有登陆过则为null
        userInfo.value = CacheUtil.getUser()
    }

    /**
     * 初始化错误提示页面
     */
    fun initErrorPage(errorpage: ErrorPageView, errorIcon: Int, onCallBack: () -> Unit) {
        errorpage.apply {
            visibility = View.VISIBLE
            setErrorIcon(
                errorIcon,
                ScreenTools.getInstance().dp2px(App.instance, 124f, true),
                ScreenTools.getInstance().dp2px(App.instance, 124f, true)
            )
            setErrorContent("数据错误", 12f, "#A1A7AF")
            changeErrorIconPositionToTOP((0.4 * AutoSizeConfig.getInstance().screenHeight).toInt())
            changeErrorTextPositionToTOP(0)
            setToOperation("点击关闭页面")
            toOperateOnClickListener {
                onCallBack()
            }
        }
    }

    /**
     * 金币转化
     */
    var goldCoinConversion = { goldCoin: Int ->
        var result = ""
        val myformat = DecimalFormat("0.00")
        result = if (goldCoin in 10000..999999) {
            "${myformat.format(goldCoin.toDouble() / 1000)}K"
        } else if (goldCoin in 1000000..99999999) {
            "${myformat.format(goldCoin.toDouble() / 1000000)}m"
        } else if (goldCoin in 100000000..100000000000) {
            "${myformat.format(goldCoin.toDouble() / 1000000000)}b"
        } else if (goldCoin in 0..9999) {
            goldCoin.toString()
        } else {
            "${myformat.format(goldCoin.toDouble() / 1000000000000)}t"
        }
        result
    }

    /**
     * 开始轨迹动画
     */
    fun startTrajectoryAnimation(
        targetView: View,
        mStartPointX: Float, mStartPointY: Float,
        mEndPointX: Float, mEndPointY: Float,
        onCallBack: (animationState: String) -> Unit
    ) {
        val mPath = Path()
//        this.mStartPointX = mStartPointX
//        this.mStartPointY = mStartPointY
//        this.mEndPointX = mEndPointX
//        this.mEndPointY = mEndPointY
        val mControlPointX = (mStartPointX + mEndPointX) / 2
        val mControlPointY = (mStartPointY + mEndPointY) / 2

        mPath.reset()
        mPath.moveTo(mStartPointX, mStartPointY)
        mPath.quadTo(mControlPointX, mControlPointY, mEndPointX, mEndPointY)

        val pathInterpolator = PathInterpolator(0.33f, 0f, 0.12f, 1f)

        val scalex = ObjectAnimator.ofFloat(targetView, View.SCALE_X, 1.0f, 0.3f)
        val scaley = ObjectAnimator.ofFloat(targetView, View.SCALE_Y, 1.0f, 0.3f)
        val traslateAnimator = ObjectAnimator.ofFloat(targetView, "x", "y", mPath)

        val animSet = AnimatorSet()
        animSet.apply {
            playTogether(scalex, scaley, traslateAnimator)
            interpolator = pathInterpolator
            duration = 1000
            doOnStart {
                LogUtils.iTag(ConfigConstants.CONSTANT.TAG_ALL, "doOnStart =-= ")
                targetView.visibility = View.VISIBLE
                onCallBack("doOnStart")
            }
            doOnEnd {
                LogUtils.iTag(ConfigConstants.CONSTANT.TAG_ALL, "doOnEnd =-= ")
                targetView.visibility = View.GONE
                onCallBack("doOnEnd")
            }
            start()
        }
    }


    /**
     * 获取用户信息
     */
    val userInfoLive = MutableLiveData<PersonalInfo>()

    fun getuserInfo() {
        HttpFactory.getInstance().APINew(ApiService.BASE_URL).getuserInfo()
            .convert().funSubscribe(false, {
                if (it is NettServerException) {
                    ToastUtil.showCenter("${it.errorMessage}")
                }
                LogUtils.iTag(ConfigConstants.CONSTANT.TAG_ALL, "${it.message}")
            }, { bean ->
                CacheUtil.getUser()?.let {
                    it.avatarUrl = dataNullConvertToString(bean.avatarUrl) as String
                    it.email = dataNullConvertToString(bean.email) as String
                    it.gender = dataNullConvertToString(bean.gender) as Int
                    it.guideStageId = bean.guideStageId
                    it.goldCoin = bean.goldCoin
                    it.score = dataNullConvertToString(bean.score) as Int
                    it.idCard = dataNullConvertToString(bean.idCard) as String
                    it.mobile = bean.mobile
                    it.nickName = bean.nickName
                    it.realName = dataNullConvertToString(bean.realName) as String
                    it.userId = bean.userId
                    it.birthday = dataNullConvertToString(bean.birthday) as String
                    it.boolGuide = bean.boolGuide
                    it.boolNewUserActivityExchange = bean.boolNewUserActivityExchange
                    it.iv = bean.iv
                    it.key = bean.key
                    it.userCode = dataNullConvert(bean.userCode)
                    it.loadingUrl = dataNullConvert(bean.loadingUrl)

                    CacheUtil.setUser(it)
                }
                userInfoLive.postValue(bean)
            })
    }

    /**
     * UI控件控制显示或者隐藏
     */
    fun uiShowHide(view: Array<View>, visibleState: Int) {
        view.forEach { _view ->
            _view.visibility = visibleState
        }
    }
}