package com.aplikasi.capstone.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase



@Database(
    entities = [FavoriteUser::class], version = 1
)
abstract class FavoriteUserDatabase : RoomDatabase() {

    companion object {
        private var instance : FavoriteUserDatabase? = null

        fun getDatabase(context: Context): FavoriteUserDatabase?{
            if (instance==null){
                synchronized(FavoriteUserDatabase::class){
                    instance = Room.databaseBuilder(context.applicationContext, FavoriteUserDatabase::class.java,"favorite_database").build()
                }
            }
            return instance
        }
    }
    abstract fun favoriteUserDao(): FavoriteUserDao
}