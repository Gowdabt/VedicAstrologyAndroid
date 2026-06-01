package com.astrologyvedic.app.data.api

import com.google.gson.JsonObject
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AstrologyApi {

    @POST("kundli")
    suspend fun getKundli(@Body body: RequestBody): Response<JsonObject>

    @POST("daily")
    suspend fun getDaily(@Body body: RequestBody): Response<JsonObject>

    @POST("daily-rashifal")
    suspend fun getDailyRashifal(@Body body: RequestBody): Response<JsonObject>

    @POST("panchang")
    suspend fun getPanchang(@Body body: RequestBody): Response<JsonObject>

    @POST("match")
    suspend fun getMatch(@Body body: RequestBody): Response<JsonObject>

    @POST("guna-milan")
    suspend fun getGunaMilan(@Body body: RequestBody): Response<JsonObject>

    @POST("timeline")
    suspend fun getTimeline(@Body body: RequestBody): Response<JsonObject>

    @POST("transit-report")
    suspend fun getTransitReport(@Body body: RequestBody): Response<JsonObject>

    @POST("tools")
    suspend fun getTools(@Body body: RequestBody): Response<JsonObject>

    @POST("sade-sati")
    suspend fun getSadeSati(@Body body: RequestBody): Response<JsonObject>

    @POST("rahu-kaal")
    suspend fun getRahuKaal(@Body body: RequestBody): Response<JsonObject>

    @POST("kaalsarp")
    suspend fun getKaalsarp(@Body body: RequestBody): Response<JsonObject>

    @POST("numerology")
    suspend fun getNumerology(@Body body: RequestBody): Response<JsonObject>

    @POST("muhurat")
    suspend fun getMuhurat(@Body body: RequestBody): Response<JsonObject>

    @POST("festivals")
    suspend fun getFestivals(@Body body: RequestBody): Response<JsonObject>

    @POST("love-compatibility")
    suspend fun getLoveCompatibility(@Body body: RequestBody): Response<JsonObject>

    @POST("choghadiya")
    suspend fun getChoghadiya(@Body body: RequestBody): Response<JsonObject>

    @POST("hora")
    suspend fun getHora(@Body body: RequestBody): Response<JsonObject>

    @POST("baby-names")
    suspend fun getBabyNames(@Body body: RequestBody): Response<JsonObject>

    @POST("palm-reading")
    suspend fun getPalmReading(@Body body: RequestBody): Response<JsonObject>

    @POST("ai-chat")
    suspend fun getAiChat(@Body body: RequestBody): Response<JsonObject>

    @POST("gemstone")
    suspend fun getGemstone(@Body body: RequestBody): Response<JsonObject>

    @POST("puja-guide")
    suspend fun getPujaGuide(@Body body: RequestBody): Response<JsonObject>

    @POST("sunrise-sunset")
    suspend fun getSunriseSunset(@Body body: RequestBody): Response<JsonObject>

    @POST("tarot")
    suspend fun getTarot(@Body body: RequestBody): Response<JsonObject>

    @POST("vastu")
    suspend fun getVastu(@Body body: RequestBody): Response<JsonObject>

    @POST("navamsa")
    suspend fun getNavamsa(@Body body: RequestBody): Response<JsonObject>

    @POST("kp-astrology")
    suspend fun getKpAstrology(@Body body: RequestBody): Response<JsonObject>

    @POST("celebrity-horoscope")
    suspend fun getCelebrityHoroscope(@Body body: RequestBody): Response<JsonObject>

    @POST("past-life")
    suspend fun getPastLife(@Body body: RequestBody): Response<JsonObject>

    @POST("lal-kitab")
    suspend fun getLalKitab(@Body body: RequestBody): Response<JsonObject>

    @POST("pathfinder")
    suspend fun getPathfinder(@Body body: RequestBody): Response<JsonObject>

    @POST("western-astrology")
    suspend fun getWesternAstrology(@Body body: RequestBody): Response<JsonObject>

    @POST("chinese-zodiac")
    suspend fun getChineseZodiac(@Body body: RequestBody): Response<JsonObject>

    @POST("dasamsa")
    suspend fun getDasamsa(@Body body: RequestBody): Response<JsonObject>

    @GET("health")
    suspend fun healthCheck(): Response<JsonObject>
}
