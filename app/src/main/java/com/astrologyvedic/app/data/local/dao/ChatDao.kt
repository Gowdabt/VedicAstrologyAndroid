package com.astrologyvedic.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.astrologyvedic.app.data.local.entities.ChatMessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {

    @Query("SELECT * FROM chat_messages WHERE profile_id = :profileId ORDER BY timestamp ASC")
    fun getMessages(profileId: Long): Flow<List<ChatMessageEntity>>

    @Query("SELECT * FROM chat_messages ORDER BY timestamp ASC")
    fun getAllMessages(): Flow<List<ChatMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(message: ChatMessageEntity): Long

    @Query("DELETE FROM chat_messages WHERE profile_id = :profileId")
    suspend fun clearHistory(profileId: Long)

    @Query("DELETE FROM chat_messages")
    suspend fun clearAllHistory()
}
