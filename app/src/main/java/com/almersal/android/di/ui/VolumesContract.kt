package com.almersal.android.di.ui

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
    }

    interface View : BaseContract.View {
        fun loadedData(volume: Volume)
        fun noMoreData()
        fun disableNext()
        fun enableNext()
    }

}