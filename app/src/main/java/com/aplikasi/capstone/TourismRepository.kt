package com.aplikasi.capstone

import androidx.lifecycle.LiveData
import androidx.paging.*

class TourismRepository/*(
    private val tourismDatabase: TourismDatabase?,
    private val apiService: ApiService
) {
    @ExperimentalPagingApi
    fun getTourism(token: String): LiveData<PagingData<ResultItem>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            remoteMediator = tourismDatabase?.let { TourismRemoteMediator(it, apiService, token) },
            pagingSourceFactory = {
                tourismDatabase?.TourismDao()!!.getAllStory()
            }
        ).liveData
    }

    suspend fun getData(): List<ResultItem> {
        return tourismDatabase?.TourismDao()!!.findAll()
    }*/
