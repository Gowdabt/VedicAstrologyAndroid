package com.astrologyvedic.app.data.repository

import com.astrologyvedic.app.data.api.AstrologyApi
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
class AstrologyRepository(
    private val api: AstrologyApi,
    private val gson: Gson
) {

    private val mediaType = "application/json; charset=utf-8".toMediaType()

    suspend fun getKundli(request: Any): Result<JsonObject> =
        safeApiCall { api.getKundli(request.toRequestBody()) }

    suspend fun getDaily(request: Any): Result<JsonObject> =
        safeApiCall { api.getDaily(request.toRequestBody()) }

    suspend fun getDailyRashifal(request: Any): Result<JsonObject> =
        safeApiCall { api.getDailyRashifal(request.toRequestBody()) }

    suspend fun getPanchang(request: Any): Result<JsonObject> =
        safeApiCall { api.getPanchang(request.toRequestBody()) }

    suspend fun getMatch(request: Any): Result<JsonObject> =
        safeApiCall { api.getMatch(request.toRequestBody()) }

    suspend fun getGunaMilan(request: Any): Result<JsonObject> =
        safeApiCall { api.getGunaMilan(request.toRequestBody()) }

    suspend fun getTimeline(request: Any): Result<JsonObject> =
        safeApiCall { api.getTimeline(request.toRequestBody()) }

    suspend fun getTransitReport(request: Any): Result<JsonObject> =
        safeApiCall { api.getTransitReport(request.toRequestBody()) }

    suspend fun getTools(request: Any): Result<JsonObject> =
        safeApiCall { api.getTools(request.toRequestBody()) }

    suspend fun getSadeSati(request: Any): Result<JsonObject> =
        safeApiCall { api.getSadeSati(request.toRequestBody()) }

    suspend fun getRahuKaal(request: Any): Result<JsonObject> =
        safeApiCall { api.getRahuKaal(request.toRequestBody()) }

    suspend fun getKaalsarp(request: Any): Result<JsonObject> =
        safeApiCall { api.getKaalsarp(request.toRequestBody()) }

    suspend fun getNumerology(request: Any): Result<JsonObject> =
        safeApiCall { api.getNumerology(request.toRequestBody()) }

    suspend fun getMuhurat(request: Any): Result<JsonObject> =
        safeApiCall { api.getMuhurat(request.toRequestBody()) }

    suspend fun getFestivals(request: Any): Result<JsonObject> =
        safeApiCall { api.getFestivals(request.toRequestBody()) }

    suspend fun getLoveCompatibility(request: Any): Result<JsonObject> =
        safeApiCall { api.getLoveCompatibility(request.toRequestBody()) }

    suspend fun getChoghadiya(request: Any): Result<JsonObject> =
        safeApiCall { api.getChoghadiya(request.toRequestBody()) }

    suspend fun getHora(request: Any): Result<JsonObject> =
        safeApiCall { api.getHora(request.toRequestBody()) }

    suspend fun getBabyNames(request: Any): Result<JsonObject> =
        safeApiCall { api.getBabyNames(request.toRequestBody()) }

    suspend fun getPalmReading(request: Any): Result<JsonObject> =
        safeApiCall { api.getPalmReading(request.toRequestBody()) }

    suspend fun getAiChat(request: Any): Result<JsonObject> =
        safeApiCall { api.getAiChat(request.toRequestBody()) }

    suspend fun getGemstone(request: Any): Result<JsonObject> =
        safeApiCall { api.getGemstone(request.toRequestBody()) }

    suspend fun getPujaGuide(request: Any): Result<JsonObject> =
        safeApiCall { api.getPujaGuide(request.toRequestBody()) }

    suspend fun getSunriseSunset(request: Any): Result<JsonObject> =
        safeApiCall { api.getSunriseSunset(request.toRequestBody()) }

    suspend fun getTarot(request: Any): Result<JsonObject> =
        safeApiCall { api.getTarot(request.toRequestBody()) }

    suspend fun getVastu(request: Any): Result<JsonObject> =
        safeApiCall { api.getVastu(request.toRequestBody()) }

    suspend fun getNavamsa(request: Any): Result<JsonObject> =
        safeApiCall { api.getNavamsa(request.toRequestBody()) }

    suspend fun getKpAstrology(request: Any): Result<JsonObject> =
        safeApiCall { api.getKpAstrology(request.toRequestBody()) }

    suspend fun getCelebrityHoroscope(request: Any): Result<JsonObject> =
        safeApiCall { api.getCelebrityHoroscope(request.toRequestBody()) }

    suspend fun getPastLife(request: Any): Result<JsonObject> =
        safeApiCall { api.getPastLife(request.toRequestBody()) }

    suspend fun getLalKitab(request: Any): Result<JsonObject> =
        safeApiCall { api.getLalKitab(request.toRequestBody()) }

    suspend fun getPathfinder(request: Any): Result<JsonObject> =
        safeApiCall { api.getPathfinder(request.toRequestBody()) }

    suspend fun getWesternAstrology(request: Any): Result<JsonObject> =
        safeApiCall { api.getWesternAstrology(request.toRequestBody()) }

    suspend fun getChineseZodiac(request: Any): Result<JsonObject> =
        safeApiCall { api.getChineseZodiac(request.toRequestBody()) }

    suspend fun getDasamsa(request: Any): Result<JsonObject> =
        safeApiCall { api.getDasamsa(request.toRequestBody()) }

    suspend fun healthCheck(): Result<JsonObject> =
        safeApiCall { api.healthCheck() }

    private fun Any.toRequestBody(): okhttp3.RequestBody {
        val json = gson.toJson(this)
        return json.toRequestBody(mediaType)
    }

    private suspend fun safeApiCall(
        apiCall: suspend () -> Response<JsonObject>
    ): Result<JsonObject> {
        return try {
            val response = apiCall()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(body)
                } else {
                    Result.failure(Exception("Empty response body"))
                }
            } else {
                val errorBody = response.errorBody()?.string() ?: "Unknown error"
                Result.failure(Exception("API error ${response.code()}: $errorBody"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
