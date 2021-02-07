package com.fairy.viewholdergenerator

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

open class BaseAdapter(
    private val itemList: List<Any>,
    private val bindCallback: (data: Any) -> RecyclerViewUIModel
) : RecyclerView.Adapter<BaseViewHolder>() {

    companion object {
        private val viewHolderGenerator by lazy {
            ViewHolderGenerator()
        }
    }

    protected fun getViewHolderGenerator(): ViewHolderGenerator {
        return viewHolderGenerator
    }

    private var uiModelList: MutableList<RecyclerViewUIModel?> =
        arrayOfNulls<RecyclerViewUIModel>(itemList.size).toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return getViewHolderGenerator().createViewHolder(parent, viewType)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun getItemViewType(position: Int): Int {
        val uiModel = uiModelList[position] ?: bindCallback.invoke(itemList[position])
            .also { uiModelList[position] = it }
        return getViewHolderGenerator().getViewType(uiModel::class)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val item = uiModelList[position] ?: return
        holder.onBind(item)
    }
}