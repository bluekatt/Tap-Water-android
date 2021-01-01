package com.parkjongseok.tapwater.di

import android.content.Context
import com.parkjongseok.tapwater.database.AppDatabase
import com.parkjongseok.tapwater.database.DayRecordDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {
    @Singleton
    @Provides
    fun provideDB(context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Singleton
    @Provides
    fun provideDayRecordDao(db: AppDatabase): DayRecordDao {
        return db.dayRecordDao
    }
}