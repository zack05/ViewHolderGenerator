package com.fairy.templateapp.recyclerviewmodels

import android.view.View
import com.fairy.sugar_annotation.SugarViewHolder
import com.fairy.templateapp.R
import com.fairy.templateapp.databinding.ViewHolderTestBinding
import com.fairy.templateapp.viewholders.RecyclerViewUIModel

@SugarViewHolder(layoutRes = R.layout.view_holder_test)
class TestUIModel(private val title: String) : RecyclerViewUIModel {

    private lateinit var binding: ViewHolderTestBinding

    override fun onBind(itemView: View) {
        binding = ViewHolderTestBinding.bind(itemView)
        binding.testTitle.text = title
    }
}