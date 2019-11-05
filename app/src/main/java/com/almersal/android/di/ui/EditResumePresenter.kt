package com.almersal.android.di.ui

import android.content.Context
import android.util.Log
import com.almersal.android.api.ApiService
import com.almersal.android.api.ServerInfo
import com.almersal.android.data.entities.City
import com.almersal.android.data.entities.SentUpdateProfile
import com.almersal.android.data.entities.Tag
import com.almersal.android.data.entities.User
import com.almersal.android.data.entitiesResponses.AttachmentResponse
import com.almersal.android.repositories.DataStoreRepositories
import com.almersal.android.repositories.UserRepository
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.androidnetworking.interfaces.UploadProgressListener
import io.reactivex.disposables.CompositeDisposable
import java.io.File
import java.util.HashMap

class EditResumePresenter(val context: Context) : EditResumeContract.Presenter {

    private val subscriptions = CompositeDisposable()
    private lateinit var view: EditResumeContract.View

    override fun subscribe() {
    }

    override fun unSubscribe() {
    }

    override fun attachView(view: EditResumeContract.View) {
        this.view = view
        this.view.bindView()
    }

    override fun loadCities(withCache: Boolean) {
        view.showCitiesProgress(true)
        if (withCache) {
            val cityList = DataStoreRepositories(context).getCities()
            if (cityList != null) {
                view.showCitiesProgress(false)
                view.onCitiesLoaded(cityList)
                return
            }
        }

        val criteriaCity: MutableMap<String, String> = HashMap()//[ownerId]
        criteriaCity["include"] = "locations"

        ApiService().getCities(ServerInfo.citiesUrl, criteriaCity, object : ParsedRequestListener<MutableList<City>> {
            override fun onResponse(response: MutableList<City>?) {
                view.showCitiesProgress(false)
                if (response != null) {
                    DataStoreRepositories(context).putCities(response)
                    view.onCitiesLoaded(response)
                } else {
                    view.showCitiesEmptyView(true)
                }
            }

            override fun onError(anError: ANError?) {
                view.showCitiesProgress(false)
                view.showCitiesLoadErrorMessage(true)
            }
        })

    }


    override fun getTags(keyword: String) {
        view.showAutoCompleteProgress(true)


        ApiService().getTags(ServerInfo.tagsUrl, keyword, object : ParsedRequestListener<MutableList<Tag>> {
            override fun onResponse(response: MutableList<Tag>?) {
                view.showAutoCompleteProgress(false)
                if (response != null) {
                    view.onTagsLoaded(response)
                } else {
                    view.onTagsLoaded(mutableListOf())
                }
            }

            override fun onError(anError: ANError?) {
                view.showAutoCompleteProgress(false)
                view.onTagsLoaded(mutableListOf())
            }
        })
    }

    override fun addTag(tag: Tag?) {
        if (tag != null) {
            view.showAutoCompleteProgress(true)

            ApiService().postTag(
                ServerInfo.tagsUrl,
                tag,
                UserRepository(context).getUser()!!.token,
                object : ParsedRequestListener<Tag> {
                    override fun onResponse(response: Tag?) {
                        view.showAutoCompleteProgress(false)
                        if (response != null) {
                            view.onTagAdded(tag)
                        }
                    }

                    override fun onError(anError: ANError?) {
                        view.showAutoCompleteProgress(false)
                        view.showLoadErrorMessage(true)
                    }
                })
        }
    }

    override fun uploadProfilePic(file: File) {
        view.showProgress(true)
        ApiService().uploadAttachment(ServerInfo.uploadImageUrl, file,
            UploadProgressListener { bytesUploaded, totalBytes ->
                Log.v("", "")
            },
            object : ParsedRequestListener<MutableList<AttachmentResponse>> {
                override fun onResponse(response: MutableList<AttachmentResponse>?) {
                    view.showProgress(false)
                    if (response != null) {
                        view.onProfilePicLoaded(response[0])
                    }
                    Log.v("", "")
                }

                override fun onError(anError: ANError?) {
                    view.showProgress(false)
                    view.showLoadErrorMessage(true)
                    Log.v("", "")
                }

            })
    }

    override fun updateProfile(updateProfile: SentUpdateProfile) {
        view.showProgress(true)
        ApiService().updateProfile(ServerInfo.cvUrl, updateProfile, UserRepository(context).getUser()!!.token,
            object : ParsedRequestListener<User?> {
                override fun onResponse(response: User?) {

                    view.showProgress(false)
                    if (response != null) {
                        val token = UserRepository(context).getUser()!!.token
                        response.token = token
                        UserRepository(context = context).addUser(response)
                        view.onProfileUpdated()
                    }
                    Log.v("", "")
                }

                override fun onError(anError: ANError?) {
                    view.showProgress(false)
                    view.showLoadErrorMessage(true)
                    Log.v("", "")
                }

            })
    }

    override fun uploadCV(file: File) {
        view.showProgress(true)
        ApiService().uploadAttachment(ServerInfo.uploadImageUrl, file,
            UploadProgressListener { bytesUploaded, totalBytes ->
                Log.v("", "")
            },
            object : ParsedRequestListener<MutableList<AttachmentResponse>> {
                override fun onResponse(response: MutableList<AttachmentResponse>?) {
                    view.showProgress(false)
                    if (response != null) {
                        view.onCvUploaded(response[0])
                    }
                    Log.v("", "")
                }

                override fun onError(anError: ANError?) {
                    view.showProgress(false)
                    view.showLoadErrorMessage(true)
                    Log.v("", "")
                }

            })
    }


}