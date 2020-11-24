package com.android.example.tapwater.di

import android.content.Context
import com.android.example.tapwater.database.DatabaseModule
import com.android.example.tapwater.ui.record.RecordFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DatabaseModule::class])
interface AppComponent {
    @Component.Factory
    interface Factory{
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun inject(fragment: RecordFragment)
}