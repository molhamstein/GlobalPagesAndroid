package com.brainsocket.globalpages.di.ui

import android.content.Context
import android.util.Log
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.androidnetworking.interfaces.StringRequestListener
import com.brainsocket.globalpages.api.ApiService
import com.brainsocket.globalpages.api.ServerInfo
import com.brainsocket.globalpages.data.entities.BusinessGuide
import com.brainsocket.globalpages.data.entities.SubCategory
import io.reactivex.disposables.CompositeDisposable
import java.util.HashMap
import kotlin.math.log


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

    override fun loadBusinessGuideList(subCategory: SubCategory) {
        view.showBusinessGuideProgress(true)
        val criteria: MutableMap<String, String> = HashMap()
        criteria.apply {
            //  put("where", "subCategoryId:"+subCategory.id)
        }
        ApiService().getBusinessGuides(ServerInfo.businessGuideUrl, criteria, object : ParsedRequestListener<MutableList<BusinessGuide>> {
            override fun onResponse(response: MutableList<BusinessGuide>?) {
                val pojo = response
                if ((pojo != null)) {
                    view.showBusinessGuideProgress(false)
                    view.onLoadBusinessGuideListSuccessfully(pojo)
                    return
                }
                view.showEmptyView(true)
            }

            override fun onError(anError: ANError?) {
                view.showBusinessGuideLoadErrorMessage(true)
            }
        })

    }

    override fun addBusinessGuide(businessGuide: BusinessGuide) {
        view.showBusinessGuideProgress(true)
        ApiService().postBusinessGuides(ServerInfo.businessGuideUrl, businessGuide, object : StringRequestListener {
            override fun onResponse(response: String?) {
                if (response != null) {
                    view.showBusinessGuideProgress(false)
                    view.onAddBusinessGuideSuccessfully()
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