package com.hbs.newfaceproject02.Activity

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.hbs.newfaceproject02.R
import com.hbs.newfaceproject02.Utils.TimeMaker
import kotlinx.android.synthetic.main.activity_buy_ticket.*
import kotlinx.android.synthetic.main.item_card.*

/**
 * Created by asmwj on 2018-09-29.
 */
class BuyTicketActivity : AppCompatActivity() {
    private var ticketKind = ""
    private var ticketCost = ""
    private var ticketBuyDate = ""
    private var ticketAvalDate = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buy_ticket)

        setSupportActionBar(buyTicketToolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = null
        ticketDateText.text = "${TimeMaker.instance.currentTime_yyyyMMdd_seperate}"
        ticketBuyDate = TimeMaker.instance.currentTime_yyyyMMdd_seperate
        ticketKind = getKind(0)
        ticketCost = getCost(0)
        ticketAvalDate = getAvailDate(0)
        cardNameText.text = "KB국민은행"
        cardNumber.text = "46020204005***"
        cardPeriodNumber.text = "08/21"


        setTicketKindLinear.setOnClickListener {
            val items = arrayOf<CharSequence>("1시간 (1,000원)", "2시간 (2,000원)", "7일권 (3,000원)", "30일권 (5,000원)", "180일권 (15,000원)", "365일권 (30,000원)")

            val builder = AlertDialog.Builder(this)
            var myIndex = 0
            // 알림창의 속성 설정
            builder.setTitle("이용권을 선택하세요")
                    .setSingleChoiceItems(items, -1) { dialog, index ->
                        // 목록 클릭시 설정
                        myIndex = index
                    }
                    .setPositiveButton("확인", DialogInterface.OnClickListener { dialogInterface, i ->
                        ticketKindText.text = "${getKind(myIndex)}"
                        ticketCostText.text = "${getCost(myIndex)}원"

                        ticketKind = getKind(myIndex)
                        ticketCost = getCost(myIndex)
                        ticketAvalDate = getAvailDate(myIndex)

                    })
                    .setNegativeButton("취소", DialogInterface.OnClickListener { dialogInterface, i ->
                        dialogInterface.dismiss()
                    })

            // 알림창 객체 생성
            val dialog = builder.create()
            dialog.show()
        }

        buyTicketBtn.setOnClickListener {
            val intent = Intent(this, PasswordActivity::class.java)
            intent.putExtra("ticketKind", ticketKind)
            intent.putExtra("ticketCost", ticketCost)
            intent.putExtra("ticketAvalDate", ticketAvalDate)
            intent.putExtra("ticketBuyDate", ticketBuyDate)
            startActivity(intent)
        }

    }


    fun getCost(position: Int): String {
        when (position) {
            0 -> return "1,000"
            1 -> return "2,000"
            2 -> return "3,000"
            3 -> return "5,000"
            4 -> return "15,000"
            5 -> return "30,000"
            else -> return "0원"
        }
    }

    fun getKind(position: Int): String {
        when (position) {
            0 -> return "1시간 이용권"
            1 -> return "2시간 이용권"
            2 -> return "7일 이용권"
            3 -> return "30일 이용권"
            4 -> return "180일 이용권"
            5 -> return "365일 이용권"
            else -> return "없음"
        }
    }

    fun getAvailDate(position: Int): String {
        when (position) {
            0 -> return TimeMaker.instance.currentTime_yyyyMMdd_seperate
            1 -> return TimeMaker.instance.currentTime_yyyyMMdd_seperate
            2 -> return TimeMaker.instance.currentTime_yyyyMMdd_seperate_7
            3 -> return TimeMaker.instance.currentTime_yyyyMMdd_seperate_30
            4 -> return TimeMaker.instance.currentTime_yyyyMMdd_seperate_180
            5 -> return TimeMaker.instance.currentTime_yyyyMMdd_seperate_365
            else -> return TimeMaker.instance.currentTime_yyyyMMdd_seperate
        }
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