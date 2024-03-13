package com.bootx.util

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager


class SharedPreferencesUtils(private val context: Context) {
    fun set(key: String,value: String){

        val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val edit = sharedPreferences.edit()
        edit.putString(key,value)
        edit.apply()
    }

    fun get(key: String): String {
        val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        if(key=="homeIndex"){
            return sharedPreferences.getString(key,"0") ?: "0"
        }
        return sharedPreferences.getString(key,"") ?: ""
    }

    fun remove(key: String?) {
        val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val edit = sharedPreferences.edit()
        if(key!=null){
            edit.remove(key)
        }else{
            edit.clear()
        }
        edit.apply()
    }
}