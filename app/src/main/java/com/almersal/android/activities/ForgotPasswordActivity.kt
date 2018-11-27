package com.almersal.android.activities

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.almersal.android.R
import com.almersal.android.di.component.DaggerForgotPasswordComponent
import com.almersal.android.di.module.ForgotPasswordModule
import com.almersal.android.di.ui.ForgotPasswordContract
import com.almersal.android.di.ui.ForgotPasswordPresenter
import com.almersal.android.dialogs.ProgressDialog
import com.almersal.android.viewModel.ForgotPasswordViewHolder
import javax.inject.Inject


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
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onBaseCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.forgotpassword_layout)
        ButterKnife.bind(this)
        initToolBar()
        initDI()
        viewHolder = ForgotPasswordViewHolder(findViewById(android.R.id.content))
        progressDialog = ProgressDialog.newInstance()
    }

    @OnClick(R.id.forgotPasswordBtn)
    fun onForgotPasswordClick(view: View) {
        if (viewHolder.isValid())
            presenter.requestPassword(viewHolder.getForgotModel())
        Log.v("View Clicked", view.id.toString())
    }


    /*Presenter Started*/
    override fun showProgress(show: Boolean) {
        if (show)
            progressDialog.show(supportFragmentManager, ProgressDialog.ProgressDialog_Tag)
        else
            progressDialog.dismiss()
    }

    override fun sendSuccessfully() {
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