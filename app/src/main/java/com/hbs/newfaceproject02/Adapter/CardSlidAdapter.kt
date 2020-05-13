package com.hbs.newfaceproject02.Adapter

import android.content.Context
import android.content.Intent
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.hbs.newfaceproject02.Activity.CardRegisActivity
import com.hbs.newfaceproject02.Model.ResListInfo
import com.hbs.newfaceproject02.R

/**
 * Created by asmwj on 2018-08-16.
 */


class CardSlidAdapter(private val context: Context, private val cardInfos: MutableList<ResListInfo>, val colorList:MutableList<Int>) : PagerAdapter() {
    private var cardImage: ImageView? = null
    private var cardName: TextView? = null
    private var cardNumber: TextView? = null
    private var cardPeriodNumber: TextView? = null
    private var cardLinear: LinearLayout? = null

    private var addCardLinear:LinearLayout?=null
    private var addCardBtn: ImageView?=null

    private var sb: StringBuffer? = null
    private var sb2: StringBuffer? = null

    private var cnt = 0


    override fun getCount(): Int {
        return cardInfos.size
    }


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(context).inflate(R.layout.item_card, container, false)!!
        init(view)

        if(position%2==0){
            cardLinear!!.setBackgroundColor(context.resources.getColor(colorList[0]))
        }
        else{
            cardLinear!!.setBackgroundColor(context.resources.getColor(colorList[1]))
        }

        cardLinear!!.visibility = View.VISIBLE
        addCardLinear!!.visibility = View.GONE
        if(cardInfos[position]!=null){
            cardName!!.text = cardInfos[position].bank_name
            cardNumber!!.text = cardInfos[position].account_num_masked
            cardPeriodNumber!!.text = makeDate(cardInfos[position].inquiry_agree_dtime.substring(4,8))
        }

        addCardBtn!!.setOnClickListener {
            val intent = Intent(context, CardRegisActivity::class.java)
            intent.putExtra("addCard", 1)
            context.startActivity(intent)
        }

        container.addView(view)

        return view
    }

    fun init(view: View) {
        cardLinear = view.findViewById(R.id.cardLinear)
        cardImage = view.findViewById(R.id.cardLogo)
        cardName = view.findViewById(R.id.cardNameText)
        cardNumber = view.findViewById(R.id.cardNumber)
        cardPeriodNumber = view.findViewById(R.id.cardPeriodNumber)

        addCardBtn = view.findViewById(R.id.addCardBtn)
        addCardLinear = view.findViewById(R.id.addCardLinear)
    }

    fun makeDate(subDate:String):String{
        val dateBuffer:StringBuffer = StringBuffer()
        val dateList = subDate.split("")

        for(timeIndex in dateList.indices){
            if(timeIndex==2){
                dateBuffer.append(dateList[timeIndex]).append("/")
            }else{
                dateBuffer.append(dateList[timeIndex])
            }
        }
        return dateBuffer.toString()
    }
}
