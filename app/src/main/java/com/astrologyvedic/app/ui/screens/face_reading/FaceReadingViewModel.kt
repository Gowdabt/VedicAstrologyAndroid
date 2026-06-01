package com.astrologyvedic.app.ui.screens.face_reading

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astrologyvedic.app.data.repository.AstrologyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FaceReadingUiState(val faceShape: String = "", val dob: String = "", val isLoading: Boolean = false, val error: String? = null, val reading: String = "", val traits: List<String> = emptyList(), val hasResult: Boolean = false)

@HiltViewModel
class FaceReadingViewModel @Inject constructor(private val astrologyRepository: AstrologyRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(FaceReadingUiState())
    val uiState: StateFlow<FaceReadingUiState> = _uiState.asStateFlow()
    val faceShapes = listOf("Oval", "Round", "Square", "Heart", "Oblong", "Diamond", "Triangle")
    fun updateFaceShape(s: String) { _uiState.value = _uiState.value.copy(faceShape = s) }
    fun updateDob(d: String) { _uiState.value = _uiState.value.copy(dob = d) }
    fun calculate() {
        if (_uiState.value.faceShape.isBlank()) return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            astrologyRepository.getTools(mapOf("type" to "face_reading", "face_shape" to _uiState.value.faceShape, "dob" to _uiState.value.dob))
                .onSuccess { r -> val a = r.getAsJsonObject("analysis"); val traits = mutableListOf<String>(); try { a?.getAsJsonArray("traits")?.forEach { traits.add(it.asString) } } catch (_: Exception) {}; _uiState.value = _uiState.value.copy(isLoading = false, reading = a?.get("reading")?.asString ?: "", traits = traits, hasResult = true) }
                .onFailure { e -> _uiState.value = _uiState.value.copy(isLoading = false, error = e.message ?: "Failed") }
        }
    }
}
