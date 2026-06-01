package com.astrologyvedic.app.ui.screens.yoga_detection

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

data class YogaInfo(val name: String, val type: String, val description: String) // type: benefic/malefic
data class YogaDetectionUiState(val personForm: PersonFormState = PersonFormState(), val isLoading: Boolean = false, val error: String? = null, val yogas: List<YogaInfo> = emptyList(), val hasResult: Boolean = false)

@HiltViewModel
class YogaDetectionViewModel @Inject constructor(private val astrologyRepository: AstrologyRepository, private val profileRepository: ProfileRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(YogaDetectionUiState())
    val uiState: StateFlow<YogaDetectionUiState> = _uiState.asStateFlow()
    init { viewModelScope.launch { profileRepository.getDefaultProfile()?.let { p -> _uiState.value = _uiState.value.copy(personForm = PersonFormState(name = p.name, dob = p.dob, time = p.time, place = p.place, latitude = p.latitude.toString(), longitude = p.longitude.toString())) } } }
    fun updatePersonForm(form: PersonFormState) { _uiState.value = _uiState.value.copy(personForm = form) }
    fun calculate() {
        val person = _uiState.value.personForm.toPersonRequest() ?: return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            astrologyRepository.getTools(mapOf("person" to person, "type" to "yoga_detection"))
                .onSuccess { r -> val yogas = mutableListOf<YogaInfo>(); try { r.getAsJsonObject("analysis")?.getAsJsonArray("yogas")?.forEach { el -> val o = el.asJsonObject; yogas.add(YogaInfo(name = o.get("name")?.asString ?: "", type = o.get("type")?.asString ?: "benefic", description = o.get("description")?.asString ?: "")) } } catch (_: Exception) {}; _uiState.value = _uiState.value.copy(isLoading = false, yogas = yogas, hasResult = true) }
                .onFailure { e -> _uiState.value = _uiState.value.copy(isLoading = false, error = e.message ?: "Failed") }
        }
    }
}
