package com.almersal.android.di.ui

import android.content.Context
import com.almersal.android.api.ApiService
import com.almersal.android.api.ServerInfo
import com.almersal.android.data.entities.*
import com.almersal.android.repositories.UserRepository
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener

class ProductPresenter constructor(val context: Context) : ProductContract.Presenter {

    private lateinit var view: ProductContract.View

    init {

    }

    override fun subscribe() {

    }

    override fun unSubscribe() {

    }

    override fun attachView(view: ProductContract.View) {
        this.view = view
    }


    override fun addProduct(product: ProductAddModel, token: String) {
        view.showProgress(true)
        ApiService().addProduct(
            ServerInfo.addProductUrl,
            product,
            token,
            object : ParsedRequestListener<Product> {
                override fun onResponse(response: Product?) {
                    view.showProgress(false)
                    if (response != null) {
                        view.onAddProductSuccessfully(response)
                    } else {
                        view.onAddProductFail()
                    }
                }

                override fun onError(anError: ANError?) {
                    view.showLoadErrorMessage(true)
                }
            })
    }


    override fun updateProduct(product: ProductAddModel, token: String) {
        view.showProgress(true)
        ApiService().updateProduct(
            ServerInfo.productsUrl + "/${product.id}/updateProduct",
            product,
            token,
            object : ParsedRequestListener<Product> {
                override fun onResponse(response: Product?) {
                    view.showProgress(false)
                    if (response != null) {
                        view.onUpdateProductSuccessfully(response)
                    } else {
                        view.onUpdateProductFail()
                    }
                }

                override fun onError(anError: ANError?) {
                    view.showLoadErrorMessage(true)
                }
            })
    }


    override fun loadProduct(id: String) {
        view.showProgress(true)
        ApiService().getProduct(
            ServerInfo.productsUrl + "/" + id,
            object : ParsedRequestListener<Product> {
                override fun onResponse(response: Product?) {
                    view.showProgress(false)
                    if (response != null) {
                        view.onProductLoadedSuccessfully(response)
                    } else {
                        view.showEmptyView(true)
                    }
                }

                override fun onError(anError: ANError?) {
                    view.showLoadErrorMessage(true)
                }
            })
    }


    override fun loadUserBusinesses(
        userId: String /*criteria: MutableMap<String, Pair<String, String>>*/
    ) {
        view.showProgress(true)
        val url = ServerInfo.businessGuideUrl + "?filter[where][ownerId]=" + userId
        ApiService().getUserBusinesses(
            url/*, criteria*/,
            object : ParsedRequestListener<MutableList<BusinessGuide>> {
                override fun onResponse(response: MutableList<BusinessGuide>?) {
                    if ((response != null)) {
                        view.showProgress(false)
                        if (response.size > 0)
                            view.onUserBusinessesListSuccessfully(response)

                        return
                    }

                }

                override fun onError(anError: ANError?) {
                    view.showProgress(true)
                }
            })
    }


    override fun deactivateProduct(id: String) {
        view.showProgress(true)
        ApiService().deactivateProduct(
            ServerInfo.productsUrl + "/" + id,
            Status("deactivated"),
            UserRepository(context).getUser()!!.token,
            object : ParsedRequestListener<Product> {
                override fun onResponse(response: Product?) {
                    view.showProgress(false)
                    if (response != null) {
                        view.onProductDeactivatedSuccessfully()
                    } else {
                        view.showEmptyView(true)
                    }
                }

                override fun onError(anError: ANError?) {
                    view.showLoadErrorMessage(true)
                }
            })
    }


}