package com.almersal.android.adapters

import android.content.Context
import android.widget.Filterable
import android.widget.ArrayAdapter
import android.widget.Filter
import com.almersal.android.data.entities.Tag


class SkillsAutoCompleteAdapter(context: Context, resource: Int) : ArrayAdapter<String>(context, resource), Filterable {
    var data: MutableList<Tag> = ArrayList()
        set(value) {
            data.clear()
            data.addAll(value)
        }


    override fun getCount(): Int {
        return data.size
    }



    override fun getItem(position: Int): String? {
        return data[position].name
    }

    fun getTagAt(position: Int):Tag{
        return data[position]
    }


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                if (constraint != null) {
                    filterResults.values = data
                    filterResults.count = data.size
                }
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                if (results != null && results?.count > 0) {
                    notifyDataSetChanged()
                } else {
                    notifyDataSetInvalidated()
                }
            }
        }
    }
}