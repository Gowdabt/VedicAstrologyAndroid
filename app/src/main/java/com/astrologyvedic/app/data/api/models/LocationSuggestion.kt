package com.astrologyvedic.app.data.api.models

import com.google.gson.annotations.SerializedName

data class LocationSuggestion(
    @SerializedName("place_id")
    val placeId: Long,

    @SerializedName("display_name")
    val displayName: String,

    @SerializedName("lat")
    val latitude: String,

    @SerializedName("lon")
    val longitude: String,

    @SerializedName("address")
    val address: Address? = null
) {
    data class Address(
        @SerializedName("city")
        val city: String? = null,

        @SerializedName("state")
        val state: String? = null,

        @SerializedName("country")
        val country: String? = null
    )

    fun toShortDisplayName(): String {
        return address?.let { addr ->
            // If city exists, show "City, State, Country"
            // If no city, extract from displayName (first part before comma)
            val city = addr.city ?: displayName.split(",").firstOrNull()?.trim()
            listOfNotNull(city, addr.state, addr.country)
                .joinToString(", ")
        } ?: displayName.split(",").take(3).joinToString(", ").trim()
    }
}
