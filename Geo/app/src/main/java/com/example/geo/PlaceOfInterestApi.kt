package com.example.geo

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

private const val BASE_URL = "https://api.opentripmap.com"
private const val API_KEY = "5ae2e3f221c38a28845f05b6bb3925bdaab368f3646991e000edc3fc"

object RetrofitInstance {
    private val retrofit = Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(
        MoshiConverterFactory.create()
    ).build()

    val placeOfInterestApi: PlaceOfInterestApi = retrofit.create(PlaceOfInterestApi::class.java)
}

interface PlaceOfInterestApi {
    @Headers("accept: application/json")

    @GET("/0.1/ru/places/bbox?format=json&limit=100&apikey=${API_KEY}")
    suspend fun getPlacesOfInterest(
        @Query("lon_min") lonMin: Double,
        @Query("lon_max") lonMax: Double,
        @Query("lat_min") latMin: Double,
        @Query("lat_max") latMax: Double
    ): List<Place>
}