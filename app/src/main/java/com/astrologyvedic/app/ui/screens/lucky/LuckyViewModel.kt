package com.astrologyvedic.app.ui.screens.lucky

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astrologyvedic.app.data.repository.AstrologyRepository
import com.astrologyvedic.app.data.repository.ProfileRepository
import com.google.gson.JsonObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LuckyUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val luckyNumber: Int = 0,
    val luckyColor: String = "",
    val colorHex: String = "#FF9800",
    val luckyDirection: String = "",
    val luckyDay: String = "",
    val luckyGem: String = "",
    val hasResult: Boolean = false
)

@HiltViewModel
class LuckyViewModel @Inject constructor(
    private val astrologyRepository: AstrologyRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LuckyUiState())
    val uiState: StateFlow<LuckyUiState> = _uiState.asStateFlow()

    init { loadLucky() }

    fun loadLucky() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val profile = profileRepository.getDefaultProfile()
            val request = mapOf(
                "type" to "lucky",
                "name" to (profile?.name ?: ""),
                "dob" to (profile?.dob ?: "")
            )
            astrologyRepository.getTools(request)
                .onSuccess { response -> parseResult(response) }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(isLoading = false, error = e.message ?: "Failed to load lucky info")
                }
        }
    }

    private fun parseResult(response: JsonObject) {
        try {
            val analysis = response.getAsJsonObject("analysis")
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                luckyNumber = analysis?.get("number")?.asInt ?: 7,
                luckyColor = analysis?.get("color")?.asString ?: "Orange",
                colorHex = analysis?.get("color_hex")?.asString ?: "#FF9800",
                luckyDirection = analysis?.get("direction")?.asString ?: "East",
                luckyDay = analysis?.get("day")?.asString ?: "Sunday",
                luckyGem = analysis?.get("gem")?.asString ?: "Ruby",
                hasResult = true
            )
        } catch (_: Exception) {
            _uiState.value = _uiState.value.copy(isLoading = false, error = "Failed to parse results")
        }
    }
}
