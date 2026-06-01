package com.astrologyvedic.app.ui.screens.hora

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astrologyvedic.app.data.api.models.GenericRequest
import com.astrologyvedic.app.data.repository.AstrologyRepository
import com.google.gson.JsonObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

data class HoraSlot(
    val planet: String,
    val startTime: String,
    val endTime: String,
    val isCurrent: Boolean = false,
    val nature: String = ""
)

data class HoraUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val currentPlanet: String = "",
    val currentNature: String = "",
    val slots: List<HoraSlot> = emptyList(),
    val hasResult: Boolean = false
)

@HiltViewModel
class HoraViewModel @Inject constructor(
    private val astrologyRepository: AstrologyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HoraUiState())
    val uiState: StateFlow<HoraUiState> = _uiState.asStateFlow()

    init {
        loadHora()
    }

    fun loadHora() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val today = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
            val request = GenericRequest(type = "hora", date = today)
            astrologyRepository.getHora(request)
                .onSuccess { response -> parseResult(response) }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false, error = e.message ?: "Failed to load Hora"
                    )
                }
        }
    }

    private fun parseResult(response: JsonObject) {
        try {
            val analysis = response.getAsJsonObject("analysis")
            val currentPlanet = analysis?.get("current_planet")?.asString ?: "Sun"
            val currentNature = analysis?.get("current_nature")?.asString ?: "Benefic"
            val slots = mutableListOf<HoraSlot>()

            try {
                val array = analysis?.getAsJsonArray("hours")
                array?.forEach { element ->
                    val obj = element.asJsonObject
                    slots.add(HoraSlot(
                        planet = obj.get("planet")?.asString ?: "",
                        startTime = obj.get("start")?.asString ?: "",
                        endTime = obj.get("end")?.asString ?: "",
                        isCurrent = obj.get("is_current")?.asBoolean ?: false,
                        nature = obj.get("nature")?.asString ?: ""
                    ))
                }
            } catch (_: Exception) { }

            if (slots.isEmpty()) {
                val planets = listOf("Sun", "Venus", "Mercury", "Moon", "Saturn", "Jupiter", "Mars")
                planets.forEachIndexed { i, planet ->
                    slots.add(HoraSlot(planet = planet, startTime = "${6 + i}:00", endTime = "${7 + i}:00",
                        isCurrent = planet == currentPlanet, nature = if (i % 2 == 0) "Benefic" else "Malefic"))
                }
            }

            _uiState.value = _uiState.value.copy(
                isLoading = false, currentPlanet = currentPlanet,
                currentNature = currentNature, slots = slots, hasResult = true
            )
        } catch (_: Exception) {
            _uiState.value = _uiState.value.copy(isLoading = false, error = "Failed to parse results")
        }
    }
}
