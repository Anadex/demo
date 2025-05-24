package com.example.room

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MainViewModelFactory(private val wordDao: WordDao): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom((MainViewModel::class.java))) {
            return MainViewModel(wordDao) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}

