package com.sn.gameelectricity.app.base

import android.content.res.Resources
import android.os.Bundle
import android.view.View
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.AdaptScreenUtils
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.sn.gameelectricity.app.ext.dismissLoadingExt
import com.sn.gameelectricity.app.ext.showLoadingExt
import com.sn.gameelectricity.app.weight.loadCallBack.LoadingCallback
import com.sn.gameelectricity.app.weight.loadCallBack.PostUtil
import me.hgj.jetpackmvvm.base.activity.BaseVmVbActivity
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.base.viewmodel.LoadingBean

/**
 * 描述　: 你项目中的Activity基类，在这里实现显示弹窗，吐司，还有加入自己的需求操作 ，如果不想用 Databind，请继承
 * BaseVmActivity例如
 * abstract class BaseActivity<VM : BaseViewModel> : BaseVmActivity<VM>() {
 * 添加 LoadSir
 */
abstract class BaseActivity2<VM : BaseViewModel, VB : ViewBinding> : BaseVmVbActivity<VM, VB>() {

    //基础LoadService
    lateinit var mBaseLoadService: LoadService<Any>

    abstract override fun initView(savedInstanceState: Bundle?)

    /**
     * 创建liveData观察者
     */
    override fun createObserver() {}

    /**
     * 打开等待框
     */
    override fun showLoading(loadingBean: LoadingBean?) {
        PostUtil.postCallbackDelayed(mBaseLoadService, LoadingCallback::class.java, 100)
    }

    /**
     * 关闭等待框
     */
    override fun dismissLoading() {
        PostUtil.postSuccessDelayed(mBaseLoadService, 5000)
    }

    override fun getResources(): Resources? {
        return AdaptScreenUtils.adaptWidth(
            super.getResources(),
            375
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBaseLoadService = LoadSir.getDefault().register(this) {
            onNetReload(it)
        }
        loadNet()
    }

    /**
     * LoadSir点击重载事件
     */
    protected abstract fun onNetReload(view: View)

    /**
     * 加载loadsir
     */
    protected abstract fun loadNet()

}