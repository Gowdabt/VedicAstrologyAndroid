package com.astrologyvedic.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.astrologyvedic.app.data.local.entities.ReportHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReportHistoryDao {

    @Query("SELECT * FROM report_history ORDER BY created_at DESC")
    fun getAll(): Flow<List<ReportHistoryEntity>>

    @Query("SELECT * FROM report_history WHERE type = :type ORDER BY created_at DESC")
    fun getByType(type: String): Flow<List<ReportHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(report: ReportHistoryEntity): Long

    @Query("DELETE FROM report_history WHERE created_at < :timestamp")
    suspend fun deleteOld(timestamp: Long)
}
