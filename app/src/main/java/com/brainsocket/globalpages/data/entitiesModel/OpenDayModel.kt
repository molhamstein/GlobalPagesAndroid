package com.brainsocket.globalpages.data.entitiesModel

import com.brainsocket.globalpages.R
import com.brainsocket.globalpages.enums.DaysEnum

class OpenDayModel( val daysEnum: DaysEnum, var isSelected: Boolean = false) {


    fun getOpenDayStringName(): Int =
            when (daysEnum) {
                DaysEnum.SunDay -> {
                    R.string.Sunday
                }
                DaysEnum.MonDay -> {
                    R.string.Monday
                }
                DaysEnum.TueDay -> {
                    R.string.Tueday
                }
                DaysEnum.WenDay -> {
                    R.string.Wenday
                }
                DaysEnum.ThuDay -> {
                    R.string.Thuday
                }
                DaysEnum.FriDay -> {
                    R.string.Friday
                }
                DaysEnum.SatDay -> {
                    R.string.Satday
                }
            }


}