package com.astrologyvedic.app.ui.screens.stock_astrology

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astrologyvedic.app.data.repository.AstrologyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SectorPrediction(val sector: String, val outlook: String, val trend: String)
data class StockAstrologyUiState(val isLoading: Boolean = false, val error: String? = null, val todayOutlook: String = "", val weeklyOutlook: String = "", val sectors: List<SectorPrediction> = emptyList(), val planetaryIndicators: String = "", val hasResult: Boolean = false)

@HiltViewModel
class StockAstrologyViewModel @Inject constructor(private val astrologyRepository: AstrologyRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(StockAstrologyUiState())
    val uiState: StateFlow<StockAstrologyUiState> = _uiState.asStateFlow()
    init { loadPredictions() }
    fun loadPredictions() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            astrologyRepository.getTools(mapOf("type" to "stock_astrology"))
                .onSuccess { r -> val a = r.getAsJsonObject("analysis"); val sectors = mutableListOf<SectorPrediction>(); try { a?.getAsJsonArray("sectors")?.forEach { el -> val o = el.asJsonObject; sectors.add(SectorPrediction(sector = o.get("sector")?.asString ?: "", outlook = o.get("outlook")?.asString ?: "", trend = o.get("trend")?.asString ?: "")) } } catch (_: Exception) {}; _uiState.value = _uiState.value.copy(isLoading = false, todayOutlook = a?.get("today")?.asString ?: "", weeklyOutlook = a?.get("weekly")?.asString ?: "", sectors = sectors, planetaryIndicators = a?.get("planetary_indicators")?.asString ?: "", hasResult = true) }
                .onFailure { e -> _uiState.value = _uiState.value.copy(isLoading = false, error = e.message ?: "Failed") }
        }
    }
}
