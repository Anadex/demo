package com.anadex.recyclerview.data

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize


@JsonClass(generateAdapter = true)
data class Photos(
    @Json(name = "photos") val photos: List<Photo>
)

@JsonClass(generateAdapter = true)
data class Photo(
    @Json(name = "id") val id: Int,
    @Json(name = "rover") val rover: Rover,
    @Json(name = "camera") val camera: Camera,
    @Json(name = "sol") val sol: Int,
    @Json(name = "earth_date") val earthDate: String,
    @Json(name = "img_src") var imgSrc: String
){
    init {
        imgSrc = imgSrc.replace("http:", "https:")
    }
}

@JsonClass(generateAdapter = true)
data class Rover(
    @Json(name = "name") val name: String
)

@JsonClass(generateAdapter = true)
data class Camera(
    @Json(name = "name") val name: String
)

@Parcelize
data class PhotoDTO(
    val id: Int,
    val roverName: String,
    val cameraName: String,
    val sol: Int,
    val earthDate: String,
    val imgSrc: String
) : Parcelable