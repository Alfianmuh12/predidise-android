package com.aplikasi.capstone.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavoriteUserDao {
    @Insert
    suspend fun addToFavorite(favoriteUser: FavoriteUser)

    @Query("SELECT * FROM favorite_user ")
    fun getAllFavoriteUser(): LiveData<List<FavoriteUser>>

    @Query("DELETE FROM favorite_user WHERE favorite_user.id = :id")
    suspend fun deleteFromFavorite(id: kotlin.Int): Int

    @Query("SELECT count(*) FROM favorite_user WHERE favorite_user.id = :id")
    suspend fun checkFavoriteUser(id: kotlin.Int): Int


}