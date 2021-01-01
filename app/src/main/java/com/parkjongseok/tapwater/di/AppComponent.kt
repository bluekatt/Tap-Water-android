package com.parkjongseok.tapwater.di

import android.content.Context
import com.parkjongseok.tapwater.StartGoalSetFragment
import com.parkjongseok.tapwater.ui.record.RecordDetailFragment
import com.parkjongseok.tapwater.ui.record.RecordFragment
import com.parkjongseok.tapwater.ui.settings.SettingsFragment
import com.parkjongseok.tapwater.ui.settings.SpeedMeasureFragment
import com.parkjongseok.tapwater.ui.settings.SpeedMeasureHelpFragment
import com.parkjongseok.tapwater.ui.summary.StatsFragment
import com.parkjongseok.tapwater.ui.summary.SummaryFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    DatabaseModule::class, ViewModelModule::class,
])
interface AppComponent {
    @Component.Factory
    interface Factory{
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun inject(fragment: StartGoalSetFragment)
    fun inject(fragment: RecordFragment)
    fun inject(fragment: RecordDetailFragment)
    fun inject(fragment: SettingsFragment)
    fun inject(fragment: SpeedMeasureFragment)
    fun inject(fragment: SpeedMeasureHelpFragment)
    fun inject(fragment: SummaryFragment)
    fun inject(fragment: StatsFragment)
}