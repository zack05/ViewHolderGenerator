package com.fairy.templateapp

import android.view.ViewGroup
import com.fairy.viewholdergenerator.BaseViewHolder
import com.fairy.viewholdergenerator.EmptyUIModelViewHolder
import com.fairy.viewholdergenerator.RecyclerViewUIModel
import com.fairy.viewholdergenerator.viewholders.SugarGenerator

object ViewHolderGenerator {

    private val mViewHoldersViewTypes: HashMap<Class<out RecyclerViewUIModel>, Int> by lazy {
        hashMapOf<Class<out RecyclerViewUIModel>, Int>()
    }

    fun init() {
        SugarGenerator.registerViewHolders(mViewHoldersViewTypes)
    }

    fun <T> getViewType(obj: T): Int where T : RecyclerViewUIModel {
        return mViewHoldersViewTypes[obj::class.java] ?: -1
    }

    fun createViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        if (viewType == -1) return EmptyUIModelViewHolder(parent)
        return SugarGenerator.createViewHolder(parent, viewType) ?: EmptyUIModelViewHolder(parent)
    }

}
