package com.astrologyvedic.app.ui.screens.chinese_zodiac

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

data class ChineseZodiacUiState(
    val dob: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val animal: String = "", val element: String = "", val yinYang: String = "",
    val traits: String = "", val compatibility: String = "",
    val hasResult: Boolean = false
)

@HiltViewModel
class ChineseZodiacViewModel @Inject constructor(
    private val astrologyRepository: AstrologyRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ChineseZodiacUiState())
    val uiState: StateFlow<ChineseZodiacUiState> = _uiState.asStateFlow()
    fun updateDob(dob: String) { _uiState.value = _uiState.value.copy(dob = dob) }
    fun calculate() {
        if (_uiState.value.dob.isBlank()) return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            astrologyRepository.getChineseZodiac(mapOf("dob" to _uiState.value.dob))
                .onSuccess { r -> parseResult(r) }
                .onFailure { e -> _uiState.value = _uiState.value.copy(isLoading = false, error = e.message ?: "Failed") }
        }
    }
    private fun parseResult(response: JsonObject) {
        try {
            val a = response.getAsJsonObject("analysis")
            _uiState.value = _uiState.value.copy(isLoading = false, animal = a?.get("animal")?.asString ?: "", element = a?.get("element")?.asString ?: "", yinYang = a?.get("yin_yang")?.asString ?: "", traits = a?.get("traits")?.asString ?: "", compatibility = a?.get("compatibility")?.asString ?: "", hasResult = true)
        } catch (_: Exception) { _uiState.value = _uiState.value.copy(isLoading = false, error = "Failed to parse") }
    }
}
