package com.fairy.templateapp.recyclerviewmodels

import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.view.isVisible
import com.fairy.sugar_annotation.SugarViewHolder
import com.fairy.templateapp.R
import com.fairy.templateapp.databinding.SimpleViewHolderBinding
import com.fairy.viewholdergenerator.RecyclerViewUIModel

@SugarViewHolder(layoutRes = R.layout.simple_view_holder)
data class ListSimpleUIModel(
    val icon: Drawable?,
    val title: String?,
    val description: String?
) : RecyclerViewUIModel {

    private lateinit var viewBinding: SimpleViewHolderBinding

    override fun onBind(itemView: View) {
        viewBinding = SimpleViewHolderBinding.bind(itemView)
        viewBinding.itemIcon.setImageDrawable(icon)
        viewBinding.itemTitle.text = title
        viewBinding.itemDescription.text = description
        viewBinding.itemDescription.isVisible = !description.isNullOrBlank()
    }
}