package com.codepath.apps.restclienttemplate.models

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class timeformatter {
    /**
     * Given a date String of the format given by the Twitter API, returns a display-formatted
     * String representing the relative time difference, e.g. "2m", "6d", "23 May", "1 Jan 14"
     * depending on how great the time difference between now and the given date is.
     * This, as of 2016-06-29, matches the behavior of the official Twitter app.
     */
    object TimeFormatter {
        fun getTimeDifference(rawJsonDate: String?): String {
            var time = ""
            val twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy"
            val format = SimpleDateFormat(twitterFormat, Locale.ENGLISH)
            format.setLenient(true)
            try {
                val diff: Long =
                    (System.currentTimeMillis() - format.parse(rawJsonDate).getTime()) / 1000
                if (diff < 5) time = "Just now" else if (diff < 60) time = java.lang.String.format(
                    Locale.ENGLISH,
                    "%ds",
                    diff
                ) else if (diff < 60 * 60) time = java.lang.String.format(
                    Locale.ENGLISH,
                    "%dm",
                    diff / 60
                ) else if (diff < 60 * 60 * 24) time = java.lang.String.format(
                    Locale.ENGLISH,
                    "%dh",
                    diff / (60 * 60)
                ) else if (diff < 60 * 60 * 24 * 30) time =
                    java.lang.String.format(Locale.ENGLISH, "%dd", diff / (60 * 60 * 24)) else {
                    val now: Calendar = Calendar.getInstance()
                    val then: Calendar = Calendar.getInstance()
                    then.setTime(format.parse(rawJsonDate))
                    time = if (now.get(Calendar.YEAR) === then.get(Calendar.YEAR)) {
                        (java.lang.String.valueOf(then.get(Calendar.DAY_OF_MONTH)).toString() + " "
                                + then.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US))
                    } else {
                        (java.lang.String.valueOf(then.get(Calendar.DAY_OF_MONTH)).toString() + " "
                                + then.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US)
                                + " " + java.lang.String.valueOf(then.get(Calendar.YEAR) - 2000))
                    }
                }
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return time
        }

        /**
         * Given a date String of the format given by the Twitter API, returns a display-formatted
         * String of the absolute date of the form "30 Jun 16".
         * This, as of 2016-06-30, matches the behavior of the official Twitter app.
         */
        fun getTimeStamp(rawJsonDate: String?): String {
            var time = ""
            val twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy"
            val format = SimpleDateFormat(twitterFormat, Locale.ENGLISH)
            format.setLenient(true)
            try {
                val then: Calendar = Calendar.getInstance()
                then.setTime(format.parse(rawJsonDate))
                val date: Date = then.getTime()
                val format1 = SimpleDateFormat("h:mm a \u00b7 dd MMM yy")
                time = format1.format(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return time
        }
    }
}