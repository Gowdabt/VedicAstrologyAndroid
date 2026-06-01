package com.astrologyvedic.app.ui.screens.chart_comparison

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

data class PlanetPosition(val planet: String, val sign1: String, val sign2: String)

data class ChartComparisonUiState(
    val person1Form: PersonFormState = PersonFormState(), val person2Form: PersonFormState = PersonFormState(),
    val isLoading: Boolean = false, val error: String? = null,
    val positions: List<PlanetPosition> = emptyList(), val hasResult: Boolean = false
)

@HiltViewModel
class ChartComparisonViewModel @Inject constructor(private val astrologyRepository: AstrologyRepository, private val profileRepository: ProfileRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(ChartComparisonUiState())
    val uiState: StateFlow<ChartComparisonUiState> = _uiState.asStateFlow()
    init { viewModelScope.launch { profileRepository.getDefaultProfile()?.let { p -> _uiState.value = _uiState.value.copy(person1Form = PersonFormState(name = p.name, dob = p.dob, time = p.time, place = p.place, latitude = p.latitude.toString(), longitude = p.longitude.toString())) } } }
    fun updatePerson1Form(form: PersonFormState) { _uiState.value = _uiState.value.copy(person1Form = form) }
    fun updatePerson2Form(form: PersonFormState) { _uiState.value = _uiState.value.copy(person2Form = form) }
    fun calculate() {
        val p1 = _uiState.value.person1Form.toPersonRequest() ?: return
        val p2 = _uiState.value.person2Form.toPersonRequest() ?: return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            astrologyRepository.getTools(mapOf("person1" to p1, "person2" to p2, "type" to "chart_comparison"))
                .onSuccess { r -> val positions = mutableListOf<PlanetPosition>(); try { r.getAsJsonObject("analysis")?.getAsJsonArray("positions")?.forEach { el -> val o = el.asJsonObject; positions.add(PlanetPosition(planet = o.get("planet")?.asString ?: "", sign1 = o.get("sign1")?.asString ?: "", sign2 = o.get("sign2")?.asString ?: "")) } } catch (_: Exception) {}; _uiState.value = _uiState.value.copy(isLoading = false, positions = positions, hasResult = true) }
                .onFailure { e -> _uiState.value = _uiState.value.copy(isLoading = false, error = e.message ?: "Failed") }
        }
    }
}
