package com.brainsocket.globalpages.di.ui


class BaseContract {

    interface Presenter<in T> {
        fun subscribe()
        fun unSubscribe()
        fun attachView(view: T)
    }

    interface View {
        fun showProgress(show: Boolean){}
        fun showLoadErrorMessage(visible: Boolean){}
        fun showEmptyView(visible: Boolean) {}
    }

}