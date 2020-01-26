package com.almersal.android.di.ui

import com.almersal.android.data.entities.Post
import com.almersal.android.data.entities.Product
import com.almersal.android.data.entities.Volume

/**
 * Created by Adhamkh on 2018-06-29.
 */
class VolumesContract {

    interface Presenter : BaseContract.Presenter<View> {
        fun loadDefaultVolume()
        fun loadNextVolume()
        fun loadPreviousVolume()
        fun loadVolumeById(id: String)
        fun loadProducts(keyword : String? = null,categoryId: String? = null, subCategoryId: String? = null, skip: Int, limit: Int)
    }

    interface View : BaseContract.View {
        fun loadedData(volume: Volume)
        fun noMoreData()
        fun disableNext()
        fun enableNext()
        fun disablePrev()
        fun enablePrev()
        fun bindProducts(products: MutableList<Product>){}
    }

}