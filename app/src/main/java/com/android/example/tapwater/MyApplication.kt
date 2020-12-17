package com.android.example.tapwater

import android.app.Application
import com.android.example.tapwater.di.AppComponent
import com.android.example.tapwater.di.DaggerAppComponent

open class MyApplication : Application() {
    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }
}