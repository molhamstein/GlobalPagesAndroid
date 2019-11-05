package com.almersal.android.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.almersal.android.R
import com.almersal.android.data.entities.Education
import com.almersal.android.data.entities.Experience
import com.almersal.android.listeners.ResumeDialogListener
import com.almersal.android.normalization.DateNormalizer
import com.almersal.android.viewHolders.EducationViewHolder
import kotlinx.android.synthetic.main.edit_education_item_layout.*
import kotlinx.android.synthetic.main.education_item_layout.*
import kotlinx.android.synthetic.main.education_item_layout.date
import kotlinx.android.synthetic.main.education_item_layout.details
import kotlinx.android.synthetic.main.education_item_layout.title

class EducationAdapter(
    var context: Context,
    var data: MutableList<Education>,
    var editFlag: Boolean,
    var listener: ResumeDialogListener? = null
) :
    RecyclerView.Adapter<EducationViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EducationViewHolder {
        var view = if (editFlag)
            LayoutInflater.from(context).inflate(R.layout.edit_education_item_layout, parent, false)
        else
            LayoutInflater.from(context).inflate(R.layout.education_item_layout, parent, false)
        return EducationViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: EducationViewHolder, position: Int) {
        holder.title.text = data[position].title
        holder.details.text = data[position].educationalEntity
        holder.date.text =
            DateNormalizer.getCustomFormate(
                data[position].from,
                "MMM dd, yyyy"
            ) + " - " + DateNormalizer.getCustomFormate(data[position].to, "MMM dd, yyyy")
        if (editFlag)
            holder.editBtn.setOnClickListener {
                listener?.openEducationEditDialog(position, data[position])
            }
    }

    fun add(education: Education) {
        data.add(education)
        notifyDataSetChanged()
    }

    fun update(pos: Int, education: Education) {
        data[pos] = education
        notifyDataSetChanged()
    }

}