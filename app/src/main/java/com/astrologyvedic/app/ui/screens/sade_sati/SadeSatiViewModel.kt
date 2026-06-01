package com.astrologyvedic.app.ui.screens.sade_sati

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astrologyvedic.app.data.api.models.PersonRequest
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

data class SadeSatiUiState(
    val personForm: PersonFormState = PersonFormState(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isActive: Boolean = false,
    val currentPhase: String = "",
    val startDate: String = "",
    val endDate: String = "",
    val remedies: List<String> = emptyList(),
    val description: String = "",
    val hasResult: Boolean = false
)

@HiltViewModel
class SadeSatiViewModel @Inject constructor(
    private val astrologyRepository: AstrologyRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SadeSatiUiState())
    val uiState: StateFlow<SadeSatiUiState> = _uiState.asStateFlow()

    init { loadDefaultProfile() }

    private fun loadDefaultProfile() {
        viewModelScope.launch {
            profileRepository.getDefaultProfile()?.let { profile ->
                _uiState.value = _uiState.value.copy(
                    personForm = PersonFormState(
                        name = profile.name, dob = profile.dob, time = profile.time,
                        place = profile.place, latitude = profile.latitude.toString(),
                        longitude = profile.longitude.toString()
                    )
                )
            }
        }
    }

    fun updatePersonForm(form: PersonFormState) {
        _uiState.value = _uiState.value.copy(personForm = form)
    }

    fun calculate() {
        val person = _uiState.value.personForm.toPersonRequest() ?: return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            astrologyRepository.getSadeSati(mapOf("person" to person))
                .onSuccess { response -> parseResult(response) }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false, error = e.message ?: "Failed to check Sade Sati"
                    )
                }
        }
    }

    private fun parseResult(response: JsonObject) {
        try {
            val analysis = response.getAsJsonObject("analysis")
            val remedies = mutableListOf<String>()
            try {
                analysis?.getAsJsonArray("remedies")?.forEach { remedies.add(it.asString) }
            } catch (_: Exception) {
                remedies.addAll(listOf("Chant Shani mantra", "Donate black items on Saturday", "Wear blue sapphire (consult astrologer)"))
            }
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                isActive = analysis?.get("is_active")?.asBoolean ?: false,
                currentPhase = analysis?.get("phase")?.asString ?: "Not Active",
                startDate = analysis?.get("start_date")?.asString ?: "--",
                endDate = analysis?.get("end_date")?.asString ?: "--",
                remedies = remedies,
                description = analysis?.get("description")?.asString ?: "",
                hasResult = true
            )
        } catch (_: Exception) {
            _uiState.value = _uiState.value.copy(isLoading = false, error = "Failed to parse results")
        }
    }
}
