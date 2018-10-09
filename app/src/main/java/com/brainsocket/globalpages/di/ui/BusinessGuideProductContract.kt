package com.brainsocket.globalpages.di.ui

import com.brainsocket.globalpages.data.entities.ProductThumb
import com.brainsocket.globalpages.data.entitiesModel.ProductThumbModel

/**
 * Created by Adhamkh on 2018-10-07.
 */
class BusinessGuideProductContract {
    interface Presenter : BaseContract.Presenter<View> {
        fun addProduct(url: String, productThumb: ProductThumbModel)
        fun updateProduct(productThumb: ProductThumbModel)
    }

    interface View : BaseContract.View {
        fun onAddProductSuccessfully()
        fun onAddProductFail()

        fun onProductUpdateSuccessfully() {}
        fun onProductUpdateFail() {}

    }

}