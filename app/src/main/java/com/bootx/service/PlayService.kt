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

    companion object {
        fun instance(): PlayService {
            return HiRetrofit.create(PlayService::class.java)
        }
    }
}

data class NextDataResponse(val data: NextData) : BaseResponse()

data class NextData(
    var fsId: String,
    var playUrl: String,
)