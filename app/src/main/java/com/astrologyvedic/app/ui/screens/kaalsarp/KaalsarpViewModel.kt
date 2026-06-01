package com.astrologyvedic.app.ui.screens.kaalsarp

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

data class KaalsarpUiState(
    val personForm: PersonFormState = PersonFormState(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val hasDosha: Boolean = false,
    val doshaType: String = "",
    val severity: String = "",
    val description: String = "",
    val remedies: List<String> = emptyList(),
    val hasResult: Boolean = false
)

@HiltViewModel
class KaalsarpViewModel @Inject constructor(
    private val astrologyRepository: AstrologyRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(KaalsarpUiState())
    val uiState: StateFlow<KaalsarpUiState> = _uiState.asStateFlow()

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
            astrologyRepository.getKaalsarp(mapOf("person" to person))
                .onSuccess { response -> parseResult(response) }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false, error = e.message ?: "Failed to check Kaalsarp Dosha"
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
                remedies.addAll(listOf("Perform Kaalsarp Puja at Trimbakeshwar", "Chant Rahu mantra", "Donate on Nag Panchami"))
            }
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                hasDosha = analysis?.get("has_dosha")?.asBoolean ?: false,
                doshaType = analysis?.get("type")?.asString ?: "None",
                severity = analysis?.get("severity")?.asString ?: "None",
                description = analysis?.get("description")?.asString ?: "",
                remedies = remedies,
                hasResult = true
            )
        } catch (_: Exception) {
            _uiState.value = _uiState.value.copy(isLoading = false, error = "Failed to parse results")
        }
    }
}
