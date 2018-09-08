package com.brainsocket.globalpages.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import butterknife.ButterKnife
import butterknife.OnClick
import com.brainsocket.globalpages.R
import com.brainsocket.globalpages.data.entitiesResponses.LoginResponse
import com.brainsocket.globalpages.di.component.DaggerSigninComponent
import com.brainsocket.globalpages.di.module.SigninModule
import com.brainsocket.globalpages.di.ui.SigninContract
import com.brainsocket.globalpages.di.ui.SigninPresenter
import com.brainsocket.globalpages.dialogs.ProgressDialog
import com.brainsocket.globalpages.repositories.UserRepository
import com.brainsocket.globalpages.utilities.intentHelper
import com.brainsocket.globalpages.utilities.mainHelper
import com.brainsocket.globalpages.viewHolders.SigninViewHolder
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

    @OnClick(R.id.registerTextViewLink)
    fun onRegisterClick(view: View) {
        intentHelper.startSignUpActivity(SignInActivity@ this)
        Log.v("View Clicked", view.id.toString())
    }

    @OnClick(R.id.forgotTextViewLink)
    fun onForgotClick(view: View) {
        intentHelper.startForgotPasswordActivity(SignInActivity@ this)
        Log.v("View Clicked", view.id.toString())
    }

    /*Presenter started*/
    override fun showProgress(show: Boolean) {
        if (show)
            progressDialog.show(supportFragmentManager, ProgressDialog.ProgressDialog_Tag)
        else
            progressDialog.dismiss()
    }

    override fun loginSuccesfully(loginResponse: LoginResponse) {
        UserRepository(context = this).addUser(loginResponse.user)
        intentHelper.startMainActivity(this)
        Log.v("", "")
    }

    override fun loginFail() {
        mainHelper.hideKeyboard(findViewById(android.R.id.content))
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
