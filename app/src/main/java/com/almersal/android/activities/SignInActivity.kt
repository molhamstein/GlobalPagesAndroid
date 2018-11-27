package com.almersal.android.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Optional
import com.almersal.android.R
import com.almersal.android.data.entitiesResponses.LoginResponse
import com.almersal.android.di.component.DaggerSigninComponent
import com.almersal.android.di.module.SigninModule
import com.almersal.android.di.ui.SigninContract
import com.almersal.android.di.ui.SigninPresenter
import com.almersal.android.dialogs.ProgressDialog
import com.almersal.android.repositories.UserRepository
import com.almersal.android.utilities.IntentHelper
import com.almersal.android.utilities.MainHelper
import com.almersal.android.viewModel.SigninViewHolder
import javax.inject.Inject


class SignInActivity : BaseActivity(), SigninContract.View {

    @Inject
    lateinit var presenter: SigninPresenter

    private lateinit var viewHolder: SigninViewHolder

    private lateinit var progressDialog: ProgressDialog

    private fun initDI() {
        val component = DaggerSigninComponent.builder()
                .signinModule(SigninModule(this))
                .build()
        component.inject(this)
        presenter.attachView(this)
        presenter.subscribe()
    }

    override fun onBaseCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.signin_layout)
        ButterKnife.bind(this)

        initDI()
        viewHolder = SigninViewHolder(findViewById(android.R.id.content))
        progressDialog = ProgressDialog.newInstance()

    }

    @OnClick(R.id.loginBtn)
    fun onLoginBtnClick(view: View) {
        if (viewHolder.isValid())
            presenter.authenticate(viewHolder.getLoginModel())
        Log.v("View Clicked", view.id.toString())
    }

    @Optional
    @OnClick(R.id.registerTextViewLink, R.id.registerContainer)
    fun onRegisterClick(view: View) {
        IntentHelper.startSignUpActivity(SignInActivity@ this)
        Log.v("View Clicked", view.id.toString())
    }

    @OnClick(R.id.forgotTextViewLink)
    fun onForgotClick(view: View) {
        IntentHelper.startForgotPasswordActivity(SignInActivity@ this)
        Log.v("View Clicked", view.id.toString())
    }

    /*Presenter started*/
    override fun showProgress(show: Boolean) {
        if (show)
            progressDialog.show(supportFragmentManager, ProgressDialog.ProgressDialog_Tag)
        else
            progressDialog.dismiss()
    }

    override fun loginSuccessfully(loginResponse: LoginResponse) {
        loginResponse.user.token = loginResponse.id
        UserRepository(context = this).addUser(loginResponse.user)
        IntentHelper.startMainActivity(this)
        Log.v("", "")
    }

    override fun loginFail() {
        MainHelper.hideKeyboard(findViewById(android.R.id.content))
        Toast.makeText(baseContext, R.string.invalidEmailorPassword, Toast.LENGTH_LONG).show()
        Log.v("", "")
    }

    override fun showLoadErrorMessage(visible: Boolean) {
        if (visible)
            Toast.makeText(baseContext, R.string.checkInternetConnection, Toast.LENGTH_LONG).show()
        Log.v("", "")
    }
    /*Presenter ended*/

}
