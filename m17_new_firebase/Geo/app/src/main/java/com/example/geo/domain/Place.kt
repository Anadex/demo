package com.example.geo.domain

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Place(
    val name: String,
    val point: PlacePoint
)


