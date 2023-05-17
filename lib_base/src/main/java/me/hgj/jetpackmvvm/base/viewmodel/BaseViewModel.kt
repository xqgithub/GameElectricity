package me.hgj.jetpackmvvm.base.viewmodel

import android.text.TextUtils
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.hgj.jetpackmvvm.callback.livedata.event.EventLiveData
import me.hgj.jetpackmvvm.util.isNotNull
import java.util.concurrent.ConcurrentHashMap

/**
 * 描述　: ViewModel的基类 使用ViewModel类，放弃AndroidViewModel，原因：用处不大 完全有其他方式获取Application上下文
 */
open class BaseViewModel : ViewModel(), ModelEvent {

    val loadingChange: UiLoadingChange by lazy { UiLoadingChange() }
    val responseError: NetworkResponseError by lazy { NetworkResponseError() }

    /**
     * 内置封装好的可通知Activity/fragment 显示隐藏加载框 因为需要跟网络请求显示隐藏loading配套才加的，不然我加他个鸡儿加
     */
    inner class UiLoadingChange {
        //显示加载框
        val showDialog by lazy { EventLiveData<LoadingBean>() }

        //隐藏
        val dismissDialog by lazy { EventLiveData<Boolean>() }
    }

    /**
     * 网路请求错误码
     */
    inner class NetworkResponseError {
        val responseErrorCode by lazy { EventLiveData<Int>() }
    }

    /**
     * 保存订阅的任务，在ViewModel销毁时如果任务没有执行完毕就给取消掉
     */
    private val disposableMap = ConcurrentHashMap<String, Disposable>()

    override fun onCleared() {
        super.onCleared()
        for (item in disposableMap.values) {
            if (!item.isDisposed) {
                item.dispose()
            }
        }
        disposableMap.clear()
    }

    /**
     * 以ViewModel中的方法名为键，将任务请求保存起来，保证每一次请求之前的请求
     * 没有执行完毕的情况下，将之前的请求任务取消掉
     * @param tag String
     * @param disposable Disposable
     */
    private fun saveDisposable(tag: String, disposable: Disposable) {
        if (disposableMap.containsKey(tag)) {
            disposableMap[tag]?.dispose()
        }
        disposableMap[tag] = disposable
    }

    override fun saveDisposable(disposable: Disposable) {
        val stacks = Throwable().stackTrace
        viewModelScope.launch(Dispatchers.IO) {
            repeat(stacks.size) {
                if (TextUtils.equals(stacks[it].className, this@BaseViewModel.javaClass.name)) {
                    saveDisposable(stacks[it].methodName, disposable)
                    return@launch
                }
            }
        }
    }


    /**
     *  绑定观察者
     * @param onNext Function1<T, Unit> 接收到消息的回调
     * @param onError Function1<Throwable, Unit> 任务异常的回调
     * @return ViewObserver<T>
     */
    protected fun <T> bindObserver(
        showLoading: Boolean,
        loadingBean: LoadingBean? = null,
        onNext: (T) -> Unit,
        onError: (Throwable) -> Unit
    ) = ViewObserver<T>(
        showLoading,
        this,
        onSubscribeNext = onNext,
        onSubscribeError = onError,
        loadingBean = loadingBean
    )


    protected inline fun <reified T> Observable<T>.funSubscribe(
        showLoading: Boolean = true,
        noinline onError: (Throwable) -> Unit = {},
        noinline onNext: (T) -> Unit,
        loadingBean: LoadingBean? = null,
    ) {
        subscribe(bindObserver(showLoading, loadingBean, onNext, onError))
    }


    override fun showLoading(loadingBean: LoadingBean?) {
        loadingBean.isNotNull({
            loadingChange.showDialog.postValue(loadingBean)
        }, {
            loadingChange.showDialog.postValue(LoadingBean())
        })
    }

    override fun dismissLoading() {
        loadingChange.dismissDialog.postValue(false)
    }

    override fun responseError(errorCode: Int) {
        responseError.responseErrorCode.postValue(errorCode)
    }


}