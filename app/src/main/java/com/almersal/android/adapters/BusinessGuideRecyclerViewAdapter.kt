package com.almersal.android.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.almersal.android.R
import com.almersal.android.data.entities.*
import com.almersal.android.data.filtration.FilterEntity
import com.almersal.android.enums.TagType
import com.almersal.android.repositories.DataStoreRepositories
import com.almersal.android.utilities.IntentHelper
import com.almersal.android.viewHolders.BusinessGuideViewHolder

class BusinessGuideRecyclerViewAdapter constructor(var context: Context, var businessGuideList: MutableList<BusinessGuide>
                                                   , private var businessGuideFilterList: MutableList<BusinessGuide>? = null)
    : RecyclerView.Adapter<BusinessGuideViewHolder>() {

    var filterEntity: FilterEntity? = null

    init {
        businessGuideFilterList = businessGuideList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusinessGuideViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.business_guide_item_layout, parent, false)
        return BusinessGuideViewHolder(view)
    }

    override fun getItemCount(): Int = businessGuideList.size

    override fun onBindViewHolder(holder: BusinessGuideViewHolder, position: Int) {
        val poJo = businessGuideList[position]
        holder.bind(poJo)

        holder.itemView.setOnClickListener {
            IntentHelper.startBusinessGuideDetailsActivity(context = context, businessGuide = poJo)
        }

    }

    fun filterByCriteria(filterEntity: FilterEntity) {
        this.filterEntity = filterEntity

        if (businessGuideFilterList == null)
            businessGuideFilterList = businessGuideList

        businessGuideList = businessGuideFilterList!!.filter {
            var result = true
            if ((filterEntity.query != null) and (!filterEntity.query.isNullOrEmpty()))
                result = result and it.getName().contains(filterEntity.query!!, false)
            if (filterEntity.category != null)
                result = result and (it.category == filterEntity.category)
            if (filterEntity.subCategory != null)
                result = result and (it.subCategory == filterEntity.subCategory)
            if (filterEntity.city != null) {
                val city: City? = DataStoreRepositories(context).findCityById(it.cityId)
                result = result and (city == filterEntity.city)
            }
            if (filterEntity.area != null) {
                val location: LocationEntity? = DataStoreRepositories(context).findLocationById(it.cityId, it.locationId)
                result = result and (location == filterEntity.area)
            }

            result
        }.toMutableList()

        notifyDataSetChanged()
    }

    fun excludeFilter(tagEntity: TagEntity) {
        if (filterEntity == null)
            return

        when (tagEntity.tagType) {
            TagType.Category -> {
                filterEntity!!.category = null
            }
            TagType.SubCategory -> {
                filterEntity!!.subCategory = null
            }
            TagType.City -> {
                filterEntity!!.city = null
            }
            TagType.Location -> {
                filterEntity!!.area = null
            }
            TagType.Query -> {
                filterEntity!!.query = null
            }
        }
        filterByCriteria(filterEntity!!)
    }

}
