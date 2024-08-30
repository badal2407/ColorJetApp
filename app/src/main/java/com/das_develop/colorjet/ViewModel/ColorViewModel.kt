package com.das_develop.colorjet.ViewModel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.das_develop.colorjet.Data.ColorEntity
import com.das_develop.colorjet.Repository.ColorRepository
import com.das_develop.colorjet.isInternetAvailable
import kotlinx.coroutines.launch

class ColorViewModel(private val repository: ColorRepository) : ViewModel() {

    val allColors =repository.allColors
    val unsyncedColorsCount: LiveData<Int> = repository.unsyncedColorsCount


    fun addColor(color: ColorEntity) {
        viewModelScope.launch {
            repository.insert(color)
        }
    }


    fun syncColorsToFirebase(context: Context) {
        viewModelScope.launch {
            if (isInternetAvailable(context)) {
                repository.syncColorsFromFirebase()
            } else {
                Toast.makeText(context, "No internet connection. Colors are not synced.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}