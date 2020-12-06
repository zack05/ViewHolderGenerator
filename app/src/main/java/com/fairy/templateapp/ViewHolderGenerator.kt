package com.fairy.templateapp

import android.view.View
import android.view.ViewGroup
import com.fairy.sugar_annotation.SugarViewHolder
import com.fairy.templateapp.viewholders.*

object ViewHolderGenerator {

    private val mViewHoldersMapping: HashMap<Class<out RecyclerViewUIModel>, Class<out BaseViewHolder>> by lazy {
        hashMapOf<Class<out RecyclerViewUIModel>, Class<out BaseViewHolder>>()
    }

    private var viewTypes: List<Class<out RecyclerViewUIModel>> = listOf()

    fun init() {
        SugarGenerator.registerViewHolders(mViewHoldersMapping)
        viewTypes = mViewHoldersMapping.keys.toList()
    }

    fun <T> getViewType(obj: T): Int where T : RecyclerViewUIModel {
        return viewTypes.indexOf(obj::class.java)
    }

    fun createViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        if (viewType == -1) return EmptyUIModelViewHolder(parent)
        return SugarGenerator.createViewHolder(parent, viewTypes[viewType])
            ?: EmptyUIModelViewHolder(parent)
    }

}

@SugarViewHolder
class EmptyUIModel : RecyclerViewUIModel {
    override fun onBind(itemView: View) {
    }
}