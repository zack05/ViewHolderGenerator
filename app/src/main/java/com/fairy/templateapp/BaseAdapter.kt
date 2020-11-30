package com.fairy.templateapp

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fairy.templateapp.viewholders.RecyclerViewUIModel
import com.fairy.templateapp.viewholders.BaseViewHolder

class BaseAdapter(
    private val itemList: List<Any>,
    private val bindCallback: (data: Any) -> RecyclerViewUIModel,
    private val clickListener: (uiModel: RecyclerViewUIModel) -> Unit
) : RecyclerView.Adapter<BaseViewHolder<RecyclerViewUIModel>>() {

    private var uiModelList: MutableList<RecyclerViewUIModel?> =
        arrayOfNulls<RecyclerViewUIModel>(itemList.size).toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<RecyclerViewUIModel> {
        return ViewHolderGenerator.createViewHolder(parent, viewType) as BaseViewHolder<RecyclerViewUIModel>
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun getItemViewType(position: Int): Int {
        val uiModel = uiModelList[position] ?:
        bindCallback.invoke(itemList[position]).also { uiModelList[position] = it }
        return ViewHolderGenerator.getViewType(uiModel)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<RecyclerViewUIModel>, position: Int) {
        val item = uiModelList[position] ?: return
        holder.onBind(item)
        if (item.clickable) {
            holder.itemView.setOnClickListener { clickListener.invoke(item) }
        } else {
            holder.itemView.setOnClickListener(null)
        }
    }
}