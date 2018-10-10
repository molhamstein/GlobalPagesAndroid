package com.brainsocket.globalpages.di.ui

import com.brainsocket.globalpages.data.entities.BusinessGuide
import com.brainsocket.globalpages.data.entities.PointEntity
import com.brainsocket.globalpages.data.entities.SubCategory
import com.brainsocket.globalpages.data.entitiesModel.BusinessGuideModel
import com.brainsocket.globalpages.enums.DaysEnum

class BusinessGuidesContract {
    interface Presenter : BaseContract.Presenter<View> {

        /*for business guide action*/
        fun loadBusinessGuideByLocation(pointEntity: PointEntity)

        /*for pharmacy duty action*/
        fun loadBusinessGuideForPharmacy(pointEntity: PointEntity, daysEnum: DaysEnum)

        /*for nearby action*/
        fun loadBusinessGuideList(subCategory: SubCategory)


        fun addBusinessGuide(businessGuide: BusinessGuideModel, token: String)
    }

    interface View : BaseContract.View {

        fun showBusinessGuideProgress(show: Boolean) {}
        fun showBusinessGuideLoadErrorMessage(visible: Boolean) {}
        fun showBusinessGuideEmptyView(visible: Boolean) {}
        fun onLoadBusinessGuideListSuccessfully(businessGuideList: MutableList<BusinessGuide>)
        fun onAddBusinessGuideSuccessfully() {}

    }

}