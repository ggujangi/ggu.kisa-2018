package com.hbs.newfaceproject02.Interface

import android.content.Context

/**
 * Created by asmwj on 2018-09-26.
 */


interface EmailLogin {
    fun emailLogin(userEmail:String, userPw:String, context: Context)
    fun createEmailUser(username:String, userEmail:String, userPassword:String, context: Context)
    fun deleteUser(context: Context)
}