package com.astrologyvedic.app.data.api.models

import com.google.gson.annotations.SerializedName

data class NumerologyRequest(
    @SerializedName("name")
    val name: String,
    @SerializedName("dob")
    val dob: String,
    @SerializedName("output_language")
    val outputLanguage: String = "en"
)
