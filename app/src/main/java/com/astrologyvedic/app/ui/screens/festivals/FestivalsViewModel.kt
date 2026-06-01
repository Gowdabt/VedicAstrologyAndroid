package com.astrologyvedic.app.ui.screens.festivals

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

data class FestivalInfo(
    val name: String,
    val date: String,
    val significance: String
)

data class FestivalsUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val festivals: List<FestivalInfo> = emptyList(),
    val hasResult: Boolean = false
)

@HiltViewModel
class FestivalsViewModel @Inject constructor(
    private val astrologyRepository: AstrologyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FestivalsUiState())
    val uiState: StateFlow<FestivalsUiState> = _uiState.asStateFlow()

    init { loadFestivals() }

    fun loadFestivals() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            astrologyRepository.getFestivals(mapOf("year" to "2026"))
                .onSuccess { response -> parseResult(response) }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(isLoading = false, error = e.message ?: "Failed to load festivals")
                }
        }
    }

    private fun parseResult(response: JsonObject) {
        try {
            val festivals = mutableListOf<FestivalInfo>()
            val array = response.getAsJsonObject("analysis")?.getAsJsonArray("festivals")
            array?.forEach { element ->
                val obj = element.asJsonObject
                festivals.add(FestivalInfo(
                    name = obj.get("name")?.asString ?: "",
                    date = obj.get("date")?.asString ?: "",
                    significance = obj.get("significance")?.asString ?: ""
                ))
            }
            _uiState.value = _uiState.value.copy(isLoading = false, festivals = festivals, hasResult = true)
        } catch (_: Exception) {
            _uiState.value = _uiState.value.copy(isLoading = false, error = "Failed to parse results")
        }
    }
}
