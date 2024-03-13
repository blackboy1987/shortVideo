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


    companion object {
        fun instance(): HomeService {
            return HiRetrofit.create(HomeService::class.java)
        }
    }
}


data class HomeDataResponse(val data: List<HomeData>) : BaseResponse()

data class HomeData(
    val name: String,
    var fsId: String,
    var category: Int,
    var cover: String,
)