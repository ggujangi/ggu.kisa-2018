package com.hbs.newfaceproject02.Control

import android.app.Activity
import android.content.Context
import android.support.v4.app.FragmentActivity
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.hbs.newfaceproject02.Interface.GoogleLogin
import com.hbs.newfaceproject02.R
import com.hbs.newfaceproject02.Utils.StaticVars.Companion.RC_SIGN_IN

/**
 * Created by asmwj on 2018-08-11.
 */
class GoogleLoginControl:GoogleLogin {
    override fun makeClient(context: Context): GoogleApiClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        val googleClient = GoogleApiClient.Builder(context)
                .enableAutoManage(context as FragmentActivity){}
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()

        return googleClient
    }

    override fun googleLogin(context: Context, apiClient: GoogleApiClient) {
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(apiClient)
        (context as Activity).startActivityForResult(signInIntent, RC_SIGN_IN)
    }
}