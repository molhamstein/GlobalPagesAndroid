package com.brainsocket.globalpages.di.ui

import com.brainsocket.globalpages.data.entities.ProductThumb
import com.brainsocket.globalpages.data.entitiesModel.ProductThumbEditModel
import com.brainsocket.globalpages.data.entitiesModel.ProductThumbModel

/**
 * Created by Adhamkh on 2018-10-07.
 */
class BusinessGuideProductContract {
    interface Presenter : BaseContract.Presenter<View> {
        fun addProduct(url: String, productThumb: ProductThumbModel, token: String)
        fun updateProduct(url: String, productThumb: ProductThumbEditModel, token: String)
    }

    interface View : BaseContract.View {
        fun onAddProductSuccessfully(productThumb: ProductThumb)
        fun onAddProductFail()

        fun onProductUpdateSuccessfully(productThumb: ProductThumb) {}
        fun onProductUpdateFail() {}

    }

}