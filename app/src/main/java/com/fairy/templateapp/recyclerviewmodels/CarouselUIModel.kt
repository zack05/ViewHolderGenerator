package com.fairy.templateapp.recyclerviewmodels

import android.view.View
import com.fairy.sugar_annotation.SugarViewHolder
import com.fairy.templateapp.BaseAdapter
import com.fairy.templateapp.R
import com.fairy.templateapp.databinding.ViewHolderCarouselBinding
import com.fairy.viewholdergenerator.RecyclerViewUIModel

@SugarViewHolder(layoutRes = R.layout.view_holder_carousel)
class CarouselUIModel(private val itemList: List<String>) : RecyclerViewUIModel {


    private lateinit var binding: ViewHolderCarouselBinding

    override fun onBind(itemView: View) {
        binding = ViewHolderCarouselBinding.bind(itemView)
        binding.root.adapter = BaseAdapter(itemList){ data ->
            return@BaseAdapter CarouselCardUIModel(data as String)
        }
    }
}