package com.hbs.newfaceproject02.Adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.hbs.newfaceproject02.Fragment.MyStopFragment
import com.hbs.newfaceproject02.Fragment.RouteFragment

/**
 * Created by asmwj on 2018-08-17.
 */
class FavorAdapter(fragmentManager: FragmentManager, val tabCount:Int): FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        when(position){
            0 -> return MyStopFragment()
            1 -> return RouteFragment()
            else -> return RouteFragment()
        }
    }

    override fun getCount(): Int {
        return tabCount
    }

}