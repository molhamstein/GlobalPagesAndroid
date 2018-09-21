package com.brainsocket.globalpages.di.ui

import com.brainsocket.globalpages.data.entities.BusinessGuide
import com.brainsocket.globalpages.data.entities.SubCategory
import com.brainsocket.globalpages.data.entitiesModel.BusinessGuideModel

class BusinessGuidesContract {
    interface Presenter : BaseContract.Presenter<View> {
        fun loadBusinessGuideList(subCategory: SubCategory)
        fun addBusinessGuide(businessGuide: BusinessGuideModel)
    }

    interface View : BaseContract.View {

        fun showBusinessGuideProgress(show: Boolean) {}
        fun showBusinessGuideLoadErrorMessage(visible: Boolean) {}
        fun showBusinessGuideEmptyView(visible: Boolean) {}
        fun onLoadBusinessGuideListSuccessfully(businessGuideList: MutableList<BusinessGuide>)
        fun onAddBusinessGuideSuccessfully()

    }

}