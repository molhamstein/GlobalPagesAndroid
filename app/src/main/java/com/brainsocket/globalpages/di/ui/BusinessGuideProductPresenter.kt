package com.brainsocket.globalpages.di.ui

import android.content.Context
import android.util.Log
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.StringRequestListener
import com.brainsocket.globalpages.api.ApiService
import com.brainsocket.globalpages.data.entitiesModel.ProductThumbModel
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by Adhamkh on 2018-10-07.
 */
class BusinessGuideProductPresenter constructor(val context: Context) : BusinessGuideProductContract.Presenter {


    private val subscriptions = CompositeDisposable()
    private lateinit var view: BusinessGuideProductContract.View

    init {

    }

    override fun subscribe() {

    }

    override fun attachView(view: BusinessGuideProductContract.View) {
        this.view = view
    }

    override fun addProduct(url: String, productThumb: ProductThumbModel) {
        view.showProgress(true)

        ApiService().postBusinessGuideProduct(url = url, productThumbModel = productThumb, requestListener =
        object : StringRequestListener {
            override fun onResponse(response: String?) {
                if (response != null) {
                    Log.v("", "")
                    view.onAddProductSuccessfully()
                }
            }

            override fun onError(anError: ANError?) {
                view.showLoadErrorMessage(true)
                view.onAddProductFail()
            }
        })
    }


    override fun updateProduct(productThumb: ProductThumbModel) {

    }

    override fun unSubscribe() {

    }


}