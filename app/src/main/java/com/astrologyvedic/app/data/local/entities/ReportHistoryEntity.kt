package com.astrologyvedic.app.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "report_history")
data class ReportHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "type")
    val type: String,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "person_name")
    val personName: String,
    @ColumnInfo(name = "date")
    val date: String,
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)
