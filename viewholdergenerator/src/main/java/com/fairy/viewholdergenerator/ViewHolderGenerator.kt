package com.fairy.viewholdergenerator

import android.view.ViewGroup
import kotlin.reflect.KClass

object ViewHolderGenerator {

    private val mViewHoldersViewTypes: HashMap<Class<out RecyclerViewUIModel>, Int> by lazy {
        hashMapOf<Class<out RecyclerViewUIModel>, Int>()
    }

    fun init() {
        //TODO aloup
    }

    fun getViewType(obj: KClass<out RecyclerViewUIModel>): Int {
        //TODO aloup
        return 0
    }

    fun createViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return EmptyUIModelViewHolder(parent)
    }

}
