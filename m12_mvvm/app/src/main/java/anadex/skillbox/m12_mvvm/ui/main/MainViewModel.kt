package anadex.skillbox.m12_mvvm.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: Repository
) : ViewModel() {
    var request = ""
    var foundText: String = "Здесь будет результат поискового запроса"
    private var _state = MutableStateFlow(State.SEARCH_IS_NOT_AVAILABLE)
    var state = _state.asStateFlow()

    fun changeSearchAvailability(numOfChar: Int) {
        if (_state.value != State.SEARCH_IN_PROCESS) {
            when {
                numOfChar < 3 -> _state.value = State.SEARCH_IS_NOT_AVAILABLE
                else -> _state.value = State.SEARCH_IS_AVAILABLE
            }
        }
    }

    fun find() {
        viewModelScope.launch {
            foundText = "Ищем..."
            _state.value = State.SEARCH_IN_PROCESS
            foundText = repository.getData() ?: "По запросу \"$request\" ничего не найдено"
            _state.value = State.SEARCH_RESULT_IS_AVAILABLE
        }
    }
}