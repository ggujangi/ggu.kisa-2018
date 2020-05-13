package com.hbs.newfaceproject02.Activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.hbs.newfaceproject02.R
import kotlinx.android.synthetic.main.activity_community.*

/**
 * Created by asmwj on 2018-09-30.
 */
class CommunityActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community)

        setSupportActionBar(communityToolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = null
        commuDetail_0.setOnClickListener {
            goToDetailCommu(0)
        }

        commuDetail_1.setOnClickListener {
            goToDetailCommu(1)
        }
        commuDetail_2.setOnClickListener {
            goToDetailCommu(2)
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

    fun goToDetailCommu(selectInt:Int){
        val intent = Intent(this, CommunityDetailActivity::class.java)
        intent.putExtra("selectInt", selectInt)
        startActivity(intent)
    }
}