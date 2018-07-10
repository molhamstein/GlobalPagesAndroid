package com.brainsocket.globalpages.listeners

import com.brainsocket.globalpages.data.entities.TagEntity

/**
 * Created by Adhamkh on 2018-07-04.
 */
interface OnTagSelectListener {
    fun onSelectTag(tagEntity: TagEntity)
}