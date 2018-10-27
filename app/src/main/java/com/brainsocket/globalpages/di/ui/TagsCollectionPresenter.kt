package com.brainsocket.globalpages.di.ui

import android.content.Context
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.brainsocket.globalpages.api.ApiService
import com.brainsocket.globalpages.api.ServerInfo
import com.brainsocket.globalpages.data.entities.BusinessGuideCategory
import com.brainsocket.globalpages.data.entities.City
import com.brainsocket.globalpages.data.entities.PostCategory
import com.brainsocket.globalpages.repositories.DataStoreRepositories
import com.brainsocket.globalpages.repositories.UserRepository
import io.reactivex.disposables.CompositeDisposable
import java.util.HashMap

/**
 * Created by Adhamkh on 2018-08-09.
 */
class TagsCollectionPresenter constructor(val context: Context) : TagsCollectionContact.Presenter {


    private val subscriptions = CompositeDisposable()
    private lateinit var view: TagsCollectionContact.View

    init {

    }

    override fun attachView(view: TagsCollectionContact.View) {
        this.view = view
    }

    override fun subscribe() {

    }

    override fun unSubscribe() {
        subscriptions.clear()
    }

    override fun loadBusinessCategories(withCache: Boolean) {
        view.showBusinessCategoriesProgress(true)
        if (withCache) {
            val categoriesList = DataStoreRepositories(context).getBusinessCategories()
            if (categoriesList != null) {
                view.showBusinessCategoriesProgress(false)
                view.onBusinessCategoriesLoaded(categoriesList.filter { it.parentCategoryId.isNullOrEmpty() }.toMutableList())
                return
            }
        }

        val criteriaCategory: MutableMap<String, String> = HashMap()//[ownerId]
        criteriaCategory["include"] = "subCategories"

        //businessCategoriesUrl
        ApiService().getBusinessCategories(ServerInfo.businessCategoriesUrl, criteriaCategory, object : ParsedRequestListener<MutableList<BusinessGuideCategory>> {
            override fun onResponse(response: MutableList<BusinessGuideCategory>?) {
                view.showProgress(false)
                if (response != null) {
                    DataStoreRepositories(context).putBusinessCategories(response)
                    view.onBusinessCategoriesLoaded(response)
                } else {
                    view.showBusinessCategoriesEmptyView(true)
                }
            }

            override fun onError(anError: ANError?) {
                view.showBusinessCategoriesProgress(false)
                view.showBusinessCategoriesLoadErrorMessage(true)
            }
        })

    }

    override fun loadPostCategories(withCache: Boolean) {
        view.showPostCategoriesProgress(true)
        if (withCache) {
            var categoriesList = DataStoreRepositories(context).getPostCategories()
            if (categoriesList != null) {
                view.showPostCategoriesProgress(false)
                view.onPostCategoriesLoaded(categoriesList)
                return
            }
        }

        val criteriaCategory: MutableMap<String, String> = HashMap()//[ownerId]
        criteriaCategory["include"] = "subCategories"

        ApiService().getPostCategories(ServerInfo.postCategoriesUrl, criteriaCategory, object : ParsedRequestListener<MutableList<PostCategory>> {
            override fun onResponse(response: MutableList<PostCategory>?) {
                view.showPostCategoriesProgress(false)
                if (response != null) {
                    DataStoreRepositories(context).putPostCategories(response)
                    view.onPostCategoriesLoaded(response)
                } else {
                    view.showPostCategoriesEmptyView(true)
                }
            }

            override fun onError(anError: ANError?) {
                view.showPostCategoriesProgress(false)
                view.showPostCategoriesLoadErrorMessage(true)
            }
        })
    }

    override fun loadCities(withCache: Boolean) {
        view.showCitiesProgress(true)
        if (withCache) {
            var cityList = DataStoreRepositories(context).getCities()
            if (cityList != null) {
                view.showCitiesProgress(false)
                view.onCitiesLoaded(cityList)
                return
            }
        }

        val criteriaCity: MutableMap<String, String> = HashMap()//[ownerId]
        criteriaCity["include"] = "locations"

        ApiService().getCities(ServerInfo.citiesUrl, criteriaCity, object : ParsedRequestListener<MutableList<City>> {
            override fun onResponse(response: MutableList<City>?) {
                view.showCitiesProgress(false)
                if (response != null) {
                    DataStoreRepositories(context).putCities(response)
                    view.onCitiesLoaded(response)
                } else {
                    view.showCitiesEmptyView(true)
                }
            }

            override fun onError(anError: ANError?) {
                view.showCitiesProgress(false)
                view.showCitiesLoadErrorMessage(true)
            }
        })

    }

}