package com.astrologyvedic.app.ui.screens.guna_milan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astrologyvedic.app.data.api.models.GunaMilanRequest
import com.astrologyvedic.app.data.repository.AstrologyRepository
import com.google.gson.JsonObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class GunaMilanUiState(
    val nakshatra1: String = "", val nakshatra2: String = "",
    val isLoading: Boolean = false, val error: String? = null,
    val totalScore: Int = 0, val maxScore: Int = 36,
    val verdict: String = "", val hasResult: Boolean = false
)

@HiltViewModel
class GunaMilanViewModel @Inject constructor(private val astrologyRepository: AstrologyRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(GunaMilanUiState())
    val uiState: StateFlow<GunaMilanUiState> = _uiState.asStateFlow()
    val nakshatras = listOf("Ashwini", "Bharani", "Krittika", "Rohini", "Mrigashira", "Ardra", "Punarvasu", "Pushya", "Ashlesha", "Magha", "Purva Phalguni", "Uttara Phalguni", "Hasta", "Chitra", "Swati", "Vishakha", "Anuradha", "Jyeshtha", "Moola", "Purva Ashadha", "Uttara Ashadha", "Shravana", "Dhanishta", "Shatabhisha", "Purva Bhadrapada", "Uttara Bhadrapada", "Revati")
    fun updateNakshatra1(n: String) { _uiState.value = _uiState.value.copy(nakshatra1 = n) }
    fun updateNakshatra2(n: String) { _uiState.value = _uiState.value.copy(nakshatra2 = n) }
    fun calculate() {
        if (_uiState.value.nakshatra1.isBlank() || _uiState.value.nakshatra2.isBlank()) return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            astrologyRepository.getGunaMilan(GunaMilanRequest(bride = com.astrologyvedic.app.data.api.models.NakshatraInfo(nakshatra = _uiState.value.nakshatra1), groom = com.astrologyvedic.app.data.api.models.NakshatraInfo(nakshatra = _uiState.value.nakshatra2)))
                .onSuccess { r -> val a = r.getAsJsonObject("analysis"); val score = a?.get("total_score")?.asInt ?: 0; _uiState.value = _uiState.value.copy(isLoading = false, totalScore = score, verdict = when { score >= 28 -> "Excellent Match"; score >= 21 -> "Good Match"; score >= 14 -> "Average"; else -> "Below Average" }, hasResult = true) }
                .onFailure { e -> _uiState.value = _uiState.value.copy(isLoading = false, error = e.message ?: "Failed") }
        }
    }
}
