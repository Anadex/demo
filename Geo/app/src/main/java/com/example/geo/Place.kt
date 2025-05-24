package com.example.geo

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Place(
    val name: String,
    val point: PlacePoint
)

@JsonClass(generateAdapter = true)
data class PlacePoint(
    val lon: Double,
    val lat: Double
)
