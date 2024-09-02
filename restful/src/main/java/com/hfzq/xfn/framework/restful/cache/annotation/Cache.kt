package com.hfzq.xfn.framework.restful.cache.annotation

import java.util.concurrent.TimeUnit

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class Cache(
    val timeUnit: TimeUnit = TimeUnit.NANOSECONDS,
    val time: Long = -1,
    val forceCacheNoNet: Boolean = true
)