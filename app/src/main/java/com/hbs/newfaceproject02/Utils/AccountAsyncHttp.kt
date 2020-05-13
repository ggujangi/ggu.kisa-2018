package com.hbs.newfaceproject02.Utils

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import com.google.firebase.database.*
import com.hbs.newfaceproject02.Adapter.CardSlidAdapter
import com.hbs.newfaceproject02.Model.GetAllUserInfo
import com.hbs.newfaceproject02.Model.ResListInfo
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/**
 * Created by asmwj on 2018-08-20.
 */
class AccountAsyncHttp(val context: Context, val authCode: String, val requestMethod: String, val code: Int, val accessToken: String) : AsyncTask<String, String, String>() {

    private var restList: MutableList<ResListInfo> = arrayListOf()
    private val firebaseDatabase:FirebaseDatabase = FirebaseDatabase.getInstance()
    private val cardRef: DatabaseReference = firebaseDatabase.getReference().child("userInfo")
    private lateinit var cardSlideAdapter:CardSlidAdapter

    fun setCardListArray(cardList:MutableList<ResListInfo>){
        this.restList = cardList
    }

    fun setCardListAdapter(adapter: CardSlidAdapter){
        this.cardSlideAdapter = adapter
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        jsonParse(result, code)

        if(code==101){
            getCardData()
            cardSlideAdapter.notifyDataSetChanged()
        }
    }

    // code
    override fun doInBackground(vararg p0: String?): String {
        val resultString = sendTokenRequest(p0[0]!!)
        return resultString
    }

    private fun sendTokenRequest(urlStr: String): String {
        // 데이터를 HttpUrlConnection을 통해서 가져온다.
        // @INPUT : url
        // @OUTPUT : resultData

        val output = StringBuilder()
        try {
            val url = URL(urlStr)
            val conn = url.openConnection() as HttpURLConnection
            if (conn != null) {
                conn.connectTimeout = 10000
                conn.requestMethod = requestMethod
                conn.doInput = true
                if (code == 101 || code == 102) {
                    conn.setRequestProperty("Authorization", "Bearer " + accessToken)
                }
                val reader = BufferedReader(InputStreamReader(conn.inputStream))
                var line: String? = null
                while (true) {
                    line = reader.readLine()
                    if (line == null) {
                        break
                    }
                    output.append(line + "\n")
                }

                reader.close()
                conn.disconnect()
            }
        } catch (ex: Exception) {
            Log.e("Connect Error", "Exception in processing response.", ex)
            ex.printStackTrace()
        }

        return output.toString()
    }

    fun getCardData(){
        var cnt = 0;

        cardRef.child(MakeUserDb().makeUserId()).child("resInfo").child("res_list").addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError?) {}

            override fun onDataChange(p0: DataSnapshot?) {

                for(childSnap in p0!!.children){
                    cnt++
                    if(cnt>3){
                    }else{
                        restList.add(ResListInfo(childSnap.child("fintech_use_num").value.toString(),childSnap.child("bank_code_std").value.toString(), childSnap.child("bank_name").value.toString(), childSnap.child("account_num_masked").value.toString(), childSnap.child("account_holder_name").value.toString(), childSnap.child("inquiry_agree_dtime").value.toString()))
                        cardSlideAdapter.notifyDataSetChanged()
                    }
                }
            }
        })
    }
    private fun jsonParse(result: String?, code: Int) {
        // code :
        // /token : 100
        // / me : 101
        // /balance : 102
        if (code == 100) {
            val jsonObj = JSONObject(result)
            val accessToken = jsonObj.optString("access_token")
            val userSeqNo = jsonObj.optString("user_seq_no")
            Log.d("resultString", result)

            val accountRef = FirebaseDatabase.getInstance().getReference().child("userInfo").child(MakeUserDb().makeUserId()).child("accountInfo")
            accountRef.child(authCode).child("accessToken").setValue(accessToken)
            accountRef.child(authCode).child("userSeqNo").setValue(userSeqNo)
        } else if (code == 101) {
            val userResRef = FirebaseDatabase.getInstance().getReference().child("userInfo").child(MakeUserDb().makeUserId()).child("resInfo")

            val jsonObj = JSONObject(result)
            val api_tran_id = jsonObj.optString("api_tran_id")
            val rsp_code = jsonObj.optString("rsp_code")
            val rsp_message = jsonObj.optString("rsp_message")
            val api_tran_dtm = jsonObj.optString("api_tran_dtm")
            val user_seq_no = jsonObj.optString("user_seq_no")
            val user_ci = jsonObj.optString("user_ci")
            val user_name = jsonObj.optString("user_name")
            val res_cnt = jsonObj.optString("res_cnt")


            userResRef.setValue(GetAllUserInfo(api_tran_id, rsp_code, rsp_message,
                    api_tran_dtm, user_seq_no, user_ci, user_name, res_cnt))
            val res_list = jsonObj.optJSONArray("res_list")
            var index = 0
            for (resInfo in 0 until res_list.length()) {
                var resObj = res_list.getJSONObject(resInfo)
                if (resObj != null) {
                    userResRef.child("res_list").child(index.toString()).child("fintech_use_num").setValue(resObj.optString("fintech_use_num"))
                    userResRef.child("res_list").child(index.toString()).child("bank_code_std").setValue(resObj.optString("bank_code_std"))
                    userResRef.child("res_list").child(index.toString()).child("bank_name").setValue(resObj.optString("bank_name"))
                    userResRef.child("res_list").child(index.toString()).child("account_num_masked").setValue(resObj.optString("account_num_masked"))
                    userResRef.child("res_list").child(index.toString()).child("account_holder_name").setValue(resObj.optString("account_holder_name"))
                    userResRef.child("res_list").child(index.toString()).child("inquiry_agree_dtime").setValue(resObj.optString("inquiry_agree_dtime"))
                    index++
                }
            }



        } else if (code == 102) {
            val userResRef = FirebaseDatabase.getInstance().getReference().child("userInfo").child(MakeUserDb().makeUserId()).child("currentAccount")

            if(result!=null){
                val balanceObj = JSONObject(result)
                Log.d("balanceResult", result)
                val balance_amt = balanceObj.optString("balance_amt")
                val available_amt = balanceObj.optString("available_amt")

                userResRef.addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError?) {}

                    override fun onDataChange(p0: DataSnapshot?) {
                        if (!p0!!.hasChildren()) {
                            userResRef.child("balance_amt").setValue(balance_amt)
                            userResRef.child("available_amt").setValue(available_amt)
                        }
                    }

                })
            }
        }
    }
}