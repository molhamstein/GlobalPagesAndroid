package com.brainsocket.globalpages.di.ui

import com.brainsocket.globalpages.data.entities.BusinessGuideCategory
import com.brainsocket.globalpages.data.entities.City
import com.brainsocket.globalpages.data.entities.PostCategory

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
    }

    interface View : BaseContract.View {
        fun onBusinessCategoriesLoaded(categoriesList: MutableList<BusinessGuideCategory>){}
        fun onPostCategoriesLoaded(categoriesList: MutableList<PostCategory>){}
        fun onCitiesLoaded(citiesList: MutableList<City>){}

        fun showBusinessCategoriesProgress(show: Boolean){}
        fun showBusinessCategoriesLoadErrorMessage(visible: Boolean){}
        fun showBusinessCategoriesEmptyView(visible: Boolean) {}

        fun showPostCategoriesProgress(show: Boolean){}
        fun showPostCategoriesLoadErrorMessage(visible: Boolean){}
        fun showPostCategoriesEmptyView(visible: Boolean) {}


        fun showCitiesProgress(show: Boolean){}
        fun showCitiesLoadErrorMessage(visible: Boolean){}
        fun showCitiesEmptyView(visible: Boolean) {}
    }
}