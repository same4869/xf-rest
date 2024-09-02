package com.hfzq.xfn.framework.restful.cache.bean

import java.util.concurrent.TimeUnit

class CacheConfig {
    private var timeUnit = TimeUnit.NANOSECONDS
    private var time = 0L

    /**
     * 在无网时是否强制使用缓存
     */
    private var forceCacheNoNet = true

    constructor(timeUnit: TimeUnit, time: Long, forceCacheNoNet: Boolean) {
        this.timeUnit = timeUnit
        this.time = time
        this.forceCacheNoNet = forceCacheNoNet
    }

    fun getTimeUnit(): TimeUnit {
        return timeUnit
    }

    fun setTimeUnit(timeUnit: TimeUnit) {
        this.timeUnit = timeUnit
    }

    fun getTime(): Long {
        return when (timeUnit) {
            TimeUnit.MINUTES -> time * 60
            TimeUnit.HOURS -> time * 60 * 60
            TimeUnit.DAYS -> time * 60 * 60 * 24
            else -> time
        }
    }

    fun setTime(time: Long) {
        this.time = time
    }

    fun isForceCacheNoNet(): Boolean {
        return forceCacheNoNet
    }

    fun setForceCacheNoNet(forceCacheNoNet: Boolean) {
        this.forceCacheNoNet = forceCacheNoNet
    }

}