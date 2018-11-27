package com.almersal.android.views

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.widget.RelativeLayout
import com.almersal.android.R

/**
 * Created by Adhamkh on 2018-07-27.
 */
class SelectedTagsView : RelativeLayout {

    lateinit var selectedTags: RecyclerView

    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(context)
    }

    private fun initView(context: Context) {
        View.inflate(context, R.layout.tags_view_layout, this)
        selectedTags = findViewById(R.id.selectedTags)
        selectedTags.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

    }

    fun setAdapter(adapter: RecyclerView.Adapter<*>) {
        selectedTags.adapter = adapter
    }

    fun getAdapter(): RecyclerView.Adapter<*> {
        return selectedTags.adapter
    }

}