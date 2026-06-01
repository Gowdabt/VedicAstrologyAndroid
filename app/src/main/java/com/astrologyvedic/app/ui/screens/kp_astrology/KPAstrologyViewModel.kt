package com.astrologyvedic.app.ui.screens.kp_astrology

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

data class SubLordEntry(val house: String, val signLord: String, val starLord: String, val subLord: String)

data class KPAstrologyUiState(
    val personForm: PersonFormState = PersonFormState(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val subLords: List<SubLordEntry> = emptyList(),
    val significators: String = "",
    val prediction: String = "",
    val hasResult: Boolean = false
)

@HiltViewModel
class KPAstrologyViewModel @Inject constructor(
    private val astrologyRepository: AstrologyRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(KPAstrologyUiState())
    val uiState: StateFlow<KPAstrologyUiState> = _uiState.asStateFlow()

    init { loadDefaultProfile() }

    private fun loadDefaultProfile() {
        viewModelScope.launch {
            profileRepository.getDefaultProfile()?.let { profile ->
                _uiState.value = _uiState.value.copy(personForm = PersonFormState(
                    name = profile.name, dob = profile.dob, time = profile.time,
                    place = profile.place, latitude = profile.latitude.toString(), longitude = profile.longitude.toString()
                ))
            }
        }
    }

    fun updatePersonForm(form: PersonFormState) { _uiState.value = _uiState.value.copy(personForm = form) }

    fun calculate() {
        val person = _uiState.value.personForm.toPersonRequest() ?: return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            astrologyRepository.getKpAstrology(mapOf("person" to person))
                .onSuccess { response -> parseResult(response) }
                .onFailure { e -> _uiState.value = _uiState.value.copy(isLoading = false, error = e.message ?: "Failed to generate KP chart") }
        }
    }

    private fun parseResult(response: JsonObject) {
        try {
            val analysis = response.getAsJsonObject("analysis")
            val subLords = mutableListOf<SubLordEntry>()
            try {
                analysis?.getAsJsonArray("sub_lords")?.forEach { el ->
                    val obj = el.asJsonObject
                    subLords.add(SubLordEntry(
                        house = obj.get("house")?.asString ?: "",
                        signLord = obj.get("sign_lord")?.asString ?: "",
                        starLord = obj.get("star_lord")?.asString ?: "",
                        subLord = obj.get("sub_lord")?.asString ?: ""
                    ))
                }
            } catch (_: Exception) { }
            _uiState.value = _uiState.value.copy(
                isLoading = false, subLords = subLords,
                significators = analysis?.get("significators")?.asString ?: "",
                prediction = analysis?.get("prediction")?.asString ?: "",
                hasResult = true
            )
        } catch (_: Exception) { _uiState.value = _uiState.value.copy(isLoading = false, error = "Failed to parse results") }
    }
}
