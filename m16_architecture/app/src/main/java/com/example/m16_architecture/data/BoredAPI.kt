package com.example.m16_architecture.data

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers

private const val BASE_URL = "https://www.boredapi.com/api/"

object RetrofitInstance {
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    val boredApi: BoredApi = retrofit.create(BoredApi::class.java)
}

interface BoredApi {
    @Headers(
        "Accept: application/json",
        "Content-type: application/json"
    )

    @GET("activity/")
    suspend fun getUsefulActivity(): UsefulActivityDto
}


