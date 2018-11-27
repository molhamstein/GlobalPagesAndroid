package com.almersal.android.dialogs

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Optional
import com.almersal.android.R
import com.almersal.android.utilities.IntentHelper

/**
 * Created by Adhamkh on 2018-10-13.
 */
class ContactPostDialog : DialogFragment() {

    companion object {
        const val ContactPostDialog_Tag = "ContactPostDialog"

        fun newInstance(phoneNumber1: String): ContactPostDialog {
            val f = ContactPostDialog()
            f.phoneNumber1 = phoneNumber1

            // Supply num input as an argument.
            val args = Bundle()
            f.arguments = args

            return f
        }
    }

    lateinit var phoneNumber1: String


    @BindView(R.id.phone1)
    lateinit var phone1: EditText


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val mView = inflater.inflate(R.layout.contact_post_dialog_layout, container)
        ButterKnife.bind(this, mView)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        phone1.setText(phoneNumber1)

        Log.v("", "")
    }

    @Optional
    @OnClick(R.id.phone1)
    fun onPhoneNumberClick(view: View) {
        IntentHelper.startCallNumber(context = context!!, phoneNumber = phoneNumber1)
        dismiss()
    }

    @OnClick(R.id.okBtn)
    fun onOkClick(view: View) {
        IntentHelper.startCallNumber(context = context!!, phoneNumber = phoneNumber1)
        dismiss()
        Log.v("Clicked view", view.id.toString())
    }

}
