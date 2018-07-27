package com.brainsocket.globalpages.views

import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.ToggleButton
import com.brainsocket.globalpages.BuildConfig
import com.brainsocket.globalpages.R
import com.brainsocket.globalpages.adapters.TagsRecyclerViewAdapter
import com.brainsocket.globalpages.data.entities.TagEntity
import com.brainsocket.globalpages.listeners.OnTagSelectListener
import com.google.android.flexbox.FlexboxLayout
import com.google.gson.Gson

/**
 * Created by Adhamkh on 2018-07-05.
 */
class SearchTagView : RelativeLayout, OnTagSelectListener /*, TextWatcher , CompoundButton.OnCheckedChangeListener,
        View.OnFocusChangeListener, View.OnTouchListener */ {

    lateinit var selectedTags: RecyclerView
    //    lateinit var flexBox: FlexboxLayout
    lateinit var searchEditText: EditText
    lateinit var tagsAdapter: TagsRecyclerViewAdapter

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
        View.inflate(context, R.layout.tags_search_layout, this)
        searchEditText = findViewById(R.id.searchEditText)
        selectedTags = findViewById(R.id.selectedTags)
//        flexBox = findViewById(R.id.flexBox)

        selectedTags.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        tagsAdapter = TagsRecyclerViewAdapter(context, mutableListOf())
        selectedTags.adapter = tagsAdapter

        tagsAdapter.onTagSelectListener = this
//        searchEditText.addTextChangedListener(this)
//        onFocusChangeListener = this
//        setOnTouchListener(this)
    }


    public fun setSuggestionList(tagList: MutableList<TagEntity>) {
        tagsAdapter.tagsListList = tagList
        tagsAdapter.notifyDataSetChanged()
    }

    override fun onSelectTag(tagEntity: TagEntity) {
    }

    /*
    fun addSuggestionList(tagEntityList: MutableList<TagEntity>) {
        tagEntityList.forEach {
            addTagEntity(it)
        }
    }
    */

    /*
      fun addTagEntity(it: TagEntity) {
          var context = this.context
          var tagId: String = flexBox.childCount.toString()

          var view: View = LayoutInflater.from(context).inflate(R.layout.tag_item_layout, null, false)
          view.tag = tagId

          var tag = view.findViewById<ToggleButton>(R.id.tag_toggle)
          tag.tag = Gson().toJson(it)

          tag.textOn = it.getTitle()
          tag.textOff = it.getTitle()
          tag.text = it.getTitle()
          tag.setCompoundDrawables(null, null, null, null)
          if (Build.VERSION.SDK_INT >= 17) {
              tag.setCompoundDrawablesRelative(null, null, null, null)
          }

          if (view.parent != null)
              (view.parent as ViewGroup).removeView(view)

          tag.setOnCheckedChangeListener(this)
          var param: FlexboxLayout.LayoutParams = FlexboxLayout.LayoutParams(
                  FlexboxLayout.LayoutParams.WRAP_CONTENT,
                  FlexboxLayout.LayoutParams.WRAP_CONTENT)
          param.order = 1

          flexBox.addView(view, param)
      }
  */


    fun addTag2Search(it: TagEntity, parentTag: String) {
        it.tagId = parentTag
        tagsAdapter.addTag(it)
    }

    /*
    fun isExpand(): Boolean {
        return flexBox.visibility == View.VISIBLE
    }

    fun setExpand(visibale: Boolean) {
        if (visibale)
            flexBox.visibility = View.VISIBLE
        else
            flexBox.visibility = View.GONE
    }
*/

    /*
    /*Listeners Started*/
    override fun afterTextChanged(s: Editable?) {

    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (s == null /*|| s.isEmpty()*/) {
            flexBox.visibility = View.GONE
        } else {
            flexBox.visibility = View.VISIBLE
            var cnt = flexBox.childCount
            for (i: Int in 0..(cnt - 1)) {
                var view: ViewGroup = flexBox.getChildAt(i) as ViewGroup
                if (view.getChildAt(0) != null) {
                    var child = view.getChildAt(0)
                    var childTag = child.tag
                    if (childTag == null)
                        continue
                    if (childTag.toString().contains(s, false)) {
                        view.visibility = View.VISIBLE
                    } else {
                        view.visibility = View.GONE
                    }
                }
            }
        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        if (buttonView?.parent != null) {
            var parent = (buttonView.parent as View)
            var pTag: String = parent.tag.toString()
            parent.visibility = View.GONE
            var pojo = Gson().fromJson<TagEntity>(buttonView.tag.toString(), TagEntity::class.java)
            addTag2Search(pojo, pTag)
        }
    }

  */


/*
    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        if (!hasFocus)
            flexBox.visibility = View.GONE
    }
*/
/*

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (event != null && v != null) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (searchEditText.isFocused()) {
                    val outRect = Rect()
                    searchEditText.getGlobalVisibleRect(outRect)
                    if (!outRect.contains(event.getRawX().toInt(), event.getRawY().toInt())) {
                        searchEditText.clearFocus()
                        val imm = v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
                    }
                }
            }
        }
        return false
    }
    /*Listeners Ended*/
    */

}