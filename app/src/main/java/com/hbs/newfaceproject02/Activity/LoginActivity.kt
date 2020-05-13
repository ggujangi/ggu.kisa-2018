package com.hbs.newfaceproject02.Activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.google.android.gms.auth.api.Auth
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.hbs.newfaceproject02.Control.EmailLoginControl
import com.hbs.newfaceproject02.Control.GoogleLoginControl
import com.hbs.newfaceproject02.R
import com.hbs.newfaceproject02.Utils.MakeUserDb
import com.hbs.newfaceproject02.Utils.StaticVars.Companion.RC_SIGN_IN
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.content_login.*

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private val mAuth = FirebaseAuth.getInstance()
    private lateinit var mAuthListener: FirebaseAuth.AuthStateListener


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailJoinBtn.setOnClickListener(this)
        googleLogin.setOnClickListener(this)
        emailLoginBtn.setOnClickListener(this)

        // 자동 로그인을 위한 mAuthListener
        mAuthListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                // User is signed in
                val i = Intent(this@LoginActivity, TestWebViewActivity::class.java)
                startActivity(i)
                finish()
            } else {
                // User is signed out
            }
            // ...
        }
    }

    override fun onStart() {
        super.onStart()
        mAuth.addAuthStateListener(mAuthListener)
    }
    override fun onClick(p0: View?) {
        when (p0) {
            emailJoinBtn -> {
                val intent = Intent(this, JoinActivity::class.java)
                startActivity(intent)
            }

            emailLoginBtn->{
                val userEmail = emailInput.text.toString()
                val userPassword = passwordInput.text.toString()
                EmailLoginControl().emailLogin(userEmail, userPassword, this)
            }

            googleLogin ->{
                val googleLoginCon = GoogleLoginControl()
                googleLoginCon.googleLogin(this, googleLoginCon.makeClient(this))
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result.isSuccess) {
                val account = result.signInAccount
                val credential = GoogleAuthProvider.getCredential(account!!.idToken, null)

                mAuth.signInWithCredential(credential)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                val makeUserId = MakeUserDb()
                                makeUserId.makeUserDatabase()
                                val loginIntent = Intent(applicationContext, TestWebViewActivity::class.java)
                                loginIntent.putExtra("firstBool", true)
                                startActivity(loginIntent)
                                finish()
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(applicationContext, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show()
                            }
                        }
            } else {}
        }
    }
}