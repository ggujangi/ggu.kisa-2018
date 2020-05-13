package com.hbs.newfaceproject02.Control

import android.content.Context
import com.hbs.newfaceproject02.Model.BikeStopInfo
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.Charset

/**
 * Created by asmwj on 2018-09-22.
 */
class CvsControl {
    val stopList = mutableListOf<BikeStopInfo>()

    fun readCvsFile(context: Context): MutableList<BikeStopInfo> {
        val inputStram = context.assets.open("seoul_bike.csv")
        val bufferedReader = BufferedReader(InputStreamReader(inputStram, Charset.forName("x-windows-949")))

        var line: String = ""
        try {
            bufferedReader.read()

            while ((bufferedReader.readLine()) != null) {
                line = bufferedReader.readLine()

                line = line.substring(line.indexOf("\"") + 1, line.length - 1)
                val tokens = line.split(",")

                val bikeStopInfo = BikeStopInfo(tokens[0], tokens[2], tokens[3], tokens[4], tokens[5], tokens[6], tokens[7], tokens[11])
                stopList.add(bikeStopInfo)
            }
        } catch (exception: Exception) {
        }

        return stopList
    }
}