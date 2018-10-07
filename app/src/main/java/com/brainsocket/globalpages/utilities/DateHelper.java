package com.brainsocket.globalpages.utilities;

import com.brainsocket.globalpages.enums.DaysEnum;

import java.util.Calendar;

/**
 * Created by Adhamkh on 2018-10-06.
 */

public class DateHelper {

    public static DaysEnum getCurrentDay() {

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case Calendar.SUNDAY:
                return DaysEnum.SunDay;
            case Calendar.MONDAY:
                return DaysEnum.MonDay;
            case Calendar.TUESDAY:
                return DaysEnum.TueDay;
            case Calendar.WEDNESDAY:
                return DaysEnum.WenDay;
            case Calendar.THURSDAY:
                return DaysEnum.ThuDay;
            case Calendar.FRIDAY:
                return DaysEnum.FriDay;
            case Calendar.SATURDAY:
                return DaysEnum.SatDay;
        }
        return DaysEnum.SunDay;
    }

}
