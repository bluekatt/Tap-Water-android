package com.parkjongseok.tapwater.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "day_record")
@TypeConverters(Converters::class)
data class DayRecord (
    @ColumnInfo(name = "drank_today")
    var drankToday: Float = 0f,

    @ColumnInfo(name = "daily_goal")
    var dailyGoal: Float = 0f,

    @ColumnInfo(name = "drink_log")
    var drinkLog: List<DrinkLogItem> = emptyList(),

    @PrimaryKey
    @ColumnInfo(name = "date")
    var date: String = "",
)
