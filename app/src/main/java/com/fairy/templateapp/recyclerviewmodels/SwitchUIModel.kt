package com.fairy.templateapp.recyclerviewmodels

import android.view.View
import com.fairy.sugar_annotation.SugarViewHolder
import com.fairy.templateapp.R
import com.fairy.templateapp.viewholders.RecyclerViewUIModel
import kotlinx.android.synthetic.main.view_holder_switch.view.*

@SugarViewHolder(layoutRes = R.layout.view_holder_switch)
data class SwitchUIModel(
    val title: String,
    val description: String,
    var isChecked: Boolean,
    val checkListener: (isChecked: Boolean) -> Unit
) : RecyclerViewUIModel {
    override fun onBind(itemView: View) {
        itemView.itemTitle.text = this.title
        itemView.itemDescription.text = this.description
        itemView.itemSwitch.setOnCheckedChangeListener { _, isChecked ->
            this.isChecked = isChecked
            this.checkListener.invoke(isChecked)
        }
        itemView.itemSwitch.isChecked = this.isChecked
    }
}