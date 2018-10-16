package com.brainsocket.globalpages.di.ui

import android.content.Context
import android.util.Log
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.androidnetworking.interfaces.StringRequestListener
import com.brainsocket.globalpages.api.ApiService
import com.brainsocket.globalpages.api.ServerInfo
import com.brainsocket.globalpages.data.entities.BusinessGuide
import com.brainsocket.globalpages.data.entities.PointEntity
import com.brainsocket.globalpages.data.entities.SubCategory
import com.brainsocket.globalpages.data.entitiesModel.BusinessGuideEditModel
import com.brainsocket.globalpages.data.entitiesModel.BusinessGuideModel
import com.brainsocket.globalpages.enums.DaysEnum
import com.brainsocket.globalpages.repositories.SettingData
import com.google.gson.Gson
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
//                val poJo = response
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
                pointEntity.lat.toString() + "," + pointEntity.lng.toString() + "&filter[limit]=1000"

        loadBusinessGuideByUrl(url)
    }

    override fun loadBusinessGuideForPharmacy(pointEntity: PointEntity, daysEnum: DaysEnum) {

//        val url = ServerInfo.businessGuideUrl + "?filter[where][categoryId]=" + SettingData.pharmacyCategoryId +
//                "&filter[where][locationPoint][near]=" +
//                pointEntity.lat.toString() + "," + pointEntity.lng.toString() + "&filter[limit]=1000"
        val url = ServerInfo.businessGuideUrl + "/searchByLocation?lat=" + pointEntity.lat +
                "&lng=" + pointEntity.lng + "&openingDay=" + daysEnum.number.toString()
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
