package com.hbs.newfaceproject02.Activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.hbs.newfaceproject02.Adapter.CardSlidAdapter
import com.hbs.newfaceproject02.Model.ResListInfo
import com.hbs.newfaceproject02.R
import com.hbs.newfaceproject02.Utils.AccountAsyncHttp
import com.hbs.newfaceproject02.Utils.MakeUserDb
import kotlinx.android.synthetic.main.activity_mypage.*
import java.net.URL

/**
 * Created by asmwj on 2018-08-14.
 */
class MyPageActivity:AppCompatActivity() {
    private val firebaseDatabase:FirebaseDatabase = FirebaseDatabase.getInstance()
    private val cardRef:DatabaseReference = firebaseDatabase.getReference().child("userInfo")
    private lateinit var cardSlideAdapter:CardSlidAdapter

    val cardList = mutableListOf<ResListInfo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mypage)
        val colorList = mutableListOf<Int>()
        colorList.add(R.color.cardColor)
        colorList.add(R.color.yelloCard)


        cardSlideAdapter = CardSlidAdapter(this, cardList, colorList)
        cardViewPager.adapter = cardSlideAdapter
        cardTab.setupWithViewPager(cardViewPager, true)

        logoutLinear.setOnClickListener {
            val mAuth = FirebaseAuth.getInstance()
            mAuth.signOut()
            val mainIntent = Intent(this, LoginActivity::class.java)
            startActivity(mainIntent)
            finish()
        }


        cardEditTLinear.setOnClickListener {
            val intent = Intent(this, FavorActivity::class.java)
            startActivity(intent)
        }

        myTicketLinear.setOnClickListener {
            val ticketIntent = Intent(this, TicketActivity::class.java)
            startActivity(ticketIntent)
        }
        timelineLinear.setOnClickListener {
            val timeIntent = Intent(this, TimeLineActivity::class.java)
            startActivity(timeIntent)
        }

        setSupportActionBar(myPageToolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = null


        val userAccountRef = FirebaseDatabase.getInstance().getReference().child("userInfo").child(MakeUserDb().makeUserId()).child("accountInfo")
        userAccountRef.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError?) {}

            override fun onDataChange(p0: DataSnapshot?) {
                for(childSnap in p0!!.children){
                    Log.d("childSnap", childSnap.child("accessToken").value.toString())
                    val userAccountUrl = URL("https://testapi.open-platform.or.kr/user/me?user_seq_no=${childSnap.child("userSeqNo").value.toString()}")
                    val userInfoHttp = AccountAsyncHttp(applicationContext, childSnap.child("authCode").value.toString(), "GET", 101, childSnap.child("accessToken").value.toString())
                    userInfoHttp.setCardListArray(cardList)
                    userInfoHttp.setCardListAdapter(cardSlideAdapter)
                    userInfoHttp.execute(userAccountUrl.toString())
                }
            }
        })

/*
        val balanceUrl = URL("https://testapi.open-platform.or.kr/v1.0/account/balance?fintech_use_num=199002759057723684743759&tran_dtime=${TimeMaker.instance.currentTime_yyyyMMddHHmmss}")
        val balanceHttp = AccountAsyncHttp(applicationContext, "", "GET",102, "74b7b8dd-1bb7-4f81-953d-def216975fc0")
        balanceHttp.execute(balanceUrl.toString())*/
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> { //toolbar의 back키 눌렀을 때 동작
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}