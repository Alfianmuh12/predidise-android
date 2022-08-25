package com.aplikasi.capstone.response

import com.google.gson.annotations.SerializedName

data class UserResponse(

	@field:SerializedName("result")
	val result: List<ResultUser>,

	@field:SerializedName("code")
	val code: Int,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class ResultUser(

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("profile")
	val profile: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("username")
	val username: String
)
