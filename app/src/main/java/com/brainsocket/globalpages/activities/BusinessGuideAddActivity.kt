package com.brainsocket.globalpages.activities

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.brainsocket.globalpages.R
import com.brainsocket.globalpages.adapters.AttachmentRecyclerViewAdapter
import com.brainsocket.globalpages.adapters.CategoryRecyclerViewAdapter
import com.brainsocket.globalpages.adapters.SubCategoryRecyclerViewAdapter
import com.brainsocket.globalpages.data.entities.Attachment
import com.brainsocket.globalpages.repositories.DummydataRepositories

/**
 * Created by Adhamkh on 2018-06-08.
 */
class BusinessGuideAddActivity : BaseActivity() {

    @BindView(R.id.businessImages)
    lateinit var businessImages: RecyclerView

    @BindView(R.id.businessTypes)
    lateinit var businessTypes: RecyclerView

    @BindView(R.id.businessSubCategories)
    lateinit var businessSubCategories: RecyclerView

    fun initRecyclerView() {
        businessImages.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        businessImages.adapter = AttachmentRecyclerViewAdapter(this, DummydataRepositories.getAttachmentList())

        businessTypes.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        businessTypes.adapter = CategoryRecyclerViewAdapter(this, DummydataRepositories.getCategoiesList())

        businessSubCategories.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        businessSubCategories.adapter = SubCategoryRecyclerViewAdapter(this, DummydataRepositories.getSubCategoriesList())

    }

    override fun onBaseCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.business_guide_add_layout)
        ButterKnife.bind(this)
        initRecyclerView()

    }

    @OnClick(R.id.addAttachmentBtn)
    fun onAddAttachmentClick(view: View) {
        (businessImages.adapter as AttachmentRecyclerViewAdapter).addItem(Attachment())
    }

    @OnClick(R.id.skipBtn)
    fun OnSkipClick(view: View) {
        finish()
    }

}