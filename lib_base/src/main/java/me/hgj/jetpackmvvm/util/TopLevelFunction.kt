package me.hgj.jetpackmvvm.util

import android.annotation.SuppressLint
import android.view.View
import io.reactivex.Observable
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * Date:2022/6/10
 * Time:13:57
 * author:dimple
 */

fun <T> observableToMain(): ObservableTransformer<T, T> {
    return ObservableTransformer { ob ->
        ob.subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}


/**
 * 类型转化
 */
inline fun <reified T> dataNullConvert(params: Any?): T {
    when (T::class) {
        Int::class -> {
            return if (PublicPracticalMethodFromKT.ppmfKT.isBlank(params)) {
                -99 as T
            } else {
                params as T
            }
        }
        String::class -> {
            return if (PublicPracticalMethodFromKT.ppmfKT.isBlank(params)) {
                "" as T
            } else {
                params as T
            }
        }
        Long::class -> {
            return if (PublicPracticalMethodFromKT.ppmfKT.isBlank(params)) {
                -99L as T
            } else {
                params as T
            }
        }
        Float::class -> {
            return if (PublicPracticalMethodFromKT.ppmfKT.isBlank(params)) {
                -99f as T
            } else {
                params as T
            }
        }
        Double::class -> {
            return if (PublicPracticalMethodFromKT.ppmfKT.isBlank(params)) {
                -99.00 as T
            } else {
                params as T
            }
        }
        else -> {
            return throw IllegalStateException("不支持该类型")
        }
    }
}

var dataNullConvertToInt = { params: Any? ->
    if (PublicPracticalMethodFromKT.ppmfKT.isBlank(params)) {
        -99
    } else {
        params
    }
}

var dataNullConvertToString = { params: Any? ->
    if (PublicPracticalMethodFromKT.ppmfKT.isBlank(params)) {
        ""
    } else {
        params
    }
}

var dataNullConvertToLong = { params: Any? ->
    if (PublicPracticalMethodFromKT.ppmfKT.isBlank(params)) {
        -99L
    } else {
        params
    }
}


/**
 * 判断是否为空 并传入相关操作
 */
inline fun <reified T> T?.isNotNull(notNullAction: (T) -> Unit, nullAction: () -> Unit = {}) {
    if (!PublicPracticalMethodFromKT.ppmfKT.isBlank(this)) {
        notNullAction.invoke(this!!)
    } else {
        nullAction.invoke()
    }
}


/**
 * 金额数据处理
 */
fun amountDataConvert(params: Any?): String {
    if (!PublicPracticalMethodFromKT.ppmfKT.isBlank(params)) {
        if (params is Double) {
            val remainder = params % 1
            if (remainder == 0.0) {
                return params.toInt().toString()
            } else {
                return params.toString()
            }
        } else if (params is Float) {
            val remainder = params % 1
            if (remainder == 0f) {
                return params.toInt().toString()
            } else {
                return params.toString()
            }
        } else if (params is Int) {
            return params.toString()
        }
    }
    return "0"
}

/**
 * 延迟多少秒做什么事情
 */
@SuppressLint("CheckResult")
fun delayToDoSometing(delay: Long, onCallBack: () -> Unit) {
    Observable.timer(delay, TimeUnit.MILLISECONDS)
        .subscribe {
            onCallBack()
        }
}








