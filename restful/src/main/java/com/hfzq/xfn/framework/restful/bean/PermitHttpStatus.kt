package com.hfzq.xfn.framework.restful.bean

import androidx.annotation.Keep

/**
 * @Description:    反序列化通行证接口数据，判断是否接口报错
 * @Author:         xwang
 * @CreateDate:     2020/5/21
 */
//todo 此处业务相关，需改
@Keep
data class PermitHttpStatus(
    val code: Int,
    val `data`: PermitHttpStatusData
)

@Keep
data class PermitHttpStatusData(
    val info: String,
    val msg: String,
    val status: Int
)

