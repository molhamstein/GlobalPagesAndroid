package com.almersal.android.di.ui

import com.almersal.android.api.ApiService
import com.almersal.android.api.ServerInfo
import com.almersal.android.data.entities.*
import com.almersal.android.repositories.DataStoreRepositories
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener

class TagsCollectionContact {
    interface Presenter : BaseContract.Presenter<View> {

        fun loadBundle(withCache: Boolean) {
            loadBusinessCategories(withCache)
            loadPostCategories(withCache)
            loadCities(withCache)
        }

        fun loadBusinessCategories(withCache: Boolean)
        fun loadPostCategories(withCache: Boolean)
        fun loadCities(withCache: Boolean)
        fun loadProductCategories()
        fun getJobCategories(withCache: Boolean)
    }

    interface View : BaseContract.View {
        fun onBusinessCategoriesLoaded(categoriesList: MutableList<BusinessGuideCategory>) {}
        fun onPostCategoriesLoaded(categoriesList: MutableList<PostCategory>) {}
        fun onCitiesLoaded(citiesList: MutableList<City>) {}


        fun showBusinessCategoriesProgress(show: Boolean) {}
        fun showBusinessCategoriesLoadErrorMessage(visible: Boolean) {}
        fun showBusinessCategoriesEmptyView(visible: Boolean) {}

        fun showPostCategoriesProgress(show: Boolean) {}
        fun showPostCategoriesLoadErrorMessage(visible: Boolean) {}
        fun showPostCategoriesEmptyView(visible: Boolean) {}



        fun showCitiesProgress(show: Boolean) {}
        fun showCitiesLoadErrorMessage(visible: Boolean) {}
        fun showCitiesEmptyView(visible: Boolean) {}


        fun showJobCategoriesLoadErrorMessage(visible: Boolean) {}
        fun showJobCategoriesEmptyView(visible: Boolean) {}
        fun showJobCategoriesProgress(visible: Boolean) {}
        fun onJobCategoriesLoaded(categoriesList: MutableList<JobCategory>) {}

    }
}