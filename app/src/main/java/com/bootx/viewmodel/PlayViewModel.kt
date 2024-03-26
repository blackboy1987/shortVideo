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

    var list by mutableStateOf(listOf(ItemData(id = "")))

    var playUrl by mutableStateOf("")

    var loading by mutableStateOf(false)
        private set

    suspend fun items(context:Context,id: String){
        loading = true
        try {
            val res = playService.items(SharedPreferencesUtils(context).get("token"),id)
            if (res.code == 0) {
                val tmpList = mutableListOf<ItemData>()
                tmpList.addAll(res.data)
                list = tmpList
                playUrl = playService.play(SharedPreferencesUtils(context).get("token"), list[0].id).data
            }else{
                CommonUtils.toast(context,res.msg)
            }
            loading = false
        }catch (e: Exception){
            Log.e("MainViewModel", "load: ${e.message}", )
        }
    }

    suspend fun play(context:Context,id: String): String{
        val playUrl = SharedPreferencesUtils(context).get(id)
        if(playUrl!=""){
            return playUrl
        }
        try {
            val res = playService.play(SharedPreferencesUtils(context).get("token"),id)
            return if (res.code == 0) {
                SharedPreferencesUtils(context).set(id,res.data)
                res.data
            }else{
                CommonUtils.toast(context,res.msg)
                ""
            }
        }catch (e: Exception){
            Log.e("MainViewModel", "load: ${e.message}", )
        }
        return ""
    }
}








