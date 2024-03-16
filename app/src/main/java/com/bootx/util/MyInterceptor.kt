package com.bootx.util

import android.util.Log
import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import java.nio.charset.Charset


class MyInterceptor:Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url.toString()
        val params = request.body
        val currentTimeMillis = System.currentTimeMillis()
        Log.e("MyInterceptor", "请求地址: $url")
        if(params!=null){
            Log.e("MyInterceptor", "请求参数: ${Gson().toJson(params)}")
        }
        var response: Response = chain.proceed(request)
        val body = response.body
        val source = body!!.source()
        source.request(Long.MAX_VALUE)
        val buffer = source.buffer
        var charset = Charset.defaultCharset()
        val contentType = body.contentType()
        if (contentType != null) {
            charset = contentType.charset(charset)
        }
        val string: String = buffer.clone().readString(charset)
        val responseBody = ResponseBody.create(contentType, string)
        response = response.newBuilder().body(responseBody).build()
        Log.e("MyInterceptor", "响应数据: $string")
        Log.e("MyInterceptor", "请求完成，累计耗时: ${System.currentTimeMillis()-currentTimeMillis}毫秒")
        return response;
    }

}
