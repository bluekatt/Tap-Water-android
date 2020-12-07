package com.android.example.tapwater.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "day_record")
data class DayRecord (
    @ColumnInfo(name = "drank_today")
    var drankToday: Float = 0f,

    @ColumnInfo(name = "daily_goal")
    var dailyGoal: Float = 0f,

    @PrimaryKey
    @ColumnInfo(name = "date")
    var date: String = "",
)
