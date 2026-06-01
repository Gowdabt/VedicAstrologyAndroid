package com.astrologyvedic.app.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mantra_counts")
data class MantraCountEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "mantra")
    val mantra: String,
    @ColumnInfo(name = "count")
    val count: Int = 0,
    @ColumnInfo(name = "target")
    val target: Int = 108,
    @ColumnInfo(name = "date")
    val date: String
)
