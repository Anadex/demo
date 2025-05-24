package com.example.retrofit.models

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
@JsonClass(generateAdapter = true)
data class User(
    @Json(name = "results") val results: @RawValue List<Results>
) : Parcelable

@JsonClass(generateAdapter = true)
data class Results(
    @Json(name = "gender") val gender: String,
    @Json(name = "name") val name: Name,
    @Json(name = "location") val location: Location,
    @Json(name = "email") val email: String,
    @Json(name = "dob") val dob: Dob,
    @Json(name = "picture") val picture: Picture,
    @Json(name = "phone") val phone: String
)

@JsonClass(generateAdapter = true)
data class Name(
    @Json(name = "title") val title: String,
    @Json(name = "first") val first: String,
    @Json(name = "last") val last: String
)

@JsonClass(generateAdapter = true)
data class Location(
    @Json(name = "country") val country: String,
    @Json(name = "city") val city: String
)

@JsonClass(generateAdapter = true)
data class Dob(
    @Json(name = "age") val age: String
)

@JsonClass(generateAdapter = true)
data class Picture(
    @Json(name = "large") val large: String
)


