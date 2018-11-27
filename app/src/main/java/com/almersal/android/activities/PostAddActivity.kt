package com.almersal.android.activities

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.almersal.android.R
import com.almersal.android.adapters.*
import com.almersal.android.data.entities.*
import com.almersal.android.data.entitiesModel.PostEditModel
import com.almersal.android.di.component.DaggerPostAddComponent
import com.almersal.android.di.module.AttachmentModule
import com.almersal.android.di.module.PostModule
import com.almersal.android.di.module.TagsCollectionModule
import com.almersal.android.di.ui.*
import com.almersal.android.dialogs.ProgressDialog
import com.almersal.android.listeners.OnCategorySelectListener
import com.almersal.android.listeners.OnCitySelectListener
import com.almersal.android.repositories.DummyDataRepositories
import com.almersal.android.repositories.UserRepository
import com.almersal.android.viewModel.PostAddViewHolder
import com.brainsocket.mainlibrary.Enums.LayoutStatesEnum
import com.brainsocket.mainlibrary.Listeners.OnRefreshLayoutListener
import com.brainsocket.mainlibrary.Views.Stateslayoutview
import com.google.android.gms.location.places.ui.PlacePicker
import java.io.File
import javax.inject.Inject

import com.fxn.pix.Pix
import com.fxn.utility.PermUtil
import com.github.florent37.viewanimator.ViewAnimator
import com.google.gson.Gson


class PostAddActivity : BaseActivity(), PostContract.View, TagsCollectionContact.View, AttachmentContract.View
        , OnCategorySelectListener, OnCitySelectListener {

    companion object {
        var PLACE_PICKER_REQUEST = 1
        var PICTURE_REQUEST = 100

        var MAP_BUTTON_REQUEST_CODE = 101

        var USER_ID_TAG: String = "USER_ID"

        const val PostAddActivity_Tag = "PostAddActivity_Tag"
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

    @BindView(R.id.stateLayout)
    lateinit var stateLayout: Stateslayoutview

    @BindView(R.id.adAddBtn)
    lateinit var adAddBtn: Button

    @BindView(R.id.categoryStateLayout)
    lateinit var categoryStateLayout: Stateslayoutview

    @BindView(R.id.cityStateLayout)
    lateinit var cityStateLayout: Stateslayoutview

    lateinit var postAddViewHolder: PostAddViewHolder

    @Inject
    lateinit var tagsCollectionPresenter: TagsCollectionPresenter

    @Inject
    lateinit var attachmentPresenter: AttachmentPresenter

    @Inject
    lateinit var postPresenter: PostPresenter

    private fun initToolBar() {
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun initRecyclerViews() {
        adImages.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        adImages.adapter = AttachmentRecyclerViewAdapter(this, DummyDataRepositories.getAttachmentList())

        adCategories.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
//        adCategories.adapter = CategoryRecyclerViewAdapter(this, DummyDataRepositories.getCategoriesList())

        adSubCategories.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
//        adSubCategories.adapter = SubCategoryRecyclerViewAdapter(this, DummyDataRepositories.getSubCategoriesList())

        adCities.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
//        adCities.adapter = CityRecyclerViewAdapter(this, DummyDataRepositories.getCityList())

        adLocations.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
//        adLocations.adapter = LocationEntityRecyclerViewAdapter(this, DummyDataRepositories.getLocationList())

    }

    private fun initDI() {
        val component = DaggerPostAddComponent.builder()
                .tagsCollectionModule(TagsCollectionModule(this))
                .postModule(PostModule(this))
                .attachmentModule(AttachmentModule(this))
                .build()
        component.inject(this)

        tagsCollectionPresenter.attachView(this)
        tagsCollectionPresenter.subscribe()
        tagsCollectionPresenter.loadPostCategories(withCache = true)
        tagsCollectionPresenter.loadCities(withCache = true)

        attachmentPresenter.attachView(this)
        attachmentPresenter.subscribe()

        postPresenter.attachView(this)
        postPresenter.subscribe()

    }

    override fun onBaseCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.post_add_layout)
        ButterKnife.bind(this)

        initToolBar()
        initRecyclerViews()
        initDI()

        postAddViewHolder = PostAddViewHolder(findViewById(android.R.id.content))

        val postString: String? = intent.extras?.getString(PostAddActivity_Tag, null)
        if (!postString.isNullOrEmpty()) {
            val post: Post = Gson().fromJson(postString, Post::class.java)
            postAddViewHolder.bindPost(post)
            adAddBtn.setText(R.string.Update)
        }

        categoryStateLayout.setOnRefreshLayoutListener(object : OnRefreshLayoutListener {
            override fun onRefresh() {
                tagsCollectionPresenter.loadPostCategories(false)
            }

            override fun onRequestPermission() {

            }
        })

        cityStateLayout.setOnRefreshLayoutListener(object : OnRefreshLayoutListener {
            override fun onRefresh() {
                tagsCollectionPresenter.loadCities(false)
            }

            override fun onRequestPermission() {

            }
        })

        stateLayout.setOnRefreshLayoutListener(object : OnRefreshLayoutListener {
            override fun onRefresh() {
                requestAction()
            }

            override fun onRequestPermission() {

            }
        })


    }

    fun requestAction() {
        if (postAddViewHolder.isValid()) {

            val token = UserRepository(baseContext).getUser()!!.token
            if (postAddViewHolder.isAdd())
                postPresenter.addPost(postAddViewHolder.getPostModel(), token)
            else
                postPresenter.updatePost(postAddViewHolder.getPostModel()
                        as PostEditModel, token)
        }
    }


    @OnClick(R.id.addAttachmentBtn)
    fun onAddAttachmentClick(view: View) {
        Pix.start(this@PostAddActivity, BusinessGuideAddActivity.PICTURE_REQUEST, 1)
//        (adImages.adapter as AttachmentRecyclerViewAdapter).addItem(Attachment())
        Log.v("View Clicked", view.id.toString())
    }

    @OnClick(R.id.adAddBtn)
    fun onAdAddBtn(view: View) {
        requestAction()
//        animate()
        Log.v("View Clicked", view.id.toString())
    }

    @OnClick(R.id.adBackHomeBtn)
    fun onAddBackHomeClick(view: View) {
        finish()
        Log.v("View Clicked", view.id.toString())
    }

    private fun animate() {
        resultContainer.visibility = View.VISIBLE
        ViewAnimator.animate(baseContainer).translationY(0f, -1000f)
                .alpha(1f, 0f).andAnimate(resultContainer).translationY(1000f, 0f)
                .alpha(0f, 1f).start()

//        resultContainer.visibility = View.VISIBLE
//        val baseAnimator: ObjectAnimator = ObjectAnimator.ofFloat(baseContainer, "alpha", 1.0f, 0.0f)
//        val baseTranslationYAnimator: ObjectAnimator = ObjectAnimator.ofFloat(baseContainer, "translationY", 0.0f, -baseContainer.height.toFloat())
//        val baseTranslationZAnimator: ObjectAnimator = ObjectAnimator.ofFloat(baseContainer, "translationZ", 0.0f, 10.0f)
//
//        //Hard code toolbar margin top
//        val resultAnimator: ObjectAnimator = ObjectAnimator.ofFloat(resultContainer, "alpha", 0.0f, 1.0f)
//        val resultTranslationYAnimator: ObjectAnimator = ObjectAnimator.ofFloat(resultContainer, "translationY", baseContainer.height.toFloat() - 56, .0f)
//        val resultTranslationZAnimator: ObjectAnimator = ObjectAnimator.ofFloat(resultContainer, "translationZ", 10.0f, 0.0f)
//
//        val animatorSet = AnimatorSet()
//        animatorSet.interpolator = LinearInterpolator()
//        animatorSet.duration = 2000
//        animatorSet.playTogether(baseAnimator, resultAnimator, baseTranslationYAnimator, baseTranslationZAnimator,
//                resultTranslationYAnimator, resultTranslationZAnimator)
//        animatorSet.start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                PostAddActivity.PLACE_PICKER_REQUEST -> {
                    val place = PlacePicker.getPlace(this, data)
                    val toastMsg = String.format("Place: %s", place.name)
                    Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show()
                }
                PostAddActivity.PICTURE_REQUEST -> {
                    val returnValue = data!!.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                    attachmentPresenter.loadAttachmentFile(File(returnValue[0]))
                    Log.v("", "")
                }
                PostAddActivity.MAP_BUTTON_REQUEST_CODE -> {
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
                    Pix.start(this@PostAddActivity, PostAddActivity.PICTURE_REQUEST, 1)
                } else {
                    Toast.makeText(this@PostAddActivity, resources.getString(R.string.Approve_Permissions_To_Pick_Images), Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }


    override fun showProgress(show: Boolean) {
        if (show) {
            stateLayout.FlipLayout(LayoutStatesEnum.Waitinglayout)
        } else {
            stateLayout.FlipLayout(LayoutStatesEnum.SuccessLayout)
        }
    }

    override fun showLoadErrorMessage(visible: Boolean) {
        if (visible) {
            stateLayout.FlipLayout(LayoutStatesEnum.Noconnectionlayout)
        } else {
            stateLayout.FlipLayout(LayoutStatesEnum.SuccessLayout)
        }
    }

    override fun onAddPostSuccessfully(post: Post) {
        postAddViewHolder.bindPost(post)
        animate()
        Log.v("", "")
    }

    override fun onAddPostFail() {
        Toast.makeText(this@PostAddActivity, R.string.unexpectedErrorHappend, Toast.LENGTH_LONG).show()
        Log.v("", "")
    }

    override fun onUpdatePostSuccessfully(post: Post) {
        postAddViewHolder.bindPost(post)
        animate()
        Log.v("", "")
    }

    override fun onUpdatePostFail() {
        Toast.makeText(this@PostAddActivity, R.string.unexpectedErrorHappend, Toast.LENGTH_LONG).show()
        Log.v("", "")
    }

    /*Post Presenter ended*/


    /*Tags Presenter started*/

    override fun showCitiesProgress(show: Boolean) {
        if (show) {
            cityStateLayout.FlipLayout(LayoutStatesEnum.Waitinglayout)
        } else {
            cityStateLayout.FlipLayout(LayoutStatesEnum.SuccessLayout)
        }
    }

    override fun showCitiesLoadErrorMessage(visible: Boolean) {
        if (visible) {
            cityStateLayout.FlipLayout(LayoutStatesEnum.Noconnectionlayout)
        } else {
            cityStateLayout.FlipLayout(LayoutStatesEnum.SuccessLayout)
        }
    }

    override fun showCitiesEmptyView(visible: Boolean) {
        if (visible) {
            cityStateLayout.FlipLayout(LayoutStatesEnum.Nodatalayout)
        } else {
            cityStateLayout.FlipLayout(LayoutStatesEnum.SuccessLayout)
        }
    }

    override fun onCitiesLoaded(citiesList: MutableList<City>) {
        adCities.adapter = CityRecyclerViewAdapter(context = baseContext, citiesListList = citiesList, onCitySelectListener = this)
    }


    override fun showPostCategoriesProgress(show: Boolean) {
        if (show) {
            categoryStateLayout.FlipLayout(LayoutStatesEnum.Waitinglayout)
        } else {
            categoryStateLayout.FlipLayout(LayoutStatesEnum.SuccessLayout)
        }
    }

    override fun showPostCategoriesLoadErrorMessage(visible: Boolean) {
        if (visible) {
            categoryStateLayout.FlipLayout(LayoutStatesEnum.Noconnectionlayout)
        } else {
            categoryStateLayout.FlipLayout(LayoutStatesEnum.SuccessLayout)
        }
    }

    override fun showPostCategoriesEmptyView(visible: Boolean) {
        if (visible) {
            categoryStateLayout.FlipLayout(LayoutStatesEnum.Nodatalayout)
        } else {
            categoryStateLayout.FlipLayout(LayoutStatesEnum.SuccessLayout)
        }
    }

    override fun onPostCategoriesLoaded(categoriesList: MutableList<PostCategory>) {
        adCategories.adapter = CategoryRecyclerViewAdapter(context = baseContext,
                categoriesList = categoriesList.toMutableList(), onCategorySelectListener = this)
    }
    /*Tags presenter ended*/


    /*Attachment presenter started */
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
        Log.v("", "")
    }

    override fun showAttachmentProcessingPercentage(percentage: String) {
        Log.v("", "")
    }

    override fun showAttachmentLoadErrorMessage(visible: Boolean) {
        if (visible) {
            Toast.makeText(baseContext, R.string.checkInternetConnection, Toast.LENGTH_LONG).show()
        } else {

        }
        Log.v("", "")
    }

    override fun showAttachmentEmptyView(visible: Boolean) {
        Log.v("", "")
    }

    override fun onLoadAttachmentListSuccessfully(filePath: String) {
        (adImages.adapter as AttachmentRecyclerViewAdapter).addItem(Attachment(filePath))
        Toast.makeText(baseContext, R.string.uploadFileSuccessfully, Toast.LENGTH_LONG).show()
        Log.v("", "")
    }
    /*Attachment presenter ended*/


    override fun onSelectCategory(category: Category) {
        adSubCategories.adapter = SubCategoryRecyclerViewAdapter(context = baseContext,
                subCategoriesList = category.subCategoriesList)
    }

    override fun onSelectCity(city: City) {
        adLocations.adapter = LocationEntityRecyclerViewAdapter(baseContext, city.locations)
    }

}