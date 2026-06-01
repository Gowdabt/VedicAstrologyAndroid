package com.astrologyvedic.app.ui.screens.vrat_calendar

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

data class VratDay(
    val date: String,
    val name: String,
    val type: String // Ekadashi, Pradosh, Amavasya, Purnima
)

data class VratCalendarUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val vrats: List<VratDay> = emptyList(),
    val selectedMonth: String = "January",
    val hasResult: Boolean = false
)

@HiltViewModel
class VratCalendarViewModel @Inject constructor(
    private val astrologyRepository: AstrologyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(VratCalendarUiState())
    val uiState: StateFlow<VratCalendarUiState> = _uiState.asStateFlow()

    val months = listOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")

    init { loadVrats() }

    fun selectMonth(month: String) {
        _uiState.value = _uiState.value.copy(selectedMonth = month)
        loadVrats()
    }

    fun loadVrats() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val request = mapOf("month" to _uiState.value.selectedMonth, "year" to "2026", "type" to "vrat_calendar")
            astrologyRepository.getTools(request)
                .onSuccess { response -> parseResult(response) }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(isLoading = false, error = e.message ?: "Failed to load calendar")
                }
        }
    }

    private fun parseResult(response: JsonObject) {
        try {
            val vrats = mutableListOf<VratDay>()
            val array = response.getAsJsonObject("analysis")?.getAsJsonArray("vrats")
            array?.forEach { element ->
                val obj = element.asJsonObject
                vrats.add(VratDay(
                    date = obj.get("date")?.asString ?: "",
                    name = obj.get("name")?.asString ?: "",
                    type = obj.get("type")?.asString ?: ""
                ))
            }
            _uiState.value = _uiState.value.copy(isLoading = false, vrats = vrats, hasResult = true)
        } catch (_: Exception) {
            _uiState.value = _uiState.value.copy(isLoading = false, error = "Failed to parse results")
        }
    }
}
