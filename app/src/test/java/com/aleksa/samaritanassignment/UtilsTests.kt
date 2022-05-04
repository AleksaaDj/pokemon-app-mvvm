package com.aleksa.samaritanassignment

import android.text.format.DateUtils
import org.junit.Test
import org.junit.Assert.*
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.text.SimpleDateFormat
import java.util.*

@RunWith(RobolectricTestRunner::class)
class UtilsTests {

    @Test
    fun dateFormatCorrect() {
        val preFormatted = "1997-04-01T13:43:27-06:00"
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss-mm:ss", Locale.ENGLISH)
        sdf.timeZone = TimeZone.getTimeZone("GMT")
        val time: Long? = sdf.parse(preFormatted)?.time
        val now = System.currentTimeMillis()
        val result = time?.let {
            DateUtils.getRelativeTimeSpanString(it, now, DateUtils.MINUTE_IN_MILLIS).toString()
        }
        assertEquals("Apr 1, 1997", result)
    }
}