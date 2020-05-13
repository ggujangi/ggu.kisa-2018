package com.hbs.newfaceproject02.Activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import com.hbs.newfaceproject02.Model.TicketInfo
import com.hbs.newfaceproject02.R
import com.hbs.newfaceproject02.Utils.MakeUserDb
import kotlinx.android.synthetic.main.activity_password.*

/**
 * Created by asmwj on 2018-09-30.
 */
class PasswordActivity : AppCompatActivity() {

    val myTicketRef = FirebaseDatabase.getInstance().getReference().child("ticketInfo").child(MakeUserDb().makeUserId())

    private var ticketKind = ""
    private var ticketCost = ""
    private var ticketAvalDate = ""
    private var ticketBuyDate = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password)

        ticketKind = getIntent().getStringExtra("ticketKind")
        ticketCost = getIntent().getStringExtra("ticketCost")
        ticketAvalDate = getIntent().getStringExtra("ticketAvalDate")
        ticketBuyDate = getIntent().getStringExtra("ticketBuyDate")
        pinview.setPinViewEventListener { pinview, b ->
            val goToMain = Intent(applicationContext, MainActivity::class.java)
            myTicketRef.push().setValue(TicketInfo(ticketKind,ticketBuyDate,ticketAvalDate, ticketCost))
            startActivity(goToMain)
            Toast.makeText(applicationContext, "결제가 완료되었습니다.", Toast.LENGTH_SHORT).show()
        }
    }
}