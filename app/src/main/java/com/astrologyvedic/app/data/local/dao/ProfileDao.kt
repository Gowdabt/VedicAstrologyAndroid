package com.astrologyvedic.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.astrologyvedic.app.data.local.entities.ProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDao {

    @Query("SELECT * FROM profiles ORDER BY created_at DESC")
    fun getAll(): Flow<List<ProfileEntity>>

    @Query("SELECT * FROM profiles WHERE is_default = 1 LIMIT 1")
    suspend fun getDefault(): ProfileEntity?

    @Query("SELECT * FROM profiles WHERE id = :id")
    suspend fun getById(id: Long): ProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(profile: ProfileEntity): Long

    @Update
    suspend fun update(profile: ProfileEntity)

    @Delete
    suspend fun delete(profile: ProfileEntity)

    @Query("UPDATE profiles SET is_default = 0")
    suspend fun clearDefaultFlag()
}
