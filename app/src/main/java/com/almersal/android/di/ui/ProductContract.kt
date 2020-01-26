package com.almersal.android.di.ui

import com.almersal.android.data.entities.Post
import com.almersal.android.data.entities.Product
import com.almersal.android.data.entities.ProductAddModel
import com.almersal.android.data.entitiesModel.PostEditModel
import com.almersal.android.data.entitiesModel.PostModel

class ProductContract {
    interface Presenter : BaseContract.Presenter<View> {


        fun loadProduct(id: String)
        fun addProduct(product: ProductAddModel, token: String)
        fun updateProduct(product: ProductAddModel, token: String)

    }

    interface View : BaseContract.View {
        fun onAddProductSuccessfully(product: Product) {}
        fun onAddProductFail() {}

        fun onUpdateProductSuccessfully(product: Product) {}
        fun onUpdateProductFail() {}

        fun onProductLoadedSuccessfully(product: Product) {}
    }

}