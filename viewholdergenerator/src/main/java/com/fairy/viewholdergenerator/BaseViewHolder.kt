package com.fairy.viewholdergenerator

import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder : RecyclerView.ViewHolder{

    protected constructor(parent: ViewGroup, @LayoutRes resId: Int) : super(parent.inflate(resId))

    constructor(parent: ViewGroup) : super(View(parent.context))

    abstract fun onBind(data: RecyclerViewUIModel)
}