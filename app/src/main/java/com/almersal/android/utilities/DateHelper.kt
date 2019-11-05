package com.almersal.android.utilities

import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import android.widget.EditText
import com.almersal.android.R
import com.almersal.android.enums.DaysEnum
import com.almersal.android.normalization.DateNormalizer
import java.util.*


object DateHelper {

    val currentDay: DaysEnum
        get() {

            val calendar = Calendar.getInstance()
            val day = calendar.get(Calendar.DAY_OF_WEEK)

            when (day) {
                Calendar.SUNDAY -> return DaysEnum.SunDay
                Calendar.MONDAY -> return DaysEnum.MonDay
                Calendar.TUESDAY -> return DaysEnum.TueDay
                Calendar.WEDNESDAY -> return DaysEnum.WenDay
                Calendar.THURSDAY -> return DaysEnum.ThuDay
                Calendar.FRIDAY -> return DaysEnum.FriDay
                Calendar.SATURDAY -> return DaysEnum.SatDay
            }
            return DaysEnum.SunDay
        }


     fun openDatePicker(editText: EditText,context:Context) {
        val date = GregorianCalendar(Locale.ENGLISH)
        val datePicker = DatePickerDialog(context, R.style.AppTheme_DialogSlideAnimwithback,
            DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, month: Int,
                                                 dayOfMonth: Int ->
                date.set(Calendar.YEAR, year)
                date.set(Calendar.MONTH, month)
                date.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                editText.setText(DateNormalizer.getCanonicalDateFormat(date = date.time,formatString = "yyyy-MM-dd'T'HH:mm:ss"))
                editText.error = null
            }
            , date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH))
        datePicker.show()
    }


}
