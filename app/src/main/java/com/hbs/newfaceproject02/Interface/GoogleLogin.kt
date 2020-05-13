package com.hbs.newfaceproject02.Interface

import android.content.Context
import com.google.android.gms.common.api.GoogleApiClient

/**
 * Created by asmwj on 2018-08-11.
 */
interface GoogleLogin {
    fun makeClient(context: Context):GoogleApiClient
    fun googleLogin(context:Context, apiClient:GoogleApiClient)
}