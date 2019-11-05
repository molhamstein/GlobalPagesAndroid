package com.almersal.android.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.almersal.android.R
import com.almersal.android.data.entities.Experience
import com.almersal.android.listeners.ResumeDialogListener
import com.almersal.android.normalization.DateNormalizer
import com.almersal.android.viewHolders.ExperienceViewHolder
import kotlinx.android.synthetic.main.edit_education_item_layout.*
import kotlinx.android.synthetic.main.experience_item_layout.*
import kotlinx.android.synthetic.main.experience_item_layout.date
import kotlinx.android.synthetic.main.experience_item_layout.details
import kotlinx.android.synthetic.main.experience_item_layout.title

class ExperiencesAdapter(
    var context: Context,
    var data: MutableList<Experience>,
    var editFlag: Boolean,
    var listener: ResumeDialogListener? = null
) :
    RecyclerView.Adapter<ExperienceViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExperienceViewHolder {
        val view =
            if (!editFlag)
                LayoutInflater.from(context).inflate(R.layout.experience_item_layout, parent, false)
            else
                LayoutInflater.from(context).inflate(R.layout.edit_experience_item_layout, parent, false)

        return ExperienceViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ExperienceViewHolder, position: Int) {
        holder.title.text = data[position].title
        holder.details.text = data[position].companyName
        holder.date.text =
            DateNormalizer.getCustomFormate(data[position].from, "MMM dd, yyyy") +
                    " - " + if (data[position].isPresent) context.getString(R.string.present) else DateNormalizer.getCustomFormate(
                data[position].to,
                "MMM dd, yyyy"
            )

        if (position == data.size - 1 && !editFlag)
            holder.line.visibility = View.GONE
        if (editFlag)
            holder.editBtn.setOnClickListener {
                listener?.openExperienceEditDialog(position, data[position])
            }

    }

    fun add(experience: Experience) {
        data.add(experience)
        notifyDataSetChanged()
    }

    fun update(pos: Int, experience: Experience) {
        data[pos] = experience
        notifyDataSetChanged()
    }
}