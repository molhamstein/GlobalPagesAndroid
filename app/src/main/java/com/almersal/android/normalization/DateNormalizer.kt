package com.almersal.android.normalization

import android.text.format.DateFormat;
import java.text.SimpleDateFormat
import java.util.*


class DateNormalizer {
    companion object {

        fun getCanonicalDateTimeFormat(date: Date): String {//2018-06-15T05:10:27.290Z

            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss", Locale.ENGLISH)
            return dateFormat.format(date)

//            val result: String = DateFormat.format("yyyy-MM-dd'T'HH:mm:sss", date).toString()
//            return result
        }

        fun getCanonicalDateTime(string: String?): String {
            try {
                if (string == null)
                    return ""
                val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
                val date = format.parse(string)

                return DateFormat.format("yyyy-MM-dd", date).toString()
            } catch (ex: Exception) {
            }
            return string!!
        }


        fun getCanonicalDateFormat(date: Date, formatString: String? = null): String {//2018-06-15T05:10:27.290Z
            val dateFormat = SimpleDateFormat(formatString ?: "yyyy-MM-dd", Locale.ENGLISH)
            return dateFormat.format(date)

//            val result: String = DateFormat.format("yyyy-MM-dd", date).toString()
//            return result
        }

        fun getCustomFormate(fromDate: String?, formatString: String): String {
            val mainFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
            val date = mainFormat.parse(fromDate)

            val toFormate = SimpleDateFormat(formatString, Locale.ENGLISH)
            return toFormate.format(date)


        }

        fun getCanonicalDateFormat(string: String?): String {
            try {
                if (string == null)
                    return ""
                val format = SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH)
                val date = format.parse(string)

                val result: String = DateFormat.format("yyyy-MM-dd", date).toString()
                return result
            } catch (ex: Exception) {
            }
            return string!!
        }

    }

}