package com.fairy.templateapp.viewholders

import android.view.View

/**
 * represents any recycler view model
 * implement on your ui model to define how the model will be represented
 */
interface RecyclerViewUIModel {


    fun onBind(itemView: View)

//    /**
//     * callback invoked when clicked
//     */
//    val clickable: Boolean
//        get() = false
}