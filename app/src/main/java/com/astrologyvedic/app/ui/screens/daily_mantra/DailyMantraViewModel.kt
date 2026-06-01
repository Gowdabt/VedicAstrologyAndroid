package com.astrologyvedic.app.ui.screens.daily_mantra

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

data class DailyMantraUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val mantra: String = "",
    val meaning: String = "",
    val deity: String = "",
    val reason: String = "",
    val hasResult: Boolean = false
)

@HiltViewModel
class DailyMantraViewModel @Inject constructor(
    private val astrologyRepository: AstrologyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DailyMantraUiState())
    val uiState: StateFlow<DailyMantraUiState> = _uiState.asStateFlow()

    init { loadMantra() }

    fun loadMantra() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val today = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
            val day = SimpleDateFormat("EEEE", Locale.getDefault()).format(Date())
            val request = mapOf("date" to today, "day" to day, "type" to "daily_mantra")
            astrologyRepository.getTools(request)
                .onSuccess { response -> parseResult(response) }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(isLoading = false, error = e.message ?: "Failed to load mantra")
                }
        }
    }

    private fun parseResult(response: JsonObject) {
        try {
            val analysis = response.getAsJsonObject("analysis")
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                mantra = analysis?.get("mantra")?.asString ?: "Om Namah Shivaya",
                meaning = analysis?.get("meaning")?.asString ?: "I bow to Lord Shiva",
                deity = analysis?.get("deity")?.asString ?: "Lord Shiva",
                reason = analysis?.get("reason")?.asString ?: "Based on today's nakshatra and planetary hour",
                hasResult = true
            )
        } catch (_: Exception) {
            _uiState.value = _uiState.value.copy(isLoading = false, error = "Failed to parse results")
        }
    }
}
