package com.bootx.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.bootx.service.ItemData
import com.bootx.service.ListData
import com.bootx.service.PlayService
import com.bootx.util.CommonUtils
import com.bootx.util.SharedPreferencesUtils

class PlayViewModel:ViewModel() {
    private val playService = PlayService.instance()

    var list by mutableStateOf(listOf(ItemData(playUrl = "", fsId = "",path="")))

    var loading by mutableStateOf(false)
        private set

    suspend fun items(context:Context,fsId: String){
        loading = true
        try {
            val res = playService.items(SharedPreferencesUtils(context).get("token"),fsId)
            if (res.code == 0) {
                val tmpList = mutableListOf<ItemData>()
                tmpList.addAll(res.data)
                list = tmpList
            }else{
                CommonUtils.toast(context,res.msg)
            }
            loading = false
        }catch (e: Exception){
            Log.e("MainViewModel", "load: ${e.message}", )
        }
    }

    suspend fun getNext(context:Context,fsId: String): String{
        val playUrl = SharedPreferencesUtils(context).get(fsId)
        if(playUrl!=""){
            return playUrl
        }
        try {
            val res = playService.next(SharedPreferencesUtils(context).get("token"),fsId)
            if (res.code == 0) {
                SharedPreferencesUtils(context).set(res.data.fsId+"",res.data.playUrl)
                return res.data.fsId
            }else{
                CommonUtils.toast(context,res.msg)
                return ""
            }
        }catch (e: Exception){
            Log.e("MainViewModel", "load: ${e.message}", )
        }
        return ""
    }
}








