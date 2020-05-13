package com.hbs.newfaceproject02.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hbs.newfaceproject02.Model.RouteInfo
import com.hbs.newfaceproject02.R

/**
 * Created by asmwj on 2018-08-17.
 */
class RouteHistoryAdapter(val items: MutableList<RouteInfo>, val context: Context?)
    : RecyclerView.Adapter<RouteHistoryAdapter.ItemViewHolder>() {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.routeDate.text = makeDate(items[position].date)
        holder.arrivalLocation.text = items[position].arrival
        holder.departureLocation.text = items[position].departure
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_route, parent, false)

        return ItemViewHolder(view)
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val routeDate: TextView
        val arrivalLocation: TextView
        val departureLocation: TextView
        init {
            routeDate = itemView.findViewById(R.id.routeDate)
            arrivalLocation = itemView.findViewById(R.id.arrivalLocation)
            departureLocation = itemView.findViewById(R.id.departureLocation)
        }
    }
    fun makeDate(subDate:String):String{
        val dateBuffer:StringBuffer = StringBuffer()
        val dateList = subDate.split("")

        for(timeIndex in dateList.indices){
            if(timeIndex==4 || timeIndex==6){
                dateBuffer.append(dateList[timeIndex]).append(".")
            }else{
                dateBuffer.append(dateList[timeIndex])
            }
        }
        return dateBuffer.toString()
    }
}