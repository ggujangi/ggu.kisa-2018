package com.hbs.newfaceproject02.Utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by asmwj on 2018-08-21.
 */
class TimeMaker {

    val cal = Calendar.getInstance()

    val currentTime_yyyyMMddHHmmss: String
        get() {
            val currentTime = Date()
            val yyyyMMddHHmmss = SimpleDateFormat("yyyyMMddHHmmss")
            return yyyyMMddHHmmss.format(currentTime)
        }

    val currentTime_yyyyMMdd: String
        get() {
            val currentTime = Date()
            val yyyyMMddHHmmss = SimpleDateFormat("yyyyMMdd")
            return yyyyMMddHHmmss.format(currentTime)
        }

    val currentTime_yyyyMMdd_seperate: String
        get() {
            val currentTime = Date()
            val yyyyMMddSep = SimpleDateFormat("yyyy.MM.dd")
            return yyyyMMddSep.format(currentTime)
        }

    val currentTime_yyyyMMdd_seperate_7: String
        get() {
            val currentTime = Date()
            cal.time = currentTime
            cal.add(Calendar.DAY_OF_MONTH, 7)
            val yyyyMMddSep = SimpleDateFormat("yyyy.MM.dd")
            return yyyyMMddSep.format(cal.time)
        }

    val currentTime_yyyyMMdd_seperate_30: String
        get() {
            val currentTime = Date()
            cal.time = currentTime
            cal.add(Calendar.DAY_OF_MONTH, 30)
            val yyyyMMddSep = SimpleDateFormat("yyyy.MM.dd")
            return yyyyMMddSep.format(cal.time)
        }


    val currentTime_yyyyMMdd_seperate_180: String
        get() {
            val currentTime = Date()
            cal.time = currentTime
            cal.add(Calendar.DAY_OF_MONTH, 180)
            val yyyyMMddSep = SimpleDateFormat("yyyy.MM.dd")
            return yyyyMMddSep.format(cal.time)
        }

    val currentTime_yyyyMMdd_seperate_365: String
        get() {
            val currentTime = Date()
            cal.time = currentTime
            cal.add(Calendar.DAY_OF_MONTH, 365)
            val yyyyMMddSep = SimpleDateFormat("yyyy.MM.dd")
            return yyyyMMddSep.format(cal.time)
        }

    companion object {
        private var timeMaker: TimeMaker? = null

        val instance: TimeMaker
            get() {
                if (timeMaker == null)
                    timeMaker = TimeMaker()

                return timeMaker!!
            }
    }

}