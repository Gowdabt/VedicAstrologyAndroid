package com.astrologyvedic.app.data.api.models

import com.google.gson.annotations.SerializedName

data class MatchRequest(
    @SerializedName("person1")
    val person1: PersonRequest,
    @SerializedName("person2")
    val person2: PersonRequest,
    @SerializedName("output_language")
    val outputLanguage: String = "en"
)
