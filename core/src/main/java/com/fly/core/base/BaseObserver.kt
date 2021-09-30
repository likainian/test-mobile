package com.fly.core.base

import com.fly.core.util.ToastUtil
import io.reactivex.observers.DisposableObserver

/**
 * Created by likainian on 2021/9/17
 * Description:
 */

abstract class BaseObserver<T> : DisposableObserver<T>() {

    //请求成功且返回码为0的回调方法,这里抽象方法申明
    abstract fun onSuccess(tBaseResponse: T)

    override fun onComplete() {
    }

    override fun onError(e: Throwable) {
        ToastUtil.showToast(e)
    }

    override fun onNext(t: T) {
        onSuccess(t)
    }

}