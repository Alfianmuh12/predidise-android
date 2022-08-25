package com.aplikasi.capstone.response

import androidx.room.PrimaryKey
import com.aplikasi.capstone.ResultItem
import com.google.gson.annotations.SerializedName



data class ReviewResponse(
    @field:SerializedName("result")
    val result: List<Review>,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)

data class Review(
    @PrimaryKey
    @field:SerializedName("tourism_id")
    val tourism_id: Int,

    @field:SerializedName("image_user")
    val image_user: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("city")
    val city: String,

    @field:SerializedName("rating")
    val rating: Int,

    @field:SerializedName("review")
    val review: String,

    @field:SerializedName("image")
    val image: String,


    )