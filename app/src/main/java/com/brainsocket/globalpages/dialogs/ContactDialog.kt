package com.brainsocket.globalpages.dialogs

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
import com.brainsocket.globalpages.R
import com.brainsocket.globalpages.utilities.IntentHelper

class ContactDialog : DialogFragment() {

    companion object {
        const val ContactDialog_Tag = "ContactDialog"

        fun newInstance(phoneNumber1: String, phoneNumber2: String, faxNumber: String): ContactDialog {
            val f = ContactDialog()
            f.phoneNumber1 = phoneNumber1
            f.phoneNumber2 = phoneNumber2
            f.faxNumber = faxNumber
            // Supply num input as an argument.
            val args = Bundle()
            f.arguments = args

            return f
        }
    }

    lateinit var phoneNumber1: String

    lateinit var phoneNumber2: String

    lateinit var faxNumber: String

    @BindView(R.id.phone1)
    lateinit var phone1: EditText

    @BindView(R.id.phone2)
    lateinit var phone2: EditText

    @BindView(R.id.fax)
    lateinit var fax: EditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val mView = inflater.inflate(R.layout.contact_dialog_layout, container)
        ButterKnife.bind(this, mView)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        phone1.setText(phoneNumber1)
        phone2.setText(phoneNumber2)
        fax.setText(faxNumber)

        Log.v("", "")
    }

    @Optional
    @OnClick(R.id.phone1, R.id.phone2)
    fun onPhoneNumberClick(view: View) {
        var number = phoneNumber1
        when (view.id) {
            R.id.phone2 -> {
                number = phoneNumber2
            }
        }
        IntentHelper.startCallNumber(context = context!!, phoneNumber = number)
    }

    @OnClick(R.id.okBtn)
    fun onOkClick(view: View) {
        dismiss()
        Log.v("Clicked view", view.id.toString())
    }

}
