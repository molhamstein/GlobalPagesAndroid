package com.brainsocket.globalpages.dialogs

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import butterknife.OnClick
import com.brainsocket.globalpages.R

/**
 * Created by Adhamkh on 2018-07-20.
 */
class ContactDialog : DialogFragment() {

    companion object {
        val ContactDialog_Tag = "ContactDialog"

        fun newInstance(): ContactDialog {
            val f = ContactDialog()
            // Supply num input as an argument.
            val args = Bundle()
            f.arguments = args
            return f
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val mView = inflater.inflate(R.layout.contact_dialog_layout, container)
        ButterKnife.bind(this, mView)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    @OnClick(R.id.okBtn)
    fun onOkClick(view: View) {
        dismiss()
    }

}
