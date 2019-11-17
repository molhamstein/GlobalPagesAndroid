package com.almersal.android.adapters

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.almersal.android.R
import com.almersal.android.utilities.DrawablesProvider
import com.almersal.android.viewHolders.ReferenceViewHolder

import kotlinx.android.synthetic.main.reference_item_layout.*

import android.content.Intent
import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import java.lang.Exception


class ReferencesAdapter(var context: Context, var data: List<String?>, var editFlag: Boolean) :
    RecyclerView.Adapter<ReferenceViewHolder>() {

    var resultData = data.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReferenceViewHolder {
        val view =
            if (editFlag)
                LayoutInflater.from(context).inflate(R.layout.edit_references_item_layout, parent, false)
            else

                LayoutInflater.from(context).inflate(R.layout.reference_item_layout, parent, false)

        return ReferenceViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ReferenceViewHolder, position: Int) {

        if (data[position].isNullOrBlank() && !editFlag)
            holder.containerView?.visibility = View.GONE

        holder.icon.setImageDrawable(
            ContextCompat.getDrawable(
                context,
                DrawablesProvider.referencesDrawables[position]
            )
        )
        holder.url.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                resultData[position] = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })
        holder.url.text = data[position]
        holder.icon.setOnClickListener {
            try {

                val url = data[position]
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                context.startActivity(i)

            } catch (e: Exception) {
                Toast.makeText(context, context.getString(R.string.invalid_link), Toast.LENGTH_SHORT).show()
            }
        }
    }
}