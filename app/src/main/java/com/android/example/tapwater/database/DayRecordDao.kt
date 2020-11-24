package com.android.example.tapwater.database

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.IGNORE

@Dao
interface DayRecordDao {
    @Insert(onConflict = IGNORE)
    suspend fun insert(record: DayRecord)

    @Delete
    suspend fun delete(record: DayRecord)

    @Update
    suspend fun update(record: DayRecord)

    @Query("SELECT * from day_record WHERE recordId = :key")
    fun get(key: Long): LiveData<DayRecord>

    @Query("SELECT * from day_record ORDER BY recordId DESC LIMIT 1")
    suspend fun getTodayRecord(): DayRecord?

    @Query("SELECT * from day_record ORDER BY recordId DESC")
    fun getAllRecords(): LiveData<List<DayRecord>>
}