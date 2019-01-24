package com.almersal.android.repositories

import com.almersal.android.data.entitiesModel.OpenDayModel
import com.almersal.android.enums.DaysEnum

class SettingData {
    companion object {
        const val pharmacyCategoryId = "5c0e8b3800e72d4b9ea9c49c"
        const val EpsDistance = 0.5

        const val emailAddress = "almersalgroupe@gmail.com"
        const val siteAddress = "http://almersal.co/privacy"

        private val dayList = mutableListOf<OpenDayModel>().apply {
            add(OpenDayModel(DaysEnum.SunDay))
            add(OpenDayModel(DaysEnum.MonDay))
            add(OpenDayModel(DaysEnum.TueDay))
            add(OpenDayModel(DaysEnum.WenDay))
            add(OpenDayModel(DaysEnum.ThuDay))
            add(OpenDayModel(DaysEnum.FriDay))
            add(OpenDayModel(DaysEnum.SatDay))
        }

        fun getOpenDayListWithCombineOriginalList(days: MutableList<String>): MutableList<OpenDayModel> {
            val dayResultList: MutableList<OpenDayModel> = dayList
            try {
                days.forEach {
                    val index: Int = it.toInt() - 1
                    dayResultList[index].isSelected = true
                }
            } catch (ex: Exception) {

            }
            return dayResultList
        }

        fun getOriginalListWithCombineOpenDayList(days: MutableList<OpenDayModel>): MutableList<String> {
            val daysResultList: MutableList<String> = mutableListOf()
            days.forEach {
                if (it.isSelected)
                    daysResultList.add(it.daysEnum.number.toString())
            }
            return daysResultList
        }

    }

}