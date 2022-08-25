package com.aplikasi.capstone.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.aplikasi.capstone.ResultItem

@Database(
    entities = [ResultItem::class, RemoteKeys::class],
    version = 1,
    exportSchema = false
)


abstract class TourismDatabase : RoomDatabase() {

    abstract fun TourismDao(): TourismDao
    abstract fun KeysDao(): RemoteKeysDao


    companion object {

        @Volatile
        private var INSTANCE: TourismDatabase? = null


        @JvmStatic
        fun getDatabase(context: Context): TourismDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    TourismDatabase::class.java, "listTourism.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}