package com.astrologyvedic.app.data.api

import com.astrologyvedic.app.data.api.models.LocationSuggestion
import retrofit2.http.GET
import retrofit2.http.Query

interface GeocodingApi {

    @GET("search")
    suspend fun searchLocations(
        @Query("q") query: String,
        @Query("format") format: String = "json",
        @Query("limit") limit: Int = 5,
        @Query("addressdetails") addressDetails: Int = 1
    ): List<LocationSuggestion>
}
