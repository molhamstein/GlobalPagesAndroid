package com.brainsocket.globalpages.data.validations

import android.text.TextUtils
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Created by Adhamkh on 2018-06-16.
 */
class ValidationHelper {
    companion object {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        val DATE_PATTERN = "(0?[1-9]|1[012]) [/.-] (0?[1-9]|[12][0-9]|3[01]) [/.-] ((19|20)\\d\\d)"

        fun isEmail(value: String): Boolean {
            if (value.matches(Regex(emailPattern)))
                return true
            return false
        }

        fun isEmpty(value: String): Boolean {
            return value.isEmpty()
        }

        fun isNullorEmpty(value: String?): Boolean {
            return value.isNullOrEmpty()
        }

        fun validate(date: String): Boolean {
            var pattern: Pattern = Pattern.compile(DATE_PATTERN)
            var matcher = pattern.matcher(date)

            if (matcher.matches()) {
                matcher.reset()

                if (matcher.find()) {
                    val day: String = matcher.group(1)
                    val month: String = matcher.group(2)
                    val year = Integer.parseInt(matcher.group(3))

                    return if (day == "31" && (month == "4" || month == "6" || month == "9" ||
                                    month == "11" || month == "04" || month == "06" ||
                                    month == "09")) {
                        false // only 1,3,5,7,8,10,12 has 31 days
                    } else if (month == "2" || month == "02") {
                        //leap year
                        if (year % 4 == 0) {
                            if (day == "30" || day == "31") {
                                return false
                            } else {
                                return true
                            }
                        } else {
                            if (day == "29" || day == "30" || day == "31") {
                                return false
                            } else {
                                return true
                            }
                        }
                    } else {
                        return true
                    }
                } else {
                    return false
                }
            } else {
                return false
            }
        }

        fun isDate(value: String): Boolean {
            var matcher = Pattern.compile(DATE_PATTERN).matcher(value)
            return matcher.matches() //&& validate(value)
        }

    }
}