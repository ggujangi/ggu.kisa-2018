package com.hbs.newfaceproject02.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.hbs.newfaceproject02.Model.TimeLineInfo
import com.hbs.newfaceproject02.R

/**
 * Created by asmwj on 2018-08-16.
 */

class TimeLineAdapter (val items: MutableList<TimeLineInfo>, val context: Context?)
    : RecyclerView.Adapter<TimeLineAdapter.ItemViewHolder>(){

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var timelineLinear:LinearLayout
        var timelineDate:TextView
        var startTime:TextView
        var endTime:TextView
        var totalTime:TextView
        var totalMoney:TextView
        var timelineDivider:View

        init {
            timelineLinear = itemView.findViewById(R.id.timelineLinear)
            startTime = itemView.findViewById(R.id.startTime)
            endTime = itemView.findViewById(R.id.endTime)
            totalTime = itemView.findViewById(R.id.totalTime)
            totalMoney = itemView.findViewById(R.id.totalMoney)
            timelineDate = itemView.findViewById(R.id.timelineDate)
            timelineDivider = itemView.findViewById(R.id.timelineDivider)
        }
    }
    override fun onBindViewHolder(holder:ItemViewHolder, position: Int) {

        if(position == 0){
            holder.timelineDivider.visibility = View.GONE
        }
        else{
            if(dateSubString(items[position].date) == dateSubString(items[position-1].date)){
                holder.timelineLinear.visibility = View.GONE
            }
        }
        holder.timelineDate.text = makeDate(dateSubString(items[position].date))
        holder.startTime.text = makeTIme(items[position].start)
        holder.endTime.text = makeTIme(items[position].end)
        holder.totalTime.text = items[position].spend + "분"
        holder.totalMoney.text = items[position].money +"원"
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_timeline, parent, false)
        return ItemViewHolder(view)
    }

    fun makeTIme(time:String):String{
        val timeBuffer:StringBuffer = StringBuffer()
        val timeList = time.split("")

        for(timeIndex in timeList.indices){
            if(timeIndex==2){
                timeBuffer.append(timeList[timeIndex]).append(":")
            }else{
                timeBuffer.append(timeList[timeIndex])
            }
        }
        return timeBuffer.toString()
    }

    fun dateSubString(date:String):String{
        val result:String = date.substring(0,8)

        return result
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