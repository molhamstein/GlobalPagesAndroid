package com.almersal.android.dialogs

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
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

        fun closeDialog(supportFragmentManager: FragmentManager) {
            try {
                val dialog: ProgressDialog? = supportFragmentManager.findFragmentByTag(ProgressDialog_Tag) as ProgressDialog?
                val res = dialog?.dialog?.isShowing
                if ((res != null) && (res != false)) {
                    dialog.dismiss()
                }
            } catch (ex: Exception) {
            }
        }
    }



    fun showDialog(supportFragmentManager: FragmentManager) {
        closeDialog(supportFragmentManager)
        show(supportFragmentManager, ProgressDialog_Tag)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val mView = inflater.inflate(R.layout.progress_dialog_layout, container)
        ButterKnife.bind(this, mView)
        return mView
    }

}
