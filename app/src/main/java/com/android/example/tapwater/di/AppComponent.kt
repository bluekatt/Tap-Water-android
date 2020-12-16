package com.android.example.tapwater.di

import android.content.Context
import com.android.example.tapwater.database.DatabaseModule
import com.android.example.tapwater.ui.record.RecordFragment
import com.android.example.tapwater.ui.settings.SpeedMeasureFragment
import com.android.example.tapwater.ui.summary.SummaryFragment
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
    fun inject(fragment: SpeedMeasureFragment)
    fun inject(fragment: SummaryFragment)
}