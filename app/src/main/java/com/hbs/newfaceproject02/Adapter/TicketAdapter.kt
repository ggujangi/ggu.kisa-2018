package com.hbs.newfaceproject02.Adapter

import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.hbs.newfaceproject02.Model.TicketInfo
import com.hbs.newfaceproject02.R
import com.hbs.newfaceproject02.Utils.TimeMaker

/**
 * Created by asmwj on 2018-08-16.
 */

class TicketAdapter(val items: MutableList<TicketInfo>, val context: Context?)
    : RecyclerView.Adapter<TicketAdapter.ItemViewHolder>(){

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ticketLinear: LinearLayout
        var ticketDate: TextView
        var ticketTitle: TextView
        var ticketPlace: TextView
        var totalMoney: TextView
        var ticketDivider:View
        var availArrow:ImageView

        init {
            ticketLinear = itemView.findViewById(R.id.ticketLinear)
            ticketDate = itemView.findViewById(R.id.ticketDate)
            ticketTitle = itemView.findViewById(R.id.ticketTitle)
            ticketPlace = itemView.findViewById(R.id.ticketPlace)
            totalMoney = itemView.findViewById(R.id.totalMoney)
            ticketDivider = itemView.findViewById(R.id.ticketDivider)
            availArrow = itemView.findViewById(R.id.availArrow)
        }
    }
    override fun onBindViewHolder(holder:ItemViewHolder, position: Int) {

        if(position == 0){
            holder.ticketDivider.visibility = View.GONE
        }
        else{
            if(items[position].buyDate==items[position-1].buyDate){
                holder.ticketLinear.visibility = View.GONE
            }
        }

        if(Integer.parseInt(items[position].buyAvailDate.replace(".", ""))<Integer.parseInt(TimeMaker.instance.currentTime_yyyyMMdd_seperate.replace(".", ""))){
            holder.availArrow.setImageDrawable(context!!.resources.getDrawable(R.drawable.failed_arrow))
        }
        else{
            holder.availArrow.setImageDrawable(context!!.resources.getDrawable(R.drawable.timeline_arrow))
            holder.availArrow.setOnClickListener {
                val builder = AlertDialog.Builder(context!!)
                builder.setMessage("이용권을 사용하겠습니까?")
                        .setPositiveButton("예", DialogInterface.OnClickListener { dialogInterface, i ->
                            dialogInterface.dismiss()
                        })
                        .setNegativeButton("아니요", DialogInterface.OnClickListener { dialogInterface, i ->
                            dialogInterface.dismiss()
                        })
                builder.show()
            }
        }
        holder.ticketDate.text = items[position].buyDate
        holder.ticketTitle.text = items[position].tiket
        holder.ticketPlace.text = items[position].buyAvailDate
        holder.totalMoney.text = items[position].ticketCost+"원"
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ticket, parent, false)
        return ItemViewHolder(view)
    }


    fun dateSubString(date:String):String{
        val result:String = date.substring(0,8)

        return result
    }

}