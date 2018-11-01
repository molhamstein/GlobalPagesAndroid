package com.brainsocket.globalpages.activities

import android.os.Bundle
import android.view.View
import butterknife.ButterKnife
import butterknife.OnClick
import com.brainsocket.globalpages.R
import com.brainsocket.globalpages.repositories.SettingData
import com.brainsocket.globalpages.repositories.UserRepository
import com.brainsocket.globalpages.utilities.IntentHelper


class SettingsActivity : BaseActivity() {

    override fun onBaseCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.settings_layout)
        ButterKnife.bind(this)
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