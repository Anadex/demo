package com.example.geo

enum class CurrentOrientation(val mapAzimuth: Float, val placeInfoViewRotation: Float) {
    UP(0F, 0F),
    DOWN(180F, 180F),
    RIGHT(90F, 270F),
    LEFT(270F, 90F)
}