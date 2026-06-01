package com.astrologyvedic.app.ui.screens.western

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

data class WesternUiState(
    val personForm: PersonFormState = PersonFormState(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val sunSign: String = "", val moonSign: String = "", val rising: String = "",
    val elements: Map<String, Int> = emptyMap(),
    val aspects: List<String> = emptyList(),
    val hasResult: Boolean = false
)

@HiltViewModel
class WesternViewModel @Inject constructor(
    private val astrologyRepository: AstrologyRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(WesternUiState())
    val uiState: StateFlow<WesternUiState> = _uiState.asStateFlow()
    init { viewModelScope.launch { profileRepository.getDefaultProfile()?.let { p -> _uiState.value = _uiState.value.copy(personForm = PersonFormState(name = p.name, dob = p.dob, time = p.time, place = p.place, latitude = p.latitude.toString(), longitude = p.longitude.toString())) } } }
    fun updatePersonForm(form: PersonFormState) { _uiState.value = _uiState.value.copy(personForm = form) }
    fun calculate() {
        val person = _uiState.value.personForm.toPersonRequest() ?: return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            astrologyRepository.getWesternAstrology(mapOf("person" to person))
                .onSuccess { r -> parseResult(r) }
                .onFailure { e -> _uiState.value = _uiState.value.copy(isLoading = false, error = e.message ?: "Failed") }
        }
    }
    private fun parseResult(response: JsonObject) {
        try {
            val a = response.getAsJsonObject("analysis")
            val aspects = mutableListOf<String>()
            a?.getAsJsonArray("aspects")?.forEach { aspects.add(it.asString) }
            val elements = mutableMapOf<String, Int>()
            try { val el = a?.getAsJsonObject("elements"); el?.keySet()?.forEach { elements[it] = el.get(it).asInt } } catch (_: Exception) {}
            _uiState.value = _uiState.value.copy(isLoading = false, sunSign = a?.get("sun_sign")?.asString ?: "", moonSign = a?.get("moon_sign")?.asString ?: "", rising = a?.get("rising")?.asString ?: "", elements = elements, aspects = aspects, hasResult = true)
        } catch (_: Exception) { _uiState.value = _uiState.value.copy(isLoading = false, error = "Failed to parse") }
    }
}
