package com.astrologyvedic.app.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.astrologyvedic.app.data.api.GeocodingApi
import com.astrologyvedic.app.data.api.models.LocationSuggestion
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

/**
 * Result of GPS location detection
 */
sealed class GpsLocationResult {
    data class Success(
        val placeName: String,
        val latitude: String,
        val longitude: String
    ) : GpsLocationResult()

    data class Error(val message: String) : GpsLocationResult()
}

/**
 * Helper class for GPS location detection and reverse geocoding
 */
@Singleton
class LocationHelper @Inject constructor(
    private val geocodingApi: GeocodingApi
) {
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var permissionLauncher: ActivityResultLauncher<Array<String>>? = null
    private var pendingLocationCallback: ((GpsLocationResult) -> Unit)? = null

    /**
     * Initialize location permission launcher
     * Must be called from Activity.onCreate before Activity is in STARTED state
     */
    fun initializePermissionLauncher(activity: ComponentActivity) {
        permissionLauncher = activity.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val fineLocation = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
            val coarseLocation = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

            if (fineLocation || coarseLocation) {
                // Permission granted, try to get location
                pendingLocationCallback?.let { callback ->
                    requestCurrentLocation(activity, callback)
                }
            } else {
                // Permission denied
                pendingLocationCallback?.invoke(
                    GpsLocationResult.Error("Location permission denied. Please enable location access in settings.")
                )
                pendingLocationCallback = null
            }
        }
    }

    /**
     * Check if location permissions are granted
     */
    fun hasLocationPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Request location permission if not granted
     */
    fun requestLocationPermission() {
        permissionLauncher?.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    /**
     * Get current GPS location and reverse geocode to place name
     * This will request permission if not granted
     */
    fun getCurrentLocation(context: Context, callback: (GpsLocationResult) -> Unit) {
        if (hasLocationPermission(context)) {
            requestCurrentLocation(context, callback)
        } else {
            // Store callback for after permission is granted
            pendingLocationCallback = callback
            requestLocationPermission()
        }
    }

    /**
     * Internal method to get location after permission is granted
     */
    private fun requestCurrentLocation(context: Context, callback: (GpsLocationResult) -> Unit) {
        if (fusedLocationClient == null) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        }

        try {
            // First try to get last known location
            fusedLocationClient?.lastLocation?.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    // Got cached location
                    reverseGeocodeAndCallback(location.latitude, location.longitude, callback)
                } else {
                    // No cached location, request fresh location
                    requestFreshLocation(context, callback)
                }
            }?.addOnFailureListener {
                // Failed to get last location, try fresh
                requestFreshLocation(context, callback)
            }
        } catch (e: SecurityException) {
            callback(GpsLocationResult.Error("Location permission not granted"))
        } catch (e: Exception) {
            callback(GpsLocationResult.Error("Failed to get location: ${e.message}"))
        }
    }

    /**
     * Request fresh GPS location
     */
    private fun requestFreshLocation(context: Context, callback: (GpsLocationResult) -> Unit) {
        try {
            val locationRequest = LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY,
                10000L // 10 seconds
            ).apply {
                setMinUpdateIntervalMillis(5000L) // 5 seconds
                setMaxUpdates(1)
            }.build()

            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    locationResult.lastLocation?.let { location ->
                        reverseGeocodeAndCallback(location.latitude, location.longitude, callback)
                        fusedLocationClient?.removeLocationUpdates(this)
                    } ?: run {
                        callback(GpsLocationResult.Error("Unable to determine location. Please try again."))
                    }
                }
            }

            fusedLocationClient?.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )

        } catch (e: SecurityException) {
            callback(GpsLocationResult.Error("Location permission not granted"))
        } catch (e: Exception) {
            callback(GpsLocationResult.Error("Failed to get location: ${e.message}"))
        }
    }

    /**
     * Reverse geocode coordinates to place name using Nominatim API
     */
    private fun reverseGeocodeAndCallback(
        latitude: Double,
        longitude: Double,
        callback: (GpsLocationResult) -> Unit
    ) {
        // Launch coroutine for network call
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val locationSuggestion = reverseGeocode(latitude, longitude)
                if (locationSuggestion != null) {
                    // Extract city name from the location
                    val placeName = locationSuggestion.address?.city
                        ?: locationSuggestion.displayName.split(",").firstOrNull()?.trim()
                        ?: "Unknown Location"

                    callback(
                        GpsLocationResult.Success(
                            placeName = placeName,
                            latitude = String.format("%.4f", latitude),
                            longitude = String.format("%.4f", longitude)
                        )
                    )
                } else {
                    callback(
                        GpsLocationResult.Success(
                            placeName = "Unknown Location",
                            latitude = String.format("%.4f", latitude),
                            longitude = String.format("%.4f", longitude)
                        )
                    )
                }
            } catch (e: Exception) {
                // Even if reverse geocoding fails, return coordinates
                callback(
                    GpsLocationResult.Success(
                        placeName = "Unknown Location",
                        latitude = String.format("%.4f", latitude),
                        longitude = String.format("%.4f", longitude)
                    )
                )
            }
        }
    }

    /**
     * Reverse geocode coordinates to place name using Nominatim API
     * Returns LocationSuggestion or null if failed
     */
    suspend fun reverseGeocode(latitude: Double, longitude: Double): LocationSuggestion? {
        return withContext(Dispatchers.IO) {
            try {
                val query = String.format("%.6f,%.6f", latitude, longitude)
                val results = geocodingApi.searchLocations(
                    query = query,
                    format = "json",
                    limit = 1,
                    addressDetails = 1
                )
                results.firstOrNull()
            } catch (e: Exception) {
                null
            }
        }
    }

    /**
     * Suspend version of getCurrentLocation for use in coroutines
     */
    suspend fun getCurrentLocationSuspend(context: Context): GpsLocationResult = suspendCancellableCoroutine { continuation ->
        getCurrentLocation(context) { result ->
            continuation.resume(result)
        }
    }

    /**
     * Clean up resources
     */
    fun cleanup() {
        fusedLocationClient = null
        permissionLauncher = null
        pendingLocationCallback = null
    }
}
