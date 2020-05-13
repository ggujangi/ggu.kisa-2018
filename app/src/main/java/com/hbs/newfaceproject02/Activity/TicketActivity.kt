package com.hbs.newfaceproject02.Activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.hbs.newfaceproject02.Adapter.TicketAdapter
import com.hbs.newfaceproject02.Model.TicketInfo
import com.hbs.newfaceproject02.R
import com.hbs.newfaceproject02.Utils.MakeUserDb
import kotlinx.android.synthetic.main.activity_timeline.*

/**
 * Created by asmwj on 2018-08-21.
 */
class TicketActivity:AppCompatActivity() {
    private val ticketDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val ticketRef: DatabaseReference = ticketDatabase.getReference().child("ticketInfo").child(MakeUserDb().makeUserId())
    private lateinit var ticketAdapter: TicketAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticket)


        val ticketList = mutableListOf<TicketInfo>()

        ticketRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {}

            override fun onDataChange(p0: DataSnapshot?) {
                for(childSnap in p0!!.children){
                    ticketList.add(TicketInfo(childSnap.child("tiket").value.toString(), childSnap.child("buyDate").value.toString(), childSnap.child("buyAvailDate").value.toString(),childSnap.child("ticketCost").value.toString()))
                    if(ticketAdapter!=null)
                        ticketAdapter.notifyItemInserted(ticketList.size-1)
                }
            }
        })


        cancelBtn.setOnClickListener {
            finish()
        }

        val linearLayoutMang = LinearLayoutManager(this)
        linearLayoutMang.orientation = LinearLayoutManager.VERTICAL
        timelineRecycler.layoutManager = linearLayoutMang
        ticketAdapter = TicketAdapter(ticketList, this)
        timelineRecycler.adapter = ticketAdapter
    }
}