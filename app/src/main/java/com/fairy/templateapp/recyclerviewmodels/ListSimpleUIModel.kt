package com.fairy.templateapp.recyclerviewmodels

import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.view.isVisible
import com.fairy.sugar_annotation.SugarViewHolder
import com.fairy.templateapp.R
import com.fairy.templateapp.viewholders.RecyclerViewUIModel
import kotlinx.android.synthetic.main.simple_view_holder.view.*

@SugarViewHolder(layoutRes = R.layout.simple_view_holder)
data class ListSimpleUIModel(
    val icon: Drawable?,
    val title: String?,
    val description: String?
) : RecyclerViewUIModel {

    override fun onBind(itemView: View) {
        itemView.itemIcon.setImageDrawable(icon)
        itemView.itemTitle.text = title
        itemView.itemDescription.text = description
        itemView.itemDescription.isVisible = !description.isNullOrBlank()
    }
}