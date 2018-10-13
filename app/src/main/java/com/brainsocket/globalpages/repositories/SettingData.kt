package com.brainsocket.globalpages.repositories

import com.brainsocket.globalpages.data.entitiesModel.OpenDayModel
import com.brainsocket.globalpages.enums.DaysEnum

/**
 * Created by Adhamkh on 2018-10-06.
 */
class SettingData {
    companion object {
        const val pharmacyCategoryId = "5b825cda4892087d4b0bac95"
        const val EpsDistance = 0.5

        val dayList = mutableListOf<OpenDayModel>().apply {
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