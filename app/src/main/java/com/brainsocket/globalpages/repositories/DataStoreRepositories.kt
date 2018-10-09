package com.brainsocket.globalpages.repositories

import android.content.Context
import com.brainsocket.globalpages.data.entities.BusinessGuideCategory
import com.brainsocket.globalpages.data.entities.City
import com.brainsocket.globalpages.data.entities.LocationEntity
import com.brainsocket.globalpages.data.entities.PostCategory
import com.google.gson.Gson

/**
 * Created by Adhamkh on 2018-08-08.
 */
class DataStoreRepositories constructor(context: Context) : Repository(context) {

    companion object {
        var BusinessCategories_TAG: String = "BusinessCategories_Tag"
        var BusinessCategories_Default_TAG: String = ""

        var PostCategories_TAG: String = "PostCategories_Tag"
        var PostCategories_Default_TAG: String = ""

        var Cities_TAG: String = "Cities_Tag"
        var Cities_Default_TAG: String = ""
    }

    /*Business Categories Started*/
    fun putBusinessCategories(businessList: MutableList<BusinessGuideCategory>) {
        editor.putString(BusinessCategories_TAG, Gson().toJson(businessList)).apply()
    }

    fun getBusinessCategories(): MutableList<BusinessGuideCategory>? {
        var businessList: MutableList<BusinessGuideCategory>? = null
        var pojo = pref.getString(BusinessCategories_TAG, BusinessCategories_Default_TAG)
        if (pojo.isNotEmpty())
            businessList = Gson().fromJson(pojo, Array<BusinessGuideCategory>::class.java).toMutableList()
        return businessList
    }

    fun flushBusinessCategories() {
        editor.putString(BusinessCategories_TAG, BusinessCategories_Default_TAG)
    }

    /*Business Categories Ended*/


    /*Post Categories started*/
    fun putPostCategories(businessList: MutableList<PostCategory>) {
        editor.putString(PostCategories_TAG, Gson().toJson(businessList)).apply()
    }

    fun getPostCategories(): MutableList<PostCategory>? {
        var businessList: MutableList<PostCategory>? = null
        var pojo = pref.getString(PostCategories_TAG, PostCategories_Default_TAG)
        if (pojo.isNotEmpty())
            businessList = Gson().fromJson(pojo, Array<PostCategory>::class.java).toMutableList()
        return businessList
    }

    fun flushPostCategories() {
        editor.putString(PostCategories_TAG, PostCategories_Default_TAG)
    }

    /*Post Categories ended*/


    /*Cities started*/
    fun putCities(cityList: MutableList<City>) {
        editor.putString(Cities_TAG, Gson().toJson(cityList)).apply()
    }

    fun getCities(): MutableList<City>? {
        var cityList: MutableList<City>? = null
        var pojo = pref.getString(Cities_TAG, Cities_Default_TAG)
        if (pojo.isNotEmpty())
            cityList = Gson().fromJson(pojo, Array<City>::class.java).toMutableList()
        return cityList
    }

    fun flushCity() {
        editor.putString(Cities_TAG, Cities_Default_TAG)
    }

    fun findCityById(cityId: String): City? {
        val cityList = getCities()
        cityList?.forEach {
            if (it.id.equals(cityId, true))
                return it
        }
        return null
    }

    fun findLocationById(cityId: String, locateId: String): LocationEntity? {
        val city: City? = findCityById(cityId)
        city?.locations?.forEach {
            if (it.id.equals(locateId, true)) {
                return it
            }
        }
        return null
    }
    /*Cities ended*/

}