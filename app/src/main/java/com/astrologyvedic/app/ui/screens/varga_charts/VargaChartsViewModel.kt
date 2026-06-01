package com.astrologyvedic.app.ui.screens.varga_charts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astrologyvedic.app.data.repository.AstrologyRepository
import com.astrologyvedic.app.data.repository.ProfileRepository
import com.astrologyvedic.app.ui.components.PersonFormState
import com.astrologyvedic.app.ui.components.toPersonRequest
import com.google.gson.JsonObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class VargaChartsUiState(
    val personForm: PersonFormState = PersonFormState(),
    val selectedChart: String = "D1",
    val isLoading: Boolean = false, val error: String? = null,
    val chartData: String = "", val hasResult: Boolean = false
)

@HiltViewModel
class VargaChartsViewModel @Inject constructor(private val astrologyRepository: AstrologyRepository, private val profileRepository: ProfileRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(VargaChartsUiState())
    val uiState: StateFlow<VargaChartsUiState> = _uiState.asStateFlow()
    val chartTypes = listOf("D1", "D2", "D3", "D4", "D7", "D9", "D10", "D12", "D16", "D20", "D24", "D27", "D30", "D40", "D45", "D60")
    init { viewModelScope.launch { profileRepository.getDefaultProfile()?.let { p -> _uiState.value = _uiState.value.copy(personForm = PersonFormState(name = p.name, dob = p.dob, time = p.time, place = p.place, latitude = p.latitude.toString(), longitude = p.longitude.toString())) } } }
    fun updatePersonForm(form: PersonFormState) { _uiState.value = _uiState.value.copy(personForm = form) }
    fun selectChart(chart: String) { _uiState.value = _uiState.value.copy(selectedChart = chart) }
    fun calculate() {
        val person = _uiState.value.personForm.toPersonRequest() ?: return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            astrologyRepository.getTools(mapOf("person" to person, "chart" to _uiState.value.selectedChart, "type" to "varga"))
                .onSuccess { r -> _uiState.value = _uiState.value.copy(isLoading = false, chartData = r.getAsJsonObject("analysis")?.get("chart_data")?.asString ?: "Chart generated successfully", hasResult = true) }
                .onFailure { e -> _uiState.value = _uiState.value.copy(isLoading = false, error = e.message ?: "Failed") }
        }
    }
}
