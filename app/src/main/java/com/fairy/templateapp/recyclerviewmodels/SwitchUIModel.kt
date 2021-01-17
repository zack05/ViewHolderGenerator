package com.fairy.templateapp.recyclerviewmodels

import android.view.View
import com.fairy.sugar_annotation.SugarViewHolder
import com.fairy.templateapp.R
import com.fairy.templateapp.databinding.ViewHolderSwitchBinding
import com.fairy.viewholdergenerator.RecyclerViewUIModel

@SugarViewHolder(layoutRes = R.layout.view_holder_switch)
data class SwitchUIModel(
    val title: String,
    val description: String,
    var isChecked: Boolean,
    val checkListener: (isChecked: Boolean) -> Unit
) : RecyclerViewUIModel {

    private lateinit var viewBinding: ViewHolderSwitchBinding

    override fun onBind(itemView: View) {
        viewBinding = ViewHolderSwitchBinding.bind(itemView)
        viewBinding.itemTitle.text = this.title
        viewBinding.itemDescription.text = this.description
        viewBinding.itemSwitch.setOnCheckedChangeListener { _, isChecked ->
            this.isChecked = isChecked
            this.checkListener.invoke(isChecked)
        }
        viewBinding.itemSwitch.isChecked = this.isChecked
    }
}