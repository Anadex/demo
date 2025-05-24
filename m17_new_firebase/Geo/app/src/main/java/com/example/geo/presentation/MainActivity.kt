package com.example.geo.presentation

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color.argb
import android.graphics.PointF
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.OrientationEventListener
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.geo.App
import com.example.geo.R
import com.example.geo.databinding.ActivityMainBinding
import com.example.geo.domain.CurrentOrientation
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraListener
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.MapWindow
import com.yandex.mapkit.map.RotationType
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.user_location.UserLocationLayer
import com.yandex.runtime.image.ImageProvider
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var mapView: MapView
    private lateinit var mapWindow: MapWindow
    private lateinit var map: Map

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var cancellationTokenSource = CancellationTokenSource()

    private lateinit var userLocationLayer: UserLocationLayer

    private val mapObjectTapListener = MapObjectTapListener { mapObject, _ ->
        viewModel.changePlaceNameInUIState(mapObject.userData.toString())
        viewModel.changeVisibleInUIState(true)
        true
    }

    private lateinit var cameraListener: CameraListener

    private val geoLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { map ->
        if (map.values.isNotEmpty() && map.values.all { it }) {
            makeStartMapView()
        } else {
            Toast.makeText(
                this,
                "Необходимо разрешение на определение вашей геопозиции",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private val notifyLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                createNotification()
            } else {
                Toast.makeText(
                    this,
                    "Необходимо разрешение на показ уведомлений",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mapView = binding.mapview
        mapWindow = mapView.mapWindow
        map = mapView.mapWindow.map

        val mapKit = MapKitFactory.getInstance()
        userLocationLayer = mapKit.createUserLocationLayer(mapWindow)
        userLocationLayer.isVisible = true

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val orientationEventListener = createOrientationEventListener()

        if (orientationEventListener.canDetectOrientation()) {
            orientationEventListener.enable()
        } else {
            Log.d("INFO", "Невозможно получить ориентацию экрана")
        }

        cameraListener = createCameraListener()

        makeStartMapView()

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.plusButton.setOnClickListener { changeTheMapScale(2) }

        binding.minusButton.setOnClickListener { changeTheMapScale(-2) }

        binding.locationButton.setOnClickListener { moveToMyLocation() }

        binding.placeInfo.closeButton.setOnClickListener {
            viewModel.changeVisibleInUIState(false)
        }
        binding.crashButton.setOnClickListener {
            throw RuntimeException("Test Crash") // Force a crash
        }
    }

    private fun createOrientationEventListener(): OrientationEventListener {
        return object : OrientationEventListener(this, SensorManager.SENSOR_DELAY_NORMAL) {
            override fun onOrientationChanged(orientation: Int) {
                when (viewModel.currentUIState.placeInfoViewOrientation) {
                    CurrentOrientation.UP -> when (orientation) {
                        in 70..110 -> {
                            viewModel.changePlaceInfoViewOrientation(CurrentOrientation.RIGHT)
                        }

                        in 111..249 -> {
                            viewModel.changePlaceInfoViewOrientation(CurrentOrientation.DOWN)
                        }

                        in 250..290 -> {
                            viewModel.changePlaceInfoViewOrientation(CurrentOrientation.LEFT)
                        }
                    }

                    CurrentOrientation.DOWN -> when (orientation) {
                        in 291..360, in 0..69 -> {
                            viewModel.changePlaceInfoViewOrientation(CurrentOrientation.UP)
                        }

                        in 70..110 -> {
                            viewModel.changePlaceInfoViewOrientation(CurrentOrientation.RIGHT)
                        }

                        in 250..290 -> {
                            viewModel.changePlaceInfoViewOrientation(CurrentOrientation.LEFT)
                        }
                    }

                    CurrentOrientation.RIGHT -> when (orientation) {
                        in 0..30, in 330..360 -> {
                            viewModel.changePlaceInfoViewOrientation(CurrentOrientation.UP)
                        }

                        in 150..210 -> {
                            viewModel.changePlaceInfoViewOrientation(CurrentOrientation.DOWN)
                        }

                        in 211..329 -> {
                            viewModel.changePlaceInfoViewOrientation(CurrentOrientation.LEFT)
                        }
                    }

                    CurrentOrientation.LEFT -> when (orientation) {
                        in 0..30, in 330..360 -> {
                            viewModel.changePlaceInfoViewOrientation(CurrentOrientation.UP)
                        }

                        in 31..149 -> {
                            viewModel.changePlaceInfoViewOrientation(CurrentOrientation.RIGHT)
                        }

                        in 150..210 -> {
                            viewModel.changePlaceInfoViewOrientation(CurrentOrientation.DOWN)
                        }
                    }

                    null -> when (orientation) {
                        in 0..69, in 291..360 -> {
                            viewModel.changePlaceInfoViewOrientation(CurrentOrientation.UP)
                        }

                        in 70..110 -> {
                            viewModel.changePlaceInfoViewOrientation(CurrentOrientation.RIGHT)
                        }

                        in 111..249 -> {
                            viewModel.changePlaceInfoViewOrientation(CurrentOrientation.DOWN)
                        }

                        in 250..290 -> {
                            viewModel.changePlaceInfoViewOrientation(CurrentOrientation.LEFT)
                        }
                    }
                }
            }
        }
    }

    private fun createCameraListener(): CameraListener {
        return CameraListener { _, cameraPosition, _, finished ->
            when (finished) {
                true -> {
                    viewModel.getPlacesOfInterest(mapWindow)
                    viewModel.currentCameraPosition = cameraPosition
                }
                false -> {
                }
            }
        }
    }

    private fun changeTheMapScale(level: Int) {
        map.cameraPosition.let {
            map.move(
                CameraPosition(
                    it.target,
                    it.zoom + level,
                    it.azimuth,
                    it.tilt
                ),
                Animation(Animation.Type.LINEAR, 1f),
                null
            )
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                createNotification()
            } else {
                notifyLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else{
            createNotification()
        }
    }

    private fun moveToMyLocation() {
        if (REQUIRED_PERMISSIONS.all { permission ->
                ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) == PackageManager.PERMISSION_GRANTED
            }) {

            if (viewModel.currentCameraPosition == null) {
                makeStartMapView()
            } else {
                viewModel.currentCameraPosition?.let {
                    fusedLocationClient.getCurrentLocation(
                        Priority.PRIORITY_HIGH_ACCURACY,
                        cancellationTokenSource.token
                    ).addOnSuccessListener { location ->
                        viewModel.currentCameraPosition = CameraPosition(
                            Point(location.latitude, location.longitude),
                            it.zoom,
                            it.azimuth,
                            it.tilt
                        )

                        mapView.mapWindow.map.move(
                            viewModel.currentCameraPosition!!,
                            Animation(Animation.Type.LINEAR, 1f),
                            null
                        )
                    }
                }
            }
        } else {
            Toast.makeText(
                this,
                "Необходимо разрешение на определение вашей геопозиции",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun makeStartMapView() {
        if (REQUIRED_PERMISSIONS.all { permission ->
                ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) == PackageManager.PERMISSION_GRANTED
            }) {
            val pinsCollection = map.mapObjects.addCollection()
            pinsCollection.addTapListener(mapObjectTapListener)

            val imageProvider = ImageProvider.fromResource(this, R.drawable.location)

            val cameraCallback = Map.CameraCallback {
                viewModel.getPlacesOfInterest(mapWindow)
                lifecycleScope.launch {
                    viewModel.uiState.collect { uiState ->
                        binding.placeInfo.placeName.text = uiState.placeName

                        uiState.placeInfoViewOrientation?.let {
                            rotateTheMap(
                                map,
                                it.mapAzimuth
                            )
                            binding.placeInfo.root.rotation =
                                it.placeInfoViewRotation
                        }

                        when (uiState.isPlaceInfoViewVisible) {
                            true -> binding.placeInfo.root.visibility = VISIBLE
                            false -> binding.placeInfo.root.visibility = GONE
                        }

                        map.addCameraListener(cameraListener)
                    }
                }
            }

            if (viewModel.currentCameraPosition == null) {
                fusedLocationClient.getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    cancellationTokenSource.token
                ).addOnSuccessListener { location ->
                    viewModel.currentCameraPosition = CameraPosition(
                        Point(location.latitude, location.longitude),
                        16.0f,
                        0f,
                        0.0f
                    )

                    map.move(
                        viewModel.currentCameraPosition!!,
                        Animation(Animation.Type.LINEAR, 1f),
                        cameraCallback
                    )

                    addPlacesOfInterestToTheMapView(
                        pinsCollection,
                        imageProvider
                    )
                }
            } else {
                map.move(
                    viewModel.currentCameraPosition!!,
                    Animation(Animation.Type.LINEAR, 1f),
                    cameraCallback
                )
                addPlacesOfInterestToTheMapView(
                    pinsCollection,
                    imageProvider
                )
            }
        } else {
            geoLauncher.launch(REQUIRED_PERMISSIONS)
        }
    }

    private fun addPlacesOfInterestToTheMapView(
        pinsCollection: MapObjectCollection,
        imageProvider: ImageProvider
    ) {
        lifecycleScope.launch {
            viewModel.placesOfInterest.collect { places ->
                places.forEach { place ->
                    pinsCollection.addPlacemark().apply {
                        geometry = Point(place.point.lat, place.point.lon)
                        setIcon(imageProvider)
                        userData = place.name
                    }.setIconStyle(
                        IconStyle().apply {
                            anchor = PointF(0.5f, 0.5f)
                            scale = 1f
                            rotationType = RotationType.ROTATE
                        })
                }
            }
        }
    }

    private fun rotateTheMap(map: Map, azimuth: Float) {
        val currentCameraPosition = map.cameraPosition

        map.move(
            CameraPosition(
                currentCameraPosition.target,
                currentCameraPosition.zoom,
                azimuth,
                currentCameraPosition.tilt
            ),
            Animation(Animation.Type.LINEAR, 1f),
            null
        )
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView.onStart()
    }

    override fun onRestart() {
        super.onRestart()
        cancellationTokenSource = CancellationTokenSource()
        if (viewModel.currentCameraPosition == null) makeStartMapView()
    }

    override fun onStop() {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        cancellationTokenSource.cancel()
        super.onStop()
    }

    private fun createNotification() {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val notification = NotificationCompat.Builder(this, App.CHANNEL_ID)
            .setSmallIcon(R.drawable.place_of_interest)
            .setContentTitle("Мое первое уведомление")
            .setContentText("Масштаб карты изменен")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setColor(argb(255, 255, 92, 0))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManagerCompat.from(this).notify(NOTIFICATION_ID, notification)
        } else {
            Toast.makeText(
                this,
                "Необходимо разрешение на отправку уведомлений",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    companion object {
        private val REQUIRED_PERMISSIONS: Array<String> = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        private const val NOTIFICATION_ID = 1
    }
}









