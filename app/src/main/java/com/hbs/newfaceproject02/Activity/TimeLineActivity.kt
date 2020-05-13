package com.hbs.newfaceproject02.Activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.firebase.database.*
import com.hbs.newfaceproject02.Adapter.TimeLineAdapter
import com.hbs.newfaceproject02.Model.TimeLineInfo
import com.hbs.newfaceproject02.R
import kotlinx.android.synthetic.main.activity_timeline.*

/**
 * Created by asmwj on 2018-08-16.
 */
class TimeLineActivity:AppCompatActivity() {
    private val timelineDatabse:FirebaseDatabase = FirebaseDatabase.getInstance()
    private val timelineRef:DatabaseReference = timelineDatabse.getReference().child("userlog").child("user").child("timeline")
    private lateinit var timelineAdapter:TimeLineAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timeline)
        val timelineList = mutableListOf<TimeLineInfo>()

        timelineRef.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError?) {}

            override fun onDataChange(p0: DataSnapshot?) {
                for(childSnap in p0!!.children){
                    timelineList.add(TimeLineInfo(childSnap.key.toString(), childSnap.child("end").value.toString(), childSnap.child("spend").value.toString(), childSnap.child("start").value.toString(), childSnap.child("money").value.toString()))
                    if(timelineAdapter!=null)
                        timelineAdapter.notifyItemInserted(timelineList.size-1)
                }
            }
        })

        cancelBtn.setOnClickListener {
            finish()
        }

        timelineAdapter = TimeLineAdapter(timelineList, this)
        timelineRecycler.adapter = timelineAdapter

    }
}