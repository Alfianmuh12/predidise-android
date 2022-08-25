package com.aplikasi.capstone.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aplikasi.capstone.ResultItem

@Dao
interface TourismDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(story: List<ResultItem>)

    @Query("SELECT * FROM listTourism")
    fun getAllStory(): PagingSource<Int, ResultItem>

    @Query("SELECT * FROM listTourism")
    suspend fun findAll(): List<ResultItem>

    @Query("DELETE FROM listTourism")
    suspend fun deleteAll()
}