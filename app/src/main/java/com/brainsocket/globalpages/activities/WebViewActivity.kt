package com.brainsocket.globalpages.activities

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.webkit.*
import android.widget.ProgressBar
import butterknife.BindView
import butterknife.ButterKnife
import com.brainsocket.globalpages.R
import com.brainsocket.globalpages.R.id.webView
import com.brainsocket.globalpages.views.AppWebViewClients


class WebViewActivity : BaseActivity() {

    companion object {
        const val WebViewActivity_Tag = "WebViewActivity_Tag"
    }

    @BindView(R.id.webView)
    lateinit var webView: WebView

    @BindView(R.id.toolbar)
    lateinit var toolbar: Toolbar

    lateinit var progressBar: ProgressBar

    fun initToolBar() {
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }


    override fun onBaseCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.web_view_layout)
        ButterKnife.bind(this)

        initToolBar()
        progressBar = ProgressBar(this)

        val url = intent.getStringExtra(WebViewActivity_Tag)
        loadPage(url)
    }

    private fun loadPage(url: String) {
        webView.setNetworkAvailable(true)
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webView.webChromeClient = WebChromeClient()
        webView.webViewClient = AppWebViewClients(progressBar)
//        webView.webViewClient = object : WebViewClient() {
//
//            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
//                super.onReceivedError(view, request, error)
//            }
//
//            override fun onPageFinished(view: WebView?, url: String?) {
//                super.onPageFinished(view, url)
//            }
//        }
        webView.loadUrl(url)
    }

}