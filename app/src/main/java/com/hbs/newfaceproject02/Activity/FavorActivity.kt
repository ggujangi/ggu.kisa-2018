package com.hbs.newfaceproject02.Activity

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.hbs.newfaceproject02.Adapter.FavorAdapter
import com.hbs.newfaceproject02.R
import kotlinx.android.synthetic.main.activity_favor.*

/**
 * Created by asmwj on 2018-08-14.
 */
class FavorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favor)

        setSupportActionBar(favorToolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = null

        val favorAdapter = FavorAdapter(supportFragmentManager, favorTabLayout.tabCount)
        favorViewPager.adapter = favorAdapter
        favorTabLayout.setupWithViewPager(favorViewPager)
        favorTabLayout.getTabAt(0)!!.text = "내 정류장"
        favorTabLayout.getTabAt(1)!!.text = "경로 이력"

        favorTabLayout.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {}

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabSelected(tab: TabLayout.Tab?) {
                favorViewPager.setCurrentItem(tab!!.position)
            }
        })
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