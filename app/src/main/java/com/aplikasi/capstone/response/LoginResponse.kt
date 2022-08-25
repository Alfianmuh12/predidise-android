package com.aplikasi.capstone.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

    @field:SerializedName("code")
    val code: Int,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("result")
    val loginResult: LoginResult,
)

data class LoginResult(

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("profile")
    val profile: String,

    @field:SerializedName("username")
    val username: String,

    @field:SerializedName("access_token")
    val token: String
)
