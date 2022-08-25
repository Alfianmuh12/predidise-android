package com.aplikasi.capstone

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction

@ExperimentalPagingApi
class TourismRemoteMediator/*(
    private val database: TourismDatabase,
    private val apiService: ApiService,
    private val token: String
) : RemoteMediator<Int, ResultItem>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }


    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ResultItem>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }


        try {
            val responseData = apiService.getAllTourism(token)
            val endOfPaginationReached = responseData.r.isEmpty()

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.TourismDao().deleteAll()
                    database.KeysDao().deleteRemoteKeys()
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = responseData.result.map { keys ->
                    RemoteKeys(id = keys.id, prevKey = prevKey, nextKey = nextKey)
                }
                database.KeysDao().insertAll(keys)
                database.TourismDao().insertStory(responseData.result)

            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: Exception) {
            return MediatorResult.Error(exception)
        }
    }


    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, ResultItem>): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            database.KeysDao().getRemoteKeysId(data.id)
        }
    }


    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, ResultItem>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            database.KeysDao().getRemoteKeysId(data.id)
        }
    }


    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, ResultItem>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                database.KeysDao().getRemoteKeysId(id)

            }
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }*/
