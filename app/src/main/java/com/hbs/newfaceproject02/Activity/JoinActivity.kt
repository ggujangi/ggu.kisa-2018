package com.hbs.newfaceproject02.Activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.hbs.newfaceproject02.Control.EmailLoginControl
import com.hbs.newfaceproject02.R
import kotlinx.android.synthetic.main.activity_join.*
import kotlinx.android.synthetic.main.content_login.*

/**
 * Created by asmwj on 2018-08-10.
 */
class JoinActivity:AppCompatActivity(), View.OnClickListener {
    override fun onClick(p0: View?) {
        when(p0){
            signInCancelBtn->finish()
            signInOkBtn ->{
                val userName = nameInput.text.toString()
                val userEmail = emailInput.text.toString()
                val userPassWord = passwordInput.text.toString()

                EmailLoginControl().createEmailUser(userName, userEmail, userPassWord, this)

                startActivity(Intent(applicationContext, LoginActivity::class.java))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)
        signInCancelBtn.setOnClickListener(this)
        signInOkBtn.setOnClickListener(this)
    }
}