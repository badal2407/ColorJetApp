package com.das_develop.colorjet.Repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.das_develop.colorjet.Dao.ColorDao
import com.das_develop.colorjet.Data.ColorEntity
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class ColorRepository(private val colorDao: ColorDao) {

       val allColors: LiveData<List<ColorEntity>> = colorDao.getAllColors().asLiveData()

    val unsyncedColorsCount: LiveData<Int> = colorDao.getUnsyncedColors()
        .map { it.size }
        .asLiveData()

       suspend fun insert(color: ColorEntity) {
           colorDao.insertColor(color)
       }

       suspend fun deleteAll() {
           colorDao.deleteAll()
       }

       // This methods to sync with Firebase
   suspend fun syncColorsFromFirebase() {
           val unsyncedColors = colorDao.getUnsyncedColors().first() // Get the unsynced colors
           if (unsyncedColors.isNotEmpty()) {
               val database = Firebase.database
               val myRef = database.getReference("colorDetail")

               unsyncedColors.forEach { color ->
                   myRef.push().setValue(color) // Push each color to Firebase
                   color.isSynced = true
                   colorDao.updateColor(color) // Update the entry to mark it as synced
               }
           }
    }
}