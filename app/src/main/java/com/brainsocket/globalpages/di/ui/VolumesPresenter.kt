package com.brainsocket.globalpages.di.ui

import android.content.Context
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.brainsocket.globalpages.api.ApiService
import com.brainsocket.globalpages.api.ServerInfo
import com.brainsocket.globalpages.data.entities.Volume
import io.reactivex.disposables.CompositeDisposable
import java.util.HashMap

/**
 * Created by Adhamkh on 2018-06-29.
 */
class VolumesPresenter constructor(val context: Context) : VolumesContract.Presenter {


    private val subscriptions = CompositeDisposable()
    private lateinit var view: VolumesContract.View

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

    override fun loadDefaultVolume() {
        view.showProgress(true)

        var criteria: MutableMap<String, String> = HashMap()
        criteria.apply {
            put("limit", "1")
            put("order", "id DESC")
        }

        ApiService().getVolumes(ServerInfo.VolumeUrl, criteria, object : ParsedRequestListener<MutableList<Volume>> {
            override fun onResponse(response: MutableList<Volume>?) {
                if ((response != null)) {
                    var pojo = response.firstOrNull()
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

    override fun loadVolumeById(id: String) {
//        ApiService().getVolumes(ServerInfo.VolumeUrl,)
    }

}