package com.astrologyvedic.app.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profiles")
data class ProfileEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "dob")
    val dob: String,
    @ColumnInfo(name = "time")
    val time: String,
    @ColumnInfo(name = "place")
    val place: String,
    @ColumnInfo(name = "latitude")
    val latitude: Double,
    @ColumnInfo(name = "longitude")
    val longitude: Double,
    @ColumnInfo(name = "timezone")
    val timezone: String,
    @ColumnInfo(name = "ayanamsa")
    val ayanamsa: String = "lahiri",
    @ColumnInfo(name = "chart_style")
    val chartStyle: String = "north_indian",
    @ColumnInfo(name = "is_default")
    val isDefault: Boolean = false,
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)
