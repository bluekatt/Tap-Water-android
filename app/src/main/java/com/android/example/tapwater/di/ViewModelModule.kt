package com.android.example.tapwater.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.example.tapwater.StartGoalSetViewModel
import com.android.example.tapwater.ui.record.RecordViewModel
import com.android.example.tapwater.ui.settings.SettingsViewModel
import com.android.example.tapwater.ui.settings.SpeedMeasureViewModel
import com.android.example.tapwater.ui.summary.SummaryFragment
import com.android.example.tapwater.ui.summary.SummaryViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    abstract fun bindSettingsViewModel(summaryViewModel: SummaryViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RecordViewModel::class)
    abstract fun bindRecordViewModel(recordViewModel: RecordViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SummaryViewModel::class)
    abstract fun bindSummaryViewModel(summaryViewModel: SummaryViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SpeedMeasureViewModel::class)
    abstract fun bindSpeedMeasureViewModel(speedMeasureViewModel: SpeedMeasureViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(StartGoalSetViewModel::class)
    abstract fun bindStartGoalSetViewModel(startGoalSetViewModel: StartGoalSetViewModel): ViewModel
}

