package com.astrologyvedic.app.ui.screens.timeline

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astrologyvedic.app.data.repository.AstrologyRepository
import com.astrologyvedic.app.data.repository.ProfileRepository
import com.astrologyvedic.app.ui.components.PersonFormState
import com.astrologyvedic.app.ui.components.toPersonRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DashaPeriod(val planet: String, val startDate: String, val endDate: String, val isCurrent: Boolean = false)
data class TimelineUiState(val personForm: PersonFormState = PersonFormState(), val startDate: String = "", val endDate: String = "", val isLoading: Boolean = false, val error: String? = null, val periods: List<DashaPeriod> = emptyList(), val currentDasha: String = "", val hasResult: Boolean = false)

@HiltViewModel
class TimelineViewModel @Inject constructor(private val astrologyRepository: AstrologyRepository, private val profileRepository: ProfileRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(TimelineUiState())
    val uiState: StateFlow<TimelineUiState> = _uiState.asStateFlow()
    init { viewModelScope.launch { profileRepository.getDefaultProfile()?.let { p -> _uiState.value = _uiState.value.copy(personForm = PersonFormState(name = p.name, dob = p.dob, time = p.time, place = p.place, latitude = p.latitude.toString(), longitude = p.longitude.toString())) } } }
    fun updatePersonForm(form: PersonFormState) { _uiState.value = _uiState.value.copy(personForm = form) }
    fun updateStartDate(d: String) { _uiState.value = _uiState.value.copy(startDate = d) }
    fun updateEndDate(d: String) { _uiState.value = _uiState.value.copy(endDate = d) }
    fun calculate() {
        val person = _uiState.value.personForm.toPersonRequest() ?: return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            astrologyRepository.getTimeline(mapOf("person" to person, "start_date" to _uiState.value.startDate, "end_date" to _uiState.value.endDate))
                .onSuccess { r -> val a = r.getAsJsonObject("analysis"); val periods = mutableListOf<DashaPeriod>(); try { a?.getAsJsonArray("periods")?.forEach { el -> val o = el.asJsonObject; periods.add(DashaPeriod(planet = o.get("planet")?.asString ?: "", startDate = o.get("start")?.asString ?: "", endDate = o.get("end")?.asString ?: "", isCurrent = o.get("is_current")?.asBoolean ?: false)) } } catch (_: Exception) {}; _uiState.value = _uiState.value.copy(isLoading = false, periods = periods, currentDasha = a?.get("current_dasha")?.asString ?: "", hasResult = true) }
                .onFailure { e -> _uiState.value = _uiState.value.copy(isLoading = false, error = e.message ?: "Failed") }
        }
    }
}
