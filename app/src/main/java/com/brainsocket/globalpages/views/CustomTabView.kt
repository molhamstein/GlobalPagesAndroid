package com.brainsocket.globalpages.views

import android.content.Context
import android.support.annotation.StringRes
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.brainsocket.globalpages.R

/**
 * Created by Adhamkh on 2018-04-01.
 */
class CustomTabView : LinearLayout {
    constructor(context: Context?) : super(context) {
        init(context)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    fun init(context: Context?) {
        val inflater = LayoutInflater.from(context!!)
        val params = android.widget.LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        this.addView(inflater.inflate(R.layout.gender_customtab_layout, null) as View, params)
    }

    fun setGender(titleRes: Int, iconRes: Int): CustomTabView {
//        this.findViewById<TextView>(R.id.text1).setText(titleRes)
        this.findViewById<ImageView>(R.id.gender_icon).setImageResource(iconRes)
        return this
    }

    fun setTitle(@StringRes resTitle: Int): CustomTabView {
//        val type = Typeface.createFromAsset(context.assets, "fonts/coconnextarabicregular.otf");
//        (this.getChildAt(0)as TextView).setTypeface(type, Typeface.NORMAL)
////        this.findViewById<TextView>(R.id.tabTitle).setTypeface(type, Typeface.NORMAL)
//        (this.getChildAt(0)as TextView).setText(resTitle)
        return this
    }


}