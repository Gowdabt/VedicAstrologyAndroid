package com.astrologyvedic.app.data.api.models

import com.google.gson.annotations.SerializedName

data class GenericRequest(
    @SerializedName("person")
    val person: PersonRequest? = null,
    @SerializedName("date")
    val date: String? = null,
    @SerializedName("type")
    val type: String? = null,
    @SerializedName("output_language")
    val outputLanguage: String = "en"
)
