package com.fairy.templateapp.viewholders

import android.view.ViewGroup
import com.fairy.templateapp.R
import kotlinx.android.synthetic.main.view_holder_switch.view.*

@SugarViewHolder
class SwitchViewHolder(parent: ViewGroup) :
    BaseViewHolder<SwitchViewHolder.SwitchUIModel>(parent, R.layout.view_holder_switch) {

    override fun onBind(data: SwitchUIModel) {
        itemView.itemTitle.text = data.title
        itemView.itemDescription.text = data.description
        itemView.itemSwitch.setOnCheckedChangeListener { _, isChecked ->
            data.isChecked = isChecked
            data.checkListener.invoke(isChecked)
        }
        itemView.itemSwitch.isChecked = data.isChecked
    }

    data class SwitchUIModel(
        val title: String,
        val description: String,
        var isChecked: Boolean,
        val checkListener: (isChecked: Boolean) -> Unit
    ) : RecyclerViewUIModel

}