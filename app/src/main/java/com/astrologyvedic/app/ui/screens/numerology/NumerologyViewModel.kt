package com.astrologyvedic.app.ui.screens.numerology

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astrologyvedic.app.data.api.models.NumerologyRequest
import com.astrologyvedic.app.data.repository.AstrologyRepository
import com.google.gson.JsonObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NumerologyNumber(
    val type: String,
    val value: Int,
    val meaning: String
)

data class NumerologyUiState(
    val name: String = "",
    val dob: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val numbers: List<NumerologyNumber> = emptyList(),
    val hasResult: Boolean = false
)

@HiltViewModel
class NumerologyViewModel @Inject constructor(
    private val astrologyRepository: AstrologyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(NumerologyUiState())
    val uiState: StateFlow<NumerologyUiState> = _uiState.asStateFlow()

    fun updateName(name: String) {
        _uiState.value = _uiState.value.copy(name = name)
    }

    fun updateDob(dob: String) {
        _uiState.value = _uiState.value.copy(dob = dob)
    }

    fun calculate() {
        if (_uiState.value.name.isBlank() || _uiState.value.dob.isBlank()) return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val request = NumerologyRequest(name = _uiState.value.name, dob = _uiState.value.dob)
            astrologyRepository.getNumerology(request)
                .onSuccess { response -> parseResult(response) }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to calculate numerology"
                    )
                }
        }
    }

    private fun parseResult(response: JsonObject) {
        try {
            val analysis = response.getAsJsonObject("analysis")
            val numbers = mutableListOf<NumerologyNumber>()
            numbers.add(NumerologyNumber("Life Path", analysis?.get("life_path")?.asInt ?: 0, analysis?.get("life_path_meaning")?.asString ?: "Your life's purpose and direction"))
            numbers.add(NumerologyNumber("Destiny", analysis?.get("destiny")?.asInt ?: 0, analysis?.get("destiny_meaning")?.asString ?: "What you are destined to achieve"))
            numbers.add(NumerologyNumber("Soul Urge", analysis?.get("soul_urge")?.asInt ?: 0, analysis?.get("soul_meaning")?.asString ?: "Your inner desires and motivations"))
            numbers.add(NumerologyNumber("Personality", analysis?.get("personality")?.asInt ?: 0, analysis?.get("personality_meaning")?.asString ?: "How others perceive you"))
            _uiState.value = _uiState.value.copy(isLoading = false, numbers = numbers, hasResult = true)
        } catch (_: Exception) {
            _uiState.value = _uiState.value.copy(isLoading = false, error = "Failed to parse results")
        }
    }
}
