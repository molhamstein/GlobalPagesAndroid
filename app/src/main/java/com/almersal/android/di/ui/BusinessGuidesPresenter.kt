package com.almersal.android.di.ui

import android.content.Context
import android.util.Log
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.almersal.android.api.ApiService
import com.almersal.android.api.ServerInfo
import com.almersal.android.data.entities.BusinessGuide
import com.almersal.android.data.entities.PointEntity
import com.almersal.android.data.entities.SubCategory
import com.almersal.android.data.entitiesModel.BusinessGuideEditModel
import com.almersal.android.data.entitiesModel.BusinessGuideModel
import com.almersal.android.enums.DaysEnum
import io.reactivex.disposables.CompositeDisposable

class BusinessGuidesPresenter constructor(val context: Context) : BusinessGuidesContract.Presenter {


    private val subscriptions = CompositeDisposable()
    private lateinit var view: BusinessGuidesContract.View

    init {

    }

    override fun subscribe() {

    }

    override fun attachView(view: BusinessGuidesContract.View) {
        this.view = view
    }

    override fun unSubscribe() {
        subscriptions.clear()
    }

    private fun loadBusinessGuideByUrl(url: String) {
        view.showBusinessGuideProgress(true)
        ApiService().getBusinessGuides(url,/* criteria,*/ object : ParsedRequestListener<MutableList<BusinessGuide>> {
            override fun onResponse(response: MutableList<BusinessGuide>?) {

                if ((response != null)) {
                    view.showBusinessGuideProgress(false)
                    if (response.size > 0) {
                        view.onLoadBusinessGuideListSuccessfully(response)
                        return
                    }
                }
                view.showBusinessGuideEmptyView(true)
            }

            override fun onError(anError: ANError?) {
                view.showBusinessGuideLoadErrorMessage(true)
            }
        })
    }

    override fun loadBusinessGuideList(subCategory: SubCategory) {


//        val criteria: MutableMap<String, Pair<String, String>> = HashMap()
//        criteria["where"] = Pair("subCategoryId", subCategory.id)

        val url = ServerInfo.businessGuideUrl + "?filter[where][subCategoryId]=" + subCategory.id
        loadBusinessGuideByUrl(url)

    }

    override fun loadBusinessGuideByLocation(pointEntity: PointEntity) {
//        var s = "filter[where][price][between][0]=0&filter[where][price][between][1]=7"

        val url = ServerInfo.businessGuideUrl + "?filter[where][locationPoint][near]=" +
                pointEntity.lat.toString() + "," + pointEntity.lng.toString() + "&filter[limit]=3"

        loadBusinessGuideByUrl(url)
    }

    override fun loadBusinessGuideByLocationAndCategory(pointEntity: PointEntity, subCategory: SubCategory) {
//        var s = "filter[where][price][between][0]=0&filter[where][price][between][1]=7"

        val url = ServerInfo.businessGuideUrl + "?filter[where][subCategoryId]=" + subCategory.id +
                "&filter[where][locationPoint][near]=" +
                pointEntity.lat.toString() + "," + pointEntity.lng.toString() + "&filter[limit]=3"

        loadBusinessGuideByUrl(url)
    }


    override fun loadBusinessGuideForPharmacy(pointEntity: PointEntity, daysEnum: DaysEnum) {

//        val url = ServerInfo.businessGuideUrl + "?filter[where][categoryId]=" + SettingData.pharmacyCategoryId +
//                "&filter[where][locationPoint][near]=" +
//                pointEntity.lat.toString() + "," + pointEntity.lng.toString() + "&filter[limit]=1000"
        val url = ServerInfo.businessGuideUrl + "/searchByLocation?lat=" + pointEntity.lat +
                "&lng=" + pointEntity.lng + "&openingDay=" + daysEnum.number.toString() + "&code=pharmacies"
        loadBusinessGuideByUrl(url)
    }

    override fun addBusinessGuide(businessGuide: BusinessGuideModel, token: String) {
        view.showBusinessGuideProgress(true)
        ApiService().postBusinessGuides(ServerInfo.businessGuideUrl, businessGuide, token, object : ParsedRequestListener<BusinessGuide> {
            override fun onResponse(response: BusinessGuide?) {
                if (response != null) {
                    view.showBusinessGuideProgress(false)
                    view.onAddBusinessGuideSuccessfully(response)
                    return
                }
                Log.v("", "")
            }

            override fun onError(anError: ANError?) {
                view.showBusinessGuideLoadErrorMessage(true)
                Log.v("", "")
            }
        })

    }

    override fun updateBusinessGuide(businessGuide: BusinessGuideEditModel, token: String) {
        view.showBusinessGuideProgress(true)
        ApiService().putBusinessGuides(ServerInfo.businessGuideUrl, businessGuide, token, object : ParsedRequestListener<BusinessGuide> {
            override fun onResponse(response: BusinessGuide?) {
                if (response != null) {
                    view.showBusinessGuideProgress(false)
                    view.onUpdateBusinessGuideSuccessfully(response)
                    return
                }
                Log.v("", "")
            }

            override fun onError(anError: ANError?) {
                view.showBusinessGuideLoadErrorMessage(true)
                Log.v("", "")
            }
        })

    }


}
