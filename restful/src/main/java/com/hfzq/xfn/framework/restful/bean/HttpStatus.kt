package com.hfzq.xfn.framework.restful.bean

import androidx.annotation.Keep

/**
 *  反序列化接口数据，判断是否接口报错
 *  author: xun.wang on 2019/5/31
 **/
//todo 此处业务相关，需改
@Keep
data class HttpStatus(
    val message: String = "",
    var retcode: Int = 0,
    val code: Int = 0,
    val data: Any = Any()
)
