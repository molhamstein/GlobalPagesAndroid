package com.brainsocket.globalpages.activities

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.brainsocket.globalpages.R
import com.brainsocket.globalpages.api.ServerInfo
import com.brainsocket.globalpages.data.entities.BusinessGuide
import com.brainsocket.globalpages.di.component.DaggerProductAddCommponent
import com.brainsocket.globalpages.di.module.AttachmentModule
import com.brainsocket.globalpages.di.module.BusinessGuideProductModule
import com.brainsocket.globalpages.di.module.TagsCollectionModule
import com.brainsocket.globalpages.di.ui.AttachmentContract
import com.brainsocket.globalpages.di.ui.AttachmentPresenter
import com.brainsocket.globalpages.di.ui.BusinessGuideProductContract
import com.brainsocket.globalpages.di.ui.BusinessGuideProductPresenter
import com.brainsocket.globalpages.dialogs.ProgressDialog
import com.brainsocket.globalpages.utilities.BindingUtils
import com.brainsocket.globalpages.viewModel.BusinessGuideProductViewHolder
import javax.inject.Inject

import com.fxn.pix.Pix
import com.fxn.utility.PermUtil
import com.google.gson.Gson
import java.io.File

class ProductAddActivity : BaseActivity(), AttachmentContract.View, BusinessGuideProductContract.View {
    companion object {
        const val ProductAddActivity_Tag = "ProductAddActivity_Tag"
        var PLACE_PICKER_REQUEST = 1
        var PICTURE_REQUEST = 100

        var MAP_BUTTON_REQUEST_CODE = 101
    }

    @Inject
    lateinit var presenter: BusinessGuideProductPresenter

    @Inject
    lateinit var attachmentPresenter: AttachmentPresenter

    @BindView(R.id.productImage)
    lateinit var productImage: ImageView

    lateinit var businessGuide: BusinessGuide

    lateinit var businessGuideProductProductViewHolder: BusinessGuideProductViewHolder

    fun initDI() {
        val component = DaggerProductAddCommponent.builder()
                .businessGuideProductModule(BusinessGuideProductModule(this))
                .attachmentModule(AttachmentModule(this))
                .build()
        component.inject(this)

        presenter.attachView(this)
        presenter.subscribe()

        attachmentPresenter.attachView(this)
        attachmentPresenter.subscribe()
    }


    override fun onBaseCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.product_add_layout)
        ButterKnife.bind(this)
        val jSon = intent.getStringExtra(ProductAddActivity_Tag)
        businessGuide = Gson().fromJson(jSon, BusinessGuide::class.java)

        val x = businessGuide.id
        Log.v("x", x)
        initDI()

        businessGuideProductProductViewHolder = BusinessGuideProductViewHolder(findViewById(android.R.id.content))

    }

    @OnClick(R.id.productCloseBtn)
    fun onProductCloseButtonClick(view: View) {
        finish()
    }

    @OnClick(R.id.productImage)
    fun onProductImageClick(view: View) {
        Pix.start(this@ProductAddActivity, ProductAddActivity.PICTURE_REQUEST, 1)
        Log.v("View Clicked", view.id.toString())
    }

    @OnClick(R.id.productApplyBtn)
    fun onProductApplyButtonClick(view: View) {
        if (businessGuideProductProductViewHolder.isValid())
            presenter.addProduct(ServerInfo.businessGuideUrl + "/" + businessGuide.id + "/myProducts",
                    businessGuideProductProductViewHolder.getProductThumbModel())
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                ProductAddActivity.PICTURE_REQUEST -> {
                    val returnValue = data!!.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                    attachmentPresenter.loadAttachmentFile(File(returnValue[0]))
                    Log.v("", "")
                }
                ProductAddActivity.MAP_BUTTON_REQUEST_CODE -> {
                    Log.v("", "")
                }
            }
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Pix.start(this@ProductAddActivity, ProductAddActivity.PICTURE_REQUEST, 1)
                } else {
                    Toast.makeText(this@ProductAddActivity, resources.getString(R.string.Approve_Permissions_To_Pick_Images), Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }


    /*Business Guide Product Presenter started*/
    override fun onAddProductSuccessfully() {

    }

    override fun onAddProductFail() {

    }
    /*Business Guide Product Presenter ended*/


    /*Attachment Presenter started*/
    override fun showAttachmentProgress(show: Boolean) {
        if (show) {
            val progressDialog: ProgressDialog? =
                    supportFragmentManager.findFragmentByTag(ProgressDialog.ProgressDialog_Tag) as ProgressDialog?
            progressDialog?.dialog?.dismiss()
            val dialog = ProgressDialog.newInstance()
            dialog.show(supportFragmentManager, ProgressDialog.ProgressDialog_Tag)
        } else {
            val progressDialog: ProgressDialog? =
                    supportFragmentManager.findFragmentByTag(ProgressDialog.ProgressDialog_Tag) as ProgressDialog?
            progressDialog?.dialog?.dismiss()
        }

    }

    override fun showAttachmentProcessingPercentage(percentage: String) {

    }

    override fun showAttachmentLoadErrorMessage(visible: Boolean) {
        if (visible) {
            Toast.makeText(baseContext, R.string.checkInternetConnection, Toast.LENGTH_LONG).show()
        } else {

        }
        Log.v("", "")
    }

    override fun showAttachmentEmptyView(visible: Boolean) {

    }

    override fun onLoadAttachmentListSuccessfully(filePath: String) {
        BindingUtils.loadImage(productImage, filePath)
    }
    /*Attachment Presenter ended*/


}