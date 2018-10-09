package com.brainsocket.globalpages.activities

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import butterknife.ButterKnife
import butterknife.OnClick
import com.brainsocket.globalpages.R
import com.brainsocket.globalpages.data.entities.BusinessGuide
import com.brainsocket.globalpages.data.entities.ProductThumb
import com.brainsocket.globalpages.di.component.DaggerProductManageComponent
import com.brainsocket.globalpages.di.module.AttachmentModule
import com.brainsocket.globalpages.di.module.BusinessGuideProductModule
import com.brainsocket.globalpages.di.ui.AttachmentContract
import com.brainsocket.globalpages.di.ui.AttachmentPresenter
import com.brainsocket.globalpages.di.ui.BusinessGuideProductContract
import com.brainsocket.globalpages.di.ui.BusinessGuideProductPresenter
import com.brainsocket.globalpages.viewModel.BusinessGuideProductViewHolder
import com.google.android.gms.location.places.ui.PlacePicker
import com.google.gson.Gson
import java.io.File
import javax.inject.Inject

import com.fxn.pix.Pix
import com.fxn.utility.PermUtil


class ProductManageActivity : BaseActivity(), BusinessGuideProductContract.View, AttachmentContract.View {

    companion object {
        const val ProductManageActivity_Tag = "ProductManageActivity_Tag"

        var PLACE_PICKER_REQUEST = 1
        var PICTURE_REQUEST = 100

    }


    lateinit var productThumb: ProductThumb

    @Inject
    lateinit var productPresenter: BusinessGuideProductPresenter

    @Inject
    lateinit var attachmentPresenter: AttachmentPresenter

    lateinit var businessGuideProductViewHolder: BusinessGuideProductViewHolder

    fun initDI() {
        val component = DaggerProductManageComponent.builder()
                .attachmentModule(AttachmentModule(this))
                .businessGuideProductModule(BusinessGuideProductModule(this))
                .build()
        component.inject(this)

        productPresenter.attachView(this)
        productPresenter.subscribe()


        attachmentPresenter.attachView(this)
        attachmentPresenter.subscribe()
    }

    override fun onBaseCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.product_manage_layout)
        ButterKnife.bind(this)
        val jSon = intent.getStringExtra(ProductManageActivity_Tag)
        productThumb = Gson().fromJson(jSon, ProductThumb::class.java)
        businessGuideProductViewHolder = BusinessGuideProductViewHolder(findViewById(android.R.id.content))
        initDI()

    }

    @OnClick(R.id.productImage)
    fun onProductImageClick(view: View) {
        Pix.start(this@ProductManageActivity, PICTURE_REQUEST, 1)
    }

    @OnClick(R.id.productCloseBtn)
    fun onProductCloseButtonClick(view: View) {
        finish()
    }

    @OnClick(R.id.productApplyBtn)
    fun onProductApplyButtonClick(view: View) {
//        productPresenter.addProduct()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                PLACE_PICKER_REQUEST -> {
                    val place = PlacePicker.getPlace(this, data)
                    val toastMsg = String.format("Place: %s", place.name)
                    Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show()
                }
                PICTURE_REQUEST -> {
                    val returnValue = data!!.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                    attachmentPresenter.loadAttachmentFile(File(returnValue[0]))
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
                    Pix.start(this@ProductManageActivity, BusinessGuideAddActivity.PICTURE_REQUEST, 1)
                } else {
                    Toast.makeText(this@ProductManageActivity
                            , resources.getString(R.string.Approve_Permissions_To_Pick_Images), Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }


    /*product presenter started*/
    override fun onAddProductSuccessfully() {

    }

    override fun onAddProductFail() {

    }
    /*product presenter ended*/


    /*Attachment presenter started*/
    override fun showAttachmentProgress(show: Boolean) {

    }

    override fun showAttachmentProcessingPercentage(percentage: String) {

    }

    override fun showAttachmentLoadErrorMessage(visible: Boolean) {

    }

    override fun showAttachmentEmptyView(visible: Boolean) {

    }

    override fun onLoadAttachmentListSuccessfully(filePath: String) {
        Log.v("", "");
    }
    /*Attachment presenter ended*/


}