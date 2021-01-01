package com.android.parkjongseok.tapwater

import android.app.Application
import com.android.parkjongseok.tapwater.di.AppComponent
import com.android.parkjongseok.tapwater.di.DaggerAppComponent

open class MyApplication : Application() {
    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }
}