package com.astrologyvedic.app.data.repository

import com.astrologyvedic.app.data.api.GeocodingApi
import com.astrologyvedic.app.data.api.models.LocationSuggestion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationRepository @Inject constructor(
    private val geocodingApi: GeocodingApi
) {
    suspend fun searchLocations(query: String): Result<List<LocationSuggestion>> {
        return withContext(Dispatchers.IO) {
            try {
                if (query.isBlank() || query.length < 3) {
                    return@withContext Result.success(emptyList())
                }
                val results = geocodingApi.searchLocations(query)
                Result.success(results)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
