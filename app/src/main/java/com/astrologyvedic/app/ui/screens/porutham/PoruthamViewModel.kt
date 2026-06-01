package com.astrologyvedic.app.ui.screens.porutham

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

data class PoruthamItem(val name: String, val matched: Boolean, val description: String = "")

data class PoruthamUiState(
    val person1Form: PersonFormState = PersonFormState(),
    val person2Form: PersonFormState = PersonFormState(),
    val isLoading: Boolean = false, val error: String? = null,
    val poruthams: List<PoruthamItem> = emptyList(),
    val matchedCount: Int = 0, val totalCount: Int = 10,
    val hasResult: Boolean = false
)

@HiltViewModel
class PoruthamViewModel @Inject constructor(private val astrologyRepository: AstrologyRepository, private val profileRepository: ProfileRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(PoruthamUiState())
    val uiState: StateFlow<PoruthamUiState> = _uiState.asStateFlow()
    init { viewModelScope.launch { profileRepository.getDefaultProfile()?.let { p -> _uiState.value = _uiState.value.copy(person1Form = PersonFormState(name = p.name, dob = p.dob, time = p.time, place = p.place, latitude = p.latitude.toString(), longitude = p.longitude.toString())) } } }
    fun updatePerson1Form(form: PersonFormState) { _uiState.value = _uiState.value.copy(person1Form = form) }
    fun updatePerson2Form(form: PersonFormState) { _uiState.value = _uiState.value.copy(person2Form = form) }
    fun calculate() {
        val p1 = _uiState.value.person1Form.toPersonRequest() ?: return
        val p2 = _uiState.value.person2Form.toPersonRequest() ?: return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            astrologyRepository.getTools(mapOf("person1" to p1, "person2" to p2, "type" to "porutham"))
                .onSuccess { r -> val a = r.getAsJsonObject("analysis"); val items = mutableListOf<PoruthamItem>(); try { a?.getAsJsonArray("poruthams")?.forEach { el -> val o = el.asJsonObject; items.add(PoruthamItem(name = o.get("name")?.asString ?: "", matched = o.get("matched")?.asBoolean ?: false, description = o.get("description")?.asString ?: "")) } } catch (_: Exception) {}; _uiState.value = _uiState.value.copy(isLoading = false, poruthams = items, matchedCount = items.count { it.matched }, totalCount = items.size.coerceAtLeast(10), hasResult = true) }
                .onFailure { e -> _uiState.value = _uiState.value.copy(isLoading = false, error = e.message ?: "Failed") }
        }
    }
}
