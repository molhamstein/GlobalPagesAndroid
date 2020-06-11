package com.almersal.android.di.ui

import android.content.Context
import com.almersal.android.api.ApiService
import com.almersal.android.api.ServerInfo
import com.almersal.android.data.entities.Post
import com.almersal.android.data.entities.Product
import com.almersal.android.data.entities.Volume
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import io.reactivex.disposables.CompositeDisposable

class VolumesPresenter constructor(val context: Context) : VolumesContract.Presenter {


    private val subscriptions = CompositeDisposable()
    private lateinit var view: VolumesContract.View

    private var index: Int = 0

    override fun subscribe() {

    }

    override fun attachView(view: VolumesContract.View) {
        this.view = view
    }

    override fun unSubscribe() {
        subscriptions.clear()
    }


    private fun loadData(/*criteria: MutableMap<String, String>*/) {
        ApiService().getVolumes(ServerInfo.volumeUrl + "?filter[where][status]=activated&filter[skip]=" +
                index.toString() + "&filter[limit]=1&filter[order]=creationDate DESC"/*, criteria*/,
            object : ParsedRequestListener<MutableList<Volume>> {
                override fun onResponse(response: MutableList<Volume>?) {
                    if ((response != null)) {
                        val poJo = response.firstOrNull()
                        if (poJo != null) {
                            view.showProgress(false)
                            view.loadedData(poJo)
                            view.enablePrev()
                            return
                        } else {
                            view.disablePrev()
                            view.showProgress(false)
                        }
                    }
                    view.disablePrev()
                    view.showProgress(false)
                }

                override fun onError(anError: ANError?) {
                    view.showLoadErrorMessage(true)
                }
            })

    }

    override fun loadDefaultVolume() {
        view.showProgress(true)
        view.disableNext()

//        val criteria: MutableMap<String, String> = HashMap()
//        criteria.apply {
//            put("", index.toString())
//        }
        loadData(/*criteria*/)
    }

    override fun loadNextVolume() {
        view.showProgress(true)
        index--

        if (index <= 0)
            view.disableNext()
        else
            view.enableNext()

        if (index < 0) {
            index = 0
            view.noMoreData()
        }
//        val criteria: MutableMap<String, String> = HashMap()
//        criteria.apply {
//            put("filter[limit]", "1")
//            put("filter[skip]", index.toString())
//        }
        loadData(/*criteria*/)
    }

    override fun loadPreviousVolume() {
        view.showProgress(true)
        view.enableNext()

        index++
        index = Math.max(index, 0)

//        val criteria: MutableMap<String, String> = HashMap()
//        criteria.apply {
//            put("filter[limit]", "1")
//            put("filter[skip]", index.toString())
//        }
        loadData(/*criteria*/)
    }

    override fun loadProducts(keyword: String?, categoryId: String?, subCategoryId: String?, skip: Int, limit: Int) {
        view.showProgress(true)
        val filterCategory = if (categoryId == null) "" else "filter[where][categoryId]=$categoryId"
        val filterSubCategory = if (categoryId == null) "" else "filter[where][subCategoryId]=$subCategoryId"
        val filterKeyword = if (keyword == null) "" else "filter[keyword]=$keyword"
        ApiService().getProducts(ServerInfo.productsUrl + "?$filterCategory&$filterSubCategory&$filterKeyword" +
                "filter[limit]=$limit&filter[skip]=$skip&filter[where][status]=activated&filter[order]=creationDate DESC",
            object : ParsedRequestListener<MutableList<Product>> {
                override fun onResponse(response: MutableList<Product>?) {
                    if ((response != null)) {
                        view.showProgress(false)
                        view.bindProducts(response)
                        return
                    } else {
                        view.showEmptyView(true)
                        view.showProgress(false)
                    }
                }


                override fun onError(anError: ANError?) {
                    view.showLoadErrorMessage(true)
                }
            })
    }

    override fun loadVolumeById(id: String) {
        ApiService().getVolumes(ServerInfo.volumeUrl +
                "?filter[where][status]=activated&filter[where][id]=" + id
            , object : ParsedRequestListener<MutableList<Volume>> {
                override fun onResponse(response: MutableList<Volume>?) {
                    if ((response != null)) {
                        val poJo = response.firstOrNull()
                        if (poJo != null) {
                            view.showProgress(false)
                            view.loadedData(poJo)
                            view.enablePrev()
                            return
                        } else {
                            view.disablePrev()
                            view.showProgress(false)
                        }
                    }
                    view.disablePrev()
                    view.showProgress(false)
                }

                override fun onError(anError: ANError?) {
                    view.showLoadErrorMessage(true)
                }
            })

    }

}