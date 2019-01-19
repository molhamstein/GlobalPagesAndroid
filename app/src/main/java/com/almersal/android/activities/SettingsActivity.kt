package com.almersal.android.activities

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.View
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.almersal.android.R
import com.almersal.android.repositories.SettingData
import com.almersal.android.repositories.UserRepository
import com.almersal.android.utilities.IntentHelper


class SettingsActivity : BaseActivity() {

    @BindView(R.id.toolbar)
    lateinit var toolbar: Toolbar

    fun initToolBar() {
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onBaseCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.settings_layout)
        ButterKnife.bind(this)
        initToolBar()
    }

    @OnClick(R.id.ContactUsLayout)
    fun onContactUsLayoutClick(view: View) {
        IntentHelper.startEmailAddress(baseContext, SettingData.emailAddress)
    }

    @OnClick(R.id.termsOfUseLayout)
    fun onTermsOfUseLayoutClick(view: View) {
        IntentHelper.startWebSite(baseContext, SettingData.siteAddress)
    }

    @OnClick(R.id.logoutBtn)
    fun onLogoutButtonClick(view: View) {
        UserRepository(baseContext).flushUser()
        IntentHelper.startMainActivity(baseContext)
        finish()
    }

}