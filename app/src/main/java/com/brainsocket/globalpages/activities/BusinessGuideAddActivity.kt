package com.brainsocket.globalpages.activities

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
import com.brainsocket.globalpages.data.entities.BusinessGuideCategory
import com.brainsocket.globalpages.di.ui.TagsCollectionContact
import com.brainsocket.globalpages.repositories.DummyDataRepositories


class BusinessGuideAddActivity : BaseActivity(){

    @BindView(R.id.businessImages)
    lateinit var businessImages: RecyclerView

    @BindView(R.id.businessTypes)
    lateinit var businessTypes: RecyclerView

    @BindView(R.id.businessSubCategories)
    lateinit var businessSubCategories: RecyclerView

    private fun initRecyclerView() {
        businessImages.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        businessImages.adapter = AttachmentRecyclerViewAdapter(this, DummyDataRepositories.getAttachmentList())

        businessTypes.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        businessTypes.adapter = CategoryRecyclerViewAdapter(this, DummyDataRepositories.getCategoriesList())

        businessSubCategories.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        businessSubCategories.adapter = SubCategoryRecyclerViewAdapter(this, DummyDataRepositories.getSubCategoriesList())

    }

    override fun onBaseCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.business_guide_add_layout)
        ButterKnife.bind(this)
        initRecyclerView()

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


}