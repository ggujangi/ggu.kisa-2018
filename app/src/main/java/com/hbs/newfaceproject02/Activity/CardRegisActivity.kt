package com.hbs.newfaceproject02.Activity

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.hbs.newfaceproject02.Model.CardInfo
import com.hbs.newfaceproject02.R
import com.hbs.newfaceproject02.Utils.MakeUserDb
import kotlinx.android.synthetic.main.activity_cardregis.*

/**
 * Created by asmwj on 2018-08-14.
 */
class CardRegisActivity : AppCompatActivity(), View.OnClickListener {

    //처음 카드를 등록하는 Activity
    private val makeUserId = MakeUserDb()
    private var checkBool = false

    val firebaseDatabase = FirebaseDatabase.getInstance()
    val userRef = firebaseDatabase.getReference().child("userInfo").child(makeUserId.makeUserId()).child("cardInfo")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cardregis)

        val addCard = intent.getIntExtra("addCard", 0)
        if(addCard == 1){

        }else{
            // card 정보가 있을 경우 Main으로 바로 가기
            userRef.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError?) {}

                override fun onDataChange(p0: DataSnapshot?) {
                    if (p0!!.hasChildren()) {
                        val intent = Intent(applicationContext, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            })
        }


        // 뷰에 clickListener set
        enrollCard.setOnClickListener(this)
        enrollClose.setOnClickListener(this)
        allCheck.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0) {
            enrollCard -> {
                // checkBool이 true일 때만 cardInfo에 저장
                if (checkBool) {
                    // card
                    val cardInfo = CardInfo(cardNumEditText.text.toString(), cardCvcNum.text.toString(), cardPeriodNumber.text.toString())
                    makeUserId.makeUserCardDatabase(this, cardInfo)
                }

                // checkBool 이 false일 때 -> 모두 동의를 안 했을 때
                else {

                    // Dialog pop을 띄우는 함수
                    popupExit("약관 동의를 확인해주세요.", false)
                }
            }

            // X 표시를 눌렀을 경우
            enrollClose -> {
                popupExit("카드 등록을 하지 않으시겠습니까?", true)
            }

            // 모두 동의를 체크하면 모두 체크 checkBool 데이터 바꿈
            allCheck -> {
                if (allCheck.isChecked) {
                    check1.isChecked = true
                    check2.isChecked = true
                    checkBool = true
                } else {
                    check1.isChecked = false
                    check2.isChecked = false
                    checkBool = false
                }
            }
        }
    }


    // dialog 띄우는 함수
    // true일 경우 -> MainActivity로 이동
    // false일 경우 -> 그냥 Dialog dismiss
    fun popupExit(message: String, bool: Boolean) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(message)
                .setPositiveButton("예", DialogInterface.OnClickListener({ dialogInterface, i ->
                    if (bool) {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                        userRef.child("noData").setValue(CardInfo("","",""))
                    } else {
                        dialogInterface.dismiss()
                    }
                }))
        if (bool) {
            builder.setNegativeButton("아니오", DialogInterface.OnClickListener({ dialogInterface, i ->
                dialogInterface.dismiss()
            }))
        }
        builder.create().show()
    }


    // 뒤로가기 누르는 override 함수
    override fun onBackPressed() {
        popupExit("카드 등록을 하지 않으시겠습니까?", true)
    }
}