package com.bootx.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.bootx.service.ListData
import com.bootx.service.ListService
import com.bootx.util.CommonUtils
import com.bootx.util.SharedPreferencesUtils

class ListViewModel:ViewModel() {
    private val listService = ListService.instance()

    var list by mutableStateOf(listOf(ListData(name = "", fsId = "",category=6,cover="")))

    var loading by mutableStateOf(false)
        private set

    suspend fun list(context:Context,fsId: String){
        loading = true
        try {
            val res = listService.list(SharedPreferencesUtils(context).get("token"),fsId)
            if (res.code == 0) {
                val tmpList = mutableListOf<ListData>()
                tmpList.addAll(res.data)
                list = tmpList
            }else{
                CommonUtils.toast(context,res.msg)
            }
            loading = false
        }catch (e: Exception){
            Log.e("ListViewModel", "load: ${e.message}", )
            loading = false
        }
    }


    suspend fun getPlayUrl(context:Context,fsId: String){
        try {
            val res = listService.getPlayUrl(SharedPreferencesUtils(context).get("token"),fsId)
            if (res.code == 0) {
                SharedPreferencesUtils(context).set(fsId+"",res.data)
            }else{
                CommonUtils.toast(context,res.msg)
            }
        }catch (e: Exception){
            Log.e("MainViewModel", "load: ${e.message}", )
        }
    }


    suspend fun getAllPlayUrl(context:Context,fsId: String){
        try {
            val res = listService.getAllPlayUrl(SharedPreferencesUtils(context).get("token"),fsId)
            if (res.code == 0) {
                SharedPreferencesUtils(context).set(fsId+"",res.data)
            }else{
                CommonUtils.toast(context,res.msg)
            }
        }catch (e: Exception){
            Log.e("MainViewModel", "load: ${e.message}", )
        }
    }
}








