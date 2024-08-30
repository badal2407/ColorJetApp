package com.das_develop.colorjet.Data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "color_table")
data class ColorEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val color: String,
    val timestamp: Long,
    var isSynced: Boolean = false
)
