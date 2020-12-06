package com.fairy.templateapp.recyclerviewmodels

import android.view.View
import com.fairy.sugar_annotation.SugarViewHolder
import com.fairy.templateapp.R
import com.fairy.templateapp.viewholders.RecyclerViewUIModel
import kotlinx.android.synthetic.main.view_holder_header.view.*

@SugarViewHolder(layoutRes = R.layout.view_holder_header)
data class HeaderUIModel(val header: String?) : RecyclerViewUIModel {
    override fun onBind(itemView: View) {
        itemView.itemHeader.text = this.header
    }
}