package com.almersal.android.adapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import com.almersal.android.R
import com.almersal.android.activities.ProductAddActivity
import com.almersal.android.activities.UserResumeActivity
import com.almersal.android.data.entities.Applicant
import com.almersal.android.data.entities.ApplicantStatus
import com.almersal.android.di.ui.ApplicantsContract
import com.almersal.android.normalization.DateNormalizer
import com.almersal.android.utilities.BindingUtils
import com.almersal.android.utilities.DateHelper
import com.almersal.android.utilities.EnumsProvider
import com.almersal.android.utilities.IntentHelper
import com.almersal.android.viewHolders.ApplicantViewHolder
import com.google.gson.Gson
import kotlinx.android.synthetic.main.applicants_item_layout.*

class ApplicantsAdapter(val context: Context, var data: MutableList<Applicant>) :
    RecyclerView.Adapter<ApplicantViewHolder>() {
    private var firstTimeSelectedFlags = mutableListOf<Boolean>()

    init {
        repeat(data.size) { firstTimeSelectedFlags.add(true) }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApplicantViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.applicants_item_layout, parent, false)
        return ApplicantViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ApplicantViewHolder, position: Int) {
        with(data[position].user) {
            BindingUtils.loadImage(holder.image, imageProfile)
            holder.name.text = username
            holder.positionTitle.text = CV?.primaryIdentifier
            holder.location.text = CV?.city?.getTitle()
            holder.date.text = DateNormalizer.getCustomFormate(data[position].createdAt, "MMM dd, yyyy")
        }
        holder.containerView?.setOnClickListener {
            val activityName = UserResumeActivity::class.java.canonicalName
            val jSon = Gson().toJson(data[position].user)
            val intent = Intent()
            intent.putExtra(UserResumeActivity.user_profile_key, jSon)
            intent.setClassName(context, activityName)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)

        }
        holder.statusSpinner.adapter = ArrayAdapter(
            context,
            android.R.layout.simple_spinner_dropdown_item,
            EnumsProvider.applicantStatuses.map { it.title })

        holder.statusSpinner.setSelection(EnumsProvider.applicantStatuses.indexOfFirst { it.name == data[position].status })
        holder.statusSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, spinnerPosition: Int, id: Long) {
                if (!firstTimeSelectedFlags[position]) {
                    (context as ApplicantsContract.View).updateStatus(
                        ApplicantStatus(EnumsProvider.applicantStatuses[spinnerPosition].name),
                        data[position].id
                    )

                } else
                    firstTimeSelectedFlags[position] = false
            }
        }

    }
}
