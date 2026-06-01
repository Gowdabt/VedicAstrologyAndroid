package com.astrologyvedic.app.ui.screens.ayanamsa

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astrologyvedic.app.data.repository.AstrologyRepository
import com.google.gson.JsonObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AyanamsaUiState(
    val date: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val lahiriValue: String = "",
    val explanation: String = "",
    val hasResult: Boolean = false
)

@HiltViewModel
class AyanamsaViewModel @Inject constructor(
    private val astrologyRepository: AstrologyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AyanamsaUiState())
    val uiState: StateFlow<AyanamsaUiState> = _uiState.asStateFlow()

    fun updateDate(date: String) { _uiState.value = _uiState.value.copy(date = date) }

    fun calculate() {
        if (_uiState.value.date.isBlank()) return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val request = mapOf("date" to _uiState.value.date, "type" to "ayanamsa")
            astrologyRepository.getTools(request)
                .onSuccess { response -> parseResult(response) }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(isLoading = false, error = e.message ?: "Failed to calculate")
                }
        }
    }

    private fun parseResult(response: JsonObject) {
        try {
            val analysis = response.getAsJsonObject("analysis")
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                lahiriValue = analysis?.get("lahiri_value")?.asString ?: "24d 10' 23\"",
                explanation = analysis?.get("explanation")?.asString
                    ?: "Ayanamsa is the angular difference between the tropical and sidereal zodiacs. The Lahiri ayanamsa is the most commonly used in Vedic astrology.",
                hasResult = true
            )
        } catch (_: Exception) {
            _uiState.value = _uiState.value.copy(isLoading = false, error = "Failed to parse results")
        }
    }
}
