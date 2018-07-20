package com.brainsocket.globalpages.activities

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.support.annotation.FloatRange
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.brainsocket.globalpages.R
import com.brainsocket.globalpages.adapters.*
import com.brainsocket.globalpages.data.entities.Attachment
import com.brainsocket.globalpages.repositories.DummydataRepositories

/**
 * Created by Adhamkh on 2018-07-18.
 */
class PostAddActivity : BaseActivity() {
    companion object {
        var USER_ID_TAG: String = "USER_ID"

    }

    @BindView(R.id.toolbar)
    lateinit var toolbar: Toolbar

    @BindView(R.id.adImages)
    lateinit var adImages: RecyclerView

    @BindView(R.id.adCategories)
    lateinit var adCategories: RecyclerView

    @BindView(R.id.adSubCategories)
    lateinit var adSubCategories: RecyclerView

    @BindView(R.id.adCities)
    lateinit var adCities: RecyclerView

    @BindView(R.id.adLocations)
    lateinit var adLocations: RecyclerView

    @BindView(R.id.baseContainer)
    lateinit var baseContainer: View

    @BindView(R.id.resultContainer)
    lateinit var resultContainer: View

    fun initToolBar() {
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    fun initRecyclerViews() {
        adImages.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        adImages.adapter = AttachmentRecyclerViewAdapter(this, DummydataRepositories.getAttachmentList())

        adCategories.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        adCategories.adapter = CategoryRecyclerViewAdapter(this, DummydataRepositories.getCategoiesList())

        adSubCategories.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        adSubCategories.adapter = SubCategoryRecyclerViewAdapter(this, DummydataRepositories.getSubCategoriesList())

        adCities.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        adCities.adapter = CityRecyclerViewAdapter(this, DummydataRepositories.getCityList())

        adLocations.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        adLocations.adapter = LocationEntityRecyclerViewAdapter(this, DummydataRepositories.getLocationList())

    }

    override fun onBaseCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.post_add_layout)
        ButterKnife.bind(this)

        initToolBar()
        initRecyclerViews()

    }

    @OnClick(R.id.addAttachmentBtn)
    fun onAddAttachmentClick(view: View) {
        (adImages.adapter as AttachmentRecyclerViewAdapter).addItem(Attachment())
    }

    @OnClick(R.id.adAddBtn)
    fun onAdAddBtn(view: View) {
        animate()
        Log.v("", "")
    }

    @OnClick(R.id.adBackHomeBtn)
    fun onAddBackHomeClick(view: View) {
        finish()
    }

    fun animate() {
        resultContainer.visibility = View.VISIBLE
        var baseAnimator: ObjectAnimator = ObjectAnimator.ofFloat(baseContainer, "alpha", 1.0f, 0.0f)
        var baseTranslationYAnimator: ObjectAnimator = ObjectAnimator.ofFloat(baseContainer, "translationY", 0.0f, -baseContainer.height.toFloat())
        var baseTranslationZAnimator: ObjectAnimator = ObjectAnimator.ofFloat(baseContainer, "translationZ", 0.0f, 10.0f)

        var resultAnimator: ObjectAnimator = ObjectAnimator.ofFloat(resultContainer, "alpha", 0.0f, 1.0f)
        var resultTranslationYAnimator: ObjectAnimator = ObjectAnimator.ofFloat(resultContainer, "translationY", baseContainer.height.toFloat(), .0f)
        var resultTranslationZAnimator: ObjectAnimator = ObjectAnimator.ofFloat(resultContainer, "translationZ", 10.0f, 0.0f)

        var animatorSet = AnimatorSet()
        animatorSet.interpolator = LinearInterpolator()
        animatorSet.duration = 2000
        animatorSet.playTogether(baseAnimator, resultAnimator, baseTranslationYAnimator, baseTranslationZAnimator,
                resultTranslationYAnimator, resultTranslationZAnimator)
        animatorSet.start()
    }

}