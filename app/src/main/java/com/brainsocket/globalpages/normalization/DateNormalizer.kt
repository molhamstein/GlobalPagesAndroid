package com.brainsocket.globalpages.normalization

import android.text.format.DateFormat;
import java.util.*

/**
 * Created by Adhamkh on 2018-06-15.
 */
class DateNormalizer {
    companion object {

        fun getCanonicalDateTimeFormat(date: Date): String {//2018-06-15T05:10:27.290Z
            var result: String = DateFormat.format("yyyy-MM-dd'T'HH:mm:sss", date).toString()
            return result
        }

        fun getCanonicalDateFormat(date: Date): String {//2018-06-15T05:10:27.290Z
            var result: String = DateFormat.format("yyyy-MM-dd", date).toString()
            return result
        }

    }
}