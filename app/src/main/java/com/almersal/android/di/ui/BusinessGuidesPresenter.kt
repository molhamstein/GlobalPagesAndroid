package com.almersal.android.di.ui

import android.content.Context
import android.util.Log
import com.almersal.android.App
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.almersal.android.api.ApiService
import com.almersal.android.api.ServerInfo
import com.almersal.android.data.entities.*
import com.almersal.android.data.entitiesModel.BusinessGuideEditModel
import com.almersal.android.data.entitiesModel.BusinessGuideModel
import com.almersal.android.data.filtration.FilterEntity
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
        ApiService().getBusinessGuides(url, object : ParsedRequestListener<MutableList<BusinessGuide>> {
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

    override fun loadBusinessGuideListWithLimit(subCategory: SubCategory) {


//        val criteria: MutableMap<String, Pair<String, String>> = HashMap()
//        criteria["where"] = Pair("subCategoryId", subCategory.id)

        val url = ServerInfo.businessGuideUrl + "?filter[where][subCategoryId]=" + subCategory.id + "&filter[limit]=3"
        loadBusinessGuideByUrl(url)

    }

    override fun loadBusinessGuideByLocationWithLimit(pointEntity: PointEntity) {

        val url = ServerInfo.businessGuideUrl + "?filter[where][locationPoint][near]=" +
                pointEntity.lat.toString() + "," + pointEntity.lng.toString() + "&filter[where][status]=activated&filter[limit]=3"

        loadBusinessGuideByUrl(url)
    }

    override fun loadBusinessGuideByLocation(
        pointEntity: PointEntity,
        limit: Int,
        skip: Int,
        filterEntity: FilterEntity?,
        maxDistance: Float?
    ) {

        var filterQuery = ""
        val language = App.app.isArabic()

        if (!filterEntity?.query.isNullOrEmpty())
            filterQuery += if (language) {
                "&keyword=${filterEntity?.query}"
            } else {
                "&keyword=${filterEntity?.query}"
            }

        if (filterEntity?.category != null)
            filterQuery += "&catId=${filterEntity.category?.id}"

        if (filterEntity?.subCategory != null)
            filterQuery += "&subCatId=${filterEntity.subCategory?.id}"

        if (filterEntity?.city != null) {
            filterQuery += "&cityId=${filterEntity.city?.id}"
        }
        if (filterEntity?.area != null) {
            filterQuery += "&locationId=${filterEntity.area?.id}"
        }

        filterQuery += "&units=kilometers&maxDistance=$maxDistance"
        //http://192.168.1.8:3000/api/businesses/searchByLocation?lat=33.514&lng=36.31&codeCat=pharmacies
        // &limit=100&skip=0&openingDay=0&units=kilometers&maxDistance=3000
        // &cityId=123&locationId=123&catId=123&subCatId=123

        val url = ServerInfo.businessGuideUrl + "/searchByLocation?lat=" +
                pointEntity.lat.toString() + "&lng=" + pointEntity.lng.toString() +
                "&limit=$limit&skip=$skip" + filterQuery

//        val url = ServerInfo.businessGuideUrl + "?filter[where][locationPoint][near]=" +
//                pointEntity.lat.toString() + "," + pointEntity.lng.toString() + "&filter[where][status]=activated" +
//                "&filter[limit]=$limit&filter[skip]=$skip" + filterQuery

        loadBusinessGuideByUrl(url)
    }

    override fun loadBusinessGuideByLocationAndCategoryWithLimit(
        pointEntity: PointEntity,
        subCategory: SubCategory,
        maxDistance: Float?
    ) {
//        var s = "filter[where][price][between][0]=0&filter[where][price][between][1]=7"



        val url = ServerInfo.businessGuideUrl + "/searchByLocation?lat=" +
                pointEntity.lat.toString() + "&lng=" + pointEntity.lng.toString() +
                "&limit=3"+ "&subCatId=${subCategory.id}"+"&units=kilometers&maxDistance=$maxDistance&status=activated"

        loadBusinessGuideByUrl(url)
    }

    override fun loadBusinessGuideByLocationAndCategory(pointEntity: PointEntity, subCategory: SubCategory) {
//        var s = "filter[where][price][between][0]=0&filter[where][price][between][1]=7"

        val url = ServerInfo.businessGuideUrl + "?filter[where][subCategoryId]=" + subCategory.id +
                "&filter[where][locationPoint][near]=" +
                pointEntity.lat.toString() + "," + pointEntity.lng.toString() + "&filter[where][status]=activated"//&filter[limit]=3

        loadBusinessGuideByUrl(url)
    }

    override fun loadBusinessGuideForPharmacy(
        pointEntity: PointEntity,
        daysEnum: DaysEnum,
        limit: Int,
        skip: Int,
        maxDistance: Float?
    ) {

//        val url = ServerInfo.businessGuideUrl + "?filter[where][categoryId]=" + SettingData.pharmacyCategoryId +
//                "&filter[where][locationPoint][near]=" +
//                pointEntity.lat.toString() + "," + pointEntity.lng.toString() + "&filter[limit]=1000"
        val url = ServerInfo.businessGuideUrl + "/searchByLocation?lat=" + pointEntity.lat +
                "&lng=" + pointEntity.lng + "&openingDay=" + daysEnum.number.toString() + "&codeCat=pharmacies"+
                "&units=kilometers&maxDistance=$maxDistance&status=activated"
        loadBusinessGuideByUrl(url)
    }

    override fun loadBusinessGuideById(id: String) {
        view.showProgress(true)
        val url = ServerInfo.businessGuideUrl + "/" + id
        ApiService().getBusinessGuide(url,/* criteria,*/ object : ParsedRequestListener<BusinessGuide> {
            override fun onResponse(response: BusinessGuide?) {
                view.showProgress(false)
                if (response != null) {
                    view.onLoadBusinessGuide(response)
                    return
                }
                view.showEmptyView(true)
            }

            override fun onError(anError: ANError?) {
                view.showLoadErrorMessage(true)
            }
        })

    }

    override fun addBusinessGuide(businessGuide: BusinessGuideModel, token: String) {
        view.showBusinessGuideProgress(true)
        ApiService().postBusinessGuides(
            ServerInfo.businessGuideUrl,
            businessGuide,
            token,
            object : ParsedRequestListener<BusinessGuide> {
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
        ApiService().putBusinessGuides(
            ServerInfo.businessGuideUrl,
            businessGuide,
            token,
            object : ParsedRequestListener<BusinessGuide> {
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
