package com.astrologyvedic.app.ui.screens.baby_names

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

data class BabyName(val name: String, val meaning: String, val origin: String = "")

data class BabyNamesUiState(
    val selectedNakshatra: String = "", val selectedGender: String = "Boy",
    val isLoading: Boolean = false, val error: String? = null,
    val names: List<BabyName> = emptyList(), val hasResult: Boolean = false
)

@HiltViewModel
class BabyNamesViewModel @Inject constructor(private val astrologyRepository: AstrologyRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(BabyNamesUiState())
    val uiState: StateFlow<BabyNamesUiState> = _uiState.asStateFlow()
    val nakshatras = listOf("Ashwini", "Bharani", "Krittika", "Rohini", "Mrigashira", "Ardra", "Punarvasu", "Pushya", "Ashlesha", "Magha", "Purva Phalguni", "Uttara Phalguni", "Hasta", "Chitra", "Swati", "Vishakha", "Anuradha", "Jyeshtha", "Moola", "Purva Ashadha", "Uttara Ashadha", "Shravana", "Dhanishta", "Shatabhisha", "Purva Bhadrapada", "Uttara Bhadrapada", "Revati")
    fun selectNakshatra(n: String) { _uiState.value = _uiState.value.copy(selectedNakshatra = n) }
    fun selectGender(g: String) { _uiState.value = _uiState.value.copy(selectedGender = g) }
    fun search() {
        if (_uiState.value.selectedNakshatra.isBlank()) return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            astrologyRepository.getBabyNames(mapOf("nakshatra" to _uiState.value.selectedNakshatra, "gender" to _uiState.value.selectedGender))
                .onSuccess { r -> val names = mutableListOf<BabyName>(); try { r.getAsJsonObject("analysis")?.getAsJsonArray("names")?.forEach { el -> val o = el.asJsonObject; names.add(BabyName(name = o.get("name")?.asString ?: "", meaning = o.get("meaning")?.asString ?: "", origin = o.get("origin")?.asString ?: "")) } } catch (_: Exception) {}; _uiState.value = _uiState.value.copy(isLoading = false, names = names, hasResult = true) }
                .onFailure { e -> _uiState.value = _uiState.value.copy(isLoading = false, error = e.message ?: "Failed") }
        }
    }
}
