package com.almersal.android.dialogs.bottomSheetFragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.BottomSheetDialogFragment
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
import com.almersal.android.data.entities.SubCategory
import com.almersal.android.data.entities.User
import com.almersal.android.data.mapping.UserProfileMapper
import com.almersal.android.di.component.DaggerSubCategorySubscriptionComponent
import com.almersal.android.di.module.ProfileModule
import com.almersal.android.di.ui.ProfileContract
import com.almersal.android.di.ui.ProfilePresenter
import com.almersal.android.dialogs.ProgressDialog
import com.almersal.android.eventsBus.EventActions
import com.almersal.android.eventsBus.MessageEvent
import com.almersal.android.eventsBus.RxBus
import com.almersal.android.listeners.OnSubCategorySelectListener
import com.almersal.android.repositories.UserRepository
import com.almersal.android.views.SuggestionTagView
import javax.inject.Inject


class SubCategorySubscriptionBottomSheet : BottomSheetDialogFragment(), OnSubCategorySelectListener, ProfileContract.View {

    lateinit var subCategoryList: MutableList<SubCategory>

    @BindView(R.id.suggestionTags)
    lateinit var suggestionTags: SuggestionTagView

    lateinit var adapter: SubCategoryRecyclerViewAdapter

    @Inject
    lateinit var presenter: ProfilePresenter

    private var progressDialog: ProgressDialog = ProgressDialog()

    companion object {
        const val SubCategorySubscriptionBottomSheet_Tag = "SubCategorySubscriptionBottomSheet_Tag"
        fun getNewInstance(subCategoryList: MutableList<SubCategory>)
                : SubCategorySubscriptionBottomSheet {
            val subCategorySubscriptionBottomSheet = SubCategorySubscriptionBottomSheet()
            subCategorySubscriptionBottomSheet.subCategoryList = subCategoryList
            return subCategorySubscriptionBottomSheet
        }
    }

    private fun initDI() {
        val component = DaggerSubCategorySubscriptionComponent.builder()
                .profileModule(ProfileModule(activity!!))
                .build()
        component.inject(this)
        presenter.attachView(this)
        presenter.subscribe()

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
        initDI()
        adapter = SubCategoryRecyclerViewAdapter(context!!, subCategoryList, this)
        suggestionTags.setAdapter(adapter)

        bindUserInfo(UserRepository(context!!).getUser())
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

//    fun getAlertStyle(): Int {
//    }

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

    /*Profile Presenter started*/
    override fun showUpdateProfileProgress(show: Boolean) {
        if (show)
            progressDialog.show(childFragmentManager, ProgressDialog.ProgressDialog_Tag)
        else
            progressDialog.dismiss()
    }

    override fun showUpdateProfileLoadErrorMessage(visible: Boolean) {
        if (visible) {
            bindUserInfo(UserRepository(context!!).getUser())
            Toast.makeText(context, resources.getString(R.string.checkInternetConnection), Toast.LENGTH_LONG).show()
        }
    }

    override fun onUpdateProfileSuccessfully(user: User) {
        UserRepository(context!!).addUser(user)
        Toast.makeText(context, resources.getString(R.string.SubscribedCategoriesUpdateSuccessfully), Toast.LENGTH_LONG).show()
    }
    /*Profile Presenter ended*/

}

