package com.brainsocket.globalpages.activities

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.brainsocket.globalpages.R
import com.brainsocket.globalpages.di.component.DaggerForgotPasswordComponent
import com.brainsocket.globalpages.di.module.ForgotPasswordModule
import com.brainsocket.globalpages.di.ui.ForgotPasswordContract
import com.brainsocket.globalpages.di.ui.ForgotPasswordPresenter
import com.brainsocket.globalpages.dialogs.ProgressDialog
import com.brainsocket.globalpages.viewHolders.ForgotPasswordViewHolder
import javax.inject.Inject

/**
 * Created by Adhamkh on 2018-06-08.
 */
class ForgotPasswordActivity : BaseActivity(), ForgotPasswordContract.View {


    @Inject
    lateinit var presenter: ForgotPasswordPresenter

    lateinit var viewHolder: ForgotPasswordViewHolder

    lateinit var progressDialog: ProgressDialog

    @BindView(R.id.toolbar)
    lateinit var toolbar: Toolbar

    fun initDI() {
        val component = DaggerForgotPasswordComponent.builder()
                .forgotPasswordModule(ForgotPasswordModule(this))
                .build()
        component.inject(this)
        presenter.attachView(this)
        presenter.subscribe()

    }

    fun initToolBar() {
        toolbar.setNavigationOnClickListener { onBackPressed() }
        setSupportActionBar(toolbar)
    }

    override fun onBaseCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.forgotpassword_layout)
        ButterKnife.bind(this)
        initDI()
        initToolBar()
        viewHolder = ForgotPasswordViewHolder(findViewById(android.R.id.content))
        progressDialog = ProgressDialog.newInstance()
    }

    @OnClick(R.id.forgotPasswordBtn)
    fun onForgorPasswordClick(view: View) {
        if (viewHolder.isValid())
            presenter.requestPassword(viewHolder.getForgotModel())
    }


    /*Presenter Started*/
    override fun showProgress(show: Boolean) {
        if (show)
            progressDialog.show(supportFragmentManager, ProgressDialog.ProgressDialog_Tag)
        else
            progressDialog.dismiss()
    }

    override fun sendSuccesfully() {
        Toast.makeText(baseContext, R.string.resetPasswordSuccesfully, Toast.LENGTH_LONG).show()
        Log.v("", "")
    }

    override fun requestFail() {
        Toast.makeText(baseContext, R.string.emailnotExisted, Toast.LENGTH_LONG).show()
    }

    override fun showLoadErrorMessage(visible: Boolean) {
        if (visible)
            Toast.makeText(baseContext, R.string.checkInternetConnection, Toast.LENGTH_LONG).show()
        Log.v("", "")
    }
    /*Presenter Ended*/
}