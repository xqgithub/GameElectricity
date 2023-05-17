package com.sn.gameelectricity.app

import android.os.Looper
import androidx.multidex.MultiDex
import cat.ereza.customactivityoncrash.CustomActivityOnCrash
import cat.ereza.customactivityoncrash.config.CaocConfig
import cn.jiguang.share.android.api.JShareInterface
import com.blankj.utilcode.util.AppUtils
import com.caij.app.startup.Config
import com.caij.app.startup.DGAppStartup
import com.facebook.stetho.Stetho
import com.kingja.loadsir.callback.SuccessCallback
import com.kingja.loadsir.core.LoadSir
import com.sn.gameelectricity.app.event.AppViewModel
import com.sn.gameelectricity.app.event.EventViewModel
import com.sn.gameelectricity.app.network.HttpInterceptorTask
import com.sn.gameelectricity.app.network.TokenInterceptorHelper
import com.sn.gameelectricity.app.util.CacheUtil
import com.sn.gameelectricity.app.weight.loadCallBack.EmptyCallback
import com.sn.gameelectricity.app.weight.loadCallBack.ErrorCallback
import com.sn.gameelectricity.app.weight.loadCallBack.LoadingCallback
import com.sn.gameelectricity.ui.activity.WelcomeActivity
import com.tencent.bugly.crashreport.CrashReport
import com.tencent.mmkv.MMKV
import me.hgj.jetpackmvvm.base.BaseApp
import me.hgj.jetpackmvvm.initializer.BaiDuTask
import me.hgj.jetpackmvvm.initializer.MonitorTaskListener
import me.hgj.jetpackmvvm.initializer.ThreadManager
import me.hgj.jetpackmvvm.util.CrashHandler
import me.hgj.jetpackmvvm.util.ScreenTools
import me.hgj.jetpackmvvm.util.ToastUtil


/**
 * 描述　:
 */

//Application全局的ViewModel，里面存放了一些账户信息，基本配置信息等
val appViewModel: AppViewModel by lazy { App.appViewModelInstance }

//Application全局的ViewModel，用于发送全局通知操作
val eventViewModel: EventViewModel by lazy { App.eventViewModelInstance }

class App : BaseApp() {

    companion object {
        lateinit var instance: App
        lateinit var eventViewModelInstance: EventViewModel
        lateinit var appViewModelInstance: AppViewModel
    }

    override fun onCreate() {
        super.onCreate()
        MMKV.initialize(this.filesDir.absolutePath + "/mmkv")
        instance = this
        eventViewModelInstance = getAppViewModelProvider().get(EventViewModel::class.java)
        appViewModelInstance = getAppViewModelProvider().get(AppViewModel::class.java)
        MultiDex.install(this)
        //界面加载管理 初始化
        LoadSir.beginBuilder()
            .addCallback(LoadingCallback())//加载
            .addCallback(ErrorCallback())//错误
            .addCallback(EmptyCallback())//空
            .setDefaultCallback(SuccessCallback::class.java)//设置默认加载状态页
            .commit()

        //初始化AutoSize
        ScreenTools.getInstance().initAutoSize(this, this)
        //加载全部异常捕获
        CrashHandler.getInstance().init(this)
        //初始化ToastUtil工具类
        ToastUtil.init(this)
        //初始化Stetho出正式包的时候，建议屏蔽掉
        if (AppUtils.isAppDebug()) {
            Stetho.initializeWithDefaults(this);
        }
        
        JShareInterface.init(this)

        val config = Config()
        config.isStrictMode = AppUtils.isAppDebug()
        val startupBuilder = DGAppStartup.Builder()

        startupBuilder.add(HttpInterceptorTask(instance))
        startupBuilder.add(Looper.myLooper()?.let { BaiDuTask(it, this) })

        startupBuilder.setConfig(config)
            .setExecutorService(ThreadManager.getInstance().WORK_EXECUTOR)
            .addTaskListener(MonitorTaskListener("initializerTag", true))
            .create().start().await()

        CrashReport.initCrashReport(getApplicationContext(), "a919d506f6", true);

//        CaocConfig.Builder.create()
//            .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT)
//            .enabled(true)
//            .showErrorDetails(false)
//            .showRestartButton(false)
//            .logErrorOnRestart(false)
//            .trackActivities(true)
//            .minTimeBetweenCrashesMs(2000)
//            .restartActivity(WelcomeActivity::class.java)
//            .errorActivity(WelcomeActivity::class.java)
//            .eventListener(MyCustomEventListener())
//            .apply()
    }

    private class MyCustomEventListener : CustomActivityOnCrash.EventListener {
        override fun onLaunchErrorActivity() {
            CacheUtil.setIsLogin(false)
            TokenInterceptorHelper.getInstance().saveAppToken(instance, "")
        }

        override fun onRestartAppFromErrorActivity() {
        }

        override fun onCloseAppFromErrorActivity() {
        }
    }
}
