package com.almersal.android.di.ui

import android.content.Context
import android.util.Log
import com.almersal.android.api.ApiService
import com.almersal.android.api.ServerInfo
import com.almersal.android.data.entitiesResponses.AttachmentResponse
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.androidnetworking.interfaces.UploadProgressListener
import io.reactivex.disposables.CompositeDisposable
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
                            view.onLoadAttachmentListSuccessfully(response[0].url,response[0].thumbnail)
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

    override fun loadVideoAttachmentFile(file: File) {
        view.showAttachmentProgress(true)
        ApiService().uploadAttachment(ServerInfo.uploadVideoUrl, file,
                UploadProgressListener { bytesUploaded, totalBytes ->
                    Log.v("", "")
                },
                object : ParsedRequestListener<MutableList<AttachmentResponse>> {
                    override fun onResponse(response: MutableList<AttachmentResponse>?) {
                        view.showAttachmentProgress(false)
                        if (response != null) {
                            view.onLoadVideoAttachmentListSuccessfully(response[0].url, response[0].thumbnail)
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
