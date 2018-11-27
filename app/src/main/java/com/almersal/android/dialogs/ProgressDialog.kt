package com.almersal.android.dialogs

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import com.almersal.android.R

class ProgressDialog : DialogFragment() {

    companion object {
        const val ProgressDialog_Tag = "ProgressDialog"

        fun newInstance(): ProgressDialog {
            val f = ProgressDialog()
            f.isCancelable = false
            // Supply num input as an argument.
            val args = Bundle()
            f.arguments = args
            return f
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val mView = inflater.inflate(R.layout.progress_dialog_layout, container)
        ButterKnife.bind(this, mView)
        return mView
    }

}
