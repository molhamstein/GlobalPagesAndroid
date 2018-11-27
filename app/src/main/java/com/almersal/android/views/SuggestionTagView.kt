package com.almersal.android.views

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.almersal.android.R
import com.google.android.flexbox.*

class SuggestionTagView : RelativeLayout {

    lateinit var suggestionTagsRecyclerView: RecyclerView

    constructor(context: Context) : super(context) {
        init(context)

    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    fun init(context: Context) {
        View.inflate(context, R.layout.suggestion_tags_layout, this)
        suggestionTagsRecyclerView = findViewById(R.id.suggestionTagsRecyclerView)

        val layoutManager = FlexboxLayoutManager(context)
//        layoutManager.alignContent = AlignContent.SPACE_AROUND
        layoutManager.alignItems = AlignItems.BASELINE
        layoutManager.flexWrap = FlexWrap.WRAP
//        layoutManager.justifyContent = JustifyContent.SPACE_EVENLY

        suggestionTagsRecyclerView.layoutManager = layoutManager

    }

    fun setAdapter(adapter: RecyclerView.Adapter<*>) {
        suggestionTagsRecyclerView.adapter = adapter
    }


}