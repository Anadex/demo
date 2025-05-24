package com.example.room

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Update
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(private val wordDao: WordDao) : ViewModel() {

    val allWords: StateFlow<List<Word>> = this.wordDao.getFirstFive().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList()
    )

    fun onAddButton(string: String): Boolean {
        val regex = """[^\p{L}-]|^$""".toRegex()

        if (regex.containsMatchIn(string)) {
            return false
        } else {
            viewModelScope.launch {
                wordDao.insertOrUpdate(Word(word = string))
            }
            return true
        }
    }

    fun onClearButton() {
        viewModelScope.launch {
            wordDao.deleteAll()
        }
    }
}
