package com.das_develop.colorjet.Database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.das_develop.colorjet.Dao.ColorDao
import com.das_develop.colorjet.Data.ColorEntity

@Database(entities = [ColorEntity::class], version = 1, exportSchema = false)
abstract class ColorDatabase : RoomDatabase() {
    abstract fun colorDao(): ColorDao
}