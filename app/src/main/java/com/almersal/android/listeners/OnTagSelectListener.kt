package com.almersal.android.listeners

import com.almersal.android.data.entities.TagEntity

interface OnTagSelectListener {

    fun onSelectTag(tagEntity: TagEntity)
    fun onTagClick(tagEntity: TagEntity){}
}