package com.astrologyvedic.app.data.api.models

import com.google.gson.annotations.SerializedName

data class KundliRequest(
    @SerializedName("person")
    val person: PersonRequest,
    @SerializedName("output_language")
    val outputLanguage: String = "en"
)
