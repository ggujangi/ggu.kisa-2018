package com.hbs.newfaceproject02.Control

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.hbs.newfaceproject02.Activity.LoginActivity
import com.hbs.newfaceproject02.Activity.MainActivity
import com.hbs.newfaceproject02.Interface.EmailLogin

/**
 * Created by asmwj on 2018-09-26.
 */


class EmailLoginControl : EmailLogin {
    private val mAuth = FirebaseAuth.getInstance()

    override fun emailLogin(userEmail: String, userPw: String, context: Context) {
        mAuth.signInWithEmailAndPassword(userEmail, userPw)
                .addOnCompleteListener { task: Task<AuthResult> ->
                    if(task.isSuccessful){
                        val emailLoginIntent = Intent(context, MainActivity::class.java)
                        context.startActivity(emailLoginIntent)
                        (context as Activity).finish()
                    }
                    else{
                        Toast.makeText(context, "Login Failed!", Toast.LENGTH_SHORT).show()
                    }
                }
    }

    override fun createEmailUser(username: String, userEmail: String, userPassword: String, context: Context) {
        mAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener { task: Task<AuthResult> ->
                    if (task.isSuccessful) {
                        val gotoLoginIntent = Intent(context, LoginActivity::class.java)
                        context.startActivity(gotoLoginIntent)
                        (context as Activity).finish()

                        mAuth.currentUser!!.updateProfile(userProfile(username, "https://firebasestorage.googleapis.com/v0/b/naeilallimproject.appspot.com/o/bike_profile.png?alt=media&token=c35ccc89-7f41-474a-a6bb-78a6903eba09")
                        ).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                            }
                        }
                    } else {
                        Toast.makeText(context, "Authentication failed",
                                Toast.LENGTH_SHORT).show()
                    }

                }
    }

    override fun deleteUser(context: Context) {
    }


    fun userProfile(name: String, image: String): UserProfileChangeRequest {

        return UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .setPhotoUri(Uri.parse(image))
                .build()
    }
}