package com.bootx.service

import com.bootx.util.HiRetrofit
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST


interface PlayService {

    @POST("/api/next")
    @FormUrlEncoded
    suspend fun next(@Header("token") token: String, @Field("fsId") fsId: String): NextDataResponse
    @POST("/api/items")
    @FormUrlEncoded
    suspend fun items(@Header("token") token: String, @Field("fsId") fsId: String): ItemDataResponse

    companion object {
        fun instance(): PlayService {
            return HiRetrofit.create(PlayService::class.java)
        }
    }
}

data class NextDataResponse(val data: NextData) : BaseResponse()
data class ItemDataResponse(val data: List<ItemData>) : BaseResponse()

data class NextData(
    var fsId: String,
    var playUrl: String,
)

data class ItemData(
    var fsId: String,
    var playUrl: String,
    var path: String,
)