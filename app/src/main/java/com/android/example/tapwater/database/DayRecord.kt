package com.android.example.tapwater.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "day_record")
data class DayRecord (
    @PrimaryKey(autoGenerate = true)
    var recordId: Long = 0L,

    @ColumnInfo(name = "drank_today")
    var drankToday: Double = 0.0,

    @ColumnInfo(name = "daily_goal")
    var dailyGoal: Double = 0.0,

    @ColumnInfo(name = "date")
    var date: String = "",
)
