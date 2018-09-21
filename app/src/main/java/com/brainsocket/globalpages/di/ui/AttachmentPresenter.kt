package com.brainsocket.globalpages.di.ui

import android.content.Context
import android.util.Log
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.*
import com.brainsocket.globalpages.api.ApiService
import com.brainsocket.globalpages.api.ServerInfo
import com.brainsocket.globalpages.data.entitiesResponses.AttachmentResponse
import io.reactivex.disposables.CompositeDisposable
import org.json.JSONArray
import org.json.JSONObject
import java.io.File


class AttachmentPresenter constructor(val context: Context) : AttachmentContract.Presenter {

    private val subscriptions = CompositeDisposable()
    private lateinit var view: AttachmentContract.View

    override fun subscribe() {

    }

    override fun attachView(view: AttachmentContract.View) {
        this.view = view
    }

    override fun unSubscribe() {
        subscriptions.clear()
    }

    override fun loadAttachmentFile(file: File) {
        view.showAttachmentProgress(true)
        ApiService().uploadAttachment(ServerInfo.uploadImageUrl, file,
                UploadProgressListener { bytesUploaded, totalBytes ->
                    Log.v("", "")
                },
                object : ParsedRequestListener<MutableList<AttachmentResponse>> {
                    override fun onResponse(response: MutableList<AttachmentResponse>?) {
                        view.showAttachmentProgress(false)
                        if (response != null) {
                            view.onLoadAttachmentListSuccessfully(response[0].url)
                        }
                        Log.v("", "")
                    }

                    override fun onError(anError: ANError?) {
                        view.showAttachmentProgress(false)
                        view.showLoadErrorMessage(true)
                        Log.v("", "")
                    }

                })

    }

}
