package com.fairy.templateapp.recyclerviewmodels

import android.view.View
import com.fairy.sugar_annotation.SugarViewHolder
import com.fairy.templateapp.R
import com.fairy.templateapp.databinding.ViewHolderCarouselCardBinding
import com.fairy.viewholdergenerator.RecyclerViewUIModel

@SugarViewHolder(layoutRes = R.layout.view_holder_carousel_card)
class CarouselCardUIModel(private val title: String) : RecyclerViewUIModel {

    private lateinit var binding: ViewHolderCarouselCardBinding

    override fun onBind(itemView: View) {
        binding = ViewHolderCarouselCardBinding.bind(itemView)
        binding.itemTitle.text = title
    }
}