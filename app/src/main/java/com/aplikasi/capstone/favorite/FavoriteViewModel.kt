package com.aplikasi.capstone.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.aplikasi.capstone.database.FavoriteUser
import com.aplikasi.capstone.database.FavoriteUserDao
import com.aplikasi.capstone.database.FavoriteUserDatabase

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {
    private var favDao: FavoriteUserDao?
    private var favDb: FavoriteUserDatabase?


    init {
        favDb = FavoriteUserDatabase.getDatabase(application)
        favDao = favDb?.favoriteUserDao()
    }




    fun addToFavorite(
        username: String, avatarUrl: String,
        description: String,
        id: Int?, location: String, price: String, rating: String
    ){
        CoroutineScope(Dispatchers.IO).launch {
            val user = FavoriteUser(
                username,
                id,
                avatarUrl,
                description,
                location,
                price,
                rating
            )
            favDao?.addToFavorite(user)
        }
    }

    suspend fun checkUser(id: Int?) = id?.let { favDao?.checkFavoriteUser(it) }

    fun deleteFromFavorite(id: Int?){
        CoroutineScope(Dispatchers.IO).launch {
            if (id != null) {
                favDao?.deleteFromFavorite(id)
            }
        }
    }

    fun getFavoriteUser(): LiveData<List<FavoriteUser>>? {
        return favDao?.getAllFavoriteUser()
    }

}