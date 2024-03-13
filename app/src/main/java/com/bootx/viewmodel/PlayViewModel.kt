package com.bootx.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.bootx.service.PlayService
import com.bootx.util.CommonUtils
import com.bootx.util.SharedPreferencesUtils

class PlayViewModel:ViewModel() {
    private val playService = PlayService.instance()

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








