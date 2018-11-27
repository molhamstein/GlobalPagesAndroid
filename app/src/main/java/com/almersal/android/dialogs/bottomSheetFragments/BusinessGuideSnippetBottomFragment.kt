package com.almersal.android.dialogs.bottomSheetFragments

import android.app.Dialog
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import butterknife.BindView
import butterknife.ButterKnife
import com.almersal.android.R
import com.almersal.android.adapters.BusinessGuideRecyclerViewAdapter
import com.almersal.android.data.entities.BusinessGuide

/**
 * Created by Adhamkh on 2018-07-27.
 */
class BusinessGuideSnippetBottomFragment : BottomSheetDialogFragment() {

    lateinit var businessGuide: BusinessGuide


    companion object {

        const val BusinessGuideSnippetBottomFragment_Tag = "BusinessGuideSnippetBottomFragment"

        fun getNewInstance(businessGuide: BusinessGuide): BusinessGuideSnippetBottomFragment {
            val businessGuideSnippetBottomFragment = BusinessGuideSnippetBottomFragment()
            businessGuideSnippetBottomFragment.businessGuide = businessGuide
            val bundle = Bundle()
            businessGuideSnippetBottomFragment.arguments = bundle
            return businessGuideSnippetBottomFragment
        }

    }

    @BindView(R.id.recyclerView)
    lateinit var recyclerView: RecyclerView

    private fun initRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(activity)
        val list: MutableList<BusinessGuide> = mutableListOf()
        list.add(businessGuide)
        recyclerView.adapter = BusinessGuideRecyclerViewAdapter(context!!, list)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.TransparentBottomSheetDialogTheme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.business_guide_snippet_layout, container, false)
        ButterKnife.bind(this, view)
        val displayMetrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog.window.attributes.windowAnimations = R.style.BottomSheetAnimation
        initRecyclerView()

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


}