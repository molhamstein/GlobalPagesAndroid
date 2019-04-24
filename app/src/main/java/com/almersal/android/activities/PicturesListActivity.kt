package com.almersal.android.activities

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.almersal.android.R
import com.almersal.android.adapters.PictureRecyclerView
import com.almersal.android.data.entities.MediaEntity
import com.google.gson.Gson

class PicturesListActivity : BaseActivity() {

    companion object {
        const val PicturesListActivity_Tag = "PicturesListActivity_Tag"
    }

    @BindView(R.id.recyclerView)
    lateinit var recyclerView: RecyclerView

    override fun onBaseCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.pictures_list_layout)
        ButterKnife.bind(this)
        val mediaEntityList: MutableList<MediaEntity> =
                Gson().fromJson(intent.getStringExtra(PicturesListActivity_Tag)
                        , Array<MediaEntity>::class.java).toMutableList()
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        recyclerView.adapter = PictureRecyclerView(this, mediaEntityList)
    }


    @OnClick(R.id.closeBtn)
    fun onCloseButtonClick(view: View) {
        finish()
    }


}