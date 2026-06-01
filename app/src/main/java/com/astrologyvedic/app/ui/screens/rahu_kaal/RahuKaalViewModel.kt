package com.astrologyvedic.app.ui.screens.rahu_kaal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astrologyvedic.app.data.api.models.GenericRequest
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

data class RahuKaalTiming(
    val day: String,
    val startTime: String,
    val endTime: String,
    val isToday: Boolean = false
)

data class RahuKaalUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val todayStart: String = "",
    val todayEnd: String = "",
    val weekTimings: List<RahuKaalTiming> = emptyList(),
    val hasResult: Boolean = false
)

@HiltViewModel
class RahuKaalViewModel @Inject constructor(
    private val astrologyRepository: AstrologyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RahuKaalUiState())
    val uiState: StateFlow<RahuKaalUiState> = _uiState.asStateFlow()

    init {
        loadTimings()
    }

    fun loadTimings() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val today = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
            val request = GenericRequest(type = "rahu_kaal", date = today)
            astrologyRepository.getRahuKaal(request)
                .onSuccess { response -> parseResult(response) }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load Rahu Kaal timings"
                    )
                }
        }
    }

    private fun parseResult(response: JsonObject) {
        try {
            val analysis = response.getAsJsonObject("analysis")
            val todayStart = analysis?.get("start")?.asString ?: "07:30"
            val todayEnd = analysis?.get("end")?.asString ?: "09:00"
            val weekTimings = mutableListOf<RahuKaalTiming>()
            val days = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
            val todayDay = SimpleDateFormat("EEEE", Locale.getDefault()).format(Date())

            try {
                val week = analysis?.getAsJsonArray("week")
                week?.forEachIndexed { index, element ->
                    val obj = element.asJsonObject
                    weekTimings.add(RahuKaalTiming(
                        day = obj.get("day")?.asString ?: days.getOrElse(index) { "" },
                        startTime = obj.get("start")?.asString ?: "--:--",
                        endTime = obj.get("end")?.asString ?: "--:--",
                        isToday = obj.get("day")?.asString == todayDay
                    ))
                }
            } catch (_: Exception) {
                days.forEach { day ->
                    weekTimings.add(RahuKaalTiming(day = day, startTime = "--:--", endTime = "--:--", isToday = day == todayDay))
                }
            }

            _uiState.value = _uiState.value.copy(
                isLoading = false, todayStart = todayStart, todayEnd = todayEnd,
                weekTimings = weekTimings, hasResult = true
            )
        } catch (_: Exception) {
            _uiState.value = _uiState.value.copy(isLoading = false, error = "Failed to parse results")
        }
    }
}
