package com.astrologyvedic.app.ui.screens.transit

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

data class TransitInfo(val planet: String, val sign: String, val house: String, val effect: String)
data class TransitUiState(val personForm: PersonFormState = PersonFormState(), val isLoading: Boolean = false, val error: String? = null, val transits: List<TransitInfo> = emptyList(), val summary: String = "", val hasResult: Boolean = false)

@HiltViewModel
class TransitViewModel @Inject constructor(private val astrologyRepository: AstrologyRepository, private val profileRepository: ProfileRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(TransitUiState())
    val uiState: StateFlow<TransitUiState> = _uiState.asStateFlow()
    init { viewModelScope.launch { profileRepository.getDefaultProfile()?.let { p -> _uiState.value = _uiState.value.copy(personForm = PersonFormState(name = p.name, dob = p.dob, time = p.time, place = p.place, latitude = p.latitude.toString(), longitude = p.longitude.toString())) } } }
    fun updatePersonForm(form: PersonFormState) { _uiState.value = _uiState.value.copy(personForm = form) }
    fun calculate() {
        val person = _uiState.value.personForm.toPersonRequest() ?: return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            astrologyRepository.getTransitReport(mapOf("person" to person))
                .onSuccess { r -> val a = r.getAsJsonObject("analysis"); val transits = mutableListOf<TransitInfo>(); try { a?.getAsJsonArray("transits")?.forEach { el -> val o = el.asJsonObject; transits.add(TransitInfo(planet = o.get("planet")?.asString ?: "", sign = o.get("sign")?.asString ?: "", house = o.get("house")?.asString ?: "", effect = o.get("effect")?.asString ?: "")) } } catch (_: Exception) {}; _uiState.value = _uiState.value.copy(isLoading = false, transits = transits, summary = a?.get("summary")?.asString ?: "", hasResult = true) }
                .onFailure { e -> _uiState.value = _uiState.value.copy(isLoading = false, error = e.message ?: "Failed") }
        }
    }
}
