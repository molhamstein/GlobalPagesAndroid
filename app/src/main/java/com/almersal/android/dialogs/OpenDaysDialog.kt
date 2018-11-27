package com.almersal.android.dialogs

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.almersal.android.R
import com.almersal.android.adapters.OpenDayRecyclerViewAdapter
import com.almersal.android.data.entitiesModel.OpenDayModel
import com.almersal.android.listeners.OnOpenDayListListener
import com.almersal.android.repositories.SettingData
import com.brainsocket.mainlibrary.SupportViews.RecyclerViewDecoration.GridDividerDecoration

/**
 * Created by Adhamkh on 2018-10-11.
 */
class OpenDaysDialog : DialogFragment() {


    companion object {
        const val OpenDaysDialog_Tag = "OpenDaysDialog"

        fun newInstance(openDayList: MutableList<String>, onOpenDayListListener: OnOpenDayListListener): OpenDaysDialog {
            val f = OpenDaysDialog()
            // Supply num input as an argument.
            f.openDayList = SettingData.getOpenDayListWithCombineOriginalList(openDayList)
            f.onOpenDayListListener = onOpenDayListListener
            val args = Bundle()
            f.arguments = args

            return f
        }

    }

    @BindView(R.id.recyclerView)
    lateinit var recyclerView: RecyclerView

    lateinit var openDayList: MutableList<OpenDayModel>

    lateinit var onOpenDayListListener: OnOpenDayListListener

    private fun initRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(GridDividerDecoration(context))
        recyclerView.adapter = OpenDayRecyclerViewAdapter(context = context!!, openDayList = openDayList)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val mView = inflater.inflate(R.layout.open_day_dialog_layout, container)
        ButterKnife.bind(this, mView)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    @OnClick(R.id.okBtn)
    fun onOKButtonClick(view: View) {
        val adapter = recyclerView.adapter as OpenDayRecyclerViewAdapter
        val dayList = adapter.getSelectedOpenDayList()
        val dayResultList = SettingData.getOriginalListWithCombineOpenDayList(dayList)

        onOpenDayListListener.onOpenDayListSelect(dayResultList)

        dismiss()
    }

}