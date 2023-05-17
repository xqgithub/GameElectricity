package me.hgj.jetpackmvvm.base.viewmodel

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

/**
 *
 * @ClassName:      SingleObserveLiveData
 * @Description:     同时只允许一个订阅
 */
class SingleObserveLiveData<T> : MutableLiveData<T>() {
    private var mObserver: Observer<in T>? = null

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        ready(observer)
        super.observe(owner, observer)
    }

    override fun observeForever(observer: Observer<in T>) {
        ready(observer)
        super.observeForever(observer)
    }

    private fun ready(observer: Observer<in T>) {
        //如果之前有订阅就将之前的订阅删除掉
        mObserver?.let {
            hookLiveDataVersion()
            removeObserver(it)
        }
        mObserver = observer
    }

    /**
     * 通过反射修改LiveData版本号，避免新订阅的观察者接收到旧消息
     */
    private fun hookLiveDataVersion() {
        val classLiveData = LiveData::class.java
        val fieldVersion = classLiveData.getDeclaredField("mVersion")
        fieldVersion.isAccessible = true
        fieldVersion[this] = -1
    }

}