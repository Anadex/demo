package com.example.m16_architecture.data


import com.example.m16_architecture.entity.UsefulActivity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class UsefulActivityDto(
    @Json(name = "activity") override var activity: String
) : UsefulActivity