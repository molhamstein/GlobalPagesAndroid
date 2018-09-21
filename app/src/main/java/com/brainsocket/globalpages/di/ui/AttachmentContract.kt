package com.brainsocket.globalpages.di.ui

import java.io.File

class AttachmentContract {

    interface Presenter : BaseContract.Presenter<View> {
        fun loadAttachmentFile(file: File)
    }

    interface View : BaseContract.View {
        fun showAttachmentProgress(show: Boolean)
        fun showAttachmentProcessingPercentage(percentage: String)
        fun showAttachmentLoadErrorMessage(visible: Boolean)
        fun showAttachmentEmptyView(visible: Boolean)
        fun onLoadAttachmentListSuccessfully(filePath: String)
    }

}