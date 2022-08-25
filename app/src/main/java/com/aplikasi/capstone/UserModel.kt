package com.aplikasi.capstone

data class UserModel(
    val id: String,
    val email: String,
    val profile: String?,
    val username: String,
    val token: String,
    val isLogin: Boolean
    )