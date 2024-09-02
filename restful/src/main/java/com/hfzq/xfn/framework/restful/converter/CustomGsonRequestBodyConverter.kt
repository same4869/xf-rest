package com.hfzq.xfn.framework.restful.converter

import com.hfzq.xfn.framework.commlib.CommJsonParser
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.Converter
import java.io.IOException

/**
 *  author: xun.wang on 2019/5/31
 **/
internal class CustomGsonRequestBodyConverter : Converter<Any, RequestBody> {

    @Throws(IOException::class)
    override fun convert(value: Any): RequestBody {
        return RequestBody.create(MEDIA_TYPE, CommJsonParser.getCommJsonParser().toJson(value))
    }

    companion object {
        private val MEDIA_TYPE = "application/json; charset=UTF-8".toMediaTypeOrNull()
    }
}