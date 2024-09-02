package com.hfzq.xfn.framework.restful.cache.interceptor

import com.hfzq.xfn.framework.commlib.APPLICATION
import com.hfzq.xfn.framework.commlib.isNetworkAvailable
import com.hfzq.xfn.framework.log.MLog
import com.hfzq.xfn.framework.restful.cache.manager.RetrofitCacheManager
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response

class CacheInterceptorNoNet : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        val cacheKey: String = RetrofitCacheManager.getCacheKey(request)
        val forceCacheNoNet: Boolean =
            RetrofitCacheManager.getCacheConfig(cacheKey)?.isForceCacheNoNet() ?: false
        //无网且强制cache的时候直接拿缓存
        if (forceCacheNoNet && !isNetworkAvailable(APPLICATION)) {
            request = request.newBuilder()
                .cacheControl(CacheControl.FORCE_CACHE)
                .build()
        }
        var response = chain.proceed(request)
        val code = response.code
        if (code == 504) {
            response.close()
            response = chain.proceed(chain.request())
        }
        if (response.networkResponse != null) {
            MLog.d("cache333 get data from net")
        } else if (response.cacheResponse != null) {
            MLog.d("cache333 get data from cache")
        }
        MLog.d("cache333 networkResponse:${response.networkResponse} cacheResponse:${response.cacheResponse}")
        return response
    }

}