package com.bootx.util

import android.content.Context
import android.widget.Toast


object CommonUtils {

    fun toast(context: Context,msg: String) {
        Toast.makeText(context,msg,Toast.LENGTH_LONG).show()
    }

    fun getToken(context: Context): String{
       return SharedPreferencesUtils(context).get("token")
    }
}