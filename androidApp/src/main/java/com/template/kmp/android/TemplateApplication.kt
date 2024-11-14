package com.template.kmp.android

import android.app.Application
import com.template.kmp.randomuser.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class TemplateApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidLogger()
            androidContext(this@TemplateApplication)
        }
    }
}