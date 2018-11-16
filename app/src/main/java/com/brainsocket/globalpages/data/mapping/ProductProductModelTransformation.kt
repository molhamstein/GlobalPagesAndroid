package com.brainsocket.globalpages.data.mapping

import com.brainsocket.globalpages.data.entities.ProductThumb
import com.brainsocket.globalpages.data.entitiesModel.ProductThumbEditModel
import com.brainsocket.globalpages.data.entitiesModel.ProductThumbModel

/**
 * Created by Adhamkh on 2018-11-16.
 */
class ProductProductModelTransformation {
    companion object {
//        fun productTransform(productThumb: ProductThumb) = ProductThumbModel(name = productThumb.name,
//                price = productThumb.price, description = productThumb.description, image = productThumb.image)
//
//        fun productTransform(productThumb: ProductThumbModel): ProductThumb {
//
//        }

        fun productTransform(productThumb: ProductThumb) = ProductThumbEditModel(name = productThumb.name,
                price = productThumb.price, description = productThumb.description, image = productThumb.image, id = productThumb.id)

        fun productTransform(productThumb: ProductThumbEditModel) = ProductThumb(name = productThumb.name,
                price = productThumb.price, description = productThumb.description, image = productThumb.image, id = productThumb.id!!)


    }
}