package com.almersal.android.activities

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.almersal.android.R
import com.almersal.android.api.ServerInfo
import com.almersal.android.data.entities.BusinessGuide
import com.almersal.android.data.entities.ProductThumb
import com.almersal.android.data.entitiesModel.ProductThumbEditModel
import com.almersal.android.data.entitiesModel.ProductThumbModel
import com.almersal.android.di.component.DaggerProductAddCommponent
import com.almersal.android.di.module.AttachmentModule
import com.almersal.android.di.module.BusinessGuideProductModule
import com.almersal.android.di.module.TagsCollectionModule
import com.almersal.android.di.ui.AttachmentContract
import com.almersal.android.di.ui.AttachmentPresenter
import com.almersal.android.di.ui.BusinessGuideProductContract
import com.almersal.android.di.ui.BusinessGuideProductPresenter
import com.almersal.android.dialogs.ProgressDialog
import com.almersal.android.eventsBus.EventActions
import com.almersal.android.eventsBus.MessageEvent
import com.almersal.android.eventsBus.RxBus
import com.almersal.android.repositories.UserRepository
import com.almersal.android.utilities.BindingUtils
import com.almersal.android.viewModel.BusinessGuideProductViewHolder
import javax.inject.Inject

import com.fxn.pix.Pix
import com.fxn.utility.PermUtil
import com.google.gson.Gson
import java.io.File

class ProductAddActivity : BaseActivity(), AttachmentContract.View, BusinessGuideProductContract.View {
    companion object {
        const val ProductAddActivity_Tag = "ProductAddActivity_Tag"

        const val ProductAddActivity_Product_Tag = "ProductAddActivity_Product_Tag"

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

    @BindView(R.id.productApplyBtn)
    lateinit var productApplyBtn: Button

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

        initDI()

        businessGuideProductProductViewHolder = BusinessGuideProductViewHolder(findViewById(android.R.id.content))

        val businessGuideString: String? = intent.extras?.getString(ProductAddActivity_Product_Tag, null)
        if (!businessGuideString.isNullOrEmpty()) {
            val productThumb: ProductThumb = Gson().fromJson(businessGuideString, ProductThumb::class.java)
            businessGuideProductProductViewHolder.bind(productThumb)
            productApplyBtn.setText(R.string.Update)
        }

    }

    private fun requestAction() {
        if (businessGuideProductProductViewHolder.isValid()) {

            val token = UserRepository(baseContext).getUser()!!.token
            val url = ServerInfo.businessGuideUrl + "/" + businessGuide.id + "/myProducts"
            if (businessGuideProductProductViewHolder.isAdd())
                presenter.addProduct(url,
                        businessGuideProductProductViewHolder.getProductThumbModel(), token)
            else
                presenter.updateProduct(url, businessGuideProductProductViewHolder.getProductThumbModel()
                        as ProductThumbEditModel, token)
        }
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
        requestAction()
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


    /*Base presenter started*/
    override fun showProgress(show: Boolean) {
        if (show) {
//            val progressDialog: ProgressDialog? =
//                    supportFragmentManager.findFragmentByTag(ProgressDialog.ProgressDialog_Tag) as ProgressDialog?
//            progressDialog?.dialog?.dismiss()
            val dialog = ProgressDialog.newInstance()
            dialog.showDialog(supportFragmentManager)
        } else {
            ProgressDialog.closeDialog(supportFragmentManager)
//            val progressDialog: ProgressDialog? =
//                    supportFragmentManager.findFragmentByTag(ProgressDialog.ProgressDialog_Tag) as ProgressDialog?
//            progressDialog?.dialog?.dismiss()
        }
        Log.v("", "")
    }

    override fun showLoadErrorMessage(visible: Boolean) {
        if (visible) {
            Toast.makeText(baseContext, R.string.checkInternetConnection, Toast.LENGTH_LONG).show()
        } else {

        }
    }
    /*Base presenter ended*/

    /*Business Guide Product Presenter started*/
    override fun onAddProductSuccessfully(productThumb: ProductThumb) {
        businessGuideProductProductViewHolder.bind(productThumb)
        Toast.makeText(baseContext, R.string.productAddedSuccessfully, Toast.LENGTH_LONG).show()
        RxBus.publish(MessageEvent(EventActions.ProductAddActivity_Tag, businessGuideProductProductViewHolder.getProductThumbModel()))
   finish()
    }

    override fun onAddProductFail() {
        Toast.makeText(baseContext, R.string.unexpectedErrorHappend, Toast.LENGTH_LONG).show()
    }

    override fun onProductUpdateSuccessfully(productThumb: ProductThumb) {
        businessGuideProductProductViewHolder.bind(productThumb)
        finish()
        RxBus.publish(MessageEvent(EventActions.ProductAddActivity_Tag, businessGuideProductProductViewHolder.getProductThumbModel()))
        Toast.makeText(baseContext, R.string.productUpdatedSuccessfully, Toast.LENGTH_LONG).show()
        finish()
    }

    override fun onProductUpdateFail() {
        Toast.makeText(baseContext, R.string.unexpectedErrorHappend, Toast.LENGTH_LONG).show()
    }

    /*Business Guide Product Presenter ended*/


    /*Attachment Presenter started*/
    override fun showAttachmentProgress(show: Boolean) {
        if (show) {
//            val progressDialog: ProgressDialog? =
//                    supportFragmentManager.findFragmentByTag(ProgressDialog.ProgressDialog_Tag) as ProgressDialog?
//            progressDialog?.dialog?.dismiss()
            val dialog = ProgressDialog.newInstance()
            dialog.showDialog(supportFragmentManager)
        } else {
            ProgressDialog.Companion.closeDialog(supportFragmentManager)
        }
    }

    override fun showAttachmentProcessingPercentage(percentage: String) {

    }

    override fun showAttachmentLoadErrorMessage(visible: Boolean) {
        if (visible) {
            Toast.makeText(baseContext, R.string.checkInternetConnection, Toast.LENGTH_LONG).show()
            ProgressDialog.closeDialog(supportFragmentManager)
        } else {

        }
        Log.v("", "")
    }

    override fun showAttachmentEmptyView(visible: Boolean) {

    }

    override fun onLoadAttachmentListSuccessfully(filePath: String) {
        BindingUtils.loadImage(productImage, filePath)
        businessGuideProductProductViewHolder.productImageTag.text = (filePath)

    }
    /*Attachment Presenter ended*/


}