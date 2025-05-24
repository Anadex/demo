package anadex.skillbox.m12_mvvm.ui.main

import anadex.skillbox.m12_mvvm.R
import anadex.skillbox.m12_mvvm.RESOURCES
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.lang.String.format

class MainViewModel(
    private val repository: Repository
) : ViewModel() {

    private val requestString = MutableSharedFlow<String>(replay = 1)
    var foundText: String =
        RESOURCES.getString(R.string.default_text) //"Здесь будет результат поискового запроса"
    private val _state = MutableStateFlow(State.SEARCH_ON_PAUSE)
    val state = _state.asStateFlow()

    var request = ""
        set(value) {
            field = value
            viewModelScope.coroutineContext.cancelChildren()
            requestString.tryEmit(request)

            if (request.length > 2) find()
            else {
                _state.value = State.SEARCH_ON_PAUSE
                foundText =
                    RESOURCES.getString(R.string.too_short_text) //"Слишком короткий запрос"
            }
        }

    @OptIn(FlowPreview::class)
    private fun find() {
        requestString.debounce(300).onEach {
            foundText = RESOURCES.getString(R.string.start_search_text) //"Ищем..."
            _state.value = State.SEARCH_IN_PROCESS
            foundText =
                repository.getData() ?: format(RESOURCES.getString(R.string.nothing_found), it) //"По запросу \"$it\" ничего не найдено"
            _state.value = State.SEARCH_ON_FINISH
        }.launchIn(viewModelScope)
    }
}