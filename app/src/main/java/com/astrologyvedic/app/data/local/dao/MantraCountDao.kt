package com.astrologyvedic.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.astrologyvedic.app.data.local.entities.MantraCountEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MantraCountDao {

    @Query("SELECT * FROM mantra_counts WHERE date = :date")
    fun getByDate(date: String): Flow<List<MantraCountEntity>>

    @Query("SELECT SUM(count) FROM mantra_counts WHERE mantra = :mantra")
    suspend fun getTotalCount(mantra: String): Int?

    @Query("SELECT * FROM mantra_counts WHERE mantra = :mantra AND date = :date LIMIT 1")
    suspend fun getByMantraAndDate(mantra: String, date: String): MantraCountEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(mantraCount: MantraCountEntity): Long

    @Update
    suspend fun update(mantraCount: MantraCountEntity)
}
