package com.example.geo.domain

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PlacePoint(
    val lon: Double,
    val lat: Double
)