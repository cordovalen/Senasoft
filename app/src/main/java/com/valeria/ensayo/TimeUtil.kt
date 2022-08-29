package com.valeria.ensayo

import java.util.*

object TimeUtil {
    private val MS_TO_SECOND = 1000

    private val SECOND_TO_MINUTE = 60

    private val SECOND_TO_HOUR = 60*60

    fun formatLongToTimeStr(time:Int):String {

        val totalSeconds = time / MS_TO_SECOND

        val seconds = totalSeconds % SECOND_TO_MINUTE

        var minutes = totalSeconds / SECOND_TO_MINUTE

        val hours = totalSeconds / SECOND_TO_HOUR

        return if(hours>0){
            minutes %= SECOND_TO_MINUTE
            String.format(Locale.ENGLISH,"%d:%02d:%02d",hours,minutes,seconds)
        }else{
            String.format(Locale.ENGLISH,"%02d:%02d",minutes,seconds)
        }
    }
}