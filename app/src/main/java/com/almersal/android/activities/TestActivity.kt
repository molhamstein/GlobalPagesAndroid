package com.almersal.android.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import butterknife.BindView
import butterknife.ButterKnife
import com.almersal.android.R
import com.almersal.android.data.entities.TagEntity
import com.almersal.android.views.SearchTagView


class TestActivity : AppCompatActivity()/*, TextWatcher, CompoundButton.OnCheckedChangeListener,
        OnTagSelectListener */ {

    /*@BindView(R.id.selectedTags)
    lateinit var selectedTags: RecyclerView

    @BindView(R.id.flexBox)
    lateinit var flexBox: FlexboxLayout

    @BindView(R.id.searchEditText)
    lateinit var searchEditText: EditText

    lateinit var tagsAdapter: TagsRecyclerViewAdapter*/

    @BindView(R.id.tagSearch)
    lateinit var searchTag: SearchTagView

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
//        searchTag.addSuggestionList(list)
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
