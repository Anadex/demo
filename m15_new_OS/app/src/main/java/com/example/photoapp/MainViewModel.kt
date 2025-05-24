package com.example.photoapp

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val photoDAO: PhotoDAO,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    fun addPhoto(src: String, date: String) {
        viewModelScope.launch(Dispatchers.IO) {
            photoDAO.insert(Photo(src = src, date = date))
        }
    }

    private val _photoFlow = MutableStateFlow<List<Photo>>(emptyList())

    suspend fun getPhotoFromGallery(): StateFlow<List<Photo>> {
        _photoFlow.value = photoDAO.getAllPhoto()
        return _photoFlow.asStateFlow()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val application = checkNotNull(extras[APPLICATION_KEY])
                val savedStateHandle = extras.createSavedStateHandle()

                return MainViewModel(
                    (application as App).db.photoDao(),
                    savedStateHandle
                ) as T
            }
        }
    }
}