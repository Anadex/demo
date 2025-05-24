package com.anadex.recyclerview.data


import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://api.nasa.gov/"
private const val API_KEY = "api_key=nTjeHsueL8MQcMPJWgrbczkBFQfoSVQLirbhpCmP"

object RetrofitInstance {
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    val nasaApi: NasaApi = retrofit.create(NasaApi::class.java)
}

interface NasaApi {
    @GET("mars-photos/api/v1/rovers/curiosity/photos?$API_KEY")
    suspend fun getMarsPhotos(@Query("sol") sol: Int = 1000, @Query("page") page: Int): Photos
}