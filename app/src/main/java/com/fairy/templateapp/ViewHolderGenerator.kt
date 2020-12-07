package com.fairy.templateapp

import android.view.View
import android.view.ViewGroup
import com.fairy.sugar_annotation.SugarViewHolder
import com.fairy.templateapp.viewholders.*

object ViewHolderGenerator {

    private val mViewHoldersViewTypes: HashMap<Class<out RecyclerViewUIModel>, Int> by lazy {
        hashMapOf<Class<out RecyclerViewUIModel>, Int>()
    }

    private var viewTypes: List<Class<out RecyclerViewUIModel>> = listOf()

    fun init() {
        SugarGenerator.registerViewHolders(mViewHoldersViewTypes)
        viewTypes = mViewHoldersViewTypes.keys.toList()
    }

    fun <T> getViewType(obj: T): Int where T : RecyclerViewUIModel {
        return mViewHoldersViewTypes[obj::class.java] ?: -1
    }

    fun createViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        if (viewType == -1) return EmptyUIModelViewHolder(parent)
        return SugarGenerator.createViewHolder(parent, viewType) ?: EmptyUIModelViewHolder(parent)
    }

}

@SugarViewHolder
class EmptyUIModel : RecyclerViewUIModel {
    override fun onBind(itemView: View) {
    }
}