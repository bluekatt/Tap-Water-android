package com.parkjongseok.tapwater.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [DayRecord::class], version = 3, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract val dayRecordDao: DayRecordDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance==null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "app_database"
                    )
                        .addMigrations(MIGRATION_2_3)
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}

val MIGRATION_2_3 = object: Migration(2,3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE day_record ADD COLUMN drink_log TEXT NOT NULL DEFAULT '[]'")
    }
}