package com.almersal.android.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.ViewGroup
import com.almersal.android.R
import com.almersal.android.activities.JobDetailsActivity
import com.almersal.android.data.entities.Job
import com.almersal.android.normalization.DateNormalizer
import com.almersal.android.utilities.BindingUtils
import com.almersal.android.utilities.DateHelper
import com.almersal.android.utilities.IntentHelper
import com.almersal.android.viewHolders.JobSearchViewHolder
import kotlinx.android.synthetic.main.job_search_item_layout.*


class JobsSearchAdapter(var context: Context, var data: MutableList<Job>, var editFlag: Boolean) :
    RecyclerView.Adapter<JobSearchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobSearchViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.job_search_item_layout, parent, false)
        return JobSearchViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: JobSearchViewHolder, position: Int) {
        BindingUtils.loadBusinessImage(holder.image, data[position].business.logo)
        holder.title.text = data[position].name
        holder.companyName.text = data[position].business.name
        holder.location.text =
            data[position].business.city?.getTitle() + ", " + data[position].business.location?.getTitle()

        holder.date.text = DateNormalizer.getCustomFormate(data[position].creationDate, "MMM dd, yyyy")
        holder.containerView?.setOnClickListener {
            IntentHelper.startJobeDetailsActivity(context, data[position], editFlag)
        }
    }


    fun addData(newData: List<Job>) {
        data.addAll(newData)
        notifyDataSetChanged()
    }

    fun clear() {
        data.clear()
        notifyDataSetChanged()
    }


}