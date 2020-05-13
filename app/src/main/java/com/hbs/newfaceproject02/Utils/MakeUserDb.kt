package com.hbs.newfaceproject02.Utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.hbs.newfaceproject02.Activity.MainActivity
import com.hbs.newfaceproject02.Model.CardInfo

/**
 * Created by asmwj on 2018-08-14.
 */
class MakeUserDb {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    // user의 email을 고유 id 로 만드는 함수
    // asmwj3@naver.com 에서 "@"와 "." 를 ""로 치환
    fun makeUserId():String{
        val result = (firebaseAuth.currentUser!!.email)!!.replace(".", "").replace("@", "")

        return result
    }

    // 사용자가 로그인을 할 경우, Firebase에 사용자 정보 저장하는 함수
    fun makeUserDatabase(){
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val userRef = firebaseDatabase.getReference().child("userInfo") // 현재 경로는 "userInfo"

        userRef.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError?) {}

            override fun onDataChange(p0: DataSnapshot?) {
                // p0 는 "userInfo"
                // p0.child("asmwj3gmailcom") 에 자식이 없을 경우 == 데이터가 없을 경우에만 setValue()
                // 이렇게 하면 다음 로그인 때 userInfo에 사용자 정보를 저장하지 않음
                if(!p0!!.child(makeUserId()).hasChildren()){
                    userRef.child(makeUserId()).child("userId").setValue(makeUserId())
                }
            }
        })
    }

    // 카드를 등록할 때 사용자의 카드 정보를 저장하는 함수
    fun makeUserCardDatabase(context:Context, cardInfo:CardInfo){
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val cardRef = firebaseDatabase.getReference().child("userInfo").child(makeUserId()).child("cardInfo") // "userInfo"->"asmwj3gmailcom"->"cardInfo"

        cardRef.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError?) {}

            override fun onDataChange(p0: DataSnapshot?) {
                // p0 는 "cardInfo"
                // 마찬가지로 "asmwj3gmailcom"->"cardInfo" 에 자식이 없을 경우 == 등록된 카드가 없을 경우에만 setValue()
                if(!p0!!.hasChildren()){
                    cardRef.child(cardInfo.cardNum).setValue(cardInfo)
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                    (context as Activity).finish()
                }
                else{
                    // 자식이 있을 경우 -> 후에 카드 추가 등록
                    // for문을 통해 이미 있는 카드 번호인지 검사함.
                    for(childSnap in p0!!.children){
                        if(childSnap.key.toString().equals(cardInfo.cardNum)){
                            // Toast.makeText(context, "이미 등록하신 카드입니다.", Toast.LENGTH_SHORT).show()
                        }

                        // 처음 등록하는 카드일 경우, 데이터를 저장하고 Main으로 돌아가기
                        else{
                            cardRef.child(cardInfo.cardNum).setValue(cardInfo)
                            val intent = Intent(context, MainActivity::class.java)
                            context.startActivity(intent)
                            (context as Activity).finish()
                        }
                    }
                }
            }
        })
    }
}