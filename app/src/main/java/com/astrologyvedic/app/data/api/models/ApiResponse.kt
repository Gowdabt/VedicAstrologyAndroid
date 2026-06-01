package com.astrologyvedic.app.data.api.models

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

data class ApiResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("analysis")
    val analysis: JsonObject? = null,
    @SerializedName("error")
    val error: String? = null
)
