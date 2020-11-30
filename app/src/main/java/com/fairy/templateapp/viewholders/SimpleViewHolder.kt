package com.fairy.templateapp.viewholders

import android.graphics.drawable.Drawable
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.fairy.templateapp.R
import kotlinx.android.synthetic.main.simple_view_holder.view.*

@SugarViewHolder
class SimpleViewHolder(parent: ViewGroup) :
    BaseViewHolder<SimpleViewHolder.ListSimpleUIModel>(parent, R.layout.simple_view_holder) {

    override fun onBind(data: ListSimpleUIModel) {
        itemView.itemIcon.setImageDrawable(data.icon)
        itemView.itemTitle.text = data.title
        itemView.itemDescription.text = data.description
        itemView.itemDescription.isVisible = !data.description.isNullOrBlank()
    }

    data class ListSimpleUIModel(val icon: Drawable?, val title: String?, val description: String?) :
        RecyclerViewUIModel {

        override val clickable: Boolean
            get() = true

    }

}