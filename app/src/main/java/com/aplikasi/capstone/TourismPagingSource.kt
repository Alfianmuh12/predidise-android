package com.aplikasi.travelassistant

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.aplikasi.capstone.api.ApiService
import com.aplikasi.capstone.ResultItem


class TourismPagingSource(private val apiService: ApiService, private val header: String) : PagingSource<Int, ResultItem>() {
    override fun getRefreshKey(state: PagingState<Int, ResultItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ResultItem> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getAllTourismPaging(header)
            LoadResult.Page(
                data = responseData.result,
                prevKey = if(position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if(responseData.result.isNullOrEmpty()) null else position + 1
            )
        } catch(exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}