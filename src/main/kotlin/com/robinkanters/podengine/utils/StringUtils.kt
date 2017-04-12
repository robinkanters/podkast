package com.robinkanters.podengine.utils

import com.robinkanters.podengine.exceptions.DateFormatException
import java.lang.IllegalArgumentException
import java.net.URL

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date

object StringUtils {
    private val dateFormats = arrayOf(
            SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z"),
            SimpleDateFormat("dd MMM yyyy HH:mm:ss Z"),
            SimpleDateFormat("EEE, dd MMM yyyy HH:mm Z"),
            SimpleDateFormat("dd MMM yyyy HH:mm Z")
    )

    @JvmName("stringToDate")
    @JvmStatic
    @Throws(DateFormatException::class)
    fun String.toDate(): Date {
        dateFormats.forEach { df ->
            try {
                return df.parse(this)
            } catch (e: ParseException) {
                // This format didn't work, keep going
            }
        }

        throw DateParseException("Could not parse date $this")
    }

    @JvmName("stringToURL")
    @JvmStatic
    fun String.toURL(): URL {
        return URL(this)
    }
}
