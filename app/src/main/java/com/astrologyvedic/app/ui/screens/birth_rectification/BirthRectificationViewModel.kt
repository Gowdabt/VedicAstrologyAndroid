package com.astrologyvedic.app.ui.screens.birth_rectification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astrologyvedic.app.data.repository.AstrologyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BirthRectificationUiState(
    val approximateTime: String = "", val dob: String = "", val place: String = "",
    val marriageDate: String = "", val firstJobDate: String = "", val firstChildDate: String = "",
    val isLoading: Boolean = false, val error: String? = null,
    val rectifiedTime: String = "", val confidence: String = "", val explanation: String = "",
    val hasResult: Boolean = false
)

@HiltViewModel
class BirthRectificationViewModel @Inject constructor(private val astrologyRepository: AstrologyRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(BirthRectificationUiState())
    val uiState: StateFlow<BirthRectificationUiState> = _uiState.asStateFlow()
    fun updateField(field: String, value: String) { _uiState.value = when(field) { "time" -> _uiState.value.copy(approximateTime = value); "dob" -> _uiState.value.copy(dob = value); "place" -> _uiState.value.copy(place = value); "marriage" -> _uiState.value.copy(marriageDate = value); "job" -> _uiState.value.copy(firstJobDate = value); "child" -> _uiState.value.copy(firstChildDate = value); else -> _uiState.value } }
    fun calculate() {
        if (_uiState.value.dob.isBlank()) return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val request = mapOf("type" to "birth_rectification", "dob" to _uiState.value.dob, "approximate_time" to _uiState.value.approximateTime, "place" to _uiState.value.place, "marriage_date" to _uiState.value.marriageDate, "first_job" to _uiState.value.firstJobDate, "first_child" to _uiState.value.firstChildDate)
            astrologyRepository.getTools(request)
                .onSuccess { r -> val a = r.getAsJsonObject("analysis"); _uiState.value = _uiState.value.copy(isLoading = false, rectifiedTime = a?.get("rectified_time")?.asString ?: "", confidence = a?.get("confidence")?.asString ?: "", explanation = a?.get("explanation")?.asString ?: "", hasResult = true) }
                .onFailure { e -> _uiState.value = _uiState.value.copy(isLoading = false, error = e.message ?: "Failed") }
        }
    }
}
