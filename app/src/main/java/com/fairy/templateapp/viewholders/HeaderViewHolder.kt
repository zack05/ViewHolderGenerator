package com.fairy.templateapp.viewholders

import android.view.ViewGroup
import com.fairy.templateapp.R
import kotlinx.android.synthetic.main.view_holder_header.view.*

@SugarViewHolder
class HeaderViewHolder(parent: ViewGroup) :
    BaseViewHolder<HeaderViewHolder.HeaderUIModel>(parent, R.layout.view_holder_header) {

    override fun onBind(data: HeaderUIModel) {
        itemView.itemHeader.text = data.header
    }

    data class HeaderUIModel(val header: String?) : RecyclerViewUIModel
}