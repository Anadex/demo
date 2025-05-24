package com.example.geo

data class UIState (
    var isPlaceInfoViewVisible: Boolean = false,
    var placeInfoViewOrientation: CurrentOrientation? = null,
    var placeName: String = ""
)