package com.almersal.android.repositories

import android.content.Context
import com.almersal.android.data.entities.*
import com.google.gson.Gson

class DataStoreRepositories constructor(context: Context) : Repository(context) {

    companion object {
        var BusinessCategories_TAG: String = "BusinessCategories_Tag"
        var BusinessCategories_Default_TAG: String = ""

        var PostCategories_TAG: String = "PostCategories_Tag"
        var PostCategories_Default_TAG: String = ""

        var Cities_TAG: String = "Cities_Tag"
        var Cities_Default_TAG: String = ""

        var JobCategories_TAG: String = "JobCategories_Tag"
        var JobCategories_Default_TAG: String = ""
    }

    /*JobBusiness Categories Started*/
    fun putBusinessCategories(businessList: MutableList<BusinessGuideCategory>) {
        editor.putString(BusinessCategories_TAG, Gson().toJson(businessList)).apply()
    }

    fun getBusinessCategories(): MutableList<BusinessGuideCategory>? {
        var businessList: MutableList<BusinessGuideCategory>? = null
        val poJo = pref.getString(BusinessCategories_TAG, BusinessCategories_Default_TAG)
        if (poJo.isNotEmpty())
            businessList = Gson().fromJson(poJo, Array<BusinessGuideCategory>::class.java).toMutableList()
        return businessList
    }

    fun flushBusinessCategories() {
        editor.putString(BusinessCategories_TAG, BusinessCategories_Default_TAG)
    }

    /*JobBusiness Categories Ended*/


    /*Post Categories started*/
    fun putPostCategories(businessList: MutableList<PostCategory>) {
        editor.putString(PostCategories_TAG, Gson().toJson(businessList)).apply()
    }

    fun getPostCategories(): MutableList<PostCategory>? {
        var businessList: MutableList<PostCategory>? = null
        val pojo = pref.getString(PostCategories_TAG, PostCategories_Default_TAG)
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
        val pojo = pref.getString(Cities_TAG, Cities_Default_TAG)
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


    fun putJobCategories(businessList: MutableList<JobCategory>) {
        editor.putString(JobCategories_TAG, Gson().toJson(businessList)).apply()
    }

    fun getJobCategories(): MutableList<JobCategory>? {
        var jobList: MutableList<JobCategory>? = null
        val poJo = pref.getString(JobCategories_TAG, JobCategories_Default_TAG)
        if (poJo.isNotEmpty())
            jobList = Gson().fromJson(poJo, Array<JobCategory>::class.java).toMutableList()
        return jobList
    }

}