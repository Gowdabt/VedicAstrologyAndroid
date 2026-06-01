package com.astrologyvedic.app.ui.screens.gemstone

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

data class GemstoneInfo(
    val name: String,
    val planet: String,
    val properties: String,
    val weight: String,
    val metal: String,
    val finger: String,
    val day: String
)

data class GemstoneUiState(
    val personForm: PersonFormState = PersonFormState(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val gemstones: List<GemstoneInfo> = emptyList(),
    val hasResult: Boolean = false
)

@HiltViewModel
class GemstoneViewModel @Inject constructor(
    private val astrologyRepository: AstrologyRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GemstoneUiState())
    val uiState: StateFlow<GemstoneUiState> = _uiState.asStateFlow()

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

    fun updatePersonForm(form: PersonFormState) { _uiState.value = _uiState.value.copy(personForm = form) }

    fun calculate() {
        val person = _uiState.value.personForm.toPersonRequest() ?: return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            astrologyRepository.getGemstone(mapOf("person" to person))
                .onSuccess { response -> parseResult(response) }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(isLoading = false, error = e.message ?: "Failed to get recommendations")
                }
        }
    }

    private fun parseResult(response: JsonObject) {
        try {
            val analysis = response.getAsJsonObject("analysis")
            val gemstones = mutableListOf<GemstoneInfo>()
            try {
                analysis?.getAsJsonArray("gemstones")?.forEach { element ->
                    val obj = element.asJsonObject
                    gemstones.add(GemstoneInfo(
                        name = obj.get("name")?.asString ?: "",
                        planet = obj.get("planet")?.asString ?: "",
                        properties = obj.get("properties")?.asString ?: "",
                        weight = obj.get("weight")?.asString ?: "",
                        metal = obj.get("metal")?.asString ?: "",
                        finger = obj.get("finger")?.asString ?: "",
                        day = obj.get("day")?.asString ?: ""
                    ))
                }
            } catch (_: Exception) { }
            _uiState.value = _uiState.value.copy(isLoading = false, gemstones = gemstones, hasResult = true)
        } catch (_: Exception) {
            _uiState.value = _uiState.value.copy(isLoading = false, error = "Failed to parse results")
        }
    }
}
