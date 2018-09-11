package com.brainsocket.globalpages.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.brainsocket.globalpages.R
import com.brainsocket.globalpages.adapters.AttachmentRecyclerViewAdapter
import com.brainsocket.globalpages.adapters.CategoryRecyclerViewAdapter
import com.brainsocket.globalpages.adapters.SubCategoryRecyclerViewAdapter
import com.brainsocket.globalpages.data.entities.Attachment
import com.brainsocket.globalpages.repositories.DummyDataRepositories
import com.google.android.gms.location.places.ui.PlacePicker
import android.widget.Toast
import com.brainsocket.globalpages.data.entities.BusinessGuideCategory
import com.brainsocket.globalpages.data.entities.Category
import com.brainsocket.globalpages.di.ui.TagsCollectionContact
import com.brainsocket.globalpages.listeners.OnCategorySelectListener
import com.brainsocket.mainlibrary.Enums.LayoutStatesEnum
import com.brainsocket.mainlibrary.Views.Stateslayoutview


class BusinessGuideAddActivity : BaseActivity(), TagsCollectionContact.View, OnCategorySelectListener {

    @BindView(R.id.businessImages)
    lateinit var businessImages: RecyclerView

    @BindView(R.id.businessTypes)
    lateinit var businessCategories: RecyclerView

    @BindView(R.id.businessSubCategories)
    lateinit var businessSubCategories: RecyclerView

    @BindView(R.id.stateLayout)
    lateinit var stateLayout: Stateslayoutview

    var PLACE_PICKER_REQUEST = 1

    private fun initRecyclerView() {
        businessImages.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        businessImages.adapter = AttachmentRecyclerViewAdapter(this, DummyDataRepositories.getAttachmentList())

        businessCategories.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
//        businessTypes.adapter = CategoryRecyclerViewAdapter(this, DummyDataRepositories.getCategoriesList())

        businessSubCategories.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
//        businessSubCategories.adapter = SubCategoryRecyclerViewAdapter(this, DummyDataRepositories.getSubCategoriesList())

    }

    override fun onBaseCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.business_guide_add_layout)
        ButterKnife.bind(this)
        initRecyclerView()

    }

    @OnClick(R.id.locationEditText)
    fun onLocationEditTextClick(view: View) {
        val builder = PlacePicker.IntentBuilder()
        startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST)
        Log.v("", "")
    }

    @OnClick(R.id.addAttachmentBtn)
    fun onAddAttachmentClick(view: View) {
        (businessImages.adapter as AttachmentRecyclerViewAdapter).addItem(Attachment())
        Log.v("View Clicked", view.id.toString())
    }

    @OnClick(R.id.skipBtn)
    fun onSkipClick(view: View) {
        finish()
        Log.v("View Clicked", view.id.toString())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                val place = PlacePicker.getPlace(this, data)
                val toastMsg = String.format("Place: %s", place.name)
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show()
            }
        }
    }

    /*Tag Collection presenter started*/
    override fun onBusinessCategoriesLoaded(categoriesList: MutableList<BusinessGuideCategory>) {
        super.onBusinessCategoriesLoaded(categoriesList)
        businessCategories.adapter = CategoryRecyclerViewAdapter(context = this,
                categoriesList = categoriesList.toMutableList(), onCategorySelectListener = this)
        Log.v("", "")
    }

    override fun showBusinessCategoriesProgress(show: Boolean) {
        super.showBusinessCategoriesProgress(show)
        if (show) {
            stateLayout.FlipLayout(LayoutStatesEnum.Waitinglayout)
        } else {
            stateLayout.FlipLayout(LayoutStatesEnum.SuccessLayout)
        }
        Log.v("", "")
    }

    override fun showBusinessCategoriesLoadErrorMessage(visible: Boolean) {
        super.showBusinessCategoriesLoadErrorMessage(visible)
        if (visible) {
            stateLayout.FlipLayout(LayoutStatesEnum.Noconnectionlayout)
        } else {
            stateLayout.FlipLayout(LayoutStatesEnum.SuccessLayout)
        }
        Log.v("", "")
    }

    override fun showBusinessCategoriesEmptyView(visible: Boolean) {
        super.showBusinessCategoriesEmptyView(visible)
        if (visible) {
            stateLayout.FlipLayout(LayoutStatesEnum.Nodatalayout)
        } else {
            stateLayout.FlipLayout(LayoutStatesEnum.SuccessLayout)
        }
        Log.v("", "")
    }
    /*Tag collection presenter ended*/


    override fun onSelectCategory(category: Category) {
        businessSubCategories.adapter = SubCategoryRecyclerViewAdapter(context = this, subCategoriesList = category.subCategoriesList)
        Log.v("", "")
    }

}