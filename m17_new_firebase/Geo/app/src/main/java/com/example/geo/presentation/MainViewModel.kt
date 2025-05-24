package com.example.geo.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geo.domain.CurrentOrientation
import com.example.geo.domain.Place
import com.example.geo.domain.UIState
import com.example.geo.data.RetrofitInstance
import com.yandex.mapkit.ScreenPoint
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.MapWindow
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val coroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, exception ->
        Log.d("INFO", "Handle $exception in CoroutineExceptionHandler")
    }
    var currentCameraPosition: CameraPosition? = null

    private var currentPlaceOfInterestArea: CurrentPlaceOfInterestArea? = null

    private var _placesOfInterest: MutableStateFlow<List<Place>> = MutableStateFlow(emptyList())
    val placesOfInterest: StateFlow<List<Place>> = _placesOfInterest.asStateFlow()

    var currentUIState = UIState()

    private var _uiState = MutableStateFlow(currentUIState)
    val uiState = _uiState.asStateFlow()

    fun changeVisibleInUIState(isVisible: Boolean) {
        val newUIState = currentUIState.copy()
        newUIState.isPlaceInfoViewVisible = isVisible
        currentUIState = newUIState
        _uiState.value = newUIState
    }

    fun changePlaceNameInUIState(placeName: String) {
        val newUIState = currentUIState.copy()
        newUIState.placeName = placeName
        currentUIState = newUIState
        _uiState.value = newUIState
    }

    fun changePlaceInfoViewOrientation(orientation: CurrentOrientation) {
        val newUIState = currentUIState.copy()
        newUIState.placeInfoViewOrientation = orientation
        currentUIState = newUIState
        _uiState.value = newUIState
    }

    fun getPlacesOfInterest(mapWindow: MapWindow) {
        val topLeftWindowPoint = mapWindow.screenToWorld(
            ScreenPoint(0F, 0F)
        ) ?: Point(0.0, 0.0)

        val bottomRightWindowPoint = mapWindow.screenToWorld(
            ScreenPoint(
                mapWindow.width().toFloat(),
                mapWindow.height().toFloat()
            )
        ) ?: Point(0.0, 0.0)

        val longitudeFromMinToMax =
            listOf(topLeftWindowPoint.longitude, bottomRightWindowPoint.longitude).sorted()
        val latitudeFromMinToMax =
            listOf(topLeftWindowPoint.latitude, bottomRightWindowPoint.latitude).sorted()

        currentPlaceOfInterestArea = CurrentPlaceOfInterestArea(
            longitudeFromMinToMax[0],
            longitudeFromMinToMax[1],
            latitudeFromMinToMax[0],
            latitudeFromMinToMax[1]
        )

        viewModelScope.launch(coroutineExceptionHandler) {
            currentPlaceOfInterestArea?.let{ area ->
                _placesOfInterest.emit(
                    RetrofitInstance.placeOfInterestApi.getPlacesOfInterest(
                        area.minLongitude,
                        area.maxLongitude,
                        area.minLatitude,
                        area.maxLatitude
                    ).filter { it.name != "" }
                )
            }
        }
    }

    class CurrentPlaceOfInterestArea(
        val minLongitude: Double,
        val maxLongitude: Double,
        val minLatitude: Double,
        val maxLatitude: Double
    )
}



