package com.bootx.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.bootx.service.HomeData
import com.bootx.service.HomeService
import com.bootx.util.CommonUtils
import com.bootx.util.SharedPreferencesUtils

class MainViewModel:ViewModel() {
    private val homeService = HomeService.instance()

    var homeData by mutableStateOf(listOf(HomeData(name = "", fsId = "",category=6,cover="")))

    var list by mutableStateOf(listOf(HomeData(name = "", fsId = "",category=6,cover="")))

    var loading by mutableStateOf(false)
        private set

    suspend fun category(context:Context,fsId: String){
        loading = true
        try {
            val res = homeService.category(SharedPreferencesUtils(context).get("token"),fsId)
            if (res.code == 0) {
                val tmpList = mutableListOf<HomeData>()
                tmpList.addAll(res.data)
                homeData = tmpList
            }else{
                CommonUtils.toast(context,res.msg)
            }
            loading = false
        }catch (e: Exception){
            Log.e("MainViewModel", "load: ${e.message}", )
        }
    }

    suspend fun list(context:Context,fsId: String){
        try {
            val res = homeService.category(SharedPreferencesUtils(context).get("token"),fsId)
            if (res.code == 0) {
                val tmpList = mutableListOf<HomeData>()
                tmpList.addAll(res.data)
                list = tmpList
            }else{
                CommonUtils.toast(context,res.msg)
            }
        }catch (e: Exception){
            Log.e("MainViewModel", "load: ${e.message}", )
        }
    }

}








