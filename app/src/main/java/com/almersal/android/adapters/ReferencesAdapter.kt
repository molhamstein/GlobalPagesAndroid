package com.almersal.android.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.almersal.android.R
import com.almersal.android.utilities.BindingUtils
import com.almersal.android.viewHolders.ReferenceViewHolder
import com.almersal.android.viewHolders.SkillViewHolder
import kotlinx.android.synthetic.main.activity_resume.*
import kotlinx.android.synthetic.main.reference_item_layout.*

class ReferencesAdapter(var context: Context, var data: List<String>, var editFalg: Boolean) :
    RecyclerView.Adapter<ReferenceViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReferenceViewHolder {
        val view =
            if (editFalg)
                LayoutInflater.from(context).inflate(R.layout.edit_references_item_layout, parent, false)
            else

                LayoutInflater.from(context).inflate(R.layout.reference_item_layout, parent, false)

        return ReferenceViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ReferenceViewHolder, position: Int) {

//        BindingUtils.loadImage(holder.icon, data[position])
        holder.url.text = data[position]
    }
}