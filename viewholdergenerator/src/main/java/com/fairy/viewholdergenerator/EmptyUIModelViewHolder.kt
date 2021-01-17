package com.fairy.viewholdergenerator

import android.view.ViewGroup

class EmptyUIModelViewHolder(parent: ViewGroup) : BaseViewHolder(parent) {
    override fun onBind(data: RecyclerViewUIModel) {
        //do nothing
    }
}