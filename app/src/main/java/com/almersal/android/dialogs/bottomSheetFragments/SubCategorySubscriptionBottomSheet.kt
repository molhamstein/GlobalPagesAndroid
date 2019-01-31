package com.almersal.android.dialogs.bottomSheetFragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.os.RecoverySystem
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import com.almersal.android.BuildConfig
import com.almersal.android.R
import com.almersal.android.adapters.CategoryProfileRecyclerViewAdapter
import com.almersal.android.adapters.SubCategoryRecyclerViewAdapter
import com.almersal.android.data.entities.Category
import com.almersal.android.data.entities.PostCategory
import com.almersal.android.data.entities.SubCategory
import com.almersal.android.data.entities.User
import com.almersal.android.data.mapping.UserProfileMapper
import com.almersal.android.di.component.DaggerSubCategorySubscriptionComponent
import com.almersal.android.di.module.ProfileModule
import com.almersal.android.di.module.TagsCollectionModule
import com.almersal.android.di.ui.ProfileContract
import com.almersal.android.di.ui.ProfilePresenter
import com.almersal.android.di.ui.TagsCollectionContact
import com.almersal.android.di.ui.TagsCollectionPresenter
import com.almersal.android.dialogs.ProgressDialog
import com.almersal.android.eventsBus.EventActions
import com.almersal.android.eventsBus.MessageEvent
import com.almersal.android.eventsBus.RxBus
import com.almersal.android.listeners.OnCategorySelectListener
import com.almersal.android.listeners.OnSubCategorySelectListener
import com.almersal.android.repositories.SettingRepositories
import com.almersal.android.repositories.UserRepository
import com.almersal.android.views.SuggestionTagView
import com.brainsocket.mainlibrary.Enums.LayoutStatesEnum
import com.brainsocket.mainlibrary.Views.Stateslayoutview
import javax.inject.Inject


class SubCategorySubscriptionBottomSheet : BottomSheetDialogFragment(), OnSubCategorySelectListener,
        OnCategorySelectListener, ProfileContract.View, TagsCollectionContact.View {

    var subCategory: Category? = null

    lateinit var adapter: SubCategoryRecyclerViewAdapter

    @BindView(R.id.myCategories)
    lateinit var myCategories: RecyclerView

    @BindView(R.id.myCategoriesStateLayout)
    lateinit var myCategoriesStateLayout: Stateslayoutview

    @BindView(R.id.suggestionTags)
    lateinit var suggestionTags: SuggestionTagView

    @Inject
    lateinit var presenter: ProfilePresenter

    @Inject
    lateinit var categoryPresenter: TagsCollectionPresenter

    private var progressDialog: ProgressDialog = ProgressDialog.newInstance()

    companion object {
        const val SubCategorySubscriptionBottomSheet_Tag = "SubCategorySubscriptionBottomSheet_Tag"
        fun getNewInstance(subCategory: Category? = null)
                : SubCategorySubscriptionBottomSheet {
            val subCategorySubscriptionBottomSheet = SubCategorySubscriptionBottomSheet()
            subCategorySubscriptionBottomSheet.subCategory = subCategory
            return subCategorySubscriptionBottomSheet
        }
    }

    private fun initDI() {
        val component = DaggerSubCategorySubscriptionComponent.builder()
                .profileModule(ProfileModule(activity!!))
                .tagsCollectionModule(TagsCollectionModule(activity!!))
                .build()
        component.inject(this)
        presenter.attachView(this)
        presenter.subscribe()

        categoryPresenter.attachView(this)
        categoryPresenter.subscribe()

        initRequest()
    }

    private fun initRecyclerViews() {
        myCategories.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
    }

    private fun initRequest() {
//        val user = UserRepository(context!!).getUser()!!
//        presenter.loadUserCategories(user)
        categoryPresenter.loadPostCategories(true)
    }

    private fun initRecyclerView(categoriesList: MutableList<PostCategory>): Pair<MutableList<PostCategory>, Int> {
        var index = -1
        if (subCategory != null) {
            val sz = categoriesList.size - 1
            for (i in 0..(sz)) {
                val it = categoriesList[i]
                if (it.id == subCategory?.parentCategoryId) {
                    it.isSelected = true
                    index = i
                }
            }
        }
        return Pair(categoriesList, index)
    }

    private fun initSubCategoryRecyclerView(subCategoriesList: MutableList<SubCategory>):
            Pair<MutableList<SubCategory>, Int> {
        var index = -1
        if (subCategory != null) {
            val sz = subCategoriesList.size - 1
            for (i in 0..(sz)) {
                val it = subCategoriesList[i]
                if (it.id == subCategory?.id) {
                    it.isSelected = true
                    index = i
                }
            }
        }
        return Pair(subCategoriesList, index)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.TransparentBottomSheetDialogTheme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.subcategory_subscription_bottom_sheet, container, false)
        ButterKnife.bind(this, view)
        val displayMetrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog.window.attributes.windowAnimations = R.style.BottomSheetAnimation
        initRecyclerViews()
        initDI()
        SettingRepositories(activity!!).putFirstSubscription(false)

//        adapter = SubCategoryRecyclerViewAdapter(context!!, subCategoryList, this)
//        suggestionTags.setAdapter(adapter)
//
//        bindUserInfo(UserRepository(context!!).getUser())
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogBase = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialogBase.setOnShowListener { dialog ->
            val d = dialog as BottomSheetDialog
            val bottomSheet = d.findViewById<FrameLayout>(android.support.design.R.id.design_bottom_sheet)
            BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
        }
        dialogBase.window.setBackgroundDrawableResource(android.R.color.transparent)
        return dialogBase
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        RxBus.publish(MessageEvent(EventActions.SubCategorySubscriptionBottomSheet_Tag, null))
    }

    private fun bindUserInfo(user: User?) {
        val categories = user?.postCategoriesIds
        categories?.forEach {
            adapter.setCheck(it)
        }
    }

    /*Post categories presenter started */
    override fun showPostCategoriesProgress(show: Boolean) {
        if (show) {
            myCategoriesStateLayout.FlipLayout(LayoutStatesEnum.Waitinglayout)
        } else {
            myCategoriesStateLayout.FlipLayout(LayoutStatesEnum.SuccessLayout)
        }
    }

    override fun showPostCategoriesLoadErrorMessage(visible: Boolean) {
        if (visible) {
            myCategoriesStateLayout.FlipLayout(LayoutStatesEnum.Noconnectionlayout)
        } else {
            myCategoriesStateLayout.FlipLayout(LayoutStatesEnum.SuccessLayout)
        }
    }

    override fun showPostCategoriesEmptyView(visible: Boolean) {
        if (visible) {
            myCategoriesStateLayout.FlipLayout(LayoutStatesEnum.Nodatalayout)
        } else {
            myCategoriesStateLayout.FlipLayout(LayoutStatesEnum.SuccessLayout)
        }
    }

    override fun onPostCategoriesLoaded(categoriesList: MutableList<PostCategory>) {
        val res = initRecyclerView(categoriesList.filter { it.parentCategoryId.isNullOrBlank() }.toMutableList())
        myCategories.adapter = CategoryProfileRecyclerViewAdapter(context = context!!,
                categoriesList = res.first.toMutableList(), isClickable = true,
                onCategorySelectListener = this@SubCategorySubscriptionBottomSheet)
        if (res.second >= 0) {
            onSelectCategory(res.first[res.second])
            myCategories.smoothScrollToPosition(res.second)
        }

    }
    /*Post categories presenter ended */


    /*Category Events actions started*/
    override fun onSelectCategory(category: Category) {
        val res = initSubCategoryRecyclerView(category.subCategoriesList)
        adapter = SubCategoryRecyclerViewAdapter(context = context!!, subCategoriesList = res.first,
                onSubCategorySelectListener = this, clearAll = false)
        suggestionTags.setAdapter(adapter)
        if (res.second > 0) {
            suggestionTags.suggestionTagsRecyclerView.smoothScrollToPosition(res.second)
        }
        bindUserInfo(UserRepository(context!!).getUser())
    }

    override fun onDeselectCategory(category: Category) {
        adapter = SubCategoryRecyclerViewAdapter(context = context!!, subCategoriesList = mutableListOf(),
                onSubCategorySelectListener = this)
        suggestionTags.setAdapter(adapter)
    }
    /*Category Events actions ended*/


    /*Subcategory Events actions started*/
    override fun onSelectSubCategory(subCategory: SubCategory) {

        val builder = AlertDialog.Builder(context!!, R.style.AppAlertWhiteTheme)
        val dialogClickListener = DialogInterface.OnClickListener { _, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    val user = UserRepository(context = context!!).getUser()!!
                    if (user.postCategoriesIds == null)
                        user.postCategoriesIds = mutableListOf()
                    user.postCategoriesIds?.add(subCategory.id)
                    presenter.updateProfile(UserProfileMapper.userProfileTransform(user), user.token)
                }
                DialogInterface.BUTTON_NEGATIVE -> {
                    (adapter).setSubCategoryItemStatus(subCategory, false)
                }
            }
        }
        builder.setMessage(resources.getString(R.string.doYouWantFollowNotificationsUnderThisCategory))
                .setPositiveButton(resources.getString(R.string.Yes), dialogClickListener)
                .setNegativeButton(resources.getString(R.string.No), dialogClickListener).show()
    }

    override fun onUnSelectSubCategory(subCategory: SubCategory) {

        val builder = AlertDialog.Builder(context!!, R.style.AppAlertWhiteTheme)
        val dialogClickListener = DialogInterface.OnClickListener { _, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    val user = UserRepository(context = context!!).getUser()!!
                    if (user.postCategoriesIds == null)
                        user.postCategoriesIds = mutableListOf()
                    user.postCategoriesIds?.remove(subCategory.id)
                    presenter.updateProfile(UserProfileMapper.userProfileTransform(user), user.token)
                }
                DialogInterface.BUTTON_NEGATIVE -> {
                    (adapter).setSubCategoryItemStatus(subCategory, true)
                }
            }
        }
        builder.setMessage(resources.getString(R.string.doYouWantUnFollowNotificationsUnderThisCategory))
                .setPositiveButton(resources.getString(R.string.Yes), dialogClickListener)
                .setNegativeButton(resources.getString(R.string.No), dialogClickListener).show()

    }
    /*Subcategory Events actions ended*/


    /*Profile Presenter started*/
    override fun showUpdateProfileProgress(show: Boolean) {
        if (show)
            progressDialog.showDialog(childFragmentManager)
        else
            ProgressDialog.closeDialog(childFragmentManager)
    }

    override fun showUpdateProfileLoadErrorMessage(visible: Boolean) {
        if (visible) {
            bindUserInfo(UserRepository(context!!).getUser())
            ProgressDialog.closeDialog(childFragmentManager)
            Toast.makeText(context, resources.getString(R.string.checkInternetConnection), Toast.LENGTH_LONG).show()
        }
    }

    override fun onUpdateProfileSuccessfully(user: User) {
        UserRepository(context!!).addUser(user)
        Toast.makeText(context, resources.getString(R.string.SubscribedCategoriesUpdateSuccessfully), Toast.LENGTH_LONG).show()
    }
    /*Profile Presenter ended*/


//    /*My Categories presenter started */
//    override fun showUserCategoriesProgress(show: Boolean) {
//        if (show) {
//            myCategoriesStateLayout.FlipLayout(LayoutStatesEnum.Waitinglayout)
//        } else {
//            myCategoriesStateLayout.FlipLayout(LayoutStatesEnum.SuccessLayout)
//        }
//    }
//
//    override fun showUserCategoriesLoadErrorMessage(visible: Boolean) {
//        if (visible) {
//            myCategoriesStateLayout.FlipLayout(LayoutStatesEnum.Noconnectionlayout)
//        } else {
//            myCategoriesStateLayout.FlipLayout(LayoutStatesEnum.SuccessLayout)
//        }
//    }
//
//    override fun showUserCategoriesEmptyView(visible: Boolean) {
//        if (visible) {
//            myCategoriesStateLayout.FlipLayout(LayoutStatesEnum.Nodatalayout)
//        } else {
//            myCategoriesStateLayout.FlipLayout(LayoutStatesEnum.SuccessLayout)
//        }
//    }
//
//    override fun onUserCategoriesListSuccessfully(categories: MutableList<Category>) {
//        myCategories.adapter = CategoryProfileRecyclerViewAdapter(context = context!!,
//                categoriesList = categories, isClickable = true, onCategorySelectListener = this@SubCategorySubscriptionBottomSheet)
//    }
//    /*My Categories presenter ended */

}

