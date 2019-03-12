package com.almersal.android.dialogs.bottomSheetFragments

import android.app.Dialog
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.BottomSheetDialogFragment
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import butterknife.BindView
import butterknife.ButterKnife
import com.almersal.android.R
import com.almersal.android.adapters.SubCategoryRecyclerViewAdapter
import com.almersal.android.data.entities.SubCategory
import com.almersal.android.listeners.OnSubCategorySelectListener
import com.almersal.android.views.SuggestionTagView

class SubCategoryBottomSheet : BottomSheetDialogFragment(), OnSubCategorySelectListener {


    lateinit var subCategoryList: MutableList<SubCategory>
    var onSubCategorySelectListener: OnSubCategorySelectListener? = null

    companion object {
       const val SubCategoryBottomSheet_Tag = "SubCategoryBottomSheet_Tag"
        fun getNewInstance(subCategoryList: MutableList<SubCategory>, onSubCategorySelectListener: OnSubCategorySelectListener? = null)
                : SubCategoryBottomSheet {
            val subCategoryFilterBottomSheet = SubCategoryBottomSheet()
            subCategoryFilterBottomSheet.subCategoryList = subCategoryList
            subCategoryFilterBottomSheet.onSubCategorySelectListener = onSubCategorySelectListener
            val bundle = Bundle()
            subCategoryFilterBottomSheet.arguments = bundle
            return subCategoryFilterBottomSheet
        }
    }

    @BindView(R.id.suggestionTags)
    lateinit var suggestionTags: SuggestionTagView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.TransparentBottomSheetDialogTheme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.subcategory_filter_bottom_sheet, container, false)
        ButterKnife.bind(this, view)
        val displayMetrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog.window.attributes.windowAnimations = R.style.BottomSheetAnimation
        suggestionTags.setAdapter(SubCategoryRecyclerViewAdapter(context = context!!,
                subCategoriesList = subCategoryList, isTransparent = true, onSubCategorySelectListener = this))
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setOnShowListener { dialog ->
            val d = dialog as BottomSheetDialog
            val bottomSheet = d.findViewById<FrameLayout>(android.support.design.R.id.design_bottom_sheet)
            BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
        }
        dialog.window.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }

    override fun onSelectSubCategory(subCategory: SubCategory) {
        onSubCategorySelectListener?.onSelectSubCategory(subCategory)
        dismiss()
    }

}

