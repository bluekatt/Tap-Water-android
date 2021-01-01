package com.parkjongseok.tapwater

import android.app.Application
import com.parkjongseok.tapwater.di.AppComponent
import com.parkjongseok.tapwater.di.DaggerAppComponent

open class MyApplication : Application() {
    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }
}