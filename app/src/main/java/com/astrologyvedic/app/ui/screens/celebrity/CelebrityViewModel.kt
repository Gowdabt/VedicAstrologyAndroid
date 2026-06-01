package com.astrologyvedic.app.ui.screens.celebrity

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

data class CelebrityInfo(val name: String, val dob: String, val sunSign: String, val moonSign: String, val ascendant: String)

data class CelebrityUiState(val isLoading: Boolean = false, val error: String? = null, val celebrities: List<CelebrityInfo> = emptyList(), val hasResult: Boolean = false)

@HiltViewModel
class CelebrityViewModel @Inject constructor(private val astrologyRepository: AstrologyRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(CelebrityUiState())
    val uiState: StateFlow<CelebrityUiState> = _uiState.asStateFlow()
    init { loadCelebrities() }
    fun loadCelebrities() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            astrologyRepository.getCelebrityHoroscope(mapOf("type" to "list"))
                .onSuccess { r -> val list = mutableListOf<CelebrityInfo>(); try { r.getAsJsonObject("analysis")?.getAsJsonArray("celebrities")?.forEach { el -> val o = el.asJsonObject; list.add(CelebrityInfo(name = o.get("name")?.asString ?: "", dob = o.get("dob")?.asString ?: "", sunSign = o.get("sun_sign")?.asString ?: "", moonSign = o.get("moon_sign")?.asString ?: "", ascendant = o.get("ascendant")?.asString ?: "")) } } catch (_: Exception) {}; _uiState.value = _uiState.value.copy(isLoading = false, celebrities = list, hasResult = true) }
                .onFailure { e -> _uiState.value = _uiState.value.copy(isLoading = false, error = e.message ?: "Failed") }
        }
    }
}
