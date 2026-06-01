package com.astrologyvedic.app.ui.screens.name_match

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

data class NameMatchUiState(
    val name1: String = "", val name2: String = "",
    val isLoading: Boolean = false, val error: String? = null,
    val compatibilityPercent: Int = 0, val analysis: String = "",
    val hasResult: Boolean = false
)

@HiltViewModel
class NameMatchViewModel @Inject constructor(private val astrologyRepository: AstrologyRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(NameMatchUiState())
    val uiState: StateFlow<NameMatchUiState> = _uiState.asStateFlow()
    fun updateName1(n: String) { _uiState.value = _uiState.value.copy(name1 = n) }
    fun updateName2(n: String) { _uiState.value = _uiState.value.copy(name2 = n) }
    fun calculate() {
        if (_uiState.value.name1.isBlank() || _uiState.value.name2.isBlank()) return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            astrologyRepository.getTools(mapOf("name1" to _uiState.value.name1, "name2" to _uiState.value.name2, "type" to "name_match"))
                .onSuccess { r -> val a = r.getAsJsonObject("analysis"); _uiState.value = _uiState.value.copy(isLoading = false, compatibilityPercent = a?.get("percentage")?.asInt ?: 0, analysis = a?.get("analysis")?.asString ?: "", hasResult = true) }
                .onFailure { e -> _uiState.value = _uiState.value.copy(isLoading = false, error = e.message ?: "Failed") }
        }
    }
}
