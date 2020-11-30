package com.fairy.templateapp

import android.view.ViewGroup
import com.fairy.templateapp.viewholders.*
import kotlin.jvm.internal.Reflection
import kotlin.reflect.KClass
import kotlin.reflect.jvm.internal.impl.descriptors.runtime.structure.ReflectJavaAnnotation

object ViewHolderGenerator {

    fun init() {
        register(SimpleViewHolder::class)
        register(HeaderViewHolder::class)
        register(SwitchViewHolder::class)

    }

    private val mViewHoldersMapping: HashMap<KClass<*>, KClass<out BaseViewHolder<*>>> by lazy {
        hashMapOf<KClass<*>, KClass<out BaseViewHolder<*>>>()
    }

    private var viewTypes: List<Any> = listOf()


    fun register(type: KClass<*>, viewHolderClass: KClass<out BaseViewHolder<*>>) {
        mViewHoldersMapping[type] = viewHolderClass
        viewTypes = mViewHoldersMapping.keys.toList()
    }

    fun <T> getViewType(obj: T): Int where T : RecyclerViewUIModel {
        return viewTypes.indexOf(obj::class)
    }

    inline fun <reified T> register(viewHolderClass: KClass<out BaseViewHolder<T>>) where T : RecyclerViewUIModel {
        register(T::class, viewHolderClass)
    }

    fun createViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        if (viewType == -1) return EmptyViewHolder(parent)
        val key = viewTypes[viewType]
        return mViewHoldersMapping[key]?.constructors?.singleOrNull { it.parameters.size == 1 }
            ?.call(parent) ?: EmptyViewHolder(parent)
    }

    class EmptyViewHolder(parent: ViewGroup) : BaseViewHolder<EmptyUIModel>(parent) {
        override fun onBind(data: EmptyUIModel) {

        }
    }

    class EmptyUIModel : RecyclerViewUIModel

}