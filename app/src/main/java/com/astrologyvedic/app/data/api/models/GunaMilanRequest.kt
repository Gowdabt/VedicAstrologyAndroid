package com.astrologyvedic.app.data.api.models

import com.google.gson.annotations.SerializedName

data class GunaMilanRequest(
    @SerializedName("bride")
    val bride: NakshatraInfo,
    @SerializedName("groom")
    val groom: NakshatraInfo,
    @SerializedName("output_language")
    val outputLanguage: String = "en"
)

data class NakshatraInfo(
    @SerializedName("nakshatra")
    val nakshatra: String
)
