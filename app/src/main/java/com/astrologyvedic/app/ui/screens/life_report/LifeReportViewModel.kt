package com.astrologyvedic.app.ui.screens.life_report

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

data class ReportSection(val title: String, val content: String, val isExpanded: Boolean = false)
data class LifeReportUiState(val personForm: PersonFormState = PersonFormState(), val isLoading: Boolean = false, val error: String? = null, val sections: List<ReportSection> = emptyList(), val hasResult: Boolean = false)

@HiltViewModel
class LifeReportViewModel @Inject constructor(private val astrologyRepository: AstrologyRepository, private val profileRepository: ProfileRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(LifeReportUiState())
    val uiState: StateFlow<LifeReportUiState> = _uiState.asStateFlow()
    init { viewModelScope.launch { profileRepository.getDefaultProfile()?.let { p -> _uiState.value = _uiState.value.copy(personForm = PersonFormState(name = p.name, dob = p.dob, time = p.time, place = p.place, latitude = p.latitude.toString(), longitude = p.longitude.toString())) } } }
    fun updatePersonForm(form: PersonFormState) { _uiState.value = _uiState.value.copy(personForm = form) }
    fun toggleSection(index: Int) { val s = _uiState.value.sections.toMutableList(); s[index] = s[index].copy(isExpanded = !s[index].isExpanded); _uiState.value = _uiState.value.copy(sections = s) }
    fun calculate() {
        val person = _uiState.value.personForm.toPersonRequest() ?: return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            astrologyRepository.getTools(mapOf("person" to person, "type" to "life_report"))
                .onSuccess { r -> val sections = mutableListOf<ReportSection>(); try { r.getAsJsonObject("analysis")?.getAsJsonArray("sections")?.forEach { el -> val o = el.asJsonObject; sections.add(ReportSection(title = o.get("title")?.asString ?: "", content = o.get("content")?.asString ?: "")) } } catch (_: Exception) { sections.addAll(listOf(ReportSection("Personality", ""), ReportSection("Career", ""), ReportSection("Finance", ""), ReportSection("Health", ""), ReportSection("Marriage", ""), ReportSection("Family", ""), ReportSection("Spirituality", ""), ReportSection("Remedies", ""))) }; _uiState.value = _uiState.value.copy(isLoading = false, sections = sections, hasResult = true) }
                .onFailure { e -> _uiState.value = _uiState.value.copy(isLoading = false, error = e.message ?: "Failed") }
        }
    }
}
