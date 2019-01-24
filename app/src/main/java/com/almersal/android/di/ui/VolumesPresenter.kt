package com.almersal.android.di.ui

import android.content.Context
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.almersal.android.api.ApiService
import com.almersal.android.api.ServerInfo
import com.almersal.android.data.entities.Volume
import io.reactivex.disposables.CompositeDisposable
import java.util.HashMap

class VolumesPresenter constructor(val context: Context) : VolumesContract.Presenter {


    private val subscriptions = CompositeDisposable()
    private lateinit var view: VolumesContract.View

    private var index: Int = 0

    init {

    }

    override fun subscribe() {

    }

    override fun attachView(view: VolumesContract.View) {
        this.view = view
    }

    override fun unSubscribe() {
        subscriptions.clear()
    }

    private fun loadData(criteria: MutableMap<String, String>) {
        ApiService().getVolumes(ServerInfo.volumeUrl, criteria, object : ParsedRequestListener<MutableList<Volume>> {
            override fun onResponse(response: MutableList<Volume>?) {
                if ((response != null)) {
                    val pojo = response.firstOrNull()
                    if (pojo != null) {
                        view.showProgress(false)
                        view.loadedData(pojo)
                        return
                    }
                }
                view.showEmptyView(true)
            }

            override fun onError(anError: ANError?) {
                view.showLoadErrorMessage(true)
            }
        })

    }

    override fun loadDefaultVolume() {
        view.showProgress(true)
        view.disableNext()

        val criteria: MutableMap<String, String> = HashMap()
        criteria.apply {
            put("limit", "1")
            put("order", "creationDate DESC")
            put("skip", index.toString())
        }
        loadData(criteria)
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
        val criteria: MutableMap<String, String> = HashMap()
        criteria.apply {
            put("limit", "1")
            put("order", "creationDate DESC")
            put("skip", index.toString())
        }
        loadData(criteria)
    }

    override fun loadPreviousVolume() {
        view.showProgress(true)
        view.enableNext()

        index++
        index = Math.max(index, 0)

        val criteria: MutableMap<String, String> = HashMap()
        criteria.apply {
            put("limit", "1")
            put("order", "creationDate DESC")
            put("skip", index.toString())
        }
        loadData(criteria)
    }

    override fun loadVolumeById(id: String) {
//        ApiService().getVolumes(ServerInfo.VolumeUrl,)
    }

}