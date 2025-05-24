package com.anadex.recyclerview.data

import androidx.paging.PagingSource
import androidx.paging.PagingState

class PhotoPagingSource : PagingSource<Int, PhotoDTO>() {

    private val repository = Repository()
    override fun getRefreshKey(state: PagingState<Int, PhotoDTO>): Int = FIRST_PAGE

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PhotoDTO> {
        val page = params.key ?: FIRST_PAGE
        return kotlin.runCatching {
            repository.getPhotoList(page)
        }.fold(
            onSuccess = {
                LoadResult.Page(
                    data = it,
                    prevKey = null,
                    nextKey = if (it.isEmpty()) null else page + 1
                )
            },
            onFailure = { LoadResult.Error(it) }
        )
    }

    private companion object {
        private const val FIRST_PAGE = 0
    }
}
