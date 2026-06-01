package com.astrologyvedic.app.ui.screens.love_compatibility

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

data class LoveCompatUiState(
    val person1Form: PersonFormState = PersonFormState(),
    val person2Form: PersonFormState = PersonFormState(),
    val isLoading: Boolean = false, val error: String? = null,
    val compatibilityPercent: Int = 0,
    val emotionalScore: Int = 0, val physicalScore: Int = 0, val intellectualScore: Int = 0,
    val summary: String = "", val hasResult: Boolean = false
)

@HiltViewModel
class LoveCompatViewModel @Inject constructor(private val astrologyRepository: AstrologyRepository, private val profileRepository: ProfileRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(LoveCompatUiState())
    val uiState: StateFlow<LoveCompatUiState> = _uiState.asStateFlow()
    init { viewModelScope.launch { profileRepository.getDefaultProfile()?.let { p -> _uiState.value = _uiState.value.copy(person1Form = PersonFormState(name = p.name, dob = p.dob, time = p.time, place = p.place, latitude = p.latitude.toString(), longitude = p.longitude.toString())) } } }
    fun updatePerson1Form(form: PersonFormState) { _uiState.value = _uiState.value.copy(person1Form = form) }
    fun updatePerson2Form(form: PersonFormState) { _uiState.value = _uiState.value.copy(person2Form = form) }
    fun calculate() {
        val p1 = _uiState.value.person1Form.toPersonRequest() ?: return
        val p2 = _uiState.value.person2Form.toPersonRequest() ?: return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            astrologyRepository.getLoveCompatibility(mapOf("person1" to p1, "person2" to p2))
                .onSuccess { r -> val a = r.getAsJsonObject("analysis"); _uiState.value = _uiState.value.copy(isLoading = false, compatibilityPercent = a?.get("percentage")?.asInt ?: 0, emotionalScore = a?.get("emotional")?.asInt ?: 0, physicalScore = a?.get("physical")?.asInt ?: 0, intellectualScore = a?.get("intellectual")?.asInt ?: 0, summary = a?.get("summary")?.asString ?: "", hasResult = true) }
                .onFailure { e -> _uiState.value = _uiState.value.copy(isLoading = false, error = e.message ?: "Failed") }
        }
    }
}
