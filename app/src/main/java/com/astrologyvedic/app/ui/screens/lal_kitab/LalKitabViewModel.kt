package com.astrologyvedic.app.ui.screens.lal_kitab

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

data class LalKitabUiState(
    val personForm: PersonFormState = PersonFormState(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val predictions: List<String> = emptyList(),
    val remedies: List<String> = emptyList(),
    val hasResult: Boolean = false
)

@HiltViewModel
class LalKitabViewModel @Inject constructor(
    private val astrologyRepository: AstrologyRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(LalKitabUiState())
    val uiState: StateFlow<LalKitabUiState> = _uiState.asStateFlow()

    init { viewModelScope.launch { profileRepository.getDefaultProfile()?.let { p -> _uiState.value = _uiState.value.copy(personForm = PersonFormState(name = p.name, dob = p.dob, time = p.time, place = p.place, latitude = p.latitude.toString(), longitude = p.longitude.toString())) } } }

    fun updatePersonForm(form: PersonFormState) { _uiState.value = _uiState.value.copy(personForm = form) }

    fun calculate() {
        val person = _uiState.value.personForm.toPersonRequest() ?: return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            astrologyRepository.getLalKitab(mapOf("person" to person))
                .onSuccess { r -> parseResult(r) }
                .onFailure { e -> _uiState.value = _uiState.value.copy(isLoading = false, error = e.message ?: "Failed") }
        }
    }

    private fun parseResult(response: JsonObject) {
        try {
            val a = response.getAsJsonObject("analysis")
            val predictions = mutableListOf<String>()
            val remedies = mutableListOf<String>()
            a?.getAsJsonArray("predictions")?.forEach { predictions.add(it.asString) }
            a?.getAsJsonArray("remedies")?.forEach { remedies.add(it.asString) }
            _uiState.value = _uiState.value.copy(isLoading = false, predictions = predictions, remedies = remedies, hasResult = true)
        } catch (_: Exception) { _uiState.value = _uiState.value.copy(isLoading = false, error = "Failed to parse") }
    }
}
