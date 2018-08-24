package com.brainsocket.globalpages.di.ui

import com.brainsocket.globalpages.data.entities.BusinessGuide
import com.brainsocket.globalpages.data.entities.SubCategory

/**
 * Created by Adhamkh on 2018-08-24.
 */
class BusinessGuidesContract {
    interface Presenter : BaseContract.Presenter<View> {
        fun loadBusinessGuideList(subCategory: SubCategory)
    }

    interface View : BaseContract.View {

        fun showBusinessGuideProgress(show: Boolean) {}
        fun showBusinessGuideLoadErrorMessage(visible: Boolean) {}
        fun showBusinessGuideEmptyView(visible: Boolean) {}
        fun onLoadBusinessGuideListSuccessfully(businessGuideList: MutableList<BusinessGuide>)

//        fun loginSuccesfully(loginResponse: LoginResponse)
//        fun loginFail()
    }
}