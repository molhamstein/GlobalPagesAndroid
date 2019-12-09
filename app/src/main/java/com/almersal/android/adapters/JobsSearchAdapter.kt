package com.almersal.android.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.almersal.android.R
import com.almersal.android.data.entities.Job
import com.almersal.android.normalization.DateNormalizer
import com.almersal.android.repositories.UserRepository
import com.almersal.android.utilities.BindingUtils
import com.almersal.android.utilities.IntentHelper
import com.almersal.android.viewHolders.JobSearchViewHolder
import kotlinx.android.synthetic.main.job_search_item_layout.*
import java.text.SimpleDateFormat
import java.util.*


class JobsSearchAdapter(var context: Context, var data: MutableList<Job>, var isHorizantal: Boolean) :

    RecyclerView.Adapter<JobSearchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobSearchViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.job_search_item_layout, parent, false)
        if (isHorizantal && data.size > 1) {
            val lp = view.layoutParams as ViewGroup.LayoutParams
            lp.width = ViewGroup.LayoutParams.WRAP_CONTENT
            view.layoutParams = lp
        }
        return JobSearchViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: JobSearchViewHolder, position: Int) {
        BindingUtils.loadBusinessImage(holder.image, data[position].business?.logo ?: "")
        holder.title.text = data[position].name
        holder.companyName.text = data[position].business?.name
        holder.location.text =
            data[position].business?.city?.getTitle() + ", " + data[position].business?.location?.getTitle()

        holder.date.text = DateNormalizer.getCustomFormate(data[position].creationDate, "MMM dd, yyyy")
        holder.containerView?.setOnClickListener {
            val userId = UserRepository(context).getUser()?.id
            val flag = userId == data[position].ownerId
            IntentHelper.startJobDetailsActivity(context, data[position], flag)
        }

        val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH)
        val currentDate = Date(Calendar.getInstance().timeInMillis)
        val createdDate = sdf.parse(holder.date.text.toString())

        var diffrence = currentDate.time - createdDate.time
        val secondsInMilli: Long = 1000
        val minutesInMilli = secondsInMilli * 60
        val hoursInMilli = minutesInMilli * 60
        val daysInMilli = hoursInMilli * 24

        var diffDays = diffrence / daysInMilli
        if (diffDays <= 7)
            holder.status.visibility = View.VISIBLE
        else
            holder.status.visibility = View.GONE


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