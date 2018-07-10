package com.brainsocket.globalpages.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ToggleButton
import butterknife.BindView
import butterknife.ButterKnife
import com.brainsocket.globalpages.R
import com.brainsocket.globalpages.data.entities.TagEntity
import com.google.android.flexbox.FlexboxLayout
import android.view.ViewGroup
import android.widget.CompoundButton
import com.brainsocket.globalpages.adapters.TagsRecyclerViewAdapter
import com.brainsocket.globalpages.listeners.OnTagSelectListener
import com.brainsocket.globalpages.views.TagSearchView


/**
 * Created by Adhamkh on 2018-07-02.
 */
class testActivity : AppCompatActivity()/*, TextWatcher, CompoundButton.OnCheckedChangeListener,
        OnTagSelectListener */ {

    /*@BindView(R.id.selectedTags)
    lateinit var selectedTags: RecyclerView

    @BindView(R.id.flexBox)
    lateinit var flexBox: FlexboxLayout

    @BindView(R.id.searchEditText)
    lateinit var searchEditText: EditText

    lateinit var tagsAdapter: TagsRecyclerViewAdapter*/

    @BindView(R.id.tagSearch)
    lateinit var tagSearch: TagSearchView

    var list: MutableList<TagEntity> = mutableListOf()

    init {
        list.apply {
            add(TagEntity("دمشق شسيسيشسيش", "Damascus"))
            add(TagEntity("حلب", "Aleppo"))
            add(TagEntity("الكل", "All"))
            add(TagEntity("سيارات", "Cars"))
            add(TagEntity("دمشق شسيسيشسيش", "Damascus"))
            add(TagEntity("حلب", "Aleppo"))
            add(TagEntity("الكل", "All"))
            add(TagEntity("سيارات", "Cars"))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test_layout)
        ButterKnife.bind(this)
        tagSearch.addSuggestionList(list)
//        searchEditText.addTextChangedListener(this)
//
//        selectedTags.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
//        tagsAdapter = TagsRecyclerViewAdapter(this, mutableListOf())
//        selectedTags.adapter = tagsAdapter
//        tagsAdapter.onTagSelectListener = this
//        list.forEach {
//
//        }

    }
/*
    /*Watcher started*/
    override fun afterTextChanged(s: Editable?) {

    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (s == null || s.isEmpty()) {
            flexBox.visibility = View.GONE
        } else {
            flexBox.visibility = View.VISIBLE
        }
    }
    /*Watcher ended*/

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        if (isChecked && buttonView != null) {
            if (buttonView.parent != null)
                (buttonView.parent as View).visibility = View.GONE
            tagsAdapter.addTag(TagEntity(buttonView.text.toString(), buttonView.text.toString()))
        }
    }

    override fun onSelectTag(tagEntity: TagEntity) {
        var view = flexBox.findViewWithTag<View>(tagEntity.getTitle())
        if (view != null)
            view.visibility = View.VISIBLE

        Log.v("", "")
    }*/

}
