package com.aplikasi.capstone

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.google.gson.annotations.SerializedName

data class AllTourismResponse(

	@field:SerializedName("result")
	val result: List<ResultItem>,

	@field:SerializedName("code")
	val code: Int,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)
@Entity(tableName = "category")
data class Category(

	@field:SerializedName("name")
	val name: String,

	@PrimaryKey
	@field:SerializedName("id")
	val id: Int
)
@Entity(tableName = "listTourism")
data class ResultItem(

	@field:SerializedName("thumbnail")
	val thumbnail: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("coordinate")
	val coordinate: String,

	@field:SerializedName("maps")
	val maps: String,

	@field:SerializedName("city")
	val city: String,

	@field:SerializedName("rating")
	val rating: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("time_minutes")
	val timeMinutes: Int,

	@field:SerializedName("updated_at")
	val updatedAt: String,

	//@field:SerializedName("reviews")
	//val reviews: List<Int>,

	@field:SerializedName("price")
	val price: Int,

	@field:SerializedName("name")
	val name: String,

	@PrimaryKey
	@field:SerializedName("id")
	val id: Int,

	//@field:SerializedName("category")
	//val result: List<Category>,

	//@field:SerializedName("user")
	//val user: User
)



data class User(

	@field:SerializedName("profile")
	val profile: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("username")
	val username: String
)
