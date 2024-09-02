package com.hfzq.xfn.framework.restful.cache.interceptor

import com.hfzq.xfn.framework.log.MLog
import com.hfzq.xfn.framework.restful.cache.bean.CacheConfig
import com.hfzq.xfn.framework.restful.cache.manager.RetrofitCacheManager
import okhttp3.Interceptor
import okhttp3.Response

class CacheInterceptorOnNet : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val cacheKey: String = RetrofitCacheManager.getCacheKey(request)


        val cacheConfig: CacheConfig =
            RetrofitCacheManager.getCacheConfig(cacheKey) ?: return chain.proceed(request)
        val maxAge: Long = cacheConfig.getTime()
        val response = chain.proceed(request)

        if (response.code == 301 || response.code == 302) {
            val location = response.headers["Location"] ?: ""
            RetrofitCacheManager.addCacheConfig(location, cacheConfig)
        }

        MLog.d("cache333:CacheInterceptorOnNet 存了 maxAge:$maxAge")
        return response.newBuilder()
            .removeHeader("Pragma")
            .header("Cache-Control", "public, max-age=$maxAge")
            .build()
    }
}