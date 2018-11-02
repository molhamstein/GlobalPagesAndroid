package com.brainsocket.globalpages.di.ui

import android.content.Context
import android.util.Log
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.brainsocket.globalpages.api.ApiService
import com.brainsocket.globalpages.data.entities.ProductThumb
import com.brainsocket.globalpages.data.entitiesModel.ProductThumbEditModel
import com.brainsocket.globalpages.data.entitiesModel.ProductThumbModel


class BusinessGuideProductPresenter constructor(val context: Context) : BusinessGuideProductContract.Presenter {


    private lateinit var view: BusinessGuideProductContract.View

    init {

    }

    override fun subscribe() {

    }

    override fun attachView(view: BusinessGuideProductContract.View) {
        this.view = view
    }

    override fun addProduct(url: String, productThumb: ProductThumbModel, token: String) {
        view.showProgress(true)

        ApiService().postBusinessGuideProduct(url = url, productThumbModel = productThumb, token = token,
                requestListener = object : ParsedRequestListener<ProductThumb> {
                    override fun onResponse(response: ProductThumb?) {
                        view.showProgress(false)
                        if (response != null) {
                            Log.v("", "")
                            view.onAddProductSuccessfully(response)
                        } else {
                            view.onAddProductFail()
                        }
                    }

                    override fun onError(anError: ANError?) {
                        view.showProgress(false)
                        view.showLoadErrorMessage(true)
                    }
                })
    }

    override fun updateProduct(url: String, productThumb: ProductThumbEditModel, token: String) {
        view.showProgress(true)

        ApiService().putBusinessGuideProduct(url = url, productThumbModel = productThumb, token = token,
                requestListener = object : ParsedRequestListener<ProductThumb> {
                    override fun onResponse(response: ProductThumb?) {
                        view.showProgress(false)
                        if (response != null) {
                            Log.v("", "")
                            view.onProductUpdateSuccessfully(response)
                        } else {
                            view.onProductUpdateFail()
                        }
                    }

                    override fun onError(anError: ANError?) {
                        view.showProgress(false)
                        view.showLoadErrorMessage(true)
                    }
                })
    }

    override fun unSubscribe() {

    }


}