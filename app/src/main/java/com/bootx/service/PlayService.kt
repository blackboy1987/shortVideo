package com.bootx.service

import com.bootx.util.HiRetrofit
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST


interface PlayService {

    @POST("/api/items")
    @FormUrlEncoded
    suspend fun items(@Header("token") token: String, @Field("id") id: String): ItemDataResponse
    @POST("/api/play")
    @FormUrlEncoded
    suspend fun play(@Header("token") token: String, @Field("id") id: String): PlayDataResponse

    companion object {
        fun instance(): PlayService {
            return HiRetrofit.create(PlayService::class.java)
        }
    }
}

data class NextDataResponse(val data: NextData) : BaseResponse()
data class ItemDataResponse(val data: List<ItemData>) : BaseResponse()
data class PlayDataResponse(val data: String) : BaseResponse()

data class NextData(
    var id: String,
    var playUrl: String,
)

data class ItemData(
    var id: String,
)