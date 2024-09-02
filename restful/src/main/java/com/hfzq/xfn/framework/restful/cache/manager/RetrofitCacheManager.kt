package com.hfzq.xfn.framework.restful.cache.manager

import com.hfzq.xfn.framework.restful.cache.bean.CacheConfig
import okhttp3.Request
import java.util.concurrent.ConcurrentHashMap

object RetrofitCacheManager {
    /**
     * 暂存缓存策略,key是cacheKey，类似`GET|apihub/api/getAppSplash`,value是缓存具体的策略
     */
    private val cacheMap: ConcurrentHashMap<String, CacheConfig> = ConcurrentHashMap()

    fun addCacheConfig(cacheKey: String, cacheConfig: CacheConfig) {
        if (cacheMap[cacheKey] == null) {
            cacheMap[cacheKey] = cacheConfig
        }
    }

    fun getCacheConfig(cacheKey: String): CacheConfig? {
        return cacheMap[cacheKey]
    }

    fun formatUrlPath(path: String): String {
        if (path.startsWith("/") && path.length > 1) {
            return path.substring(1, path.length)
        }
        return path
    }

    fun getCacheKey(request: Request): String {
        return "${request.method}|${formatUrlPath(request.url.encodedPath)}"
    }
}