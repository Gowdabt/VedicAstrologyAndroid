package com.astrologyvedic.app.data.api.models

import com.google.gson.annotations.SerializedName

data class PanchangRequest(
    @SerializedName("person")
    val person: PersonRequest,
    @SerializedName("date")
    val date: String,
    @SerializedName("output_language")
    val outputLanguage: String = "en"
)
