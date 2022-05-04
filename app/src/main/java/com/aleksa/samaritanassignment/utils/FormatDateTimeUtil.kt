package com.aleksa.samaritanassignment.utils

import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.*

class FormatDateTimeUtil {

    fun parseSimpleDate(dateTime: String): String? {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss-mm:ss", Locale.ENGLISH)
        sdf.timeZone = TimeZone.getTimeZone("GMT")
        val time: Long? = sdf.parse(dateTime)?.time
        val now = System.currentTimeMillis()
        return time?.let {
            DateUtils.getRelativeTimeSpanString(it, now, DateUtils.MINUTE_IN_MILLIS).toString()
        }
    }
}