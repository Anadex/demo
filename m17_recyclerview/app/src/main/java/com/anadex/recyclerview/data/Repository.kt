package com.anadex.recyclerview.data

import kotlinx.coroutines.delay

class Repository {
    suspend fun getPhotoList(page: Int): List<PhotoDTO> {
        delay(2000)
        val photos = RetrofitInstance.nasaApi.getMarsPhotos(page = page).photos
        return photos.map {
            PhotoDTO(
                id = it.id,
                roverName = it.rover.name,
                cameraName = it.camera.name,
                sol = it.sol,
                earthDate = it.earthDate,
                imgSrc = it.imgSrc
            )
        }
    }
}