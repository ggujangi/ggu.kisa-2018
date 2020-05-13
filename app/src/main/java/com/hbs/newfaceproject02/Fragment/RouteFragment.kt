package com.hbs.newfaceproject02.Fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.hbs.newfaceproject02.Adapter.RouteHistoryAdapter
import com.hbs.newfaceproject02.Model.RouteInfo
import com.hbs.newfaceproject02.R

/**
 * Created by asmwj on 2018-08-16.
 */
class RouteFragment : Fragment() {
    private lateinit var routeRecycler:RecyclerView
    private lateinit var routeAdapter:RouteHistoryAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_route, null)
        init(view)
        val routeArray = addRouteData()
        routeAdapter = RouteHistoryAdapter(routeArray, context)
        routeRecycler.adapter = routeAdapter

        return view
    }

    fun init(view:View){
        routeRecycler = view.findViewById(R.id.routeRecycler)
    }

    fun addRouteData():MutableList<RouteInfo>{
        val routeArray = mutableListOf<RouteInfo>()
        val routeRef = FirebaseDatabase.getInstance().getReference().child("userlog").child("user")

        routeRef.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError?) { }

            override fun onDataChange(p0: DataSnapshot?) {
                for(childSnap in p0!!.children){
                    if(childSnap.key.contains("Route Search")){
                        val routeInfo = RouteInfo(childSnap.child("arrival").value.toString(), childSnap.child("date").value.toString(), childSnap.child("departure").value.toString())
                        routeArray.add(routeInfo)
                        if(routeAdapter!=null)
                            routeAdapter.notifyItemInserted(routeArray.size-1)
                    }
                }
            }
        })

        return routeArray

    }
}