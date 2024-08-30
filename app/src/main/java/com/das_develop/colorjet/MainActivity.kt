package com.das_develop.colorjet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.room.Room
import com.das_develop.colorjet.Database.ColorDatabase
import com.das_develop.colorjet.Repository.ColorRepository
import com.das_develop.colorjet.ViewModel.ColorViewModel
import com.das_develop.colorjet.ui.theme.ColorJetTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Assuming you have a database instance
        val database = Room.databaseBuilder(
            applicationContext,
            ColorDatabase::class.java, "color_database"
        ).build()

        val repository = ColorRepository(database.colorDao())


        setContent {
            ColorJetTheme {
                val factory = ColorViewModel(repository)
                val viewModel: ColorViewModel = factory
                ColorList(viewModel)
            }
        }
    }
}

