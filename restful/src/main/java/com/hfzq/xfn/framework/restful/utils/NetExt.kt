package com.hfzq.xfn.framework.restful.utils

import com.hfzq.xfn.framework.restful.mock.MockerHandler
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import java.lang.reflect.Proxy

//通用网络请求rx线程调度
fun <T> Observable<T>.applySchedulers(): Observable<T> {
    return subscribeOn(Schedulers.io())
        .unsubscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

//如果满足条件，会使用本地文件mock api的数据
fun <T> Retrofit.createMockerProxy(cls: Class<T>, needMocker: Boolean): T {
    if (!needMocker) return this.create(cls)
    val api = this.create(cls)
    return Proxy.newProxyInstance(javaClass.classLoader, arrayOf(cls), MockerHandler(this, api)) as T
}

fun <T> Retrofit.createMockerProxy(cls: Class<T>): T {
    return this.createMockerProxy(cls, true)
}