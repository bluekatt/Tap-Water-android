package com.parkjongseok.tapwater.database

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.IGNORE

@Dao
interface DayRecordDao {
    @Insert(onConflict = IGNORE)
    suspend fun insert(record: DayRecord)

    @Delete
    suspend fun delete(record: DayRecord)

    @Query("DELETE from day_record")
    suspend fun deleteAll()

    @Update
    suspend fun update(record: DayRecord)

    @Query("SELECT * from day_record WHERE date = :date")
    suspend fun get(date: String): DayRecord?

    @Query("SELECT * from day_record ORDER BY date DESC")
    fun getAllRecords(): LiveData<List<DayRecord>>
}