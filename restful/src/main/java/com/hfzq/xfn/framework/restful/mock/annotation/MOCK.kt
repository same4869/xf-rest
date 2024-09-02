package com.hfzq.xfn.framework.restful.mock.annotation

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class MOCK(val value:String,val enable:Boolean = true)