package com.fairy.templateapp.viewholders

/**
 * represents any recycler view model
 * implement on your ui model to define how the model will be represented
 */
interface RecyclerViewUIModel {

    /**
     * callback invoked when clicked
     */
    val clickable: Boolean
        get() = false
}