package com.anadex.recyclerview.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.anadex.recyclerview.data.PhotoDTO
import com.anadex.recyclerview.data.PhotoPagingSource
import kotlinx.coroutines.flow.Flow

class StartViewModel : ViewModel() {

    val pagedPhotosFlow: Flow<PagingData<PhotoDTO>> = Pager(
        config = PagingConfig(pageSize = 50),
        pagingSourceFactory = { PhotoPagingSource() }
    ).flow.cachedIn(viewModelScope)

}
