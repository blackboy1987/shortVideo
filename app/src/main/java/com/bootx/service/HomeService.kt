package com.bootx.service

import com.bootx.util.HiRetrofit
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST


interface HomeService {

    @POST("/api/category")
    @FormUrlEncoded
    suspend fun category(@Header("token") token: String, @Field("fsId") fsId: String): HomeDataResponse
    @POST("/api/list")
    @FormUrlEncoded
    suspend fun list(@Header("token") token: String, @Field("fsId") fsId: String): List1DataResponse


    companion object {
        fun instance(): HomeService {
            return HiRetrofit.create(HomeService::class.java)
        }
    }
}


data class HomeDataResponse(val data: List<HomeData>) : BaseResponse()
data class List1DataResponse(val data: List<List1Data>) : BaseResponse()

data class HomeData(
    val name: String,
    var fsId: String,
    var category: Int,
    var cover: String,
)

data class List1Data(
    val name: String,
    var fsId: String,
)