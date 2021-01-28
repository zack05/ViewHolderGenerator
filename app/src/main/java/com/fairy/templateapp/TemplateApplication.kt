package com.fairy.templateapp

import android.app.Application
import com.fairy.viewholdergenerator.ViewHolderGenerator

class TemplateApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ViewHolderGenerator.init()
    }
}