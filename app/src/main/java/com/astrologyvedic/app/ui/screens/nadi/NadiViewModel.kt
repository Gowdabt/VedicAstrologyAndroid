package com.astrologyvedic.app.ui.screens.nadi

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

data class NadiUiState(
    val personForm: PersonFormState = PersonFormState(),
    val thumbSelection: String = "Right",
    val isLoading: Boolean = false,
    val error: String? = null,
    val reading: String = "",
    val nadiType: String = "",
    val hasResult: Boolean = false
)

@HiltViewModel
class NadiViewModel @Inject constructor(
    private val astrologyRepository: AstrologyRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(NadiUiState())
    val uiState: StateFlow<NadiUiState> = _uiState.asStateFlow()
    init { viewModelScope.launch { profileRepository.getDefaultProfile()?.let { p -> _uiState.value = _uiState.value.copy(personForm = PersonFormState(name = p.name, dob = p.dob, time = p.time, place = p.place, latitude = p.latitude.toString(), longitude = p.longitude.toString())) } } }
    fun updatePersonForm(form: PersonFormState) { _uiState.value = _uiState.value.copy(personForm = form) }
    fun selectThumb(thumb: String) { _uiState.value = _uiState.value.copy(thumbSelection = thumb) }
    fun calculate() {
        val person = _uiState.value.personForm.toPersonRequest() ?: return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            astrologyRepository.getTools(mapOf("person" to person, "thumb" to _uiState.value.thumbSelection, "type" to "nadi"))
                .onSuccess { r -> val a = r.getAsJsonObject("analysis"); _uiState.value = _uiState.value.copy(isLoading = false, reading = a?.get("reading")?.asString ?: "", nadiType = a?.get("nadi_type")?.asString ?: "", hasResult = true) }
                .onFailure { e -> _uiState.value = _uiState.value.copy(isLoading = false, error = e.message ?: "Failed") }
        }
    }
}
