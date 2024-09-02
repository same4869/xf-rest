package com.hfzq.xfn.framework.restful

import com.hfzq.xfn.framework.commlib.APPLICATION
import com.hfzq.xfn.framework.restful.cache.interceptor.CacheInterceptorNoNet
import com.hfzq.xfn.framework.restful.cache.interceptor.CacheInterceptorOnNet
import com.hfzq.xfn.framework.restful.converter.CustomGsonConverterFactory
import com.hfzq.xfn.framework.restful.utils.createMockerProxy
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2021/1/13
 */

object RetrofitClient {
    var okHttpClient: OkHttpClient? = null
    private var retrofit: Retrofit? = null

    private const val DEFAULT_TIMEOUT: Long = 15
    private const val url = "https://api.baidu.com/" //默认占坑，无实际意义，真正的baseurl在header拦截器里设置
    private val serviceCacheMap = HashMap<Int, Any>()
    private var mCustomConverterFactory: Converter.Factory? = null

    /**
     * 是否开启接口mock，如果为true，那么使用了@MOCK的接口会去本地找到对应的json文件进行本地mock数据
     */
    private var isMocker: Boolean = false
    private var isInited = false

    /**
     * interceptors:外部注入的拦截器，可以注入多个
     * isMockerP:如果开启，支持本地文件mock数据（使用方法见readme）
     * customConverterFactoryP:初始化Retrofit时支持外部传ConverterFactory
     */
    @JvmOverloads
    fun init(
        isMockerP: Boolean = false,
        baseUrlP: String = url,
        customConverterFactoryP: Converter.Factory? = null,
        interceptorBuilder: ((OkHttpClient.Builder) -> Unit)? = null,
        networkInterceptorBuilder: ((OkHttpClient.Builder) -> Unit)? = null,
        applyOKHttpClientBuilder: (OkHttpClient.Builder.() -> Unit)? = null
    ) {
        if (isInited) {
            return
        }
        mCustomConverterFactory = customConverterFactoryP
        isMocker = isMockerP

        val httpCacheDirectory = File(APPLICATION.cacheDir, "app_cache")
        val cache = Cache(httpCacheDirectory, 20 * 1024 * 1024)

        val okHttpClientBuilder = OkHttpClient.Builder()
            .cache(cache)
            .addNetworkInterceptor(CacheInterceptorOnNet())
            .addInterceptor(CacheInterceptorNoNet())
            .retryOnConnectionFailure(true)
            .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)

        interceptorBuilder?.invoke(okHttpClientBuilder)
        networkInterceptorBuilder?.invoke(okHttpClientBuilder)
        applyOKHttpClientBuilder?.also { okHttpClientBuilder.apply(it) }

        okHttpClient = okHttpClientBuilder.build()
        retrofit = Retrofit.Builder().client(okHttpClient!!)
            .addConverterFactory(
                if (mCustomConverterFactory != null) {
                    mCustomConverterFactory!!
                } else {
                    CustomGsonConverterFactory()
                }
            )
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(baseUrlP)
            .build()

        isInited = true
    }

    /**
     * 如果[serviceCacheMap]中存在，则从其中取
     * 如果不存在，则新建并放入map中，并返回该值
     */
    inline fun <reified T> getOrCreateService(): T {
        return getOrCreateService(T::class.java)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getOrCreateService(clazz: Class<T>): T {
        if (!isInited) {
            throw IllegalStateException("you must init RetrofitClient first!")
        }
        var result = serviceCacheMap[clazz.name.hashCode()]
        if (result == null) {
            result = retrofit?.createMockerProxy(clazz, isMocker)
            serviceCacheMap[clazz.name.hashCode()] = result!!
        }
        return result as T
    }
}
