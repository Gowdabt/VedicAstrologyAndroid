package com.astrologyvedic.app.ui.screens.sunrise_sunset

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astrologyvedic.app.data.repository.AstrologyRepository
import com.google.gson.JsonObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

data class SunriseSunsetUiState(
    val location: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val sunrise: String = "",
    val sunset: String = "",
    val goldenHourMorning: String = "",
    val goldenHourEvening: String = "",
    val dayLength: String = "",
    val hasResult: Boolean = false
)

@HiltViewModel
class SunriseSunsetViewModel @Inject constructor(
    private val astrologyRepository: AstrologyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SunriseSunsetUiState())
    val uiState: StateFlow<SunriseSunsetUiState> = _uiState.asStateFlow()

    fun updateLocation(location: String) { _uiState.value = _uiState.value.copy(location = location) }

    fun calculate() {
        if (_uiState.value.location.isBlank()) return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val today = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
            val request = mapOf("location" to _uiState.value.location, "date" to today)
            astrologyRepository.getSunriseSunset(request)
                .onSuccess { response -> parseResult(response) }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(isLoading = false, error = e.message ?: "Failed to get timings")
                }
        }
    }

    private fun parseResult(response: JsonObject) {
        try {
            val analysis = response.getAsJsonObject("analysis")
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                sunrise = analysis?.get("sunrise")?.asString ?: "06:00",
                sunset = analysis?.get("sunset")?.asString ?: "18:30",
                goldenHourMorning = analysis?.get("golden_hour_morning")?.asString ?: "06:00 - 07:00",
                goldenHourEvening = analysis?.get("golden_hour_evening")?.asString ?: "17:30 - 18:30",
                dayLength = analysis?.get("day_length")?.asString ?: "12h 30m",
                hasResult = true
            )
        } catch (_: Exception) {
            _uiState.value = _uiState.value.copy(isLoading = false, error = "Failed to parse results")
        }
    }
}
