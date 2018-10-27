package com.brainsocket.globalpages.listeners

import com.brainsocket.globalpages.data.entities.TagEntity

interface OnTagSelectListener {

    fun onSelectTag(tagEntity: TagEntity)
    fun onTagClick(tagEntity: TagEntity){}
}