package com.example.retrofit.models


import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers


private const val BASE_URL = "https://randomuser.me/"

interface UserApi {
    @Headers(
        "Accept: application/json",
        "Content-type: application/json"
    )
    @GET("api/")
    fun getUser(): Call<User>
}

object RetrofitInstance {
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    val userApi: UserApi = retrofit.create(UserApi::class.java)
}
