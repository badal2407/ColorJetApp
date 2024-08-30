package com.das_develop.colorjet.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.das_develop.colorjet.Data.ColorEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ColorDao {

    @Insert
    suspend fun insertColor(color: ColorEntity)

    @Query("SELECT * FROM color_table")
    fun getAllColors(): Flow<List<ColorEntity>>

    @Query("DELETE FROM color_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM color_table WHERE isSynced = 0")
    fun getUnsyncedColors(): Flow<List<ColorEntity>>

    @Update
    suspend fun updateColor(color: ColorEntity)
}