package com.astrologyvedic.app.data.api.models

import com.google.gson.annotations.SerializedName

data class PersonRequest(
    @SerializedName("name")
    val name: String,
    @SerializedName("dob")
    val dob: String,
    @SerializedName("time")
    val time: String,
    @SerializedName("place")
    val place: String,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("timezone")
    val timezone: String,
    @SerializedName("dst_correction")
    val dstCorrection: Int = 0,
    @SerializedName("ayanamsa")
    val ayanamsa: String = "lahiri",
    @SerializedName("chart_style")
    val chartStyle: String = "north_indian"
)
