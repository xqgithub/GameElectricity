package me.hgj.jetpackmvvm.base.viewmodel

import com.blankj.utilcode.util.LogUtils
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import me.hgj.jetpackmvvm.network.NettServerException
import me.hgj.jetpackmvvm.util.delayToDoSometing

class ViewObserver<T>(
    private val showLoading: Boolean,
    private val modelEvent: ModelEvent,
    private val loadingBean: LoadingBean? = null,
    private val onSubscribeNext: (T) -> Unit = {},
    private val onSubscribeError: (Throwable) -> Unit = {},
) : Observer<T> {
    override fun onSubscribe(d: Disposable?) {
//        LogUtils.d("onSubscribe.............................")
        d?.let { modelEvent.saveDisposable(it) }
        if (showLoading)
            modelEvent.showLoading(loadingBean)
    }

    override fun onNext(t: T) {
//        LogUtils.d("onNext.............................")

        if (showLoading) {
            delayToDoSometing(500) {
                modelEvent.dismissLoading()
                onSubscribeNext(t)
            }
        } else {
            modelEvent.dismissLoading()
            onSubscribeNext(t)
        }
    }

    override fun onError(e: Throwable?) {
//        LogUtils.d("onError.............................${e.toString()}")
        modelEvent.dismissLoading()
        if (e is NettServerException) {
            if (e.errorCode != 65522) {
                modelEvent.responseError(e.errorCode)
            }
        } else {
            modelEvent.responseError(500)
        }
        e?.let {
            onSubscribeError(it)
        }
    }

    override fun onComplete() {
//        LogUtils.d("onComplete.............................")
    }
}