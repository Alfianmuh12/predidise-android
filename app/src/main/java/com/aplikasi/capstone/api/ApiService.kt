package com.aplikasi.capstone.api

import com.aplikasi.capstone.AllTourismResponse
import com.aplikasi.capstone.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("http://34.126.178.52/api/auth/login")
    @Headers("Accept: application/json")
    fun loginAccount(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("http://34.126.178.52/api/auth/register")
    @Headers("Accept: application/json")
    fun registerAccount(
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    @Multipart
    @POST("http://34.126.178.52/api/users/profile")
    @Headers("Accept: application/json")
    fun uploadProfile(
        @Header("Authorization") header: String,
        @Part file: MultipartBody.Part,
    ): Call<ProfileResponse>

    @GET("http://34.126.178.52/api/tourisms")
    @Headers("Accept: application/json")
    fun getAllTourism(
        @Header("Authorization") header: String
    ): Call<AllTourismResponse>

    @GET("http://34.126.178.52/api/tourisms")
    @Headers("Accept: application/json")
    fun getSeacrhTourism(
        @Header("Authorization") header: String,
        @Query("name") query: String
    ): Call<AllTourismResponse>


    @GET("http://34.126.178.52/api/tourisms")
    @Headers("Accept: application/json")
    fun getAllTourismPaging(
        @Header("Authorization") header: String
    ): AllTourismResponse



    @GET("http://34.126.178.52/api/users/me")
    @Headers("Accept: application/json")
    fun getUserTourism(
        @Header("Authorization") header: String
    ): Call<UserResponse>

    @GET("http://34.126.178.52/api/tourisms?category=Taman Hiburan")
    @Headers("Accept: application/json")
    fun getNatureTourism(
        @Header("Authorization") header: String
    ): Call<AllTourismResponse>

    @GET("http://34.126.178.52/api/tourisms?category=Budaya")
    @Headers("Accept: application/json")
    fun getHistory(
        @Header("Authorization") header: String
    ): Call<AllTourismResponse>

    @Multipart
    @POST("http://34.126.178.52/api/reviews")
    @Headers("Accept: application/json")
    fun addReview(
        @Part ("tourism_id") tourism_id: RequestBody,
        @Part("rating") rating: RequestBody,
        @Part("review") review: RequestBody,
        @Part file: MultipartBody.Part,
        @Header("Authorization") header: String
    ): Call<UploadResponse>


    @GET("http://34.126.178.52/api/reviews")
    @Headers("Accept: application/json")
    fun getReview(
        @Query("tourism_id") tourism_id: String,
        @Header("Authorization") header: String
    ): Call<ReviewResponse>

    @Multipart
    @POST("http://34.126.178.52/api/tourisms")
    @Headers("Accept: application/json")
    fun createTourism(
        @Part("name") name: RequestBody,
        @Part("description") description: RequestBody,
        @Part("category") category: RequestBody,
        @Part("city") city: RequestBody,
        @Part("price") price: RequestBody,
        @Part("rating") rating: RequestBody,
        @Part("time_minutes") time_minutes: RequestBody,
        @Part("coodinate") coodinate: RequestBody,
        @Part thumbnail: MultipartBody.Part,
        @Header("Authorization") header: String
    ): Call<UploadResponse>


    @GET("http://34.126.178.52/api/tourisms/recomendation")
    @Headers("Accept: application/json")
    fun getAllTourismRecomendation(
        @Header("Authorization") header: String
    ): Call<AllTourismResponse>



}