package com.almersal.android.di.ui

import java.io.File

class AttachmentContract {

    interface Presenter : BaseContract.Presenter<View> {
        fun loadAttachmentFile(file: File)
        fun loadVideoAttachmentFile(file: File)
    }

    interface View : BaseContract.View {
        fun showAttachmentProgress(show: Boolean)
        fun showAttachmentProcessingPercentage(percentage: String)
        fun showAttachmentLoadErrorMessage(visible: Boolean)
        fun showAttachmentEmptyView(visible: Boolean)
        fun onLoadAttachmentListSuccessfully(filePath: String)

        fun onLoadVideoAttachmentListSuccessfully(filePath: String) {}
    }

}