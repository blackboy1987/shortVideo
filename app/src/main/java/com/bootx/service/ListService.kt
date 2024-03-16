package com.bootx.service

import com.bootx.util.HiRetrofit
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST


interface ListService {

    @POST("/api/category")
    @FormUrlEncoded
    suspend fun list(@Header("token") token: String, @Field("id") id: String): ListDataResponse

    @POST("/api/getPlayUrl")
    @FormUrlEncoded
    suspend fun getPlayUrl(@Header("token") token: String, @Field("id") id: String): GetPlayUrlResponse


    @POST("/api/getAllPlayUrl")
    @FormUrlEncoded
    suspend fun getAllPlayUrl(@Header("token") token: String, @Field("id") id: String): GetPlayUrlResponse

    companion object {
        fun instance(): ListService {
            return HiRetrofit.create(ListService::class.java)
        }
    }
}

data class GetPlayUrlResponse(val data: String) : BaseResponse()
data class ListDataResponse(val data: List<ListData>) : BaseResponse()

data class ListData(
    val name: String,
    var id: String,
    var category: Int,
    var cover: String,
)