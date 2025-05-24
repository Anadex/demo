package com.example.m16_architecture.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.m16_architecture.R
import com.example.m16_architecture.data.UsefulActivityDto
import com.example.m16_architecture.domain.GetUsefulActivityUseCase
import com.example.m16_architecture.entity.UsefulActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @ApplicationContext appContext: Context,
    private val getUsefulActivityUseCase: GetUsefulActivityUseCase
) : ViewModel() {

    private val _usefulActivitySharedFlow: MutableStateFlow<UsefulActivity?> =
        MutableStateFlow(UsefulActivityDto(appContext.getString(R.string.starting_message)))

    val usefulActivitySharedFlow = _usefulActivitySharedFlow.asStateFlow()

    suspend fun reloadUsefulActivity() {
        _usefulActivitySharedFlow.value = getUsefulActivityUseCase.execute()
    }

}