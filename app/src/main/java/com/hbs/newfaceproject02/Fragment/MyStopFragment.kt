package com.hbs.newfaceproject02.Fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.hbs.newfaceproject02.Adapter.FavorRecycleAdapter
import com.hbs.newfaceproject02.Model.StopInfo
import com.hbs.newfaceproject02.R
import com.hbs.newfaceproject02.Utils.MakeUserDb

/**
 * Created by asmwj on 2018-08-16.
 */
class MyStopFragment : Fragment() {
    private lateinit var mystopRecyclerView: RecyclerView
    private lateinit var myStopAdapter: FavorRecycleAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_mystop, null)
        init(view)
        val stopArray = getMyStopData()

        myStopAdapter = FavorRecycleAdapter(stopArray, context)
        mystopRecyclerView.adapter = myStopAdapter

        return view
    }

    fun init(view: View) {
        mystopRecyclerView = view.findViewById(R.id.mystopRecyclerView)
    }

    fun getMyStopData(): MutableList<StopInfo> {
        val stopArray = mutableListOf<StopInfo>()
        val myStopRef = FirebaseDatabase.getInstance().getReference("userInfo").child(MakeUserDb().makeUserId()).child("favorInfo")
        myStopRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {}

            override fun onDataChange(p0: DataSnapshot?) {
                for (childSnap in p0!!.children) {
                    Log.d("chidSnapShot", childSnap.key.toString())
                    val name = childSnap.key.toString()
                    val total = childSnap.value.toString()
                    val remain = "5"
                    val favorStopInfo = StopInfo(name, remain, total)
                    stopArray.add(favorStopInfo)

                    if (myStopAdapter != null) {
                        myStopAdapter.notifyItemInserted(stopArray.size - 1)
                    }
                }
            }
        })
        return stopArray
    }
}