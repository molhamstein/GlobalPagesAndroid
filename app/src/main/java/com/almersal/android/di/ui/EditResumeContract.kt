package com.almersal.android.di.ui

import com.almersal.android.data.entities.*
import com.almersal.android.data.entitiesResponses.AttachmentResponse
import java.io.File

class EditResumeContract {
    interface Presenter:BaseContract.Presenter<View>{
        fun loadCities(withCache: Boolean)
        fun getTags(keyword:String)
        fun addTag(tag:Tag?)
        fun uploadProfilePic(file:File)
        fun updateProfile(updateProfile: SentUpdateProfile)
        fun uploadCV(file: File)

    }
    interface View : BaseContract.View {

        fun bindView()
        fun showCitiesProgress(show: Boolean)
        fun showCitiesLoadErrorMessage(visible: Boolean)
        fun showCitiesEmptyView(visible: Boolean)
        fun onCitiesLoaded(citiesList: MutableList<City>)
        fun showAutoCompleteProgress(flag:Boolean)
        fun onTagsLoaded(tags:List<Tag>)
        fun onTagAdded(tag:Tag)
        fun onProfilePicLoaded(attachmentResponse: AttachmentResponse)
        fun onProfileUpdated()
        fun onCvUploaded(attachmentResponse: AttachmentResponse)


    }
}