package com.astrologyvedic.app.ui.screens.past_life

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

data class PastLifeUiState(val personForm: PersonFormState = PersonFormState(), val isLoading: Boolean = false, val error: String? = null, val karmaAnalysis: String = "", val pastLifeDescription: String = "", val lessons: List<String> = emptyList(), val hasResult: Boolean = false)

@HiltViewModel
class PastLifeViewModel @Inject constructor(private val astrologyRepository: AstrologyRepository, private val profileRepository: ProfileRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(PastLifeUiState())
    val uiState: StateFlow<PastLifeUiState> = _uiState.asStateFlow()
    init { viewModelScope.launch { profileRepository.getDefaultProfile()?.let { p -> _uiState.value = _uiState.value.copy(personForm = PersonFormState(name = p.name, dob = p.dob, time = p.time, place = p.place, latitude = p.latitude.toString(), longitude = p.longitude.toString())) } } }
    fun updatePersonForm(form: PersonFormState) { _uiState.value = _uiState.value.copy(personForm = form) }
    fun calculate() {
        val person = _uiState.value.personForm.toPersonRequest() ?: return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            astrologyRepository.getPastLife(mapOf("person" to person))
                .onSuccess { r -> val a = r.getAsJsonObject("analysis"); val lessons = mutableListOf<String>(); try { a?.getAsJsonArray("lessons")?.forEach { lessons.add(it.asString) } } catch (_: Exception) {}; _uiState.value = _uiState.value.copy(isLoading = false, karmaAnalysis = a?.get("karma")?.asString ?: "", pastLifeDescription = a?.get("past_life")?.asString ?: "", lessons = lessons, hasResult = true) }
                .onFailure { e -> _uiState.value = _uiState.value.copy(isLoading = false, error = e.message ?: "Failed") }
        }
    }
}
