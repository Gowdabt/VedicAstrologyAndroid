package com.astrologyvedic.app.ui.screens.muhurat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astrologyvedic.app.data.repository.AstrologyRepository
import com.google.gson.JsonObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MuhuratWindow(
    val date: String,
    val startTime: String,
    val endTime: String,
    val quality: String = "Good"
)

data class MuhuratUiState(
    val selectedActivity: String = "",
    val startDate: String = "",
    val endDate: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val windows: List<MuhuratWindow> = emptyList(),
    val hasResult: Boolean = false
)

@HiltViewModel
class MuhuratViewModel @Inject constructor(
    private val astrologyRepository: AstrologyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MuhuratUiState())
    val uiState: StateFlow<MuhuratUiState> = _uiState.asStateFlow()

    val activities = listOf("Marriage", "Travel", "Business", "Property", "Education", "Medical", "Naming Ceremony", "Griha Pravesh")

    fun updateActivity(activity: String) { _uiState.value = _uiState.value.copy(selectedActivity = activity) }
    fun updateStartDate(date: String) { _uiState.value = _uiState.value.copy(startDate = date) }
    fun updateEndDate(date: String) { _uiState.value = _uiState.value.copy(endDate = date) }

    fun findMuhurat() {
        if (_uiState.value.selectedActivity.isBlank()) return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val request = mapOf(
                "activity" to _uiState.value.selectedActivity,
                "start_date" to _uiState.value.startDate,
                "end_date" to _uiState.value.endDate
            )
            astrologyRepository.getMuhurat(request)
                .onSuccess { response -> parseResult(response) }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(isLoading = false, error = e.message ?: "Failed to find muhurat")
                }
        }
    }

    private fun parseResult(response: JsonObject) {
        try {
            val analysis = response.getAsJsonObject("analysis")
            val windows = mutableListOf<MuhuratWindow>()
            try {
                analysis?.getAsJsonArray("windows")?.forEach { element ->
                    val obj = element.asJsonObject
                    windows.add(MuhuratWindow(
                        date = obj.get("date")?.asString ?: "",
                        startTime = obj.get("start")?.asString ?: "",
                        endTime = obj.get("end")?.asString ?: "",
                        quality = obj.get("quality")?.asString ?: "Good"
                    ))
                }
            } catch (_: Exception) { }
            _uiState.value = _uiState.value.copy(isLoading = false, windows = windows, hasResult = true)
        } catch (_: Exception) {
            _uiState.value = _uiState.value.copy(isLoading = false, error = "Failed to parse results")
        }
    }
}
