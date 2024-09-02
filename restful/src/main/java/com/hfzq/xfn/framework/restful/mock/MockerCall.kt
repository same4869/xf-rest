package com.hfzq.xfn.framework.restful.mock

import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MockerCall<Any>(var data: Any) : Call<Any> {

    override fun isExecuted(): Boolean {
        return false
    }

    override fun isCanceled(): Boolean {
        return false
    }

    override fun execute(): Response<Any> {
        return  Response.success(data)
    }


    override fun cancel() {
    }

    override fun enqueue(callback: Callback<Any>) {
        //todo 这里的this原来是null
        callback.onResponse(this, Response.success(data))
    }

    override fun clone(): Call<Any> {
        return this
    }

    override fun request(): Request {
        //todo 这里的this.request()原来是null
        return this.request()
    }

    override fun timeout(): Timeout {
        return Timeout.NONE
    }
}