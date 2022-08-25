package com.aplikasi.capstone.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "favorite_user")
data class FavoriteUser(
    val username: String,

    @PrimaryKey
    val id: Int?,
    val avatar_url: String,
    val description: String,
    val location: String,
    val price: String,
    val rating: String

) : Serializable
