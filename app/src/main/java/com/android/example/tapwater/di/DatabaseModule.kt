package com.android.example.tapwater.di

import android.content.Context
import com.android.example.tapwater.database.AppDatabase
import com.android.example.tapwater.database.DayRecordDao
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