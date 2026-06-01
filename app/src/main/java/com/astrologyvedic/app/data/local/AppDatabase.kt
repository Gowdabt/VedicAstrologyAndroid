package com.astrologyvedic.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.astrologyvedic.app.data.local.dao.ChatDao
import com.astrologyvedic.app.data.local.dao.MantraCountDao
import com.astrologyvedic.app.data.local.dao.ProfileDao
import com.astrologyvedic.app.data.local.dao.ReportHistoryDao
import com.astrologyvedic.app.data.local.entities.ChatMessageEntity
import com.astrologyvedic.app.data.local.entities.MantraCountEntity
import com.astrologyvedic.app.data.local.entities.ProfileEntity
import com.astrologyvedic.app.data.local.entities.ReportHistoryEntity

@Database(
    entities = [
        ProfileEntity::class,
        ChatMessageEntity::class,
        ReportHistoryEntity::class,
        MantraCountEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun profileDao(): ProfileDao
    abstract fun chatDao(): ChatDao
    abstract fun reportHistoryDao(): ReportHistoryDao
    abstract fun mantraCountDao(): MantraCountDao
}
