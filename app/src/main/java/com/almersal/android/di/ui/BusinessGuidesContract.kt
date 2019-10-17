package com.almersal.android.di.ui

import com.almersal.android.data.entities.BusinessGuide
import com.almersal.android.data.entities.PointEntity
import com.almersal.android.data.entities.SubCategory
import com.almersal.android.data.entitiesModel.BusinessGuideEditModel
import com.almersal.android.data.entitiesModel.BusinessGuideModel
import com.almersal.android.data.filtration.FilterEntity
import com.almersal.android.enums.DaysEnum

class BusinessGuidesContract {

    interface Presenter : BaseContract.Presenter<View> {

        /*for business guide action*/
        fun loadBusinessGuideByLocationWithLimit(pointEntity: PointEntity)
        fun loadBusinessGuideByLocation(
            pointEntity: PointEntity,
            limit: Int,
            skip: Int,
            filterEntity: FilterEntity?
        )

        /*for pharmacy duty action*/
        fun loadBusinessGuideForPharmacy(pointEntity: PointEntity, daysEnum: DaysEnum, limit: Int, skip: Int)

        /*for nearby action*/
        fun loadBusinessGuideList(subCategory: SubCategory)
        fun loadBusinessGuideListWithLimit(subCategory: SubCategory)

        fun loadBusinessGuideByLocationAndCategory(pointEntity: PointEntity, subCategory: SubCategory)
        fun loadBusinessGuideByLocationAndCategoryWithLimit(pointEntity: PointEntity, subCategory: SubCategory)

        fun loadBusinessGuideById(id: String)

        fun addBusinessGuide(businessGuide: BusinessGuideModel, token: String)

        fun updateBusinessGuide(businessGuide: BusinessGuideEditModel, token: String)

    }

    interface View : BaseContract.View {

        fun showBusinessGuideProgress(show: Boolean) {}
        fun showBusinessGuideLoadErrorMessage(visible: Boolean) {}
        fun showBusinessGuideEmptyView(visible: Boolean) {}
        fun onLoadBusinessGuideListSuccessfully(businessGuideList: MutableList<BusinessGuide>) {}
        fun onLoadBusinessGuide(businessGuide: BusinessGuide) {}
        fun onAddBusinessGuideSuccessfully(businessGuide: BusinessGuide) {}
        fun onUpdateBusinessGuideSuccessfully(businessGuide: BusinessGuide) {}

    }

}