package com.aplikasi.capstone.response

import com.google.gson.annotations.SerializedName

data class ProfileResponse (

    @field:SerializedName("code")
    val code: Int,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("result")
    val Result: Result
)


data class Result(

    @field:SerializedName("image")
    val image: String
    )