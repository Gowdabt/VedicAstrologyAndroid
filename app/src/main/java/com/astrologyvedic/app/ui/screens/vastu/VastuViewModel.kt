package com.astrologyvedic.app.ui.screens.vastu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astrologyvedic.app.data.repository.AstrologyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class VastuUiState(val propertyType: String = "", val facingDirection: String = "", val isLoading: Boolean = false, val error: String? = null, val analysis: String = "", val tips: List<String> = emptyList(), val hasResult: Boolean = false)

@HiltViewModel
class VastuViewModel @Inject constructor(private val astrologyRepository: AstrologyRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(VastuUiState())
    val uiState: StateFlow<VastuUiState> = _uiState.asStateFlow()
    val propertyTypes = listOf("House", "Apartment", "Office", "Shop", "Factory", "Plot")
    val directions = listOf("North", "South", "East", "West", "North-East", "North-West", "South-East", "South-West")
    fun updatePropertyType(t: String) { _uiState.value = _uiState.value.copy(propertyType = t) }
    fun updateDirection(d: String) { _uiState.value = _uiState.value.copy(facingDirection = d) }
    fun calculate() {
        if (_uiState.value.propertyType.isBlank() || _uiState.value.facingDirection.isBlank()) return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            astrologyRepository.getVastu(mapOf("property_type" to _uiState.value.propertyType, "facing" to _uiState.value.facingDirection))
                .onSuccess { r -> val a = r.getAsJsonObject("analysis"); val tips = mutableListOf<String>(); try { a?.getAsJsonArray("tips")?.forEach { tips.add(it.asString) } } catch (_: Exception) {}; _uiState.value = _uiState.value.copy(isLoading = false, analysis = a?.get("analysis")?.asString ?: "", tips = tips, hasResult = true) }
                .onFailure { e -> _uiState.value = _uiState.value.copy(isLoading = false, error = e.message ?: "Failed") }
        }
    }
}
