package com.fairy.templateapp.recyclerviewmodels

import android.view.View
import com.fairy.sugar_annotation.SugarViewHolder
import com.fairy.templateapp.R
import com.fairy.templateapp.databinding.ViewHolderHeaderBinding
import com.fairy.viewholdergenerator.RecyclerViewUIModel

@SugarViewHolder(layoutRes = R.layout.view_holder_header)
data class HeaderUIModel(val header: String?) : RecyclerViewUIModel {

    private lateinit var viewBinding: ViewHolderHeaderBinding

    override fun onBind(itemView: View) {
        viewBinding = ViewHolderHeaderBinding.bind(itemView)
        viewBinding.itemHeader.text = this.header
    }
}