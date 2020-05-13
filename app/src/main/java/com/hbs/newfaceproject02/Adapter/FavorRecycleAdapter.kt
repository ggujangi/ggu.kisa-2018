package com.hbs.newfaceproject02.Adapter

import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.hbs.newfaceproject02.Model.StopInfo
import com.hbs.newfaceproject02.R

/**
 * Created by asmwj on 2018-08-17.
 */
class FavorRecycleAdapter (val items: MutableList<StopInfo>, val context: Context?)
    : RecyclerView.Adapter<FavorRecycleAdapter.ItemViewHolder>() {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.remainCycle.text = items[position].remain
        holder.totalCycle.text = items[position].total
        holder.favorName.text = items[position].name

        holder.cancelFavor.setOnClickListener {
            val builder = AlertDialog.Builder(context!!)
            builder.setMessage("즐겨찾기를 해제하시겠습니까?")
                    .setPositiveButton("확인", DialogInterface.OnClickListener { dialogInterface, i -> 
                        dialogInterface.dismiss()
                    })
                    .setNegativeButton("취소", DialogInterface.OnClickListener { dialogInterface, i ->
                        dialogInterface.dismiss()
                    })
            builder.create().show()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_favor, parent, false)
        return ItemViewHolder(view)
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val favorName:TextView
        val remainCycle:TextView
        val totalCycle:TextView
        val cancelFavor:ImageView
        init {
            favorName = itemView.findViewById(R.id.favorName)
            remainCycle = itemView.findViewById(R.id.remainCycle)
            totalCycle = itemView.findViewById(R.id.totalCycle)
            cancelFavor = itemView.findViewById(R.id.cancelFavor)
        }
    }

}