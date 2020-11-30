package com.fairy.templateapp.models

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Vehicle(@StringRes val name: Int, @DrawableRes val icon: Int, val description: String?)
