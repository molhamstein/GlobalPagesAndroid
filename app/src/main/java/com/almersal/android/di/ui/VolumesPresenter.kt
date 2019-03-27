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
                index.toString() + "&filter[limit]=1&filter[order]=creationDate DESC"/*, criteria*/, object : ParsedRequestListener<MutableList<Volume>> {
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

    override fun loadVolumeById(id: String) {
//        ApiService().getVolumes(ServerInfo.VolumeUrl,)
    }

}