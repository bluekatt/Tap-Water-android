package com.parkjongseok.tapwater.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.parkjongseok.tapwater.StartGoalSetViewModel
import com.parkjongseok.tapwater.ui.record.RecordDetailViewModel
import com.parkjongseok.tapwater.ui.record.RecordViewModel
import com.parkjongseok.tapwater.ui.settings.SettingsViewModel
import com.parkjongseok.tapwater.ui.settings.SpeedMeasureViewModel
import com.parkjongseok.tapwater.ui.summary.SummaryViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    abstract fun bindSettingsViewModel(settingsViewModel: SettingsViewModel): ViewModel

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

    @Binds
    @IntoMap
    @ViewModelKey(RecordDetailViewModel::class)
    abstract fun bindRecordDetailViewModel(recordDetailViewModel: RecordDetailViewModel): ViewModel
}

