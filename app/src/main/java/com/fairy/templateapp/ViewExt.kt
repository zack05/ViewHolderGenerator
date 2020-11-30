package com.fairy.templateapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

fun ViewGroup.inflate(@LayoutRes resId : Int) : View{
    return LayoutInflater.from(context).inflate(resId, this, false)
}